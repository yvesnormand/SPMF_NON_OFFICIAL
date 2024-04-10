package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.defme.AlgoDefMe;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This class describes the Apriori algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoApriori
 */
public class DescriptionAlgoDefMe extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoDefMe() {
    }

    @Override
    public String getName() {
        return "DefMe";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/DefMe.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        AlgoDefMe algorithm = new AlgoDefMe();
        TransactionDatabase database = new TransactionDatabase();
        try {
            database.loadFile(inputFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (parameters.length >= 2 && !"".equals(parameters[1])) {
            algorithm.setMaximumPatternLength(getParamAsInteger(parameters[1]));
        }

        algorithm.runAlgorithm(outputFile, database, minsup);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)",
                Double.class, false);
        parameters[1] = new DescriptionOfParameter("Max pattern length", "(e.g. 2 items)", Integer.class, true);
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
        return new String[] { "Patterns", "Frequent patterns", "Generator patterns", "Frequent itemsets", "Frequent generator itemsets" };
    }

}
