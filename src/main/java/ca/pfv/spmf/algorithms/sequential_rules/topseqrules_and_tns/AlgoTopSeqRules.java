package ca.pfv.spmf.algorithms.sequential_rules.topseqrules_and_tns;
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

import ca.pfv.spmf.algorithms.ArraysAlgos;
import ca.pfv.spmf.datastructures.redblacktree.RedBlackTree;
import ca.pfv.spmf.input.sequence_database_array_integers.Sequence;
import ca.pfv.spmf.input.sequence_database_array_integers.SequenceDatabase;
import ca.pfv.spmf.tools.MemoryLogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * This is the original implementation of the TopSeqRule algorithm for mining Top-K sequential rules.
 * It is described in this paper:
 * <br/><br/>
 * Fournier-Viger, P. & Tseng, V. S. (2011). Mining Top-K Sequential Rules.
 * Proceedings of the 7th Intern. Conf. on Advanced Data Mining and Applications (ADMA 2011).
 * LNAI 7121, Springer, pp.180-194.
 * <br/><br/>
 * The implementation use the red-black tree data structure for storing the current top-k rules during
 * the mining process and the top-k candidate for exploration (see details in the paper).
 *
 * @author Philippe Fournier-Viger
 * @see RedBlackTree
 * @see Sequence
 * @see SequenceDatabase
 */
public class AlgoTopSeqRules {


    /**
     * start time of latest execution
     */
    long timeStart = 0;

    /**
     * end time of latest execution
     */
    long timeEnd = 0;

    // parameters
    /**
     * minimum confidence
     */
    double minConfidence;

    /**
     * the k parameter
     */
    int k = 0;

    /**
     * the sequence database
     */
    SequenceDatabase database;

    // internal variables
    /**
     * minimum support which will be raised dynamically
     */
    int minsuppRelative;

    /**
     * the top k rules found until now
     */
    RedBlackTree<Rule> kRules;

    /**
     * the candidates for expansion
     */
    RedBlackTree<Rule> candidates;

    /**
     * the max number of candidates at the same time during the last execution
     */
    int maxCandidateCount = 0;

    /**
     * Arrays where the ith position contains
     * // the map of last or first occurrences for the item i
     * // The key of the maps is a sequence ID and the value is an occurrence.
     */
    Map<Integer, Short>[] arrayMapItemCountFirst;  // item, <tid, occurrence>
    Map<Integer, Short>[] arrayMapItemCountLast;  // item, <tid, occurrence>

    /**
     * the maximum size of the antecedent of rules (optional)
     */
    int maxAntecedentSize = Integer.MAX_VALUE;

    /**
     * the maximum size of the consequent of rules (optional)
     */
    int maxConsequentSize = Integer.MAX_VALUE;

    /**
     * Default constructor
     */
    public AlgoTopSeqRules() {
    }


    /**
     * The main method to run the algorithm
     *
     * @param k             : the chosen value of k
     * @param database      : a sequence database
     * @param minConfidence : the minimum confidence threshold
     * @return a redblacktree containing the  top-k  sequential rules
     */
    public RedBlackTree<Rule> runAlgorithm(int k, SequenceDatabase database, double minConfidence) {
        // save the parameters
        this.database = database;
        this.minConfidence = minConfidence;
        this.k = k;

        // reset the utility for checking the memory usage
        MemoryLogger.getInstance().reset();

        // for statistics
        this.maxCandidateCount = 0;

        // set minsup = 1 (will be increased by the algorithm progressively)
        this.minsuppRelative = 1;

        // create the structure for storing the first/last occurrences
        arrayMapItemCountFirst = new HashMap[database.maxItem + 1];
        arrayMapItemCountLast = new HashMap[database.maxItem + 1];

        // the sets that will contain the top-k rules and the candidates
        kRules = new RedBlackTree<Rule>();
        candidates = new RedBlackTree<Rule>();

        // record start time
        timeStart = System.currentTimeMillis();

        if (maxAntecedentSize >= 1 && maxConsequentSize >= 1) {
            // scan the database to count the occurrence of each item
            scanDatabase(database);
            // start the algorithm
            start();
        }

        // record end time
        timeEnd = System.currentTimeMillis();

        // return the top-rules
        return kRules;
    }

