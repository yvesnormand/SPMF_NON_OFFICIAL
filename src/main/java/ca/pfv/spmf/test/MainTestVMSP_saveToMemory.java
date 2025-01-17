package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.sequentialpatterns.spam.AlgoVMSP;
import ca.pfv.spmf.algorithms.sequentialpatterns.spam.PatternVMSP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TreeSet;


/**
 * Example of how to use the VMSP algorithm in source code.
 *
 * @author Philippe Fournier-Viger
 */
public class MainTestVMSP_saveToMemory {

    public static void main(String[] arg) throws IOException {
        // Load a sequence database
        String input = fileToPath("dataVMSP.txt");
//		String input = fileToPath("contextPrefixSpan.txt");
        String output = ".//output.txt";

        // Create an instance of the algorithm
        AlgoVMSP algo = new AlgoVMSP();
        algo.setMaximumPatternLength(8);
        algo.setMaxGap(5);


//		 Maxgap = 2  support = 2
//		 3 -1   support : 2
//		 1 -1   support : 2
        // Maxgap = 3
        //  1 -1 3 -1   support : 2

//		#1: 41 -1 42 -1 support : 302
//		#2: 41 -1 8 -1 42 -1 support : 307
//		- minsup = 0.12
//				- maxgap = 5
//				- maximumPatternLength = 8

        // execute the algorithm with minsup = 2 sequences  (50 %)
        List<TreeSet<PatternVMSP>> maxPatterns = algo.runAlgorithm(input, output, 0.12);
        algo.printStatistics();

        // PRINT THE PATTTERNS FOUND
        for (TreeSet<PatternVMSP> tree : maxPatterns) {
            if (tree == null) {
                continue;
            }
            // for each pattern
            for (PatternVMSP pattern : tree) {
                System.out.println(" " + pattern.getPrefix() + "  support : " + pattern.getSupport());
            }
        }
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestVMSP_saveToMemory.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}