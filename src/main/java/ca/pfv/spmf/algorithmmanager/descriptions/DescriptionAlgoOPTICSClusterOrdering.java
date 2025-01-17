package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.clustering.optics.AlgoOPTICS;

import java.io.IOException;

/**
 * This class describes the OPTICS algorithm parameters an ordering of points.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoOPTICS
 */
public class DescriptionAlgoOPTICSClusterOrdering extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoOPTICSClusterOrdering() {
    }

    @Override
    public String getName() {
        return "OPTICS-cluster-ordering";
    }

    @Override
    public String getAlgorithmCategory() {
        return "CLUSTERING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Optics.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minPts = getParamAsInteger(parameters[0]);
        double epsilon = getParamAsDouble(parameters[1]);

        //The separator
        String separator;
        if (parameters.length > 2 && !"".equals(parameters[2])) {
            separator = getParamAsString(parameters[2]);
        } else {
            separator = " ";
        }

        AlgoOPTICS algo = new AlgoOPTICS();
        algo.computerClusterOrdering(inputFile, minPts, epsilon, separator);

        algo.printStatistics();
        algo.saveClusterOrderingToFile(outputFile);
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("minPts", "(e.g. 2)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("epsilon", "(e.g. 2)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("separator", "(default: ' ')", String.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Database of double vectors" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Density-based cluster ordering of points" };
    }

}
