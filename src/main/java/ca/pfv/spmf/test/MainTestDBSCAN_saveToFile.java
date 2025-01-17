package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.clustering.dbscan.AlgoDBSCAN;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the DBSCAN algorithm, in source code.
 */
public class MainTestDBSCAN_saveToFile {

    public static void main(String[] args) throws NumberFormatException, IOException {

        String input = fileToPath("inputDBScan2.txt");
        String output = ".//output.txt";

        // we set the parameters of DBScan:
        int minPts = 2;
        double epsilon = 2d;

        // We specify that in the input file, double values on each line are separated by spaces
        String separator = " ";

        // Apply the algorithm
        AlgoDBSCAN algo = new AlgoDBSCAN();

        algo.runAlgorithm(input, minPts, epsilon, separator);
        algo.printStatistics();
        algo.saveToFile(output);
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestDBSCAN_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }


}
