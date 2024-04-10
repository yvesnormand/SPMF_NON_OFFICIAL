package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_stats.TransactionStatsGenerator;
import ca.pfv.spmf.tools.other_dataset_tools.FixTransactionDatabaseTool;

import java.io.IOException;

/**
 * This class describes the algorithm to fix a transaction database. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see TransactionStatsGenerator
 */
public class DescriptionAlgoFixTransactionDB extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoFixTransactionDB() {
    }

    @Override
    public String getName() {
        return "Fix_a_transaction_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "DATASET TOOLS";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Fix_a_transaction_database.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        FixTransactionDatabaseTool tool = new FixTransactionDatabaseTool();
        tool.convert(inputFile, outputFile);
        System.out.println("Finished fixing the transaction database.");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[0];
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
        return new String[] { "Database of instances", "Transaction database", "Simple transaction database" };
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

}