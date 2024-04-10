package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.gui.texteditor.SPMFTextEditor;
import ca.pfv.spmf.tools.dataset_stats.TransactionStatsGenerator;

import java.io.File;
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
 * This class describes the algorithm to run the text editor of SPMF
 *
 * @author Philippe Fournier-Viger
 * @see TransactionStatsGenerator
 */
public class DescriptionAlgoSPMFTextEditorOpenFile extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoSPMFTextEditorOpenFile() {
    }

    @Override
    public String getName() {
        return "Open_text_file_with_SPMF_text_editor";
    }

    @Override
    public String getAlgorithmCategory() {
        return "DATASET TOOLS";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SPMF_Text_Editor.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        boolean runAsStandalone = false;
        SPMFTextEditor textEditor = new SPMFTextEditor(runAsStandalone);
        textEditor.openAFile(new File(inputFile));
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
        return new String[] { "Text file" };
    }

    @Override
    public String[] getOutputFileTypes() {
        return null;
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

}
