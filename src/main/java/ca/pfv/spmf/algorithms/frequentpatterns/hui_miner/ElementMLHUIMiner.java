package ca.pfv.spmf.algorithms.frequentpatterns.hui_miner;

/* This file is copyright (c) 2008-2015 Ying Wang, Philippe Fournier-Viger
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
 * This class represents an Element of a utility list as used by the MLHUI-Miner algorithm.
 *
 * @author Ying Wang, Philippe Fournier-Viger
 * @see AlgoMLHUIMiner
 * @see UtilityList
 */
public class ElementMLHUIMiner {
    // The three variables as described in the paper:
    /**
     * transaction id
     */
    public final int tid;
    /**
     * itemset utility
     */
    public final double iutils;
    /**
     * remaining utility
     */
    public double rutils;

    /**
     * Constructor.
     *
     * @param tid    the transaction id
     * @param iutils the itemset utility
     * @param rutils the remaining utility
     */
    public ElementMLHUIMiner(int tid, Double iutils, Double rutils) {
        this.tid = tid;
        this.iutils = iutils;
        this.rutils = rutils;
    }
}
