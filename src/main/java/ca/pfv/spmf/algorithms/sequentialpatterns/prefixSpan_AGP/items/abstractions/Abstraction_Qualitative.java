package ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.abstractions;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.PseudoSequence;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that implements a qualitative abstraction. Two different values are
 * possible: to be with an equal relation with respect to a previous pair (if
 * occurs at the same time), or to be with an after relation with respect to
 * that previous pair (the previous pair have a before relation with respect to
 * this one)
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
public class Abstraction_Qualitative extends Abstraction_Generic {

    private static final Map<Boolean, Abstraction_Qualitative> pool = new HashMap<Boolean, Abstraction_Qualitative>(4);

    static {
        Abstraction_Qualitative trueValue = new Abstraction_Qualitative(true);
        Abstraction_Qualitative falseValue = new Abstraction_Qualitative(false);
        pool.put(Boolean.TRUE, trueValue);
        pool.put(Boolean.FALSE, falseValue);
    }

    /**
     * Abstraction indicating if the item associated has an equal relation with
     * the previous item in the pattern
     */
    private final boolean equalRelation;

    private Abstraction_Qualitative(boolean equalRelation) {
        this.equalRelation = equalRelation;
    }

    public static void clear() {
        // pool=null;
    }

    /**
     * It creates a new abstraction with the given value.
     *
     * @param equalRelation
     * @return the abstraction
     */
    public static Abstraction_Qualitative create(boolean equalRelation) {
        return new Abstraction_Qualitative(equalRelation);
    }

    @Override
    public boolean equals(Object arg) {
        Abstraction_Qualitative s = (Abstraction_Qualitative) arg;
        return equalRelation == s.hasEqualRelation();
    }

    @Override
    public int hashCode() {
        if (hasEqualRelation()) {
            return 1;
        }
        return 0;
    }

    @Override
    public int compareTo(Abstraction_Generic arg) {
        Abstraction_Qualitative s = (Abstraction_Qualitative) arg;
        if (equalRelation == s.hasEqualRelation()) {
            return 0;
        } else if (!equalRelation) {
            return 1;
        }

        return -1;
    }

    public boolean hasEqualRelation() {
        return equalRelation;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!hasEqualRelation()) {
            result.append(" ->");
        }
        return result.toString();
    }

    /**
     * Get the string representation of this abstraction. Adjusted to SPMF format.
     *
     * @return the string representation
     */
    @Override
    public String toStringToFile() {
        StringBuilder result = new StringBuilder();
        if (!hasEqualRelation()) {
            result.append(" -1");
        }
        return result.toString();
    }

    /**
     * It returns true if the projection indicated starts in the middle of an
     * itemset and the abstraction is an equal relation, or if the projection
     * starts in the first item of the itemset and the abstraction does not
     * have an equal relation.
     *
     * @param sequence     The sequence in which we apply the method
     * @param projection   The projection of the pseudosequence in which we are
     *                     interested
     * @param itemsetIndex The itemset index
     * @return true if the above condition is met.
     */
    @Override
    public boolean compute(PseudoSequence sequence, int projection, int itemsetIndex) {
        return sequence.isPostfix(projection, itemsetIndex) == hasEqualRelation();
    }
}
