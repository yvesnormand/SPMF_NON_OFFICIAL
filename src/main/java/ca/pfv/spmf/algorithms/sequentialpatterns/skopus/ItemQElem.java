package ca.pfv.spmf.algorithms.sequentialpatterns.skopus;

//ééééé͵éitemééeventéĻéééԭʼ
public class ItemQElem {
    public int item;
    public double ubvalue = 0;

    public boolean equals(final ItemQElem e) {
        return this.item == e.item;
    }

    public String toString() {
        String strResult = "";
        strResult += "<";
        strResult += GlobalData.alItemName.get(item);
        strResult += ">";

        return strResult;
    }
}

