package ca.pfv.spmf.algorithms.sequentialpatterns.clofast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the Fast algorithm in source code.
 */
public class MainTestFast {

    public static void main(String[] arg) throws IOException {


        // input file
        String inputFile = fileToPath("contextPrefixSpan.txt");

        // output file path
        String outputPath = ".//output.txt";

        // Create an instance of the algorithm with minsup = 50 %
        float minsup = 0.4f;

        //=== THIS IS THE FIRST WAY OF RUNNING CLOFAST =====
        // It reads the dataset and run the algorithm.

        // run the algoritm
        AlgoFast algorithm = new AlgoFast();
        algorithm.runAlgorithm(inputFile, outputPath, minsup);

        algorithm.printStatistics();

        //=== THIS IS THE SECOND WAY OF RUNNING CLOFAST =====
        // It should be used if you want to run CloFast several times
        // on the same dataset. In that case, the total execution time
        // does not include the time for reading the database

//		FastDataset dataset = FastDataset.fromPrefixspanSource(inputFile, minsup);
//        CloFast algorithm = new CloFast();
//        algorithm.runAlgorithm(dataset, outputPath, minsup);
//        
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestFast.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}