    /**
     * Start the rule generation.
     */
    private void start() {
        // We will now try to generate rules with one item in the
        // antecedent and one item in the consequent using
        // frequent items.

        // For each pair of frequent items i  and j such that i != j
        main1:
        for (int itemI = database.minItem; itemI <= database.maxItem; itemI++) {
            // Get the map of occurrences of item I
            Map<Integer, Short> occurrencesIfirst = arrayMapItemCountFirst[itemI];

            // if none continue
            if (occurrencesIfirst == null) {
                continue main1;
            }
            // get  the set of sequence IDs containing I
            Set<Integer> tidsI = occurrencesIfirst.keySet();
            // if the support of I (cardinality of the tids) is lower
            // than minsup, than it is not frequent, so we skip this item
            if (tidsI.size() < minsuppRelative) {
                continue main1;
            }

            main2:
            for (int itemJ = itemI + 1; itemJ <= database.maxItem; itemJ++) {
                // Get the map of occurrences of item J
                Map<Integer, Short> occurrencesJfirst = arrayMapItemCountFirst[itemJ];

                // if none continue
                if (occurrencesJfirst == null) {
                    continue main2;
                }
                // get  the set of sequence IDs containing J
                Set<Integer> tidsJ = occurrencesJfirst.keySet();
                // if the support of J (cardinality of the tids) is lower
                // than minsup, than it is not frequent, so we skip this item
                if (tidsJ.size() < minsuppRelative) {
                    continue main2;
                }

                // (1) Build list of common  tids  and count occurrences
                // of i ==> j  and  j ==> i.

                // These two hashsets will store the tids of:
                Set<Integer> tidsIJ = new HashSet<Integer>();  // i ==> j
                Set<Integer> tidsJI = new HashSet<Integer>(); // j ==> i.

                // These maps will store the last occurrence of I
                // and last occurrence of J for each sequence ID (a.k.a. tid)
                //  key: tid     value:  itemset position
                Map<Integer, Short> occurrencesJlast = arrayMapItemCountLast[itemJ];
                Map<Integer, Short> occurrencesIlast = arrayMapItemCountLast[itemI];

                // if there is less tids in J, then
                // we will loop over J instead of I to calculate the tidsets
                if (tidsI.size() > tidsJ.size()) {

                    // this repsents the number of itemsets left to be scanned
                    int left = tidsJ.size();

                    // for each tid where J eappears
                    for (Entry<Integer, Short> entry : occurrencesJfirst.entrySet()) {
                        Integer tid = entry.getKey();

                        // get the first occurrence of I
                        Short occIFirst = occurrencesIfirst.get(tid);
                        // if there is one
                        if (occIFirst != null) {
                            // get the first and last occurrences of J
                            Short occJFirst = occurrencesJfirst.get(tid);
                            Short occJLast = occurrencesJlast.get(tid);
                            // If the first of I appears before the last of J
                            if (occIFirst < occJLast) {
                                // current tid to the tidset of  i ==> j
                                tidsIJ.add(tid);
                            }
                            Short occILast = occurrencesIlast.get(tid);
                            // If the first of J appears before the last of I
                            if (occJFirst < occILast) {
                                // current tid to the tidset of  j ==> i
                                tidsJI.add(tid);
                            }
                        }
                        left--;// go to next itemset (in backward direction)

                        // if there is not enough itemset left so that i--> j
                        // or j==> i could be frequent, then we can stop
                        if (((left + tidsIJ.size()) < minsuppRelative) &&
                            ((left + tidsJI.size()) < minsuppRelative)) {
                            continue main2;
                        }
                    }
                } else {
                    // otherwise
                    // we will loop over I instead of J to calculate the tidsets

                    // this repsents the number of itemsets left to be scanned
                    int left = tidsI.size();

                    for (Entry<Integer, Short> entry : occurrencesIfirst.entrySet()) {
                        Integer tid = entry.getKey();

                        // get the first occurrence of J
                        Short occJFirst = occurrencesJfirst.get(tid);

                        // if there is one
                        if (occJFirst != null) {
                            // get the first and last occurrences of I
                            Short occIFirst = occurrencesIfirst.get(tid);
                            Short occILast = occurrencesIlast.get(tid);
                            // If the first of J appears before the last of I
                            if (occJFirst < occILast) {
                                // current tid to the tidset of  j ==> i
                                tidsJI.add(tid);
                            }
                            Short occJLast = occurrencesJlast.get(tid);
                            // If the first of I appears before the last of J
                            if (occIFirst < occJLast) {
                                // current tid to the tidset of  i ==> j
                                tidsIJ.add(tid);
                            }
                        }
                        left--; // go to next itemset (in backward direction)

                        // if there is not enough itemset left so that i--> j
                        // or j==> i could be frequent, then we can stop
                        if (((left + tidsIJ.size()) < minsuppRelative) &&
                            ((left + tidsJI.size()) < minsuppRelative)) {
                            continue main2;
                        }
                    }
                }

                // (2) check if the two itemsets have enough common tids
                // if not, we don't need to generate a rule for them.
                // create rule IJ
                int supIJ = tidsIJ.size();

                // if the rule I ==> J  is frequent
                if (supIJ >= minsuppRelative) {
                    // create the rule
                    double confIJ = ((double) tidsIJ.size()) / occurrencesIfirst.size();
                    int[] itemsetI = new int[1];
                    itemsetI[0] = itemI;
                    int[] itemsetJ = new int[1];
                    itemsetJ[0] = itemJ;

                    Rule ruleIJ = new Rule(itemsetI, itemsetJ, confIJ, supIJ, tidsI, tidsJ, tidsIJ, occurrencesIfirst, occurrencesJlast);

                    // if the rule is valid
                    if (confIJ >= minConfidence) {
                        // save the rule to current top-k list
                        save(ruleIJ, supIJ);
                    }
                    if (ruleIJ.getItemset1().length < maxAntecedentSize ||
                        ruleIJ.getItemset2().length < maxConsequentSize) {
                        registerAsCandidate(true, ruleIJ);
                    }
                }

                int supJI = tidsJI.size();
                // if the rule J ==> I  is frequent
                if (supJI >= minsuppRelative) {
                    // create the rule
                    int[] itemsetI = new int[1];
                    itemsetI[0] = itemI;
                    int[] itemsetJ = new int[1];
                    itemsetJ[0] = itemJ;
                    double confJI = ((double) tidsJI.size()) / occurrencesJfirst.size();
                    Rule ruleJI = new Rule(itemsetJ, itemsetI, confJI, supJI, tidsJ, tidsI, tidsJI, occurrencesJfirst, occurrencesIlast);

                    // if the rule is valid
                    if (confJI >= minConfidence) {
                        // save the rule to current top-k list
                        save(ruleJI, supJI);
                    }
                    // register the rule as candidate for future left and right expansions
                    if (ruleJI.getItemset1().length < maxAntecedentSize ||
                        ruleJI.getItemset2().length < maxConsequentSize) {
                        registerAsCandidate(true, ruleJI);
                    }
                }
            }
        }

        // Now we have finished checking all the rules containing 1 item
        // in the left side and 1 in the right side,
        // the next step is to recursively expand rules in the set
        // "candidates" to find more rules.
        while (!candidates.isEmpty()) {
            // we take the rule with the highest support first
            Rule rule = candidates.popMaximum();
            // if there is no more candidates with enough support, then we stop
            if (rule.getAbsoluteSupport() < minsuppRelative) {
                break;
            }
            // Otherwise, we try to expand the rule
            if (rule.expandLR) {
                // we do it
                expandL(rule);
                expandR(rule);
            } else {
                // If the rule should only be expanded by left side to
                // avoid generating redundant rules, then we
                // only expand the left side.
                expandL(rule);
            }
        }
    }

