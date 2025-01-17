package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.timeseries.TimeSeries;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesWriter;
import ca.pfv.spmf.algorithms.timeseries.standardization.AlgoStandardization;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of how to calculate the standardization of a time series, using
 * the source code of SPMF, by reading a time series file and writing a time series file as output
 *
 * @author Philippe Fournier-Viger, 2016.
 */
public class MainTestStandardizationFileToFile {

    public static void main(String[] arg) throws IOException {

        // the input file
        String input = fileToPath("contextMovingAverage.txt");
        // the output file
        String output = "./output.txt";

        // The separator to be used for reading/writting the input/output file
        String separator = ",";

        // (1) Read the time series
        AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
        List<TimeSeries> multipleTimeSeries = reader.runAlgorithm(input, separator);


        // (2) Calculate the transformed time series
        List<TimeSeries> resultMultipleSeries = new ArrayList<TimeSeries>();
        for (TimeSeries timeSeries : multipleTimeSeries) {
            AlgoStandardization algorithm = new AlgoStandardization();
            TimeSeries aSeries = algorithm.runAlgorithm(timeSeries);
            resultMultipleSeries.add(aSeries);
            algorithm.printStats();
        }

        // (3) write the time series to a file
        AlgoTimeSeriesWriter algorithm2 = new AlgoTimeSeriesWriter();
        algorithm2.runAlgorithm(output, resultMultipleSeries, separator);
        algorithm2.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestStandardizationFileToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
