package ca.pfv.spmf.algorithms.graph_mining.tkg;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/* This file is copyright (c) 2022 by Shaul Zevin
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This is the consumer side of consumer/producer implementation of the iterator over all DFS code projections into the graphs database
 * <br/><br/>
 * <p>
 * The cgspan algorithm is described in : <br/>
 * <br/>
 * <p>
 * cgSpan: Closed Graph-Based Substructure Pattern Mining, by Zevin Shaul, Sheikh Naaz
 * IEEE BigData 2021 7th Special Session on Intelligent Data Mining
 * <p>
 * <p>
 * <br/>
 * <p>
 * The CGspan algorithm finds all the closed subgraphs and their support in a
 * graph provided by the user.
 * <br/><br/>
 * <p>
 * This implementation saves the result to a file
 *
 * @author Shaul Zevin
 * @see ProjectedCompact
 */
public class ProjectedIteratorConsumer implements Iterator<PDFSCompact> {
    // projections compact memory representation
    private final ProjectedCompact projected;
    // list of first edges from all projections
    private final List<ProjectedEdge> firstEdges;
    // index in the list of first edges from all projections (firstEdges).
    // projections are built starting with first edge pointed by the index
    private final int firstEdgeIndex;
    // projected edges iterators stack, iterator at offset i outputs projected edges to match DFS code edge at offset i
    private final Stack<Iterator<Edge>> vertexEdgesIterators = new Stack<Iterator<Edge>>();
    // projected edges outputted by projected edges iterators
    private final Stack<ProjectedEdge> pdfs = new Stack<ProjectedEdge>();
    // projected vertices stack, database graph vertex at offset i is projected from DFS code vertex i.
    private final Stack<Vertex> vertices = new Stack<Vertex>();
    // projections queue from which projections are consumed
    private final BlockingQueue<PDFSCompact> pdfsQueue;
    // number of producer threads
    private final int numProducers;
    // flag used by the consumer to signal producer threads to stop
    private final Control control = new Control();
    // next projection
    private PDFSCompact nextPDFS = null;
    // next projection database graph
    private DatabaseGraph databaseGraph;
    // number of producer active threads
    private int numRunningProducers;
    // list of callbacks to allow control on iterators output
    private List<IProjectedIteratorCallback> callbacks = new LinkedList<IProjectedIteratorCallback>();

    public ProjectedIteratorConsumer(ProjectedCompact projected, int queueSize, int numProducers) {
        this.projected = projected;
        this.numProducers = numProducers;
        pdfsQueue = new LinkedBlockingDeque<PDFSCompact>(queueSize);
        firstEdges = new ArrayList<ProjectedEdge>();
        for (Map<Integer, Set<ProjectedEdge>> vertexEdges : projected.getProjected().get(0).values()) {
            for (Set<ProjectedEdge> edges : vertexEdges.values()) {
                firstEdges.addAll(edges);
            }
        }
        firstEdgeIndex = 0;

        advance();
    }

    public ProjectedIteratorConsumer(ProjectedCompact projected, int queueSize, int numProducers, int gid) {
        this.projected = projected;
        this.numProducers = numProducers;
        pdfsQueue = new LinkedBlockingDeque<PDFSCompact>(queueSize);
        firstEdges = new ArrayList<ProjectedEdge>();
        for (Integer vertexEdgesGid : projected.getProjected().get(0).keySet()) {
            if (vertexEdgesGid != gid) {
                continue;
            }

            Map<Integer, Set<ProjectedEdge>> vertexEdges = projected.getProjected().get(0).get(vertexEdgesGid);
            for (Set<ProjectedEdge> edges : vertexEdges.values()) {
                firstEdges.addAll(edges);
            }
        }
        firstEdgeIndex = 0;

        advance();
    }

    public ProjectedIteratorConsumer(ProjectedCompact projected, int queueSize, int numProducers, List<IProjectedIteratorCallback> callbacks) {
        this.projected = projected;
        this.numProducers = numProducers;
        this.callbacks = callbacks;
        pdfsQueue = new LinkedBlockingDeque<PDFSCompact>(queueSize);
        firstEdges = new ArrayList<ProjectedEdge>();
        for (Map<Integer, Set<ProjectedEdge>> vertexEdges : projected.getProjected().get(0).values()) {
            for (Set<ProjectedEdge> edges : vertexEdges.values()) {
                firstEdges.addAll(edges);
            }
        }
        firstEdgeIndex = 0;

        advance();
    }

