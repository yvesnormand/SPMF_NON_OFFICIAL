package ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures;

import java.util.Comparator;

/**
 * Implementation of an item.
 * This implementation is generic in order to be able to manage any kind of item (string, integer, ...)
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
public class Item<T extends Comparable> implements Comparable<Item> {

    /**
     * Content of the item. Is implemented in a generic way in order to manage
     * several types of items.
     */
    private final T id;

    /**
     * General constructor.
     *
     * @param id Content of the item.
     */
    public Item(T id) {
        this.id = id;
    }

    /**
     * It gets the content of the item.
     *
     * @return the content
     */
    public T getId() {
        return id;
    }

    /**
     * Get the string representation of this item
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "" + getId();
    }

    /**
     * Check if this item is equal to another (if their identifier are equal).
     *
     * @param object the other item
     * @return true if yes, otherwise false
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof final Item item) {
            return item.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Item i) {
        //return -id.compareTo(i.getId());
        return id.compareTo(i.getId());
    }
}

class itemComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        int value = o1.compareTo(o2);
        return -value;
    }
}
