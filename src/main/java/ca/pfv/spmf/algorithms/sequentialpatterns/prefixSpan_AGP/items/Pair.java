package ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.abstractions.ItemAbstractionPair;

import java.util.BitSet;

/**
 * This class is used by PrefixSpan and it is based on SPMF original PrefixSpan
 * Algorithm. It represents, based on Hirate & Yamana
 * a pair of an Item  and its occurrences in the projected database. It is used for calculating the support
 * of item in a database.
 * <p>
 * Copyright Antonio Gomariz Peñalver 2013
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author agomariz
 */


public class Pair implements Comparable<Pair> {

    /**
     * Flag indicating if the item appears in the same itemset that one from
     * the projection was done.
     */
    private final boolean postfix;
    /**
     * Duple item and abstraction.
     */
    private final ItemAbstractionPair pair;
    /**
     * List of the sequences that contains this one.
     */
    private BitSet sequencesID = new BitSet();

    /**
     * Standard constructor
     *
     * @param postfix flag indicating if it is an item appearing in the same
     *                itemset as that one from the projection was done
     * @param pair    pair appearing after the item of the projection
     */
    public Pair(boolean postfix, ItemAbstractionPair pair) {
        this.postfix = postfix;
        this.pair = pair;
    }

    /**
     * Check if this pair is equal to another
     *
     * @param arg the other pair
     * @return true if equal, otherwise false.
     */
    @Override
    public boolean equals(Object arg) {
        Pair p = (Pair) arg;
        return (p.pair.equals(this.pair)) && (p.postfix == this.postfix);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.postfix ? 1 : 0);
        hash = 59 * hash + (this.pair != null ? this.pair.hashCode() : 0);
        return hash;
    }

    public boolean isPostfix() {
        return postfix;
    }

    public ItemAbstractionPair getPair() {
        return pair;
    }

    /**
     * It returns the number of sequences where the pair appears.
     *
     * @return the number of sequences
     */
    public int getSupport() {
        return sequencesID.cardinality();
    }

    /**
     * It returns the list of the sequences where the pair appears
     *
     * @return the list of sequences as a bitset.
     */
    public BitSet getSequencesID() {
        return sequencesID;
    }

    public void setSequencesID(BitSet sequencesID) {
        this.sequencesID = sequencesID;
    }

    @Override
    public int compareTo(Pair arg) {
        int comparison = this.pair.compareTo(arg.pair);
        if (comparison == 0) {
            if (this.postfix == arg.postfix) {
                return 0;
            } else {
                if (this.postfix) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            return comparison;
        }
    }

    @Override
    public String toString() {
        String post = "";
        if (postfix) {
            post = "*";
        }

        return (post + pair.toString() + "[" + sequencesID.size() + "]");
    }
}
