package ca.pfv.spmf.test;

import ca.pfv.spmf.tools.other_dataset_tools.FixTransactionDatabaseTool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the tool to fix common problem in a transaction database
 * in SPMF format.
 */
public class MainTestFixTransactionDatabase {

    public static void main(String[] arg) throws IOException {

        String inputFile = fileToPath("contextIncorrect.txt");
        String outputFile = ".//output.txt";

        FixTransactionDatabaseTool tool = new FixTransactionDatabaseTool();
        tool.convert(inputFile, outputFile);
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestFixTransactionDatabase.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
