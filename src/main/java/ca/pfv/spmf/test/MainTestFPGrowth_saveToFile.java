package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use FPGrowth from the source code and save
 * the resutls to a file.
 *
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestFPGrowth_saveToFile {

    public static void main(String[] arg) throws IOException {
        // the file paths
        String input = fileToPath("contextPasquier99.txt");  // the database
        String output = ".//output.txt";  // the path for saving the frequent itemsets found

        double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)

        // Applying the FPGROWTH algorithmMainTestFPGrowth.java
        AlgoFPGrowth algo = new AlgoFPGrowth();

        // Uncomment the following line to set the maximum pattern length (number of items per itemset, e.g. 3 )
//		algo.setMaximumPatternLength(3);

        // Uncomment the following line to set the minimum pattern length (number of items per itemset, e.g. 2 )
//		algo.setMinimumPatternLength(2);

        algo.runAlgorithm(input, output, minsup);
        algo.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestFPGrowth_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
