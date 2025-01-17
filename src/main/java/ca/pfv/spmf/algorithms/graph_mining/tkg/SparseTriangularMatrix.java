package ca.pfv.spmf.algorithms.graph_mining.tkg;

import ca.pfv.spmf.algorithms.frequentpatterns.charm.AlgoCharm_Bitset;
import ca.pfv.spmf.algorithms.frequentpatterns.eclat.AlgoEclat;
import ca.pfv.spmf.algorithms.frequentpatterns.eclat.AlgoEclat_Bitset;
import ca.pfv.spmf.datastructures.triangularmatrix.AbstractTriangularMatrix;
import ca.pfv.spmf.datastructures.triangularmatrix.TriangularMatrix;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * This class is for creating a triangular matrix of integers by using HashMaps (a sparse matrix rather than a full matrix).
 * See the TriangularMatrixArray class for more details about what is a triangular matrix.
 *
 * @author Philippe Fournier-Viger
 * @see AbstractTriangularMatrix
 * @see TriangularMatrix
 * @see AlgoCharm_Bitset
 * @see AlgoEclat_Bitset
 * @see AlgoEclat
 */
public class SparseTriangularMatrix {

    // the triangular matrix is a hashmap of hashmaps
    // where the key is an item I, then the value is a map where each entry is a key representing an item J
    // and a value representing the count of {I, J}.
    private final Map<Integer, Map<Integer, Integer>> matrix = new HashMap<Integer, Map<Integer, Integer>>();

    /**
     * Constructor of a new triangular matrix.
     *
     * @param elementCount the desired number of lines in the matrix.
     */
    public SparseTriangularMatrix() {

    }

    /**
     * This constructor is for compatibility with the other TriangularMatrix implementation.
     * The parameter "itemCount" is not used.
     *
     * @param itemCount
     */
    public SparseTriangularMatrix(int itemCount) {

    }

    /* (non-Javadoc)
     * @see ca.pfv.spmf.datastructures.triangularmatrix.AbstractTriangularMatrix#toString()
     */
    public String toString() {
        // create a string buffer
        StringBuilder temp = new StringBuilder();
        // for each row
        for (int i = 0; i < matrix.keySet().size(); i++) {
            temp.append(i);
            temp.append(": ");
            // for each column
            for (int j = 0; j < matrix.get(i).size(); j++) {
                temp.append(matrix.get(i).get(j)); // add the value at position i,j
                temp.append(" ");
            }
            temp.append("\n");
        }
        return temp.toString();
    }

    /* (non-Javadoc)
     * @see ca.pfv.spmf.datastructures.triangularmatrix.AbstractTriangularMatrix#incrementCount(int, int)
     */
    public void incrementCount(int i, int j) {
        if (i < j) {
            // First get the map of i
            Map<Integer, Integer> mapCount = matrix.get(i);
            if (mapCount == null) {
                mapCount = new HashMap<Integer, Integer>();
                matrix.put(i, mapCount);
                mapCount.put(j, 1);
            } else {
                // Second, get the count of i,j
                Integer count = mapCount.get(j);
                if (count == null) {
                    mapCount.put(j, 1);
                } else {
                    mapCount.put(j, ++count);
                }
            }
        } else {
            // First get the map of j
            Map<Integer, Integer> mapCount = matrix.get(j);
            if (mapCount == null) {
                mapCount = new HashMap<Integer, Integer>();
                matrix.put(j, mapCount);
                mapCount.put(i, 1);
            } else {
                // Second, get the count of i,j
                Integer count = mapCount.get(i);
                if (count == null) {
                    mapCount.put(i, 1);
                } else {
                    mapCount.put(i, ++count);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see ca.pfv.spmf.datastructures.triangularmatrix.AbstractTriangularMatrix#getSupportForItems(int, int)
     */
    public int getSupportForItems(int i, int j) {
        if (i < j) {
            // First get the map of i
            Map<Integer, Integer> mapCount = matrix.get(i);
            if (mapCount == null) {
                return 0;
            } else {
                // Second, get the count of i,j
                Integer count = mapCount.get(j);
                if (count == null) {
                    return 0;
                } else {
                    return count;
                }
            }
        } else {
            // First get the map of i
            Map<Integer, Integer> mapCount = matrix.get(j);
            if (mapCount == null) {
                return 0;
            } else {
                // Second, get the count of i,j
                Integer count = mapCount.get(i);
                if (count == null) {
                    return 0;
                } else {
                    return count;
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see ca.pfv.spmf.datastructures.triangularmatrix.AbstractTriangularMatrix#setSupport(java.lang.Integer, java.lang.Integer, int)
     */
    public void setSupport(Integer i, Integer j, int support) {
        if (i < j) {
            // First get the map of i
            Map<Integer, Integer> mapCount = matrix.get(i);
            if (mapCount == null) {
                mapCount = new HashMap<Integer, Integer>();
                matrix.put(i, mapCount);
                mapCount.put(j, support);
            } else {
                // Second, set the count of i,j
                mapCount.put(j, support);
            }
        } else {
            // First get the map of j
            Map<Integer, Integer> mapCount = matrix.get(j);
            if (mapCount == null) {
                mapCount = new HashMap<Integer, Integer>();
                matrix.put(j, mapCount);
                mapCount.put(i, support);
            } else {
                // Second, set the count of i,j
                mapCount.put(i, support);
            }
        }
    }

    public void removeInfrequentEntriesFromMatrix(int minsup) {
        for (Entry<Integer, Map<Integer, Integer>> entry : matrix.entrySet()) {

            Iterator<Entry<Integer, Integer>> iter = entry.getValue().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<java.lang.Integer, java.lang.Integer> entry2 = iter
                        .next();
                if (entry2.getValue() < minsup) {
                    iter.remove();
                }

            }
        }
    }
}
