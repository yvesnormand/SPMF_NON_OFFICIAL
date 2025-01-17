package ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan;

import ca.pfv.spmf.patterns.itemset_list_integers_without_support.Itemset;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a sequential pattern.
 * A sequential pattern is a list of itemsets.
 * <p>
 * Copyright (c) 2008-2012 Philippe Fournier-Viger
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SPMF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SPMF.  If not, see <http://www.gnu.org/licenses/>.
 */

public class SequentialPattern implements Comparable<SequentialPattern> {

    // the list of itemsets
    private final List<Itemset> itemsets;

    // IDs of sequences containing this pattern
    private List<Integer> sequencesIds;

    // whether the sequence was found (used in ProSecCo)
    private final boolean isFound = false;

    // additional support count for the sequential pattern
    private int additionalSupport = 0;


    /**
     * Defaults constructor
     */
    public SequentialPattern() {
        itemsets = new ArrayList<Itemset>();
    }

    /**
     * Get the relative support of this pattern (a percentage)
     *
     * @param sequencecount the number of sequences in the original database
     * @return the support as a string
     */
    public String getRelativeSupportFormated(int sequencecount) {
        double relSupport = ((double) sequencesIds.size()) / ((double) sequencecount);
        // pretty formating :
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(5);
        return format.format(relSupport);
    }

    /**
     * Get the absolute support of this pattern.
     *
     * @return the support (an integer >= 1)
     */
    public int getAbsoluteSupport() {
        return sequencesIds.size();
    }

    /**
     * Add an itemset to this sequential pattern
     *
     * @param itemset the itemset to be added
     */
    public void addItemset(Itemset itemset) {
//		itemCount += itemset.size();
        itemsets.add(itemset);
    }

    /**
     * Copy the sequential pattern
     *
     * @return copy of the sequence pattern
     */
    public SequentialPattern copy() {
        SequentialPattern clone = new SequentialPattern();
        for (Itemset it : itemsets) {
            clone.addItemset(it.cloneItemSet());
        }
        clone.additionalSupport = this.additionalSupport;
        clone.sequencesIds = new ArrayList<Integer>(this.sequencesIds);
        return clone;
    }

    /**
     * Print this sequential pattern to System.out
     */
    public void print() {
        System.out.print(this);
    }

    /**
     * Get a string representation of this sequential pattern,
     * containing the sequence IDs of sequence containing this pattern.
     */
    public String toString() {
        StringBuilder r = new StringBuilder();
        // For each itemset in this sequential pattern
        for (Itemset itemset : itemsets) {
            r.append('('); // begining of an itemset
            // For each item in the current itemset
            for (Integer item : itemset.getItems()) {
                String string = item.toString();
                r.append(string); // append the item
                r.append(' ');
            }
            r.append(')');// end of an itemset
        }
//
//		//  add the list of sequence IDs that contains this pattern.
//		if(getSequencesID() != null){
//			r.append("  Sequence ID: ");
//			for(Integer id : getSequencesID()){
//				r.append(id);
//				r.append(' ');
//			}
//		}
        return r.append("    ").toString();
    }

    /**
     * Get a string representation of this sequential pattern.
     */
    public String itemsetsToString() {
        StringBuilder r = new StringBuilder();
        for (Itemset itemset : itemsets) {
            r.append('{');
            for (Integer item : itemset.getItems()) {
                String string = item.toString();
                r.append(string);
                r.append(' ');
            }
            r.append('}');
        }
        return r.append("    ").toString();
    }

    /**
     * Get the itemsets in this sequential pattern
     *
     * @return a list of itemsets.
     */
    public List<Itemset> getItemsets() {
        return itemsets;
    }

    /**
     * Get an itemset at a given position.
     *
     * @param index the position
     * @return the itemset
     */
    public Itemset get(int index) {
        return itemsets.get(index);
    }

    /**
     * Get the number of itemsets in this sequential pattern.
     *
     * @return the number of itemsets.
     */
    public int size() {
        return itemsets.size();
    }

    public List<Integer> getSequenceIDs() {
        return sequencesIds;
    }

    /**
     * Set the set of IDs of sequence containing this prefix
     *
     * @param a set of integer containing sequence IDs
     */
    public void setSequenceIDs(List<Integer> sequencesIds) {
        this.sequencesIds = sequencesIds;
    }

    @Override
    public int compareTo(SequentialPattern o) {
        if (o == this) {
            return 0;
        }
        int compare = this.getAbsoluteSupport() - o.getAbsoluteSupport();
        if (compare != 0) {
            return compare;
        }

        return this.hashCode() - o.hashCode();
    }

    public boolean setIsFound(boolean b) {
        return isFound;

    }

    public boolean isFound() {
        return isFound;
    }

    public void addAdditionalSupport(int additionalSupport) {
        this.additionalSupport += additionalSupport;
    }

}
