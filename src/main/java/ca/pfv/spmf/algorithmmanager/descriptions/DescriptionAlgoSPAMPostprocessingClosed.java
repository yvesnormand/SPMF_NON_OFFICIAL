package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.AlgoClaSP;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPAM_AGP;

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
 * This class describes the SPAM algorithm parameters (version implemented
 * by Antonio G.P. which applies post-processing to find closed sequential patterns).
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoSPAM_AGP
 */
public class DescriptionAlgoSPAMPostprocessingClosed extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoSPAMPostprocessingClosed() {
    }

    @Override
    public String getName() {
        return "SPAM_PostProcessingClosed";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SPAM_PrefixSpan_Closed.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minSupport = getParamAsDouble(parameters[0]);

        boolean outputSeqIdentifiers = false;
        if (parameters.length >= 2 && !"".equals(parameters[1])) {
            outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
        }

        ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator abstractionCreator =
                ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator_Qualitative
                        .getInstance();
        ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreator idListCreator =
                ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreatorStandard_Map
                        .getInstance();
        ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase sd =
                new ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase(
                        abstractionCreator, idListCreator);

        double relativeMinSup = sd.loadFile(inputFile, minSupport);

        AlgoClaSP algo = new AlgoClaSP(relativeMinSup, abstractionCreator, true, false);

        algo.runAlgorithm(sd, true, false, outputFile, outputSeqIdentifiers);
        System.out.println(algo.printStatistics());
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Show sequence ids?", "(default: false)", Boolean.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Antonio Gomariz Penalver";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[] { "Database of instances", "Sequence database", "Simple sequence database" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[] { "Patterns", "Sequential patterns", "Frequent closed sequential patterns" };
    }

}
