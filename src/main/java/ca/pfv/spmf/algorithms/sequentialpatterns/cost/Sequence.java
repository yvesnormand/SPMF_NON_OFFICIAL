/**
 * * * This is an implementation of the CEPB, corCEPB, CEPN algorithm.
 * <p>
 * Copyright (c) 2014 Jiaxuan Li
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the * terms of the GNU General Public License as published by the Free
 * Software * Foundation, either version 3 of the License, or (at your option) any later * version. *
 * <p>
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR * A PARTICULAR PURPOSE. See the GNU General Public License for more details. *
 * <p>
 * You should have received a copy of the GNU General Public License along with * SPMF. If not, see .
 *
 * @author jiaxuan Li
 */

package ca.pfv.spmf.algorithms.sequentialpatterns.cost;
/***
 * A sequence
 *
 * @author Jiaxuan Li
 * @see ca.pfv.spmf.algorithms.sequentialpatterns.cost.AlgoCEPM
 */

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    private final List<Event> eventsets = new ArrayList<Event>();
    private int utility;

    /**
     * Add an itemset to this sequence.
     *
     * @param itemset An itemset (array of integers)
     */
//	public void addEventset(Object[] itemset) {
//		
//		Integer[] itemsetInt = new Integer[itemset.length];
//		System.arraycopy(itemset, 0, itemsetInt, 0, itemset.length);
//		itemsets.add(itemsetInt);
//	}

//	  Print this sequence to System.out
    public void print() {
        System.out.print(this);
    }

    /**
     * Return a string representation of this sequence.
     */
//	public String toString() {
//		StringBuilder r = new StringBuilder("");
//		// for each itemset
//		for(Integer[] itemset : itemsets){
//			r.append('(');
//			// for each item in the current itemset
//			for(int i=0; i< itemset.length; i++){
//				String string = itemset[i].toString();
//				r.append(string);
//				r.append(' ');
//			}
//			r.append(')');
//		}
//
//		return r.append("    ").toString();
//	}

    /**
     * Get the list of itemsets in this sequence.
     *
     * @return the list of itemsets.
     */
    public List<Event> getEventsets() {
        return eventsets;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    /**
     * Get the itemset at a given position in this sequence.
     *
     * @param index the position
     * @return the itemset as an array of integers.
     */

    /**
     * Get the size of this sequence (number of itemsets).
     *
     * @return the size (an integer).
     */
    public int size() {
        return eventsets.size();
    }
}
