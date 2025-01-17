package ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Sequence;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.savers.Saver;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.savers.SaverIntoFile;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.savers.SaverIntoMemory;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.tries.Trie;
import ca.pfv.spmf.tools.MemoryLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * This is an implementation of the ClaSP algorithm. ClaSP was proposed by A.
 * Gomariz et al. in 2013.
 * <p>
 * NOTE: This implementation saves the pattern to a file as soon as they are
 * found or can keep the pattern into memory, depending on what the user choose.
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
public class AlgoCM_ClaSP {

    /**
     * Start and End points in order to calculate the overall time taken by the
     * algorithm
     */
    public long overallStart, overallEnd;
    public long joinCount; // PFV 2013
    /**
     * The absolute minimum support threshold, i.e. the minimum number of
     * sequences where the patterns have to be
     */
    protected double minSupAbsolute;
    /**
     * Start and End points in order to calculate the time taken by the main
     * part of CloSpan algorithm
     */
    protected long mainMethodStart, mainMethodEnd;
    /**
     * Start and End points in order to calculate the time taken by the
     * post-processing method of CloSpan algorithm
     */
    protected long postProcessingStart, postProcessingEnd;
    /**
     * Trie root that starts with the empty pattern and from which we will be
     * able to access to all the frequent patterns generated by ClaSP
     */
    protected Trie FrequentAtomsTrie;
    /**
     * Saver variable to decide where the user want to save the results, if it
     * the case
     */
    Saver saver = null;
    /**
     * The abstraction creator
     */
    private AbstractionCreator abstractionCreator;
    /**
     * Number of frequent patterns found by the algorithm
     */
    private int numberOfFrequentPatterns, numberOfFrequentClosedPatterns;
    /**
     * flag to indicate if we are interesting in only finding the closed
     * sequences
     */
    private final boolean findClosedPatterns;
    /**
     * flag to indicate if we are interesting in only finding the closed
     * sequence through the postprocessing step
     */
    private final boolean executePruningMethods;

    /**
     * Constructor of the class that calls ClaSP algorithm.
     *
     * @param support            Absolute minimum support
     * @param abstractionCreator the abstraction creator
     * @param findClosedPatterns flag to indicate if we are interesting in only
     */
    public AlgoCM_ClaSP(double support, AbstractionCreator abstractionCreator, boolean findClosedPatterns, boolean executePruningMethods) {
        this.minSupAbsolute = support;
        this.abstractionCreator = abstractionCreator;
        this.findClosedPatterns = findClosedPatterns;
        this.executePruningMethods = executePruningMethods;
    }

    /**
     * Actual call to ClaSP algorithm. The output can be either kept or ignore.
     * Whenever we choose to keep the patterns found, we can keep them in a file
     * or in the main memory
     *
     * @param database                  Original database in where we want to search for the
     *                                  frequent patterns.
     * @param keepPatterns              Flag indicating if we want to keep the output or not
     * @param verbose                   Flag for debugging purposes
     * @param outputFilePath            Path of the file in which we want to store the
     *                                  frequent patterns. If this value is null, we keep the patterns in the
     *                                  main memory. This argument is taken into account just when keepPatterns
     *                                  is activated.
     * @param outputSequenceIdentifiers indicates if sequence ids should be output with each pattern found.
     * @throws IOException
     */
    public void runAlgorithm(SequenceDatabase database, boolean keepPatterns, boolean verbose, String outputFilePath,
                             boolean outputSequenceIdentifiers)
            throws IOException {
        //If we do no have any file path
        if (outputFilePath == null) {
            //The user wants to save the results in memory
            saver = new SaverIntoMemory(outputSequenceIdentifiers);
        } else {
            //Otherwise, the user wants to save them in the given file
            saver = new SaverIntoFile(outputFilePath, outputSequenceIdentifiers);
        }
        // reset the stats about memory usage
        MemoryLogger.getInstance().reset();
        //keeping the starting time
        overallStart = System.currentTimeMillis();
        //Starting ClaSP algorithm
        claSP(database, (long) minSupAbsolute, keepPatterns, verbose, findClosedPatterns, executePruningMethods);
        //keeping the ending time
        overallEnd = System.currentTimeMillis();
        //Search for frequent patterns: Finished
        saver.finish();
    }

