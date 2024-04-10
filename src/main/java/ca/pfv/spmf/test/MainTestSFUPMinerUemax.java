package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.SFUPMinerUemax.AlgoSFUPMinerUemax;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * Example of how to use the SFUPMinerUemax algorithm
 * from the source code.
 *
 * @author Jerry Chun-Wei Lin, Lu Yang, Philippe Fournier-Viger, 2016
 */
public class MainTestSFUPMinerUemax {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("contextHUIM.txt");
        String output = ".//output.txt";

        // Applying the SFUPMinerUemax algorithm
        AlgoSFUPMinerUemax sfupMinerUemax = new AlgoSFUPMinerUemax();
        sfupMinerUemax.runAlgorithm(input, output);
        sfupMinerUemax.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestSFUPMinerUemax.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
