package ca.pfv.spmf.algorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class provides a set of basic methods that can be used with itemsets
 * represented as arrays of integers.
 * All the methods are static methods so that they can be used in any classes.
 *
 * @author Philippe Fournier-Viger
 */
public class ArraysAlgos {

    /**
     * A Comparator for comparing two itemsets having the same size using the lexical order.
     */
    public static Comparator<int[]> comparatorItemsetSameSize = new Comparator<int[]>() {
        @Override
        /**
         * Compare two itemsets and return -1,0 and 1 if the second itemset
         * is larger, equal or smaller than the first itemset according to the lexical order.
         */
        public int compare(int[] itemset1, int[] itemset2) {
            // for each item in the first itemset
            for (int i = 0; i < itemset1.length; i++) {
                // if the current item is smaller in the first itemset
                if (itemset1[i] < itemset2[i]) {
                    return -1; // than the first itemset is smaller
                    // if the current item is larger in the first itemset
                } else if (itemset2[i] < itemset1[i]) {
                    return 1; // than the first itemset is larger
                }
                // otherwise they are equal so the next item in both itemsets will be compared next.
            }
            return 0; // both itemsets are equal
        }
    };

    /**
     * Make a copy of this itemset but exclude a given item
     *
     * @param itemToRemove the given item
     * @return the copy
     */
    public static int[] cloneItemSetMinusOneItem(int[] itemset, Integer itemToRemove) {
        // create the new itemset
        int[] newItemset = new int[itemset.length - 1];
        int i = 0;
        // for each item in this itemset
        for (int j = 0; j < itemset.length; j++) {
            // copy the item except if it is the item that should be excluded
            if (itemset[j] != itemToRemove) {
                newItemset[i++] = itemset[j];
            }
        }
        return newItemset; // return the copy
    }

    /**
     * Make a copy of this itemset but exclude a set of items
     *
     * @param itemsetToNotKeep the set of items to be excluded
     * @return the copy
     */
    public static int[] cloneItemSetMinusAnItemset(int[] itemset, int[] itemsetToNotKeep) {
        // create a new itemset
        int[] newItemset = new int[itemset.length - itemsetToNotKeep.length];
        int i = 0;
        // for each item of this itemset
        for (int j = 0; j < itemset.length; j++) {
            // copy the item except if it is not an item that should be excluded
            if (Arrays.binarySearch(itemsetToNotKeep, itemset[j]) < 0) {
                newItemset[i++] = itemset[j];
            }
        }
        return newItemset; // return the copy
    }

    /**
     * This method checks if this itemset is the same as another itemset
     * except for the last item. It assumes that both itemsets have the same length.
     *
     * @param itemset1 the first itemset
     * @param itemset2 the second itemset
     * @return true if they are the same except for the last item
     */
    public static boolean allTheSameExceptLastItem(int[] itemset1, int[] itemset2) {
        // Otherwise, we have to compare item by item
        for (int i = 0; i < itemset1.length - 1; i++) {
            // if they are not the last items, they should be the same
            // otherwise return false
            if (itemset1[i] != itemset2[i]) {
                return false;
            }
        }
        // All items are the same. We return true.
        return true;
    }

    /**
     * Method to concatenate two arrays in a new array
     *
     * @param prefix the first array
     * @param suffix the second array
     * @return the resulting array
     */
    public static int[] concatenate(int[] prefix, int[] suffix) {
        int[] concatenation = new int[prefix.length + suffix.length];
        System.arraycopy(prefix, 0, concatenation, 0, prefix.length);
        System.arraycopy(suffix, 0, concatenation, prefix.length, suffix.length);
        return concatenation;
    }