    /**
     * The actual method for extracting frequent sequences.
     *
     * @param database       The original database
     * @param minSupAbsolute the absolute minimum support
     * @param keepPatterns   flag indicating if we are interested in keeping the
     *                       output of the algorithm
     * @param verbose        Flag for debugging purposes
     * @param
     * @throws IOException
     */
    protected void claSP(SequenceDatabase database, long minSupAbsolute, boolean keepPatterns, boolean verbose, boolean findClosedPatterns,
                         boolean executePruningMethods)
            throws IOException {
        //We get the initial trie whose children are the frequent 1-patterns
        FrequentAtomsTrie = database.frequentItems();

        //  NEW-CODE-PFV 2013
        // Map: key: item   value:  another item that followed the first item + support
        // (could be replaced with a triangular matrix...)
        Map<Integer, Map<Integer, Integer>> coocMapAfter = new HashMap<Integer, Map<Integer, Integer>>(1000);
        Map<Integer, Map<Integer, Integer>> coocMapEquals = new HashMap<Integer, Map<Integer, Integer>>(1000);

        // update COOC map
        for (Sequence seq : database.getSequences()) {
            HashSet<Integer> alreadySeenA = new HashSet<Integer>();
            Map<Integer, Set<Integer>> alreadySeenB_equals = new HashMap<>();

            // for each item
            for (int i = 0; i < seq.getItemsets().size(); i++) {
                Itemset itemsetA = seq.get(i);
                for (int j = 0; j < itemsetA.size(); j++) {
                    Integer itemA = (Integer) itemsetA.get(j).getId();

                    boolean alreadyDoneForItemA = false;
                    Set equalSet = alreadySeenB_equals.get(itemA);
                    if (equalSet == null) {
                        equalSet = new HashSet();
                        alreadySeenB_equals.put(itemA, equalSet);
                    }

                    if (alreadySeenA.contains(itemA)) {
                        alreadyDoneForItemA = true;
                    }

                    // create the map if not existing already
                    Map<Integer, Integer> mapCoocItemEquals = coocMapEquals.get(itemA);
                    // create the map if not existing already
                    Map<Integer, Integer> mapCoocItemAfter = null;
                    if (!alreadyDoneForItemA) {
                        mapCoocItemAfter = coocMapAfter.get(itemA);
                    }

                    //For each item after itemA in the same itemset
                    for (int k = j + 1; k < itemsetA.size(); k++) {
                        Integer itemB = (Integer) itemsetA.get(k).getId();
                        if (!equalSet.contains(itemB)) {
                            if (mapCoocItemEquals == null) {
                                mapCoocItemEquals = new HashMap<Integer, Integer>();
                                coocMapEquals.put(itemA, mapCoocItemEquals);
                            }
                            Integer frequency = mapCoocItemEquals.get(itemB);

                            if (frequency == null) {
                                mapCoocItemEquals.put(itemB, 1);
                            } else {
                                mapCoocItemEquals.put(itemB, frequency + 1);
                            }

                            equalSet.add(itemB);
                        }
                    }

                    HashSet<Integer> alreadySeenB_after = new HashSet<Integer>();
                    // for each item after
                    if (!alreadyDoneForItemA) {
                        for (int k = i + 1; k < seq.getItemsets().size(); k++) {
                            Itemset itemsetB = seq.get(k);
                            for (int m = 0; m < itemsetB.size(); m++) {
                                Integer itemB = (Integer) itemsetB.get(m).getId();
                                if (alreadySeenB_after.contains(itemB)) {
                                    continue;
                                }

                                if (mapCoocItemAfter == null) {
                                    mapCoocItemAfter = new HashMap<Integer, Integer>();
                                    coocMapAfter.put(itemA, mapCoocItemAfter);
                                }
                                Integer frequency = mapCoocItemAfter.get(itemB);
                                if (frequency == null) {
                                    mapCoocItemAfter.put(itemB, 1);
                                } else {
                                    mapCoocItemAfter.put(itemB, frequency + 1);
                                }
                                alreadySeenB_after.add(itemB);
                            }
                        }
                        alreadySeenA.add(itemA);
                    }
                }
            }
        }

        database.clear();
        database = null;

        // DEBUGING PFV
        // Calculate the size of CMAP (hashmaps)
//    	
//		int pairCount = 0;
//		double maxMemory = getObjectSize(coocMapAfter);
//		for(Entry<Integer, Map<Integer, Integer>> entry : coocMapAfter.entrySet()) {
//			maxMemory += getObjectSize(entry.getKey());
//			for(Entry<Integer, Integer> entry2 :entry.getValue().entrySet()) {
//				pairCount++;
//				maxMemory += getObjectSize(entry2.getKey()) + getObjectSize(entry2.getValue());
//			}
//		}
//		System.out.println("CMAP size " + maxMemory + " MB");
//		System.out.println("PAIR COUNT " + pairCount);

//		// Calculate the size of CMAP (matrix) INTEGERS
        //2990, 9025, 13905, 267, 20, 497, 10094
//        int size = 2990;

//		System.exit(0);


        // Calculate the size of CMAP (matrix) BITSETS
        // BMS, KOSARAK, LEV, SNAKE, SIGN, FIFA
//		int [] sizes = new int[] {497, 10094, 9025, 20, 267, 2990};
//		for(int size : sizes) {
//			int[][] array2 = new int[size][size];
//			System.out.println(" INT MATRIX :" + getObjectSize(array2) + "MB");
//			
//			BitSet array = new BitSet(size);
//			System.out.println("BITSET : " + getObjectSize(array)*((double)size) + " MB");
//		}
//		System.exit(0);
        // END-OF-NEW-CODE

        //Inizialitation of the class that is in charge of find the frequent patterns
        FrequentPatternEnumeration_ClaSP frequentPatternEnumeration = new FrequentPatternEnumeration_ClaSP(abstractionCreator, minSupAbsolute,
                saver, findClosedPatterns, executePruningMethods);

        this.mainMethodStart = System.currentTimeMillis();
        //We dfsPruning the search
        frequentPatternEnumeration.dfsPruning(new Pattern(), FrequentAtomsTrie, verbose, coocMapAfter, coocMapEquals);
        this.mainMethodEnd = System.currentTimeMillis();
        //Once we had finished, we keep the number of frequent patterns that we found
        numberOfFrequentPatterns = frequentPatternEnumeration.getFrequentPatterns();

        // check the memory usage for statistics
        MemoryLogger.getInstance().checkMemory();

        if (verbose) {
            System.out.println("ClaSP: The algorithm takes " + (mainMethodEnd - mainMethodStart) / 1000 + " seconds and finds " + numberOfFrequentPatterns + " patterns");
        }
        //If the we are interested in closed patterns, we dfsPruning the post-processing step
        if (findClosedPatterns) {
            List<Entry<Pattern, Trie>> outputPatternsFromMainMethod = FrequentAtomsTrie.preorderTraversal(null);

            this.postProcessingStart = System.currentTimeMillis();
            frequentPatternEnumeration.removeNonClosedPatterns(outputPatternsFromMainMethod, keepPatterns);
            this.postProcessingEnd = System.currentTimeMillis();
            numberOfFrequentClosedPatterns = frequentPatternEnumeration.getFrequentClosedPatterns();
            if (verbose) {
                System.out.println("ClaSP:The post-processing algorithm to remove the non-Closed patterns takes " + (postProcessingEnd - postProcessingStart) / 1000 + " seconds and finds " + numberOfFrequentClosedPatterns + " closed patterns");
            }
        } else {
            if (keepPatterns) {
                List<Entry<Pattern, Trie>> outputPatternsFromMainMethod = FrequentAtomsTrie.preorderTraversal(null);
                for (Entry<Pattern, Trie> p : outputPatternsFromMainMethod) {
                    saver.savePattern(p.getKey());
                }
            }
        }

        numberOfFrequentPatterns = frequentPatternEnumeration.getFrequentPatterns();
        frequentPatternEnumeration.clear();

        // check the memory usage for statistics
        MemoryLogger.getInstance().checkMemory();

        joinCount = frequentPatternEnumeration.joinCount;
    }

