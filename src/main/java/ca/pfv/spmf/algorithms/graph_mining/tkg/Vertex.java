package ca.pfv.spmf.algorithms.graph_mining.tkg;

import java.util.ArrayList;
import java.util.List;
/* This file is copyright (c) 2018 by Chao Cheng
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
 * This is an implementation of a vertex, used by the GSPAN algorithm
 * <br/><br/>
 * <p>
 * The GSPAN algorithm is described  in :
 * <br/><br/>
 * <p>
 * gSpan: Graph-Based Substructure Pattern Mining, by X. Yan and J. Han.
 * Proc. 2002 of Int. Conf. on Data Mining (ICDM'02
 * <p>
 * <br/><br/>
 * <p>
 * The Gspan algorithm finds all the frequents subgraphs and their support in a
 * graph provided by the user.
 * <br/><br/>
 * <p>
 * This implementation saves the result to a file
 *
 * @author Chao Cheng
 * @see AlgoGSPAN
 */
public class Vertex implements Comparable<Object> {

    /**
     * the vertex id
     */
    private final int id;

    /**
     * the vertex label
     */
    private final int vLabel;

    /**
     * the list of edges starting from this vertex
     */
    private final List<Edge> eList;

    /**
     * Constructor
     *
     * @param id     the vertex id
     * @param vLabel the vertex label
     */
    public Vertex(int id, int vLabel) {
        this.id = id;
        this.vLabel = vLabel;
        eList = new ArrayList<>();
    }

    /**
     * Add an edge to this vertex
     *
     * @param edge an edge
     */
    public void addEdge(Edge edge) {
        eList.add(edge);
    }

    /**
     * Get the id of this vertex
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the label of this vertex
     *
     * @return the label
     */
    public int getLabel() {
        return vLabel;
    }

    /**
     * Get the list of edges from this vertex
     *
     * @return the list of edges
     */
    public List<Edge> getEdgeList() {
        return eList;
    }

    @Override
    public int compareTo(Object o) {
        Vertex vertex = (Vertex) o;
        return id - vertex.getId();
    }

    @Override
    /**
     * Compare this vertex with another vertex
     * @param obj another Vertex
     * @return true if they have the same id.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof final Vertex vertex)) {
            return false;
        }
        return id == vertex.id;
    }
}
