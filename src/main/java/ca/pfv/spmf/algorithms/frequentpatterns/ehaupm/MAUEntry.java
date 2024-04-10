package ca.pfv.spmf.algorithms.frequentpatterns.ehaupm;

/**
 * * * This is an implementation of the EHAUPM algorithm.
 * <p>
 * Copyright (c) 2018 Shi-Feng Ren
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE  (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the * terms of the GNU General Public License as published by the Free
 * Software * Foundation, either version 3 of the License, or (at your option) any later * version. *
 * <p>
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR * A PARTICULAR PURPOSE. See the GNU General Public License for more details. *
 * <p>
 * You should have received a copy of the GNU General Public License along with * SPMF. If not, see .
 *
 * @author Shi-Feng Ren
 */

/**
 * This is an implementation of the "EHAUPM" algorithm for High-Average-Utility Itemsets Mining
 * as described in the jouranl paper : <br/><br/>
 * <p>
 * Lin C W, Ren S, Fournier-Viger P, et al. EHAUPM: Efficient High Average-Utility Pattern Mining with Tighter Upper-Bounds[J]. IEEE Access, 2017,
 * PP(99):1-1.
 *
 * @author Shi-Feng Ren
 * @see MAUList
 * @see AlgoEHAUPM
 */
public class MAUEntry {

    // transaction id
    final int tid;
    // itemset utility
    final int utility;
    // remaining maximal utility
    int remu;
    // revised maximal utility
    int rmu;


    public MAUEntry(int tid, int utility, int rmu, int remu) {
        this.tid = tid;
        this.utility = utility;
        this.remu = remu;
        this.rmu = rmu;
    }
}
