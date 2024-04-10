package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.hui_miner.AlgoFHM_Freq;

import java.io.IOException;

/**
 * This class describes the FHMFreq algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoApriori
 */
public class DescriptionAlgoFHMFreq extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoFHMFreq() {
    }

    @Override
    public String getName() {
        return "FHMFreq";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/FHMFreq.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minutil = getParamAsInteger(parameters[0]);
        double minsup = getParamAsDouble(parameters[1]);
        // Applying the algorithm
        AlgoFHM_Freq algo = new AlgoFHM_Freq();
        algo.runAlgorithm(inputFile, outputFile, minutil, minsup);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 30)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Minimum support", "(e.g. 0.40)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Transaction database", "Transaction database with utility values" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Patterns", "High-utility patterns", "High-utility itemsets", "Frequent high-utility itemsets" };
    }

}