    public ProjectedIteratorConsumer(ProjectedCompact projected, int queueSize, int numProducers, int gid,
                                     List<IProjectedIteratorCallback> callbacks) {
        this.projected = projected;
        this.numProducers = numProducers;
        this.callbacks = callbacks;
        pdfsQueue = new LinkedBlockingDeque<PDFSCompact>(queueSize);
        firstEdges = new ArrayList<ProjectedEdge>();
        for (Integer vertexEdgesGid : projected.getProjected().get(0).keySet()) {
            if (vertexEdgesGid != gid) {
                continue;
            }

            Map<Integer, Set<ProjectedEdge>> vertexEdges = projected.getProjected().get(0).get(vertexEdgesGid);
            for (Set<ProjectedEdge> edges : vertexEdges.values()) {
                firstEdges.addAll(edges);
            }
        }
        firstEdgeIndex = 0;

        advance();
    }

    @Override
    public boolean hasNext() {
        return nextPDFS != null;
    }

    @Override
    public PDFSCompact next() {
        // return null if there is no active producers
        if (numRunningProducers == 0) {
            return null;
        }
        PDFSCompact pdfs = nextPDFS;
        try {
            if (pdfsQueue.size() == 0) {
                //System.out.println("queue is empty");
            }
            // consume next projection
            // checks if projection is PDFSCompact.POISON_PDFSCompact and decreases number of active producers if it is
            nextPDFS = pdfsQueue.take();
            if (nextPDFS == PDFSCompact.POISON_PDFSCompact) {
                nextPDFS = null;
            }
            while (nextPDFS == null) {
                numRunningProducers--;
                if (numRunningProducers == 0) {
                    return pdfs;
                }

                nextPDFS = pdfsQueue.take();
                if (nextPDFS == PDFSCompact.POISON_PDFSCompact) {
                    nextPDFS = null;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return pdfs;
    }

    /**
     * creates first edges chunks of equal size and starts producer thread for each chunk
     */
    private void advance() {
        // chunk size
        int size = firstEdges.size() / numProducers;
        // first firstEdges.size() % numProducers producers are started with chunk of (chunk size + 1)
        for (int p = 0; p < firstEdges.size() % numProducers; p++) {
            int from = p * (size + 1);
            int to = from + size + 1;
            if (to > from) {
                List<ProjectedEdge> producerFirstEdges = firstEdges.subList(from, to);
                ThreadPool.getProjectedIteratorProducersInstance().getExecutorService().submit(new ProjectedIteratorProducer(projected, pdfsQueue,
                        control, producerFirstEdges, callbacks));
                numRunningProducers++;
            }
        }

        // remaining producers are started with chunk of chunk size
        for (int p = firstEdges.size() % numProducers; p < numProducers; p++) {
            int from = firstEdges.size() % numProducers + p * size;
            int to = from + size;
            if (to > from) {
                List<ProjectedEdge> producerFirstEdges = firstEdges.subList(from, to);
                ThreadPool.getProjectedIteratorProducersInstance().getExecutorService().submit(new ProjectedIteratorProducer(projected, pdfsQueue,
                        control, producerFirstEdges, callbacks));
                numRunningProducers++;
            }
        }

        if (numRunningProducers > 0) {
            try {
                // consume next projection
                // checks if projection is PDFSCompact.POISON_PDFSCompact and decreases number of active producers if it is
                nextPDFS = pdfsQueue.take();
                if (nextPDFS == PDFSCompact.POISON_PDFSCompact) {
                    nextPDFS = null;
                }
                while (nextPDFS == null) {
                    numRunningProducers--;
                    if (numRunningProducers == 0) {
                        return;
                    }

                    nextPDFS = pdfsQueue.take();
                    if (nextPDFS == PDFSCompact.POISON_PDFSCompact) {
                        nextPDFS = null;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * stop all producer threads
     */
    public void stop() {
        control.runFlag = false;
        while (hasNext()) {
            next();
        }
    }

    public ProjectedCompact getProjected() {
        return projected;
    }

    public class Control {
        public volatile boolean runFlag = true;
    }
}
