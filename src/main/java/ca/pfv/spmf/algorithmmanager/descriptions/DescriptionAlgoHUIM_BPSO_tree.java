package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.HUIM_BPSO_tree.AlgoHUIM_BPSO_tree;

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
 * This class describes the HUIM-BPSO-tree algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoHUIM_BPSO_tree
 */
public class DescriptionAlgoHUIM_BPSO_tree extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoHUIM_BPSO_tree() {
    }

    @Override
    public String getName() {
        return "HUIM-BPSO-tree";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/HUIM-BPSO-tree.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minutil = getParamAsInteger(parameters[0]);
        // Applying the algorithm
        AlgoHUIM_BPSO_tree algo = new AlgoHUIM_BPSO_tree();
        algo.runAlgorithm(inputFile, outputFile, minutil);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 40)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Jerry Chun-Wei Lin, Lu Yang, Philippe Fournier-Viger";
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