    /**
     * Save a rule in the current top-k set
     *
     * @param rule    the rule
     * @param support the support of the rule
     */
    private void save(Rule rule, int support) {
        // We add the rule to the set of top-k rules
        kRules.add(rule);
        // if the size becomes larger than k
        if (kRules.size() > k) {
            // if the support of the rule that we haved added is higher than
            // the minimum support, we will need to take out at least one rule
            if (support > this.minsuppRelative) {
                // we recursively remove the rule having the lowest support,
                // until only k rules are left
                Rule lower;
                do {
                    lower = kRules.lower(new Rule(null, null, 0, this.minsuppRelative + 1, null, null, null, null, null));
                    if (lower == null) {
                        break;  /// IMPORTANT
                    }
                    kRules.remove(lower);
                } while (kRules.size() > k);
            }
            // we raise the minimum support to the lowest support in the
            // set of top-k rules
            this.minsuppRelative = kRules.minimum().getAbsoluteSupport();
        }
//		System.out.println(this.minsuppRelative);
    }

    /**
     * Add a candidate to the set of candidate
     *
     * @param expandLR a boolean indicating if this candidate is
     *                 for a left AND right expansion or just a left expansion.
     * @param ruleLR   the rule
     */
    private void registerAsCandidate(boolean expandLR, Rule ruleLR) {
        ruleLR.expandLR = expandLR;

        candidates.add(ruleLR); // add the rule

        // remember the maximum number of candidates reacher for stats
        if (candidates.size() >= maxCandidateCount) {
            maxCandidateCount = candidates.size();
        }
        // check the memory usage
        MemoryLogger.getInstance().checkMemory();
    }

