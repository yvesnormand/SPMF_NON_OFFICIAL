package ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators;

import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.Item;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.abstractions.Abstraction_Generic;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.abstractions.ItemAbstractionPair;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that implements a creator for pairs <item,abstraction> that are used in a pattern implementation.
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
public class ItemAbstractionPairCreator {

    private static final Map<Item, Map<Abstraction_Generic, ItemAbstractionPair>> pailPoors = new HashMap<Item, Map<Abstraction_Generic,
            ItemAbstractionPair>>();
    /**
     * Static reference to make the class singleton
     */
    private static ItemAbstractionPairCreator instance = null;

    private ItemAbstractionPairCreator() {
    }

    /**
     * Get the only instance of the  creator object (it is a singleton)
     *
     * @return the instance
     */
    public static ItemAbstractionPairCreator getInstance() {
        if (instance == null) {
            instance = new ItemAbstractionPairCreator();
        }
        return instance;
    }

    public static void sclear() {
        pailPoors.clear();
        instance = null;
    }

    public ItemAbstractionPair getItemAbstractionPair(Item item, Abstraction_Generic abstraction) {
        Map<Abstraction_Generic, ItemAbstractionPair> itemPair = pailPoors.get(item);
        ItemAbstractionPair pair;
        if (itemPair != null) {
            pair = itemPair.get(abstraction);
            if (pair == null) {
                pair = new ItemAbstractionPair(item, abstraction);
                itemPair.put(abstraction, pair);
            }
        } else {
            itemPair = new HashMap<Abstraction_Generic, ItemAbstractionPair>();
            pair = new ItemAbstractionPair(item, abstraction);
            itemPair.put(abstraction, pair);
            pailPoors.put(item, itemPair);
        }
        return pair;
    }

    public void clear() {
        pailPoors.clear();
    }
}
