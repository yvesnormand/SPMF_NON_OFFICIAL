package ca.pfv.spmf.test;


import ca.pfv.spmf.algorithms.sequentialpatterns.clospan_AGP.AlgoCloSpan;
import ca.pfv.spmf.algorithms.sequentialpatterns.clospan_AGP.items.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.clospan_AGP.items.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.clospan_AGP.items.creators.AbstractionCreator_Qualitative;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the algorithm CloSpan, saving the results in the
 * main memory
 *
 * @author agomariz
 */
public class MainTestCloSpan_saveToMemory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // Load a sequence database
        double support = (double) 180 / 360;

        boolean keepPatterns = true;
        boolean verbose = false;
        boolean findClosedPatterns = true;
        boolean executePruningMethods = true;

        // if you set the following parameter to true, the sequence ids of the sequences where
        // each pattern appears will be shown in the result
        boolean outputSequenceIdentifiers = false;

        AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();

        SequenceDatabase sequenceDatabase = new SequenceDatabase();

        sequenceDatabase.loadFile(fileToPath("contextPrefixSpan.txt"), support);
        //sequenceDatabase.loadFile(fileToPath("contextCloSpan.txt"), support);

        //System.out.println(sequenceDatabase.toString());

        AlgoCloSpan algorithm = new AlgoCloSpan(support, abstractionCreator, findClosedPatterns, executePruningMethods);

        algorithm.runAlgorithm(sequenceDatabase, keepPatterns, verbose, null, outputSequenceIdentifiers);
        System.out.println(algorithm.getNumberOfFrequentPatterns() + " pattern found.");

        if (keepPatterns) {
            System.out.println(algorithm.printStatistics());
        }
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestCloSpan_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
