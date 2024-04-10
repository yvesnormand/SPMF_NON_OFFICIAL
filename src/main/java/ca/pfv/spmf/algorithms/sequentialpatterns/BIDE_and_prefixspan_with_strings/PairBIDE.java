package ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings;
/* This file is copyright (c) 2008-2013 Philippe Fournier-Viger
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

import java.util.HashSet;
import java.util.Set;

/**
 * This class, used by the BIDE algorithm, represents
 * an (1) item
 * (2)  a boolean indicating if the item is contained in an itemset that was cut at left or not (a postfix).
 * (3) a boolean indicating if the item is contained in an itemset that was cut at right or not (a prefix).
 * and (4) the sequence IDs containing this item.
 * <br/><br/>
 * This class extends the class Pair used by PrefixSpan. It is is used by BIDE+ to
 * count the support of items.
 *
 * @author Philippe Fournier-Viger
 * @see Pair
 * @see AlgoBIDEPlus_withStrings
 */
class PairBIDE extends Pair {

    /**
     * indicate if this represents the item appearing
     * // in an itemset that is cut at the right or not (a prefix)
     */
    private final boolean prefix;

    /**
     * List of the its of all the patterns that contains this one.
     */
    private final Set<Integer> sequencesID = new HashSet<Integer>();

    /**
     * Constructor
     *
     * @param postfix indicate if this is the case of an item appearing
     *                in an itemset that is cut at the left because of a projection
     * @param prefix  indicate if this is the case of an item appearing
     *                in an itemset that is cut at the right because of a projection
     * @param item    the item
     */
    PairBIDE(boolean prefix, boolean postfix, String item) {
        super(postfix, item);
        this.prefix = prefix;
    }

    /**
     * Check if two pairs are equal (same item and both appears in a postfix or not and prefix or not).
     *
     * @return true if equals.
     */
    public boolean equals(Object object) {
        PairBIDE paire = (PairBIDE) object;
        return (paire.postfix == this.postfix)
               && (paire.prefix == this.prefix)
               && (paire.item.equals(this.item));
    }

    /**
     * Method to calculate an hashcode (because pairs are stored in a map).
     */
    public int hashCode() {// Ex: 127333,P,X,1  127333,N,Z,2
        // transform it into a string
        final String r = String.valueOf(postfix ? 'P' : 'N') + // the letters here have no meanings. they are just used for the hashcode
                         (prefix ? 'X' : 'Z') + // the letters here have no meanings. they are just used for the hashcode
                         item;
        // then use the hashcode method from the string class
        return r.hashCode();
    }

    /**
     * Check if this is the case of the item appearing in a prefix
     *
     * @return true if this is the case.
     */
    public boolean isPrefix() {
        return prefix;
    }
}