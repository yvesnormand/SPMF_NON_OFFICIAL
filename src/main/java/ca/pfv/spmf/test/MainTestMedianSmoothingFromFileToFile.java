package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.timeseries.TimeSeries;
import ca.pfv.spmf.algorithms.timeseries.mediansmoothing.AlgoMedianSmoothing;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of how to calculate the median smoothing of a of time series, using
 * the source code of SPMF, by reading a time series file and writing a time series file as output
 *
 * @author Philippe Fournier-Viger, 2018.
 */
public class MainTestMedianSmoothingFromFileToFile {

    public static void main(String[] arg) throws IOException {

        // the input file
        String input = fileToPath("contextMovingAverage.txt");
        // the output file
        String output = "./output.txt";

        // the number of data points that we want to use for the moving average
        int windowSize = 3;

        // The separator to be used for reading/writing the input/output file
        String separator = ",";

        // (1) Read the time series
        AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
        List<TimeSeries> multipleTimeSeries = reader.runAlgorithm(input, separator);

        // (2) Calculate the median smoothing of each time series
        List<TimeSeries> medianSmoothingMultipleTimeSeries = new ArrayList<TimeSeries>();
        for (TimeSeries timeSeries : multipleTimeSeries) {
            AlgoMedianSmoothing algorithm = new AlgoMedianSmoothing();
            TimeSeries medianSmoothingSeries = algorithm.runAlgorithm(timeSeries, windowSize);
            medianSmoothingMultipleTimeSeries.add(medianSmoothingSeries);
            algorithm.printStats();
        }

        // (3) write the time series to a file
        AlgoTimeSeriesWriter algorithm2 = new AlgoTimeSeriesWriter();
        algorithm2.runAlgorithm(output, medianSmoothingMultipleTimeSeries, separator);
        algorithm2.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestMedianSmoothingFromFileToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