    /**
     * This method search for items for expanding left side of a rule I --> J
     * with any item c. This results in rules of the form I Ué{c} --> J. The method makes sure that:
     * - c  is not already included in I or J
     * - c appear at least minsup time in tidsIJ before last occurrence of J
     * - c is lexically bigger than all items in I
     */
    private void expandL(Rule rule) {
        if (rule.getItemset1().length == maxAntecedentSize) {
            return;
        }

        // The following map will be used to count the support of each item
        // c that could potentially extend the rule.
        // The map associated a set of tids (value) to an item (key).
        Map<Integer, Set<Integer>> frequentItemsC = new HashMap<Integer, Set<Integer>>();

        // we scan the sequence where I-->J appear to search for items c that we could add.
        // for each sequence containing I-->J
        int left = rule.tidsIJ.size();
        for (Integer tid : rule.tidsIJ) {
            // get the sequence and occurrences of J in that sequence
            Sequence sequence = database.getSequences().get(tid);
            Short end = rule.occurrencesJlast.get(tid);

            // for each itemset before the last occurrence of J
            itemLoop:
            for (int k = 0; k < end; k++) {
                Integer[] itemset = sequence.get(k);
                // for each item
                for (int m = 0; m < itemset.length; m++) {
                    Integer itemC = itemset[m];

                    // We will consider if we could create a rule IU{c} --> J
                    // If lexical order is not respected or c is included in the rule already,
                    // then we cannot so return.
                    if (ArraysAlgos.containsLEXPlus(rule.getItemset1(), itemC)
                        || ArraysAlgos.containsLEX(rule.getItemset2(), itemC)) {
                        continue;
                    }

                    // Otherwise, we get the tidset of "c"
                    Set<Integer> tidsItemC = frequentItemsC.get(itemC);

                    // if this set is not null, which means that "c" was not seen yet
                    // when scanning the sequences from I==>J
                    if (tidsItemC == null) {
                        // if there is less tids left in the tidset of I-->J to be scanned than
                        // the minsup, we don't consider c anymore because  IU{c} --> J
                        // could not be frequent
                        if (left < minsuppRelative) {
                            continue itemLoop;
                        }
                        // if "c" was seen before but there is not enough sequences left to be scanned
                        // to allow IU{c} --> J to reach the minimum support threshold
                    } else if (tidsItemC.size() + left < minsuppRelative) {
                        // remove c and continue the loop of items
                        tidsItemC.remove(itemC);
                        continue itemLoop;
                    }
                    // otherwise, if we did not see "c" yet, create a new tidset for "c"
                    if (tidsItemC == null) {
                        tidsItemC = new HashSet<Integer>(rule.tidsIJ.size());
                        frequentItemsC.put(itemC, tidsItemC);
                    }
                    // add the current tid to the tidset of "c"
                    tidsItemC.add(tid);
                }
            }
            left--;// decrease the number of sequences left to be scanned
        }

        // For each item c found, we create a rule	IU{c} ==> J
        for (Entry<Integer, Set<Integer>> entry : frequentItemsC.entrySet()) {
            // get the tidset IU{c} ==> J
            Set<Integer> tidsIC_J = entry.getValue();

            // if the support of IU{c} ==> J is enough
            if (tidsIC_J.size() >= minsuppRelative) {
                Integer itemC = entry.getKey();

                // Calculate tids containing IU{c} which is necessary
                // to calculate the confidence
                Set<Integer> tidsIC = new HashSet<Integer>(rule.tidsI.size());
                for (Integer tid : rule.tidsI) {
                    if (arrayMapItemCountFirst[itemC].containsKey(tid)) {
                        tidsIC.add(tid);
                    }
                }

                // Create rule and calculate its confidence of IU{c} ==> J
                // defined as:  sup(IU{c} -->J) /  sup(IU{c})
                double confIC_J = ((double) tidsIC_J.size()) / tidsIC.size();
                int[] itemsetIC = new int[rule.getItemset1().length + 1];
                System.arraycopy(rule.getItemset1(), 0, itemsetIC, 0, rule.getItemset1().length);
                itemsetIC[rule.getItemset1().length] = itemC;

                // if the confidence is high enough, then it is a valid rule
                Rule candidate = new Rule(itemsetIC, rule.getItemset2(), confIC_J, tidsIC_J.size(), tidsIC, null, tidsIC_J, null,
                        rule.occurrencesJlast);
                if (confIC_J >= minConfidence) {
                    // save the rule
                    save(candidate, tidsIC_J.size());
                }
                registerAsCandidate(false, candidate);
            }
        }
        // check the memory usage
        MemoryLogger.getInstance().checkMemory();
    }

