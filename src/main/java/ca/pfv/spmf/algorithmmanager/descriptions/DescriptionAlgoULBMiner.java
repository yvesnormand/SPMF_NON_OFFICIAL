package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.ulbminer.AlgoULBMiner;

import java.io.IOException;

/**
 * This class describes the ULB-Miner algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoApriori
 */
public class DescriptionAlgoULBMiner extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoULBMiner() {
    }

    @Override
    public String getName() {
        return "ULB-Miner";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/ULBMiner.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minutil = getParamAsInteger(parameters[0]);
        // Applying the algorithm
        AlgoULBMiner algo = new AlgoULBMiner();
        algo.runAlgorithm(inputFile, outputFile, minutil);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 30)", Integer.class, false);
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
        return new String[] { "Patterns", "High-utility patterns", "High-utility itemsets" };
    }

}
