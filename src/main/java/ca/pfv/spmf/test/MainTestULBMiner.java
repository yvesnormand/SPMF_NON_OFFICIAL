package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.ulbminer.AlgoULBMiner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the ULB-Miner algorithm
 * from the source code.
 *
 * @author Philippe Fournier-Viger, 2017
 */
public class MainTestULBMiner {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("DB_Utility.txt");
        String output = ".//output.txt";

        int min_utility = 30;  //

        // Applying the algorithm
        AlgoULBMiner algorithm = new AlgoULBMiner();
        algorithm.runAlgorithm(input, output, min_utility);
        algorithm.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestULBMiner.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
