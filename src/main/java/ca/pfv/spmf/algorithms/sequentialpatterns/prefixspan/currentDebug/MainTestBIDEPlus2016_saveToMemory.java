package ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.currentDebug;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * Example of how to use the BIDE+ algorithm, from the source code.
 *
 * @author Philippe Fournier-Viger
 */
public class MainTestBIDEPlus2016_saveToMemory {

    public static void main(String[] arg) throws IOException {
        // input sequence database file path
        String input = fileToPath("contextPrefixSpan.txt");

        // Create an instance of the algorithm
        AlgoBIDEPlus algo = new AlgoBIDEPlus();

        // if you set the following parameter to true, the sequence ids of the sequences where
        // each pattern appears will be shown in the result
        boolean showSequenceIdentifiers = true;

        // execute the algorithm
        SequentialPatterns patterns = algo.runAlgorithm(input, null, 2);
        patterns.printFrequentPatterns(algo.patternCount, showSequenceIdentifiers);
        // print statistics
        algo.printStatistics();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestBIDEPlus2016_saveToMemory.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}