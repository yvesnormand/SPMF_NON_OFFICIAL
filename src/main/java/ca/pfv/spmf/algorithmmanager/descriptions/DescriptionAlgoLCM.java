package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.lcm.AlgoLCM;
import ca.pfv.spmf.algorithms.frequentpatterns.lcm.Dataset;

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
 * This class describes the LCM algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoLCM
 */
public class DescriptionAlgoLCM extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoLCM() {
    }

    @Override
    public String getName() {
        return "LCM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/LCM.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        Dataset dataset = new Dataset(inputFile);
        AlgoLCM algo = new AlgoLCM();
        algo.runAlgorithm(minsup, dataset, outputFile);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Alan Souza, Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Transaction database", "Simple transaction database" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Patterns", "Frequent patterns", "Frequent closed itemsets" };
    }

}
