package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.eclat.AlgoDEclat;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * Example of how to use dECLAT algorithm from the source code.
 *
 * @author Philippe Fournier-Viger - 2009
 */
public class MainTestDEclat_saveToFile {

    public static void main(String[] arg) throws IOException {

        // the file paths
        String input = fileToPath("contextPasquier99.txt");  // the database
        String output = ".//output.txt";  // the path for saving the frequent itemsets found

        // minimum support
        double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)

        // Loading the transaction database
        TransactionDatabase database = new TransactionDatabase();
        try {
            database.loadFile(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		context.printContext();

        // Applying the dECLAT algorithm
        AlgoDEclat algo = new AlgoDEclat();

        // Uncomment the following line to set the maximum pattern length (number of items per itemset)
//		algo.setMaximumPatternLength(3);

        algo.runAlgorithm(output, database, minsup, true);
        // if you change use "true" in the line above, dECLAT will use
        // a triangular matrix  for counting support of itemsets of size 2.
        // For some datasets it should make the algorithm faster.

        algo.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestDEclat_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
