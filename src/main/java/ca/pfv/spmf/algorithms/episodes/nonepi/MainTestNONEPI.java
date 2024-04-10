/*
 * This file is part of the SPMF DATA MINING SOFTWARE *
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the *
 * terms of the GNU General Public License as published by the Free Software *
 * Foundation, either version 3 of the License, or (at your option) any later *
 * version. SPMF is distributed in the hope that it will be useful, but WITHOUT
 * ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Oualid Ouarem et al.
 */
package ca.pfv.spmf.algorithms.episodes.nonepi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Example of how to run the NONEPI algorithm from the source code.
 *
 * @author oualid
 * @see AlgoNONEPI
 */
public class MainTestNONEPI {

    public static void main(String[] args) throws IOException {
        // Input file path
        String inputPath = fileToPath("contextNONEPI.txt");

        // Output file path
        String outputPath = "output.txt";

        // Minimum support (an integer representing a number of occurrences)
        int minsup = 2;

        // Minimum confidence (represnts a percentage)
        float minconf = 0.2f;

        // (1) First extracts frequent episodes with NONEPI
        AlgoNONEPI algo = new AlgoNONEPI();
        List<Episode> frequentEpisodes = algo.findFrequentEpisodes(inputPath, minsup);

        // (2) Second, find episodes rules from frequent episodes        
        List<String> rules = algo.findNONEpiRulesWithPruning(frequentEpisodes, minconf);
        algo.printStats();
        algo.saveRulesToFile(outputPath);
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestNONEPI.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}
