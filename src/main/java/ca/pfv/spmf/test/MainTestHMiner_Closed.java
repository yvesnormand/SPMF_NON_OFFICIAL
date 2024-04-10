package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.HMiner_CLosed.AlgoHMiner_Closed;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
 * Example of how to run the HMiner_Closed algorithm from the source code of SPMF
 */
public class MainTestHMiner_Closed {

    public static void main(String[] args) {
        try {
            // input file path
            String input = fileToPath("DB_Utility.txt");
            // the minutility threshold
            long min_utility = 30;

            String output = ".//output.txt";

            AlgoHMiner_Closed algorithm = new AlgoHMiner_Closed();

            boolean applyTransactionMergingOptimization = true;
            boolean applyEUCSOptimization = true;

            algorithm.runAlgorithm(input, output, min_utility,
                    applyTransactionMergingOptimization, applyEUCSOptimization);
            algorithm.printStats();

            //WRITE ALL THE CHUIs found until now to a file
            algorithm.writeCHUIsToFile(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fileToPath(String filename)
            throws UnsupportedEncodingException {
        URL url = MainTestHMiner_Closed.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
