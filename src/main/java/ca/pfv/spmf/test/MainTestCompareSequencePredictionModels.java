package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.helpers.StatsLogger;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.CPT.CPT.CPTPredictor;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.CPT.CPTPlus.CPTPlusPredictor;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.DG.DGPredictor;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.Evaluator;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.LZ78.LZ78Predictor;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.Markov.MarkovAllKPredictor;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.Markov.MarkovFirstOrderPredictor;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.TDAG.TDAGPredictor;

import java.io.IOException;

/**
 * Main controller to compare all the predictors
 */
public class MainTestCompareSequencePredictionModels {

    public static void main(String[] args) throws IOException {

        // The following line is to set optional parameters for the prediction model.
        // We can activate the recursive divider strategy to obtain more noise
        // tolerant predictions (see paper). We can also use a splitting method
        // to reduce the model size (see explanation below).
        String optionalParameters = "windowSize:1 consequentSize:2";


        //instantiate the evaluator
        // with the directory containing the datasets
        Evaluator evaluator = new Evaluator("/home/ted/java/IPredict/datasets");

        // Loading data sets
        // These datasets are assumed to be located in the above directory
        evaluator.addDataset("BMS", 5000);
        evaluator.addDataset("SIGN", 1000);
        evaluator.addDataset("MSNBC", 5000);
        evaluator.addDataset("BIBLE_WORD", 5000);
        evaluator.addDataset("BIBLE_CHAR", 5000);
        evaluator.addDataset("KOSARAK", 45000);
        evaluator.addDataset("FIFA", 5000);

        // Loading predictors
        // Here we will compare 7 predictors.
        evaluator.addPredictor(new DGPredictor("DG", "lookahead:4"));
        evaluator.addPredictor(new TDAGPredictor());
        evaluator.addPredictor(new CPTPlusPredictor("CPT+", "CCF:true CBS:true"));
        evaluator.addPredictor(new CPTPredictor());
        evaluator.addPredictor(new MarkovFirstOrderPredictor());
        evaluator.addPredictor(new MarkovAllKPredictor());
        evaluator.addPredictor(new LZ78Predictor());

        // Start the experiment
        // by using 14-fold cross-validation
        StatsLogger results = evaluator.Start(Evaluator.KFOLD, 14, true, true, true);

    }

}
