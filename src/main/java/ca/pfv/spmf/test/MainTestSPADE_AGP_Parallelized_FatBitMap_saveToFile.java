package ca.pfv.spmf.test;


import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPADE;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_FatBitmap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the algorithm SPADE, saving the results in a given
 * file
 *
 * @author agomariz
 */
public class MainTestSPADE_AGP_Parallelized_FatBitMap_saveToFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String outputPath = ".//output.txt";
        // Load a sequence database
        double support = 0.5;

        boolean keepPatterns = true;
        boolean verbose = false;

        AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
        boolean dfs = true;

        // if you set the following parameter to true, the sequence ids of the sequences where
        // each pattern appears will be shown in the result
        boolean outputSequenceIdentifiers = false;

        IdListCreator idListCreator = IdListCreator_FatBitmap.getInstance();

        CandidateGenerator candidateGenerator = CandidateGenerator_Qualitative.getInstance();

        SequenceDatabase sequenceDatabase = new SequenceDatabase(abstractionCreator, idListCreator);

        sequenceDatabase.loadFile(fileToPath("contextPrefixSpan.txt"), support);

        System.out.println(sequenceDatabase);

        AlgoSPADE algorithm = new AlgoSPADE(support, dfs, abstractionCreator);

        algorithm.runAlgorithmParallelized(sequenceDatabase, candidateGenerator, keepPatterns, verbose, outputPath, outputSequenceIdentifiers);

        System.out.println("Minimum support (relative) = " + support);
        System.out.println(algorithm.getNumberOfFrequentPatterns() + " frequent patterns.");

        System.out.println(algorithm.printStatistics());
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestSPADE_AGP_FatBitMap_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
