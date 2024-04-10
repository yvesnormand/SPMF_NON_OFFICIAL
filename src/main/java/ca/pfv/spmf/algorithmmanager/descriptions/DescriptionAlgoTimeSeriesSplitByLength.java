package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.timeseries.TimeSeries;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesWriter;
import ca.pfv.spmf.algorithms.timeseries.split.AlgoSplitTimeSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class describes the algorithm to split time series by length
 *
 * @author Philippe Fournier-Viger
 * @see AlgoSplitTimeSeries
 */
public class DescriptionAlgoTimeSeriesSplitByLength extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTimeSeriesSplitByLength() {
    }

    @Override
    public String getName() {
        return "Split_time_series_by_length";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TIME SERIES MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SplitTimeSeriesByLength.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int sizeOfSegment = getParamAsInteger(parameters[0]);

        String separator;
        if (parameters.length > 1 && !"".equals(parameters[1])) {
            separator = getParamAsString(parameters[1]);
        } else {
            separator = " ";
        }

        // (1) Read the time series
        AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
        List<TimeSeries> multipleTimeSeries = reader.runAlgorithm(inputFile, separator);

        // (2) Calculate the moving average of each time series
        List<TimeSeries> allSplittedTimeSeries = new ArrayList<TimeSeries>(multipleTimeSeries.size() * 2);
        for (TimeSeries timeSeries : multipleTimeSeries) {
            AlgoSplitTimeSeries algorithm = new AlgoSplitTimeSeries();
            TimeSeries[] splittedTimeSeries = algorithm.runAlgorithm(timeSeries, sizeOfSegment);
            algorithm.printStats();

            // not very efficient to do that...
            Collections.addAll(allSplittedTimeSeries, splittedTimeSeries);
        }

        // (3) write the time series to a file
        AlgoTimeSeriesWriter algorithm2 = new AlgoTimeSeriesWriter();
        algorithm2.runAlgorithm(outputFile, allSplittedTimeSeries, separator);
        algorithm2.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Size of segments", "(e.g. 3)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("separator", "(e.g. ',' , default: ' ')", String.class, true);

        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Time series database" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Time series database" };
    }

}
