package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings.AlgoPrefixSpan_with_Strings;
import ca.pfv.spmf.input.sequence_database_list_strings.SequenceDatabase;

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

/**
 * This class describes the BIDE+ algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoPrefixSpan_with_Strings
 */
public class DescriptionAlgoPrefixSpanWithStrings extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoPrefixSpanWithStrings() {
    }

    @Override
    public String getName() {
        return "PrefixSpan_with_strings";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/PrefixSpan.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

        SequenceDatabase sequenceDatabase = new SequenceDatabase();
        sequenceDatabase.loadFile(inputFile);

        // Create an instance of the algorithm with minsup = 50 %
        AlgoPrefixSpan_with_Strings algo = new AlgoPrefixSpan_with_Strings();

        int minsup = (int) Math
                .ceil((getParamAsDouble(parameters[0]) * sequenceDatabase
                        .size())); // we use a minimum support of 2
        // sequences.

        // execute the algorithm
        algo.runAlgorithm(sequenceDatabase, outputFile, minsup);
        algo.printStatistics(sequenceDatabase.size());
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Sequence database with strings", "Simple sequence database with strings" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Patterns", "Sequential patterns", "Frequent sequential patterns", "Frequent sequential patterns" };
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

}
