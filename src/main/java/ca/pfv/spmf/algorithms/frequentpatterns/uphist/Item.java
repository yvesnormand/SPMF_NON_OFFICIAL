package ca.pfv.spmf.algorithms.frequentpatterns.uphist;
/* This file is copyright (c) 2018+  by Siddharth Dawar et al.
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This is an implementation of an Item as used by the UPGrowth algorithm.
 * <p>
 * Copyright (c) 2014 Prashant Barhate
 * <p>
 * This file is part of the SPMF DATA MINING SOFTWARE *
 * (http://www.philippe-fournier-viger.com/spmf).
 * <p>
 * SPMF is free software: you can redistribute it and/or modify it under the *
 * terms of the GNU General Public License as published by the Free Software *
 * Foundation, either version 3 of the License, or (at your option) any later *
 * version. SPMF is distributed in the hope that it will be useful, but WITHOUT
 * ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Prashant Barhate
 * @see AlgoUPHist
 */

public class Item {

    int name = 0; // item
    int utility = 0; // utility of item
    //int quantity=0;

    // constructor that takes item name
    public Item(int name) {
        this.name = name;
    }


    public Item(int name, int utility) {
        this.name = name;
        this.utility = utility;

    }

    /**
     * method to get node utility
     */
    public int getUtility() {
        return utility;
    }

    /**
     * method to set node utility
     */
    public void setUtility(int utility) {
        this.utility = utility;
    }

    /**
     * method to get perticular item
     */
    public int getName() {
        return name;
    }

}