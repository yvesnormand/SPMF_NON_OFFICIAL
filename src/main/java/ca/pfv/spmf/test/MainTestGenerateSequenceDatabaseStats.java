package ca.pfv.spmf.test;

import ca.pfv.spmf.tools.dataset_stats.SequenceStatsGenerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to generate statistics about a sequence database
 */
public class MainTestGenerateSequenceDatabaseStats {

    public static void main(String[] arg) throws IOException {

        String inputFile = fileToPath("contextPrefixSpan.txt");
        try {
            SequenceStatsGenerator sequenceDatabase = new SequenceStatsGenerator();
            sequenceDatabase.getStats(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestApriori_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
