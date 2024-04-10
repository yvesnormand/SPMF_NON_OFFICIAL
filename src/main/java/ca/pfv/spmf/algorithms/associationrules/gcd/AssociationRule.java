/**
 * Warranty disclaimer: This software is provided 'as-is', without any express or implied warranty.
 * In no event will the author(s) be held liable for any damages arising from the use of this software.
 * <p>
 * Copyrights (c) 2015 to Ahmed El-Serafy (a.elserafy@ieee.org) and Hazem El-Raffiee (hazem.farouk.elraffiee@gmail.com)
 * <p>
 * All of the files that are part of the GCDs Association Rules algorithm are licensed under either GPL v.3 or dual-licensed under the following
 * terms.
 * <p>
 * 1- Any use of the provided source code must be preceded by a written authorization from one of the author(s).
 * 2- The license text must be kept in source files headers.
 * 3- The use of the provided source code must be acknowledged in the project documentation and any consequent presentations or documents.
 * This is achieved by referring to the original repository (https://bitbucket.org/aelserafy/gcd-association-rules)
 * 4- Any enhancements introduced to the provided algorithm must be shared with the original author(s) along with its source code and changes log.
 * This is if you are building directly or indirectly upon the algorithm provided by the original author(s).
 * 5- The public availability of the new source code is provided upon agreement with the original author(s).
 * 6- For commercial distribution and use, a license agreement must be obtained from one of the author(s).
 */

package ca.pfv.spmf.algorithms.associationrules.gcd;

public class AssociationRule {
    private static int totalTransactionsCount;
    private final MyBigInteger antecedent;
    private final MyBigInteger consequent;
    private final double support;
    private final double confidence;

    public AssociationRule(MyBigInteger antecedent, MyBigInteger consequent, double support, double confidence) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.support = support;
        this.confidence = confidence;
    }

    public static int getTotalTransactionsCount() {
        return totalTransactionsCount;
    }

    public static void setTotalTransactionsCount(int transactionsCount) {
        AssociationRule.totalTransactionsCount = transactionsCount;
    }

    public MyBigInteger getAntecedent() {
        return antecedent;
    }

    public MyBigInteger getConsequent() {
        return consequent;
    }

    public Double getSupport() {
        return support;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final AssociationRule associationRule) {
            if (!associationRule.antecedent.equals(antecedent)) {
                return false;
            }
            if (!associationRule.consequent.equals(consequent)) {
                return false;
            }
            if (!(associationRule.support == support)) {
                return false;
            }
            return associationRule.confidence == confidence;
        }
        return false;
    }
}
