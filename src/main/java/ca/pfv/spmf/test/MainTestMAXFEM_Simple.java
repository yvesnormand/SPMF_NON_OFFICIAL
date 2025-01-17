package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.episodes.maxfem.AlgoMAXFEM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This file shows how to run the MAXFEM algorithm on an input file.
 *
 * @author Philippe Fournier-Viger
 */
public class MainTestMAXFEM_Simple {
    public static void main(String[] args) throws IOException {

        // the Input and output files
        String inputFile = fileToPath("contextMAXFEM.txt");
        String outputFile = "output.txt";

        // The algorithm parameters:
        int minSup = 2;
        int maxWindow = 3;

        // If the input file does not contain timestamps, then set this variable to true
        // to automatically assign timestamps as 1,2,3...
        boolean selfIncrement = false;

        AlgoMAXFEM algo = new AlgoMAXFEM();
        algo.runAlgorithm(inputFile, outputFile, minSup, maxWindow, selfIncrement);
        algo.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestMAXFEM_Simple.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
