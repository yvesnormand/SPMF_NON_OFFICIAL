package ca.pfv.spmf.test;


import ca.pfv.spmf.algorithms.frequentpatterns.tko.AlgoTKO_Basic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class to test the TKO-Basic algorithm
 *
 * @author Philippe Fournier-Viger, 2018
 */
public class MainTestTKOBasic {

    public static void main(String[] arg) throws IOException {

        // input file path
        String input = fileToPath("DB_Utility.txt");

        // output file path
        String output = "output.txt";

        // the parameter k
        int k = 8;

        // Applying the algorithm
        AlgoTKO_Basic algorithm = new AlgoTKO_Basic();
        algorithm.runAlgorithm(input, output, k);
        algorithm.writeResultTofile(output);

        // Print statistics about the algorithm execution
        algorithm.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestTKOBasic.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