    /**
     * This method search for items for expanding right side of a rule I --> J
     * with any item c. This results in rules of the form I --> J U{c}.
     * The method makes sure that:
     * - c  is not already included in I or J
     * - c appear at least minsup time in tidsIJ after the first occurrence of I
     * - c is lexically larger than all items in J
     *
     * @param rule the rule I --> J to be extended
     */
    private void expandR(Rule rule) {

        if (rule.getItemset2().length == maxConsequentSize) {
            return;
        }

        // The following map will be used to count the support of each item
        // c that could potentially extend the rule.
        // The map associated a set of tids (value) to an item (key).
        Map<Integer, Set<Integer>> frequentItemsC = new HashMap<Integer, Set<Integer>>();

        // we scan the sequence where I-->J appear to search for items c that we could add.
        // for each sequence containing I-->J.
        int left = rule.tidsIJ.size();

        // For each tid of sequence containing I-->J
        for (Integer tid : rule.tidsIJ) {
            // get the sequence and get first occurrence of I in that sequence
            Sequence sequence = database.getSequences().get(tid);
            Short first = rule.occurrencesIfirst.get(tid);

            // for each itemset after the first occurrence of I in that sequence
            for (int k = first + 1; k < sequence.size(); k++) {
                Integer[] itemset = sequence.get(k);
                // for each item
                itemLoop:
                for (int m = 0; m < itemset.length; m++) {
                    // for each item c in that itemset
                    Integer itemC = itemset[m];

                    // We will consider if we could create a rule I --> J U{c}
                    // If lexical order is not respected or c is included in the rule already,
                    // then we cannot so the algorithm return.
                    if (ArraysAlgos.containsLEX(rule.getItemset1(), itemC)
                        || ArraysAlgos.containsLEXPlus(rule.getItemset2(), itemC)) {
                        continue;
                    }

                    Set<Integer> tidsItemC = frequentItemsC.get(itemC);
                    // if "c" was seen before but there is not enough sequences left to be scanned
                    // to allow IU --> J {c} to reach the minimum support threshold
                    if (tidsItemC == null) {
                        if (left < minsuppRelative) {
                            continue itemLoop;
                        }
                    } else if (tidsItemC.size() + left < minsuppRelative) {
                        // if "c" was seen before but there is not enough sequences left to be scanned
                        // to allow I--> JU{c}  to reach the minimum support threshold,
                        // remove "c" and continue the loop of items
                        tidsItemC.remove(itemC);
                        continue itemLoop;
                    }
                    if (tidsItemC == null) {
                        // otherwise, if we did not see "c" yet, create a new tidset for "c"
                        tidsItemC = new HashSet<Integer>(rule.tidsIJ.size());
                        frequentItemsC.put(itemC, tidsItemC);
                    }
                    // add the current tid to the tidset of "c"
                    tidsItemC.add(tid);
                }
            }
            left--;// decrease the number of sequences left to be scanned
        }

        // For each item c found, we create a rule	I ==> JU {c}
        for (Entry<Integer, Set<Integer>> entry : frequentItemsC.entrySet()) {
            // get the tidset of I ==> JU {c}
            Set<Integer> tidsI_JC = entry.getValue();

            // if the support of I ==> JU{c} is enough
            if (tidsI_JC.size() >= minsuppRelative) {
                Integer itemC = entry.getKey();

                // create the itemset JU{c} and calculate the occurrences of JU{c}
                Set<Integer> tidsJC = new HashSet<Integer>(rule.tidsJ.size());
                Map<Integer, Short> occurrencesJC = new HashMap<Integer, Short>();

                // for each sequence containing J
                for (Integer tid : rule.tidsJ) {
                    // Get the  last occurrence of C in that sequence
                    Short occurrenceCLast = arrayMapItemCountLast[itemC].get(tid);
                    // if there is an occurrence
                    if (occurrenceCLast != null) {
                        // add the tid of the sequence to the tidset of JU{c}
                        tidsJC.add(tid);
                        // calculate last occurrence of JU{c} depending on if
                        // the last occurrence of J is before the last occurrence
                        // of c or not.
                        Short occurrenceJlast = rule.occurrencesJlast.get(tid);
                        if (occurrenceCLast < occurrenceJlast) {
                            occurrencesJC.put(tid, occurrenceCLast);
                        } else {
                            occurrencesJC.put(tid, occurrenceJlast);
                        }
                    }
                }

                // Create rule I ==> J U{c} and calculate its confidence
                // defined as:  sup(I -->J U{c}) /  sup(I)
                double confI_JC = ((double) tidsI_JC.size()) / rule.tidsI.size();
                int[] itemsetJC = new int[rule.getItemset2().length + 1];
                System.arraycopy(rule.getItemset2(), 0, itemsetJC, 0, rule.getItemset2().length);
                itemsetJC[rule.getItemset2().length] = itemC;

                // create the rule
                Rule candidate = new Rule(rule.getItemset1(), itemsetJC, confI_JC, tidsI_JC.size(),
                        rule.tidsI, tidsJC, tidsI_JC, rule.occurrencesIfirst, occurrencesJC);
                // if the confidence is enough
                if (confI_JC >= minConfidence) {
                    // then it is a valid rule so save it to the set of k rules
                    save(candidate, tidsI_JC.size());
                }
                // register the rule as a candidate for future expansions
                registerAsCandidate(true, candidate);
            }
        }
        // check the memory usage
        MemoryLogger.getInstance().checkMemory();
    }


