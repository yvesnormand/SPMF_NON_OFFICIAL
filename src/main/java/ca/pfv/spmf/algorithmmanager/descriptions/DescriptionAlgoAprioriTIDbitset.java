package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.aprioriTID.AlgoAprioriTID;
import ca.pfv.spmf.algorithms.frequentpatterns.aprioriTID.AlgoAprioriTID_Bitset;

import java.io.IOException;

/**
 * This class describes the AprioriTID algorithm parameters, implemented with bitsets.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoAprioriTID_Bitset
 */
public class DescriptionAlgoAprioriTIDbitset extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoAprioriTIDbitset() {
    }

    @Override
    public String getName() {
        return "AprioriTID_Bitset";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/AprioriTID.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        AlgoAprioriTID algo = new AlgoAprioriTID();

        if (parameters.length >= 2 && !"".equals(parameters[1])) {
            algo.setShowTransactionIdentifiers(getParamAsBoolean(parameters[1]));
        }

        if (parameters.length >= 3 && !"".equals(parameters[2])) {
            algo.setMaximumPatternLength(getParamAsInteger(parameters[2]));
        }

        algo.runAlgorithm(inputFile, outputFile, minsup);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Show transaction ids?", "(default: false)", Boolean.class, true);
        parameters[2] = new DescriptionOfParameter("Max pattern length", "(e.g. 2 items)", Integer.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Transaction database", "Simple transaction database" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Patterns", "Frequent patterns", "Frequent itemsets" };
    }

}
