package ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.CPT.CPTPlus;

import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.Item;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.Sequence;

import java.util.HashMap;
import java.util.List;

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
public interface FIF {

    HashMap<Item, Integer> getItemFrequencies(List<Sequence> seqs);

    List<List<Item>> findFrequentItemsets(List<Sequence> seqs, int minLength, int maxlength, int minSup);
}
