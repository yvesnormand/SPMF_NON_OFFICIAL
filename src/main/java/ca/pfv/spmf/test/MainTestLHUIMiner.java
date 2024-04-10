package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.lhui.AlgoLHUIMiner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the LHUI-Miner algorithm from the source code
 *
 * @author Yimin Zhang, Philippe Fournier-Viger
 * @see AlgoLHUIMiner
 */
public class MainTestLHUIMiner {

    public static void main(String[] args) throws IOException {
        // Local minimum utility threshold
        long lminutil = 40;

        // Window size
        int windowSize = 3;

        // Input file
        String inputFile = fileToPath("DB_LHUI.txt");

        // Output file
        String outputFile = "output.txt";

        AlgoLHUIMiner lhuiminer = new AlgoLHUIMiner();
        lhuiminer.runAlgorithm(inputFile, outputFile, lminutil, windowSize);
        lhuiminer.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestLHUIMiner.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }

}