    /**
     * This method performs the intersection of two sorted arrays of integers and return a new sorted array.
     *
     * @param a the first array
     * @param b the second array
     * @return the resulting sorted array
     */
    public static int[] intersectTwoSortedArrays(int[] array1, int[] array2) {
        // create a new array having the smallest size between the two arrays
        final int newArraySize = (array1.length < array2.length) ? array1.length : array2.length;
        int[] newArray = new int[newArraySize];

        int pos1 = 0;
        int pos2 = 0;
        int posNewArray = 0;
        while (pos1 < array1.length && pos2 < array2.length) {
            if (array1[pos1] < array2[pos2]) {
                pos1++;
            } else if (array2[pos2] < array1[pos1]) {
                pos2++;
            } else { // if they are the same
                newArray[posNewArray] = array1[pos1];
                posNewArray++;
                pos1++;
                pos2++;
            }
        }
        // return the subrange of the new array that is full.
        return Arrays.copyOfRange(newArray, 0, posNewArray);
    }

    /**
     * Check if an itemset contains another itemset.
     * It assumes that itemsets are sorted according to the lexical order.
     *
     * @param itemset1 the first itemset
     * @param itemset2 the second itemset
     * @return true if the first itemset contains the second itemset
     */
    public static boolean containsOrEquals(Integer[] itemset1, Integer[] itemset2) {
        // for each item in the first itemset
        loop1:
        for (int i = 0; i < itemset2.length; i++) {
            // for each item in the second itemset
            for (int j = 0; j < itemset1.length; j++) {
                // if the current item in itemset1 is equal to the one in itemset2
                // search for the next one in itemset1
                if (itemset1[j].intValue() == itemset2[i].intValue()) {
                    continue loop1;
                    // if the current item in itemset1 is larger
                    // than the current item in itemset2, then
                    // stop because of the lexical order.
                } else if (itemset1[j].intValue() > itemset2[i].intValue()) {
                    return false;
                }
            }
            // means that an item was not found
            return false;
        }
        // if all items were found, return true.
        return true;
    }

    /**
     * Check if an itemset contains another itemset. It assumes that itemsets are
     * sorted according to the lexical order.
     *
     * @param itemset1 the first itemset
     * @param itemset2 the second itemset
     * @return true if the first itemset contains the second itemset
     */
    public static boolean containsOrEquals(Short[] itemset1, Short[] itemset2) {
        // for each item in the first itemset
        loop1:
        for (int i = 0; i < itemset2.length; i++) {
            // for each item in the second itemset
            for (int j = 0; j < itemset1.length; j++) {
                // if the current item in itemset1 is equal to the one in itemset2
                // search for the next one in itemset1
                if (itemset1[j].shortValue() == itemset2[i].shortValue()) {
                    continue loop1;
                    // if the current item in itemset1 is larger
                    // than the current item in itemset2, then
                    // stop because of the lexical order.
                } else if (itemset1[j].shortValue() > itemset2[i].shortValue()) {
                    return false;
                }
            }
            // means that an item was not found
            return false;
        }
        // if all items were found, return true.
        return true;
    }

    /**
     * Check the first itemset contains the second one
     *
     * @param itemset1 an itemset
     * @param itemset2 another itemset
     * @return true if itemset2 is a subset of itemset1, false otherwise
     */
    public static boolean containsOrEquals(List<Short> itemset1, List<Short> itemset2) {
        // for each item in the first itemset
        loop1:
        for (int i = 0; i < itemset2.size(); i++) {
            short val2 = itemset2.get(i);
            // for each item in the second itemset
            for (int j = 0; j < itemset1.size(); j++) {
                short val1 = itemset1.get(j);
                // if the current item in itemset1 is equal to the one in itemset2
                // search for the next one in itemset1
                if (val1 == val2) {
                    continue loop1;
                    // if the current item in itemset1 is larger
                    // than the current item in itemset2, then
                    // stop because of the lexical order.
                } else if (val1 > val2) {
                    return false;
                }
            }
            // means that an item was not found
            return false;
        }
        // if all items were found, return true.
        return true;
    }

