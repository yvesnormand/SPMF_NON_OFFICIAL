package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.associationrules.closedrules.AlgoClosedRules_UsingFPClose;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPClose;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This class describes parameters of the algorithm for generating closed association rules
 * with the FPClose algorithm.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoFPClose, AlgoAgrawalFaster94
 */
public class DescriptionAlgoClosedAssociationRulesFPClose extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoClosedAssociationRulesFPClose() {
    }

    @Override
    public String getName() {
        return "Closed_association_rules(using_fpclose)";
    }

    @Override
    public String getAlgorithmCategory() {
        return "ASSOCIATION RULE MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/ClosedAssociationRules.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        double minconf = getParamAsDouble(parameters[1]);

        // Loading the transaction database
        TransactionDatabase database = new TransactionDatabase();
        try {
            database.loadFile(inputFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // database.printDatabase();

        // STEP 1: Applying the Charm algorithm to find frequent closed
        // itemsets
        AlgoFPClose algo = new AlgoFPClose();
        Itemsets patterns = algo.runAlgorithm(inputFile, null, minsup);
        algo.printStats();

        // STEP 2: Generate all rules from the set of frequent itemsets
        // (based on Agrawal & Srikant, 94)
        AlgoClosedRules_UsingFPClose algoRule = new AlgoClosedRules_UsingFPClose();
        algoRule.runAlgorithm(patterns, outputFile, database.size(), minconf, algo.cfiTree);
        algoRule.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.6 or 60%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minconf (%)", "(e.g. 0.6 or 60%)", Double.class, false);
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
        return new String[] { "Patterns", "Association rules", "Closed association rules" };
    }

}
