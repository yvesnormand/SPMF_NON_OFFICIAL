package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.hui_miner.AlgoCHUIMinerMax;
import ca.pfv.spmf.algorithms.frequentpatterns.hui_miner.Itemset;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


/**
 * Example of how to use the CHUI-Miner(MAX) algorithm
 * from the source code and save the result to memory.
 *
 * @author Philippe Fournier-Viger, 2014
 */
public class MainTestCHUIMinerMax_saveToMemory {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("random_1.txt");
        int min_utility = 1;

        // (1) Applying the algorithm to find
        // maximal high utility itemsets (CHUIs)

        AlgoCHUIMinerMax algorithm = new AlgoCHUIMinerMax(true);
        List<Itemset> maximalItemsets = algorithm.runAlgorithm(input, min_utility, null);
        algorithm.printStats();

        //  (2) PRINTING THE ITEMSETS FOUND TO THE CONSOLE
        for (Itemset itemset : maximalItemsets) {
            Arrays.sort(itemset.itemset);
            System.out.println(itemset);
        }
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestCHUIMinerMax_saveToMemory.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