    /**
     * This method checks if an item "item" is in the itemset "itemset".
     * It assumes that items in the itemset are sorted in lexical order and
     * that the largest item in the itemset is known.
     *
     * @param itemset        an itemset
     * @param item           the item
     * @param maxItemInArray the largest item in the itemset
     * @return returnt true if the item appears in the itemset
     */
    public static boolean containsLEX(Integer[] itemset, Integer item, int maxItemInArray) {
        // if the item is larger than the largest item
        // in the itemset, return false
        if (item > maxItemInArray) {
            return false;
        }
        // Otherwise, for each item in itemset
        for (Integer itemI : itemset) {
            // check if the current item is equal to the one that is searched
            if (itemI.equals(item)) {
                // if yes return true
                return true;
            }
            // if the current item is larger than the searched item,
            // the method returns false because of the lexical order in the itemset.
            else if (itemI > item) {
                return false;  // <-- xxxx
            }
        }
        // if the searched item was not found, return false.
        return false;
    }

    /**
     * Method to compare two sorted list of integers and see if they are the same,
     * while ignoring an item from the second list of integer.
     * This methods is used by some Apriori algorithms.
     *
     * @param itemset1   the first itemset
     * @param itemsets2  the second itemset
     * @param posRemoved the position of an item that should be ignored from "itemset2" to perform the comparison.
     * @return 0 if they are the same, 1 if itemset is larger according to lexical order,
     * -1 if smaller.
     */
    public static int sameAs(int[] itemset1, int[] itemsets2, int posRemoved) {
        // a variable to know which item from candidate we are currently searching
        int j = 0;
        // loop on items from "itemset"
        for (int i = 0; i < itemset1.length; i++) {
            // if it is the item that we should ignore, we skip it
            if (j == posRemoved) {
                j++;
            }
            // if we found the item j, we will search the next one
            if (itemset1[i] == itemsets2[j]) {
                j++;
                // if  the current item from i is larger than j,
                // it means that "itemset" is larger according to lexical order
                // so we return 1
            } else if (itemset1[i] > itemsets2[j]) {
                return 1;
            } else {
                // otherwise "itemset" is smaller so we return -1.
                return -1;
            }
        }
        return 0;
    }

    /**
     * Check if a sorted itemset is contained in another
     *
     * @param itemset1 the first itemset
     * @param itemset2 the second itemset
     * @return true if yes, otherwise false
     */
    public static boolean includedIn(int[] itemset1, int[] itemset2) {
        int count = 0; // the current position of itemset1 that we want to find in itemset2

        // for each item in itemset2
        for (int i = 0; i < itemset2.length; i++) {
            // if we found the item
            if (itemset2[i] == itemset1[count]) {
                // we will look for the next item of itemset1
                count++;
                // if we have found all items already, return true
                if (count == itemset1.length) {
                    return true;
                }
            }
        }
        // it is not included, so return false!
        return false;
    }

    /**
     * Check if a sorted itemset is contained in another
     *
     * @param itemset1 the first itemset
     * @param length   of the first itemset
     * @param itemset2 the second itemset
     * @return true if yes, otherwise false
     */
    public static boolean includedIn(int[] itemset1, int itemset1Length, int[] itemset2) {
        int count = 0; // the current position of itemset1 that we want to find in itemset2

        // for each item in itemset2
        for (int i = 0; i < itemset2.length; i++) {
            // if we found the item
            if (itemset2[i] == itemset1[count]) {
                // we will look for the next item of itemset1
                count++;
                // if we have found all items already, return true
                if (count == itemset1Length) {
                    return true;
                }
            }
        }
        // it is not included, so return false!
        return false;
    }

