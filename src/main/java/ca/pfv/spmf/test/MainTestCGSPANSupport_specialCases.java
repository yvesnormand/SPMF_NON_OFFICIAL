package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.graph_mining.tkg.AlgoCGSPANSupport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the CGSPAN algorithm
 * from the source code and output the result to a file.
 * <p>
 * This example shows the five cases where  early termination failure may occur.
 * By activating the early termination failure detection, the complete results can be found.
 * But it is also possible deactivate the detection to obtain a trade-off between completness and speed.
 *
 * @author Zevin Shaul
 */
public class MainTestCGSPANSupport_specialCases {

    public static void main(String[] arg) throws IOException, ClassNotFoundException, InterruptedException {

        // set the input and output file path
        String input = fileToPath("early_termination_failure/reason_1");
//        String input = fileToPath("early_termination_failure/reason_2");
//        String input = fileToPath("early_termination_failure/reason_3");
//        String input = fileToPath("early_termination_failure/reason_4");
//        String input = fileToPath("early_termination_failure/reason_5");
        String output = ".//output.txt";
        // set the minimum support threshold
        double minSupport = 1.0;
        // The maximum number of edges for frequent subgraph patterns
        int maxNumberOfEdges = Integer.MAX_VALUE;

        // If true, single frequent vertices will be output
        boolean outputSingleFrequentVertices = false;

        // If true, a dot file will be output for visualization using GraphViz
        boolean outputDotFile = false;

        // Output the ids of graph containing each frequent subgraph
        boolean outputGraphIds = true;

        // Create the algorithm
        AlgoCGSPANSupport algo = new AlgoCGSPANSupport();

        // If the following line is uncommented, extra information is stored in the output file
        algo.setDebugMode(false);

        // run the algorithm
        algo.runAlgorithm(input, output, minSupport, outputSingleFrequentVertices,
                outputDotFile, maxNumberOfEdges, outputGraphIds);

        // Print statistics about the algorithm execution
        algo.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestGSPAN.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
