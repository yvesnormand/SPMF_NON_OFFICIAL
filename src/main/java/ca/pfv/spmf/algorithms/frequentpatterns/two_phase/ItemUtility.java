package ca.pfv.spmf.algorithms.frequentpatterns.two_phase;

// this class represent an item and its utility in a transaction
public class ItemUtility {

    public int item;
    public int utility;

    public ItemUtility(int item, int utility) {
        this.item = item;
        this.utility = utility;
    }

    public String toString() {
        return "[" + item + "," + utility + "]";
    }
}