package ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures;

import java.util.HashMap;

/**
 * Implementation of a pool of items.
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
public class ItemFactory<T extends Comparable> {

    private final HashMap<T, Item> pool = new HashMap<T, Item>();

    public ItemFactory() {
    }

    /**
     * Method which get an item from the pool. If such item does not exist,
     * a new item is created
     *
     * @param key the key for this item
     * @return the item
     */
    public Item getItem(T key) {
        Item item = pool.get(key);
        if (item == null) {
            item = new Item(key);
            pool.put(key, item);
        }
        return item;
    }
}
