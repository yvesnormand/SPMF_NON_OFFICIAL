package ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_p.items.abstractions;

import ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_p.items.Item;

/**
 * Class that represents a pair <item,abstraction>.
 * <p>
 * Copyright Antonio Gomariz Peñalver 2013
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SPMF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SPMF.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author agomariz
 */
public class ItemAbstractionPair implements Comparable<ItemAbstractionPair> {
    /**
     * Item.
     */
    Item item;
    /**
     * Abstraction associated with the item.
     */
    Abstraction_Generic abstraction;

    public ItemAbstractionPair(Item item, Abstraction_Generic abstraction) {
        this.item = item;
        this.abstraction = abstraction;
    }

    @Override
    public boolean equals(Object arg) {
        ItemAbstractionPair iap = (ItemAbstractionPair) arg;
        return (this.getItem().equals(iap.getItem()) && this.getAbstraction().equals(iap.getAbstraction()));
    }

    @Override
    public int hashCode() {
        int hash = 5 + item.hashCode();
        hash = 9 * hash + abstraction.hashCode();
        return hash;
    }

    public Abstraction_Generic getAbstraction() {
        return abstraction;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        if (abstraction instanceof Abstraction_Qualitative) {
            return (getAbstraction().toString() + " " + getItem().toString());
        }
        return (getItem().toString() + getAbstraction().toString() + " ");
    }

    /**
     * Get the string representation. Adjusted to SPMF format.
     *
     * @return the string representation
     */
    public String toStringToFile() {
        if (abstraction instanceof Abstraction_Qualitative) {
            return (getAbstraction().toStringToFile() + " " + getItem().toString());
        }
        return (getItem().toString() + getAbstraction().toString() + " ");
    }

    @Override
    public int compareTo(ItemAbstractionPair arg) {
        int comparacionItems = -getItem().compareTo(arg.getItem());
        if (comparacionItems == 0) {
            return getAbstraction().compareTo(arg.getAbstraction());
        } else {
            return comparacionItems;
        }
    }
}
