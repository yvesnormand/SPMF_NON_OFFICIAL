package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.timeseries.TimeSeries;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesWriter;
import ca.pfv.spmf.algorithms.timeseries.simplelinearregression.AlgoTimeSeriesLinearRegressionLeastSquare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the algorithm to calculate the simple regression of a time series
 *
 * @author Philippe Fournier-Viger
 * @see AlgoTimeSeriesLinearRegressionLeastSquare
 */
public class DescriptionAlgoTimeSeriesLinearRegressionLeastSquares extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTimeSeriesLinearRegressionLeastSquares() {
    }

    @Override
    public String getName() {
        return "Calculate_linear_regression_of_time_series_(least_squares)";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TIME SERIES MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/TimeSerieRegressionLineLeastSquare.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

        String separator;
        if (parameters.length > 1 && !"".equals(parameters[0])) {
            separator = getParamAsString(parameters[0]);
        } else {
            separator = " ";
        }

        // Get the text encoding
//		Charset charset = PreferencesManager.getInstance().getPreferedCharset();

        // (1) Read the time series
        AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
        List<TimeSeries> multipleTimeSeries = reader.runAlgorithm(inputFile, separator);


        // (2) Calculate the moving average of each time series
        List<TimeSeries> regressionLines = new ArrayList<TimeSeries>();
        for (TimeSeries timeSeries : multipleTimeSeries) {
            AlgoTimeSeriesLinearRegressionLeastSquare algorithm = new AlgoTimeSeriesLinearRegressionLeastSquare();
            algorithm.trainModel(timeSeries);
            TimeSeries regressionLine = algorithm.calculateRegressionLine(timeSeries);
            regressionLines.add(regressionLine);
            algorithm.printStats();
        }

        // (3) write the time series to a file
        AlgoTimeSeriesWriter algorithm2 = new AlgoTimeSeriesWriter();
        algorithm2.runAlgorithm(outputFile, regressionLines, separator);
        algorithm2.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("separator", "(e.g. ',' , default: ' ')", String.class, true);

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
