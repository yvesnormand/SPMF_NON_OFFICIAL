package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.sequentialpatterns.goKrimp.AlgoGoKrimp;
import ca.pfv.spmf.algorithms.sequentialpatterns.goKrimp.DataReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to use the GoKrimp Algorithm in source code and save
 * result to an output file.
 */
public class MainTestGoKrimp_saveToFile {

    public static void main(String[] arg) throws IOException {
        String inputDatabase = fileToPath("test_goKrimp.dat");  // the database
//		String inputLabelFile = fileToPath("test_goKrimp.lab");  // the label file
        String inputLabelFile = "";  // use this if no label file
        String output = ".//outputK.txt";  // the path for saving the frequent itemsets found

        DataReader d = new DataReader();
        //GoKrimp g=d.readData(inputDatabase, inputLabelFile);
        AlgoGoKrimp g = d.readData_SPMF(inputDatabase, inputLabelFile);
        //g.printData();
        g.setOutputFilePath(output); // if not set, then result will be printed to console
        g.gokrimp();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestGoKrimp_saveToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