    /**
     * This method calculate the frequency of each item in one database pass.
     *
     * @param database : a sequence database
     * @return A map such that key = item
     * value = a map  where a key = tid  and a value = occurrence
     * This map allows knowing the frequency of each item and their first and last occurrence in each sequence.
     */
    private void scanDatabase(SequenceDatabase database) {
        // (1) Count the support of each item in the database in one database pass

        // for each sequence in the database
        for (int tid = 0; tid < database.size(); tid++) {
            Sequence sequence = database.getSequences().get(tid);
            // for each itemset in that sequence
            for (short j = 0; j < sequence.getItemsets().size(); j++) {
                Integer[] itemset = sequence.get(j);
                // for each item in that sequence
                for (int i = 0; i < itemset.length; i++) {
                    Integer itemI = itemset[i];
                    // if the map of occurrences of that item is null, create a new one
                    if (arrayMapItemCountFirst[itemI] == null) {
                        arrayMapItemCountFirst[itemI] = new HashMap<Integer, Short>();
                        arrayMapItemCountLast[itemI] = new HashMap<Integer, Short>();
                    }
                    // then update the occurrences by adding j as the
                    // first and/or last occurrence(s) in sequence k
                    Short oldPosition = arrayMapItemCountFirst[itemI].get(tid);
                    if (oldPosition == null) {
                        arrayMapItemCountFirst[itemI].put(tid, j);
                        arrayMapItemCountLast[itemI].put(tid, j);
                    } else {
                        arrayMapItemCountLast[itemI].put(tid, j);
                    }
                }
            }
        }
    }

