package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.episodes.emma.AlgoAFEM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This file shows how to run the AFEM algorithm on an input file.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoAFEM
 */
public class MainTestAFEM_Simple {
    public static void main(String[] args) throws IOException {

        // the Input and output files
        String inputFile = fileToPath("contextEMMA.txt");
        String outputFile = "output.txt";

        // The algorithm parameters:
        int minSup = 2;
        int maxWindow = 2;

        // If the input file does not contain timestamps, then set this variable to true
        // to automatically assign timestamps as 1,2,3...
        boolean selfIncrement = false;


        // self-growth = flase only for online_minute.txt , others are true
        AlgoAFEM algo = new AlgoAFEM();
        algo.runAlgorithm(inputFile, outputFile, minSup, maxWindow, selfIncrement);
        algo.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestAFEM_Simple.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