    /**
     * This method checks if the item "item" is in the itemset.
     * It asumes that items in the itemset are sorted in lexical order
     * This version also checks that if the item "item" was added it would be the largest one
     * according to the lexical order.
     *
     * @param itemset an itemset
     * @param item    the item
     * @return return true if the above conditions are met, otherwise false
     */
    public static boolean containsLEXPlus(int[] itemset, int item) {
        // for each item in itemset
        for (int i = 0; i < itemset.length; i++) {
            // check if the current item is equal to the one that is searched
            if (itemset[i] == item) {
                // if yes return true
                return true;
                // if the current item is larger than the item that is searched,
                // then return true because if if the item "item" was added it would be the largest one
                // according to the lexical order.
            } else if (itemset[i] > item) {
                return true; // <-- XXXX
            }
        }
        // if the searched item was not found, return false.
        return false;
    }

    /**
     * This method checks if the item "item" is in the itemset.
     * It assumes that items in the itemset are sorted in lexical order
     *
     * @param itemset an itemset
     * @param item    the item
     * @return return true if the item
     */
    public static boolean containsLEX(int[] itemset, int item) {
        // for each item in itemset
        for (int i = 0; i < itemset.length; i++) {
            // check if the current item is equal to the one that is searched
            if (itemset[i] == item) {
                // if yes return true
                return true;
                // if the current item is larger than the item that is searched,
                // then return false because of the lexical order.
            } else if (itemset[i] > item) {
                return false;  // <-- xxxx
            }
        }
        // if the searched item was not found, return false.
        return false;
    }

    /**
     * Check if an a sorted list of integers contains an integer.
     *
     * @param itemset the sorted list of integers
     * @param item    the integer
     * @return true if the item appears in the list, false otherwise
     */
    public static boolean contains(int[] itemset, int item) {
        // for each item in the itemset
        for (int i = 0; i < itemset.length; i++) {
            // if the item is found, return true
            if (itemset[i] == item) {
                return true;
                // if the current item is larger than the item that is searched,
                // then return false because of the lexical order.
            } else if (itemset[i] > item) {
                return false;
            }
        }
        // not found, return false
        return false;
    }

    /**
     * Append an integer at the end of an array of integers.
     *
     * @param array   the array
     * @param integer the integer
     * @return a new array
     */
    public static int[] appendIntegerToArray(int[] array, int integer) {
        int[] newgen = new int[array.length + 1];
        System.arraycopy(array, 0, newgen, 0, array.length);
        newgen[array.length] = integer;
        return newgen;
    }

    /**
     * Convert a string array to a double array
     *
     * @param tokens a string array
     * @return a double array
     */
    public static double[] convertStringArrayToDoubleArray(String[] tokens) {
        double[] numbers = new double[tokens.length];

        // for each item, we update its support
        for (int i = 0; i < tokens.length; i++) {
//			System.err.println("Token " + tokens[i]);
            String token = tokens[i];
            numbers[i] = Double.parseDouble(token);
        }
        return numbers;
    }

    /**
     * Check if first itemset is a subset of the second one
     *
     * @param itemset1 first itemset
     * @param itemset2 second itemset
     * @return true if itemset1 is a subset of itemset2, false otherwise
     */
    public static boolean isSubsetOf(List<Short> itemset1, Short[] itemset2) {
        if (itemset1 == null || itemset1.size() == 0) {
            return true;
        }
        for (short val : itemset1) {
            boolean found = false;
            for (short value : itemset2) {
                // Makes use of lexicography order to be faster
                if (value > val) {
                    return false;
                } else if (val == value) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        // itemset1 is a subset of itemset2
        return true;
    }

    /**
     * Concatenates two sets of items
     *
     * @param itemset1 first set to join
     * @param itemset2 second set to join
     * @return the concatenation of both sets of items
     */
    public static Short[] concatenate(Short[] itemset1, Short[] itemset2) {
        Short[] concatenation = new Short[itemset1.length + itemset2.length];
        System.arraycopy(itemset1, 0, concatenation, 0, itemset1.length);
        System.arraycopy(itemset2, 0, concatenation, itemset1.length, itemset2.length);
        return concatenation;
    }


}