    /**
     * Print statistics about the last algorithm execution to System.out.
     */
    public void printStats() {
        System.out.println("=============  TOPSEQRULES - STATS ========");
        System.out.println("Max candidates: " + maxCandidateCount);
        System.out.println("Sequential rules count: " + kRules.size());
        System.out.println("-");
        System.out.println("Total time: " + (((double) (timeEnd - timeStart)) / 1000d) + " s");
        System.out.println("Max memory: " + MemoryLogger.getInstance().getMaxMemory());
        System.out.println("Minsup relative: " + minsuppRelative);
        System.out.println("==========================================");
    }

    /**
     * Get the total runtime of the last execution.
     *
     * @return the time as a double.
     */
    public double getTotalTime() {
        return timeEnd - timeStart;
    }

    /**
     * Write the result by the last execution of the method "runAlgorithm" to an output file
     *
     * @param path the output file path
     * @throws IOException exception if an error occur when writing the file.
     */
    public void writeResultTofile(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        if (kRules.size() > 0) {
            Iterator<Rule> iter = kRules.iterator();
            while (iter.hasNext()) {
                Rule rule = iter.next();
                final String buffer = rule.toString() +
                                      // write separator
                                      " #SUP: " +
                                      // write support
                                      rule.getAbsoluteSupport() +
                                      // write separator
                                      " #CONF: " +
                                      // write confidence
                                      rule.getConfidence();
                writer.write(buffer);
                writer.newLine();
            }
        }
        writer.close();
    }

    /**
     * Set the number of items that a rule antecedent should contain (optional).
     *
     * @param maxAntecedentSize the maximum number of items
     */
    public void setMaxAntecedentSize(int maxAntecedentSize) {
        this.maxAntecedentSize = maxAntecedentSize;
    }


    /**
     * Set the number of items that a rule consequent should contain (optional).
     *
     * @param maxConsequentSize the maximum number of items
     */
    public void setMaxConsequentSize(int maxConsequentSize) {
        this.maxConsequentSize = maxConsequentSize;
    }


}
