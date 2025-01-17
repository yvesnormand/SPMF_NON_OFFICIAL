package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.hui_miner.AlgoFHN;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the FHN algorithm
 * from the source code.
 *
 * @author Philippe Fournier-Viger, 2014
 */
public class MainTestFHN_saveToFile {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("DB_NegativeUtility.txt");
        String output = ".//output.txt";

        int min_utility = 80;

        // Applying the FHN algorithm
        AlgoFHN algo = new AlgoFHN();
        algo.runAlgorithm(input, output, min_utility);
        algo.printStats();

    }
    //"C:\\Users\\phil\\Desktop\\pumsb_negative.txt"

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestFHN_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
