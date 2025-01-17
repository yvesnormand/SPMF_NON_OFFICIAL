package ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.CPT.CPT;

import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.Item;

import java.io.Serializable;
import java.util.ArrayList;
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

public class PredictionTree implements Serializable {

    private final List<PredictionTree> Children; //children list
    public int Support; //support count
    public Item Item; //actual item
    public PredictionTree Parent; //parent's node

    public PredictionTree(Item itemValue) {
        Support = 0; //default support
        Item = itemValue;
        Children = new ArrayList<PredictionTree>();
        Parent = null;
    }

    public PredictionTree() {
        Support = 0; //default support
        Item = new Item();
        Children = new ArrayList<PredictionTree>();
        Parent = null;
    }

    public void addChild(Item child) {
        PredictionTree newChild = new PredictionTree(child);
        newChild.Parent = this;
        Children.add(newChild);
    }

    public Boolean hasChild(Item target) {

        for (PredictionTree child : Children) {
            if (child.Item.val.equals(target.val)) {
                return true;
            }
        }

        return false;
    }

    public PredictionTree getChild(Item target) {

        for (PredictionTree child : Children) {
            if (child.Item.val.equals(target.val)) {
                return child;
            }
        }

        return null;
    }

    public int getChildrenCount() {
        return Children.size();
    }

}
