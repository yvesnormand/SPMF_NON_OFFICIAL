package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_converter.Formats;
import ca.pfv.spmf.tools.dataset_converter.TransactionDatabaseConverter;

import java.io.IOException;
/* This file is copyright (c) 2008-2016 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */
/* This file is copyright (c) 2008-2016 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class describes the algorithm to convert a sequence database with cost values to a transaction database.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see TransactionDatabaseConverter
 */
public class DescriptionAlgoConvertCostSequenceDBTransactionDB extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoConvertCostSequenceDBTransactionDB() {
    }

    @Override
    public String getName() {
        return "Convert_cost_sequence_database_to_cost_transaction_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "DATASET TOOLS";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Converting_a_sequence_database_to_transaction_database.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int transactionCount = getParamAsInteger(parameters[0]);

        long startTime = System.currentTimeMillis();
        TransactionDatabaseConverter converter = new TransactionDatabaseConverter();
        converter.convert(inputFile, outputFile, Formats.SPMF_COST_SEQUENCE_DB,
                transactionCount);
        long endTIme = System.currentTimeMillis();
        System.out
                .println("Sequence database converted.  Time spent for conversion = "
                         + (endTIme - startTime) + " ms.");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Transaction count count", "(e.g. 5)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Sequence database", "Cost sequence database" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Database of instances", "Transaction database", "Cost transaction database" };
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

}
