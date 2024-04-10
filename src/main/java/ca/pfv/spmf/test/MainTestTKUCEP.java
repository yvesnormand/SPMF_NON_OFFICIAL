package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.tkuce.AlgoTKUCEP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to run TKU-CEP from the source code.
 * <p>
 * Heuristically mining the top-k high-utility itemsets with cross-entropy
 * optimization
 *
 * @author Wei Song, Chuanlong Zheng, Chaomin Huang, and Lu Liu
 * @see AlgoTKUCEP
 */

public class MainTestTKUCEP {
    public static void main(String[] arg) throws IOException {
        // input file
        String input = fileToPath("DB_Utility.txt");

        // output file
        String output = ".//output.txt";

        // the number of top-k huis
        int k = 3;

        AlgoTKUCEP tkucep = new AlgoTKUCEP();
        tkucep.runAlgorithm(input, output, k);
        tkucep.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestTKUCEP.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}