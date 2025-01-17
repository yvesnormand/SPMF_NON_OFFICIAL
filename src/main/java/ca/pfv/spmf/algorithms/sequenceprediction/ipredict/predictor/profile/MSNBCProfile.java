package ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.profile;

/*
 * This file is copyright (c) Ted Gueniche
 * <ted.gueniche@gmail.com>
 *
 * This file is part of the IPredict project
 * (https://github.com/tedgueniche/IPredict).
 *
 * IPredict is distributed under The MIT License (MIT).
 * You may obtain a copy of the License at
 * https://opensource.org/licenses/MIT
 */
public class MSNBCProfile extends Profile {

    @Override
    public void Apply() {

        //Global parameters
        //Pre-processing
        parameters.put("sequenceMinSize", "7");
        parameters.put("sequenceMaxSize", "999");
        parameters.put("removeDuplicatesMethod", "1");
        parameters.put("consequentSize", "2");
        parameters.put("windowSize", "5");

        ///////////////
        //CPT parameters
        //Training
        parameters.put("splitMethod", "1"); //0 for no split", "1 for basicSplit", "2 for complexSplit
        parameters.put("splitLength", "6"); // max tree height
        parameters.put("minSup", "0.05"); //SEI compression, minSup to remove low supporting items

        //CCF compression
        parameters.put("CCFmin", "2");
        parameters.put("CCFmax", "4");
        parameters.put("CCFsup", "16");

        //Prediction
        parameters.put("recursiveDividerMin", "1"); //should be >= 0 and < recursiveDividerMax
        parameters.put("recursiveDividerMax", "5"); //should be > recusiveDividerMax and < windowSize
        parameters.put("minPredictionRatio", "0.0f"); //should be over 0
        parameters.put("noiseRatio", "0.0f"); //should be in the range ]0,1]

    }

}
