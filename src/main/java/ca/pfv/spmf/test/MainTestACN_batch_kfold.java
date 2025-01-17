package ca.pfv.spmf.test;

/* This file is copyright (c) 2021 Philippe Fournier-Viger
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

import ca.pfv.spmf.algorithms.classifiers.acn.AlgoACN;
import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;
import ca.pfv.spmf.algorithms.classifiers.general.ClassificationAlgorithm;
import ca.pfv.spmf.algorithms.classifiers.general.Evaluator;
import ca.pfv.spmf.algorithms.classifiers.general.OverallResults;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Example of how to run the ACN algorithm
 *
 * @author Philippe Fournier-Viger, 2021
 */
public class MainTestACN_batch_kfold {

    public static void main(String[] args) throws Exception {

        System.out.println("========= Step 1: Read the dataset ==========");

        // We choose "play" as the target attribute that we want to predict using other
        // attributes
        String targetClassName = "play";

        // Load the dataset
        String datasetPath = fileToPath("tennisExtended.txt");
        StringDataset dataset = new StringDataset(datasetPath, targetClassName);

        // If the dataset is in ARFF format, then use these lines instead:
//		String datasetPath = fileToPath("weather-train.arff");
//		ARFFDataset dataset = new ARFFDataset(datasetPath, targetClassName);

        // If the dataset is in CSV format, then use these lines instead:
//		String datasetPath = fileToPath("tennisExtendedCSV.txt");
//		CSVDataset dataset = new CSVDataset(datasetPath, targetClassName);

        // Print stats about the dataset
        dataset.printStats();

        // For debugging (optional)
//		dataset.printInternalRepresentation();
//		dataset.printStringRepresentation();

        System.out.println("==== Step 2: Training:  Apply the algorithm to build a model (a set of rules) ===");
        // Parameters of the algorithm
        double minSup = 0.1;
        double minConf = 0.8;
        double minAcc = 0.2;
        double minCorr = 0.2;

        // Create the algorithm
        ClassificationAlgorithm algorithmACN = new AlgoACN(minSup, minConf, minAcc, minCorr);
        ClassificationAlgorithm[] algorithms = new ClassificationAlgorithm[] { algorithmACN };

        // We create an object Evaluator to run the experiment using k-fold cross
        // validation
        Evaluator experiment1 = new Evaluator();

        // We will test 3 folds
        int kFoldCount = 3;

        // We run the experiment
        OverallResults allResults = experiment1.trainAndRunClassifiersKFold(algorithms, dataset, kFoldCount);

        // Save statistics about the execution to files (optional)
        String forTrainingPath = "outputReportForTraining.txt";
        String onTrainingPath = "outputReportOnTraining.txt";
        String onTrestingPath = "outputReportOnTesting.txt";
        allResults.saveMetricsResultsToFile(forTrainingPath, onTrainingPath, onTrestingPath);

        // Print statistics to the console (optional)
        allResults.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestACN_batch_kfold.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
