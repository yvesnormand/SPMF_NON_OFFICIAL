package ca.pfv.spmf.algorithms.frequentpatterns.skymine;

/**
 * This represents an itemset with its utility value, as used by Skymine to store results
 * <p>
 * Copyright (c) 2015 Vikram Goyal, Ashish Sureka, Dhaval Patel, Siddharth Dawar
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE *
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the *
 * terms of the GNU General Public License as published by the Free Software *
 * Foundation, either version 3 of the License, or (at your option) any later *
 * version. SPMF is distributed in the hope that it will be useful, but WITHOUT
 * ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Vikram Goyal, Ashish Sureka, Dhaval Patel, Siddharth Dawar
 * @see AlgoSkyMine
 * @see UPTree
 */

public class ItemsetUtility {

    /**
     * the itemset
     */
    public int[] itemset;

    /**
     * the utility of the itemset
     */
    public long utility = 0;

}