    private double getObjectSize(
            Object object)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        double maxMemory = baos.size() / 1024d / 1024d;
        return maxMemory;
    }

    /**
     * Method to show the outlined information about the search for frequent
     * sequences by means of ClaSP algorithm
     *
     * @return
     */
    public String printStatistics() {
        final String r = "=============  Algorithm - STATISTICS =============\n Total time ~ " +
                         getRunningTime() +
                         " ms\n" +
                         " Frequent closed sequences count : " +
                         numberOfFrequentClosedPatterns +
                         '\n' +
                         " Join count : " +
                         joinCount +
                         " Max memory (mb):" +
                         MemoryLogger.getInstance().getMaxMemory() +
                         '\n' +
                         saver.print() +
                         "\n===================================================\n";
        return r;
    }

    public int getNumberOfFrequentPatterns() {
        return numberOfFrequentPatterns;
    }

    public int getNumberOfFrequentClosedPatterns() {
        return numberOfFrequentClosedPatterns;
    }


    /**
     * It gets the time spent by the algoritm in its execution.
     *
     * @return
     */
    public long getRunningTime() {
        return (overallEnd - overallStart);
    }

    /**
     * It clears all the attributes of AlgoClaSP class
     */
    public void clear() {
        FrequentAtomsTrie.removeAll();
        abstractionCreator = null;
    }

    /**
     * Get the trie (internal structure used by ClaSP).
     *
     * @return the trie
     */
    public Trie getFrequentAtomsTrie() {
        return FrequentAtomsTrie;
    }

}
