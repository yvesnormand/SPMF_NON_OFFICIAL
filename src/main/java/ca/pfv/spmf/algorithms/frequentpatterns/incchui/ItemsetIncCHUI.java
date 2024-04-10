package ca.pfv.spmf.algorithms.frequentpatterns.incchui;

import java.io.Serializable;

/**
 * An itemset as used by the IncCHUI algorithm.
 *
 * @author Dam et al.
 * @see AlgoIncCHUI
 */
public class ItemsetIncCHUI implements Comparable<ItemsetIncCHUI>, Serializable {

    private final int[] itemset;
    public double utility;
    public int supp;
    //public double simscore; 

    public ItemsetIncCHUI(int[] itemset, double utility, int support) {
        this.itemset = itemset;
        this.utility = utility;
        this.supp = support;
    }

    public int[] getItemset() {
        return itemset;
    }

    public int[] getItems() {
        return itemset;
    }

    public int compareTo(ItemsetIncCHUI o) {
        if (o == this) {
            return 0;
        }
        int compare = (int) (this.utility - o.utility);
        if (compare != 0) {
            return compare;
        }
        return this.hashCode() - o.hashCode();
    }

    /**
     * Get the size of this itemset
     */
    public int size() {
        return itemset.length;
    }

    public Integer get(int position) {
        return itemset[position];
    }

    public String toString() {

        // use a string buffer for more efficiency
        StringBuffer r = new StringBuffer();
        // for each item, append it to the stringbuffer
        for (int i = 0; i < size(); i++) {
            r.append(get(i));
            r.append(' ');
        }
        r.append(" #SUPP: ");
        r.append(this.supp);
        r.append(" #UTIL: ");
        r.append(this.utility);
        return r.toString(); // return the string
    }

//	public float SimScore()
//	{		
//		return (float) simscore;
//	}
}
