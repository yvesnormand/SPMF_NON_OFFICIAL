package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPMax;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use FPMax from the source code and save
 * the resutls to a file.
 *
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestFPMax_saveToFile {

    public static void main(String[] arg) throws IOException {
        // the file paths
        String input = fileToPath("contextPasquier99.txt");  // the database
        String output = ".//output.txt";  // the path for saving the frequent itemsets found

        double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)

        // Applying the FPMax algorithm
        AlgoFPMax algo = new AlgoFPMax();
        algo.runAlgorithm(input, output, minsup);
        algo.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestFPMax_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
