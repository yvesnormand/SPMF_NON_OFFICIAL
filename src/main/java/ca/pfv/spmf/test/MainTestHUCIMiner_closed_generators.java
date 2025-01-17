package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.associationrules.hgb.AlgoFHIM_and_HUCI;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to run the HUCI-Miner algorithm from the source code.
 *
 * @author Philippe Fournier-Viger, 2014
 */
public class MainTestHUCIMiner_closed_generators {

    public static void main(String[] arg) throws IOException {
        String input = fileToPath("DB_Utility.txt");
        String output = ".//output.txt";

        int min_utility = 30; //

        // Applying the HUIMiner algorithm
        AlgoFHIM_and_HUCI algorithm = new AlgoFHIM_and_HUCI();
        algorithm.runAlgorithmHUCIMiner(input, output, min_utility);
        algorithm.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestHUCIMiner_closed_generators.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
