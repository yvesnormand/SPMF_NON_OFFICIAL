package ca.pfv.spmf.algorithms.graph_mining.aerminer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
/* This file is copyright (c) 2020 by Ganghuan He
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
 * To read the input graph
 *
 * @author Ganghuan He 2020
 * @see AERMiner
 */
public class ReadGraph {
    /**
     * indicate whether to store all attribute values as type of double
     **/
    private static final boolean ALLASDOUBLE = true;
    /**
     * set maximal number of attribute
     */
    private static int TOTAL_NUM_ATTR = ParametersSettingAERMiner.TOTAL_NUM_ATTR;
    /**
     * store path of file that record attributes of vertices each time
     */
    private static String ATTR_FILE_PATH = ParametersSettingAERMiner.ATTR_FILE_PATH;
    /**
     * store path of file that record edges of vertices each time
     */
    private static String EDGE_FILE_PATH = ParametersSettingAERMiner.EDGE_FILE_PATH;

    public static void main(String[] args) throws IOException {
        //readGraph();
//        readAttrMapping();
        statGraph();
    }


    public static void statGraph() throws IOException {
        Map<Integer, AttributedGraph> dyAG = readGraph();

        System.out.println(dyAG.get(0).getAllVerticeId());
        int numTimestamps = dyAG.size();
        int numVertices = dyAG.get(0).getVerNum();
        int totalCount4E = 0;
        for (Entry<Integer, AttributedGraph> entry : dyAG.entrySet()) {
            Integer i = entry.getKey();

            AttributedGraph aG = dyAG.get(i);
            for (Map.Entry<Integer, Set<Integer>> edgeLinkEntry : aG.getEdgesMap().entrySet()) {
                totalCount4E += edgeLinkEntry.getValue().size();
            }
        }
        final String sb = "total timestamps: " + numTimestamps +
                          "\ntotal vertices: " + numVertices +
                          "\naverage edges for each vertex each timestamp: " + 1.0 * totalCount4E / (numTimestamps * numVertices);
        System.out.println(sb);
    }

    public static Map<Integer, AttributedGraph> readGraph() throws IOException {
        System.out.println("@@@ start to read original graph ...");
        //create an empty DyAG, use a map denote this DyAG
        Map<Integer, AttributedGraph> DyAG = new HashMap<>();

        readAttributes(DyAG);
        readEdges(DyAG);

        System.out.println("reading graph finish !");

        //test whether read attributes and edges successfully
        return DyAG;

    }

    private static void readEdges(Map<Integer, AttributedGraph> DyAG) throws IOException {
        TOTAL_NUM_ATTR = ParametersSettingAERMiner.TOTAL_NUM_ATTR;
        ATTR_FILE_PATH = ParametersSettingAERMiner.ATTR_FILE_PATH;
        EDGE_FILE_PATH = ParametersSettingAERMiner.EDGE_FILE_PATH;
        //add edges for DyAG according to file "graph - Copie.txt"
        //same with previous process except here we do not need to create new attributed graph
        //and use sub method of edgeLineProcess
//        System.out.println(EDGE_FILE_PATH);
        BufferedReader brEdges = new BufferedReader(new FileReader(EDGE_FILE_PATH));
        String line2 = brEdges.readLine();
        while (line2 != null) {
            if (line2.startsWith("T")) {
                int aGId = Integer.parseInt(line2.split("T")[1]);
                AttributedGraph aG = DyAG.get(aGId);
                while ((line2 = brEdges.readLine()) != null && !line2.startsWith("T")) {
                    edgeLineProcess(aG, line2);
                }
            }
        }
        brEdges.close();
    }


    private static void readAttributes(Map<Integer, AttributedGraph> DyAG) throws IOException {
        //add vertices and attributes for an empty DyAG according to file "attributes.txt"
        ATTR_FILE_PATH = ParametersSettingAERMiner.ATTR_FILE_PATH;
        BufferedReader brAttr = new BufferedReader(new FileReader(ATTR_FILE_PATH));
        String line1 = brAttr.readLine();
        int count = 0;
        //while still has unprocessed line
        while (line1 != null) {
            //if it indicate a new attributed graph
            if (line1.startsWith("T")) {
                AttributedGraph aG = new AttributedGraph(count);
                while ((line1 = brAttr.readLine()) != null && !line1.startsWith("T")) {
                    attrLineProcess(aG, line1);
                }
                DyAG.put(count, aG);
            }
            count++;
        }
        brAttr.close();
    }

    /**
     * This method process each edge line from "graph - Copie.txt" to add edges
     *
     * @param aG   the attributed graph
     * @param line the edge line to be processed
     */
    private static void edgeLineProcess(AttributedGraph aG, String line) {
        String[] items = line.split(" ");
        //value of first position denote id of the common vertex linking to rest vertices in the line
        Integer vId = Integer.parseInt(items[0]);
        // store ids of all other neighboring vertices
        List<Integer> neighbors = new LinkedList<>();
        //for each item other than the first one
        for (int i = 1; i < items.length; i++) {
            //parse it to integer and add it to id list
            neighbors.add(Integer.parseInt(items[i]));
        }
        aG.addEdges(vId, neighbors);
    }

    /**
     * This method process each attribute line from "attributes.txt" to create vertex and add it to DyAG
     *
     * @param aG   the attributed graph associated with this line
     * @param line the attribute line to be processed
     */
    private static void attrLineProcess(AttributedGraph aG, String line) {
        String[] items = line.split(" ");
        //value of first position denote id of the vertex
        Integer vId = Integer.parseInt(items[0]);
        aG.addVertex(vId);
        if (ALLASDOUBLE) {
            //store all attribute values as type of double
            //attribute type list
            List<Integer> attrTypes = new LinkedList<>();
            //attribute value list
            List<Double> attrVals = new LinkedList<>();
            for (int i = 1; i < TOTAL_NUM_ATTR + 1; i++) {
                Double val = Double.parseDouble(items[i]);
                attrTypes.add(i);
                attrVals.add(val);
            }
            //add attribute types and values
            aG.addAttrValForV(vId, attrTypes, attrVals);
        }
    }

}
