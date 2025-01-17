package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.AlgoTopKClassRules;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.Database;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to usAlgoTopKClassRules.javae the TOPKClassRules algorithm in source code.
 * This algorithm is designed to find association rules where the consequent
 * is fixed to a specific item.
 *
 * @author Philippe Fournier-Viger (Copyright 2010)
 */
public class MainTestTopKClassRules {

    public static void main(String[] arg) throws Exception {
        // Load database into memory
        Database database = new Database();
        database.loadFile(fileToPath("contextIGB.txt"));

        int k = 7;
        double minConf = 0.8; //

        // the item to be used as consequent for generating rules
        int[] itemToBeUsedAsConsequent = new int[] { 1, 2 };

        AlgoTopKClassRules algo = new AlgoTopKClassRules();

//		// This optional parameter allows to specify the maximum number of items in the 
//		// left side (antecedent) of rules found:
//		algo.setMaxAntecedentSize(2);  // optional

//		// This optional parameter allows to specify a maximum support for the rules to be found.
//		// The support is a percentage. This is useful to find rare rules
//		algo.setMaxSupport(0.5);

//
        algo.runAlgorithm(k, minConf, database, itemToBeUsedAsConsequent);

        algo.printStats();
        algo.writeResultTofile("output.txt");   // to save results to file

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestTopKClassRules.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
