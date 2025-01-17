package ca.pfv.spmf.algorithms.episodes.tup.tup_preinsertion;
/* This file is copyright (c) Rathore et al. 2018
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Sequence_preinsertion {
    private Set<Integer> ids;
    private Integer id;
    private List<Episode_preinsertion> EpisodeSet;
    private List<Double> UtilSet;
    private double seqUtility;

    public Sequence_preinsertion() {

    }

    public static double getUtility(int u, String str, HashMap<String, Double> externalUtill) {
        double util = u * AlgoTUP_preinsertion.getExternalUtility(str, externalUtill);
        return util;
    }

    /**
     * @param items         a set of items represented by Strings
     * @param utilityValues a set of utility values represented as Strings
     * @return new sequence
     */

    public String fromString(String[] items, String[] utilityValues) {
        double utility = 0;
        String simultEpiName = "";
        int i = 0;
        for (; i < items.length; i++) {
            if (i != 0) {
                simultEpiName += " ";
            }

            double epiUtility = 0;
            int seqCount = this.getID();

            Episode_preinsertion e = Episode_preinsertion.fromString(items[i]);
            simultEpiName += e.getName().get(0);

            // System.out.print(" event "+e);

            this.addEpisode(e);
            // System.out.println("event and util
            // "+eventAndUtil.length+eventAndUtil[0]);

            epiUtility = Double.valueOf(utilityValues[i]);  ///    CALCULATE THE UTILITY --------------------

            e.addUtility(epiUtility);
            addUtil(epiUtility);
            Episode_preinsertion.Occurrence occ = e.new Occurrence(seqCount, seqCount);
            e.addMinOcc(occ);
            e.addOccAndUtill(occ, epiUtility);
            utility += epiUtility;
        }
        if (utility > AlgoTUP_preinsertion.getUtility()) {
            ArrayList<String> epiNameList = new ArrayList<String>();
            epiNameList.add(simultEpiName);
            Episode_preinsertion newEpi = new Episode_preinsertion(epiNameList);
            newEpi.addUtility(utility);
            if (TUPPGlobalVariables.topKBuffer.size() < TUPPGlobalVariables.k) {
                TUPPGlobalVariables.topKBuffer.add(newEpi);
            } else {
                Episode_preinsertion epiTopK = TUPPGlobalVariables.topKBuffer.peek();
                AlgoTUP_preinsertion.setUtility(epiTopK.getUtility());

                if (utility > epiTopK.getUtility()) {
                    TUPPGlobalVariables.topKBuffer.poll();
                    TUPPGlobalVariables.topKBuffer.add(newEpi);
                    epiTopK = TUPPGlobalVariables.topKBuffer.peek();
                    AlgoTUP_preinsertion.setUtility(epiTopK.getUtility());
                }
            }
        }
        this.setSeqUtility(utility);
        return simultEpiName;
    }

    /**
     * @param str           the sequence string
     * @param eventSplitStr the delimiting regex for item set splitting in a set of items
     * @param utilSplitStr  the delimiting regex for utility splitter
     * @return list of events in calling sequence
     */
    public List<Episode_preinsertion> getEpisodeSet() {
        if (null == this.EpisodeSet) {
            this.EpisodeSet = new ArrayList<Episode_preinsertion>();
        }
        return this.EpisodeSet;
    }

    public void setEpisodeSet(List<Episode_preinsertion> itemSets) {
        this.EpisodeSet = itemSets;
    }

    public void addEpisode(Episode_preinsertion iset) {
        this.getEpisodeSet().add(iset);
    }

    public void removeEpisode(int index) {
        this.getEpisodeSet().remove(index);
    }

    public List<Double> getUtilSet() {
        if (null == UtilSet) {
            UtilSet = new ArrayList<Double>();
        }
        return UtilSet;
    }

    public void setUtilSet(List<Double> utilSet) {
        this.UtilSet = utilSet;
    }

    public void addUtil(double utill) {
        this.getUtilSet().add(utill);
    }

    public double getUtill(int i) {
        double internal = UtilSet.get(i);
        return internal;
    }

    public void removeUtill(int index) {
        this.getUtilSet().remove(index);
    }

    public void addID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public double getSeqUtility() {
        return this.seqUtility;
    }

    public void setSeqUtility(double u) {
        this.seqUtility = u;
    }

    public boolean contains(Episode_preinsertion e) {
        for (Episode_preinsertion iset : this.getEpisodeSet()) {
            if (e.equals(iset)) {
                return true;
            }
        }
        return false;
    }

    public boolean containSimultEpi(String str) {
        String[] tokens = str.split(" ");
        List<Episode_preinsertion> episodeList = this.getEpisodeSet();
        int count = 0;
        // System.out.println("event list in sequence " + eventList);
        if (episodeList.size() < tokens.length) {
            return false;
        }

        for (int j = 0; j < episodeList.size(); j++) {

            Episode_preinsertion e = episodeList.get(j);
            for (int i = 0; i < tokens.length; i++) {
                // System.out.println("event in contain simult epi " + e);
                if (e.getName().get(0).equals(tokens[i])) {
                    count++;
                    break;
                }
            }
        }
        // System.out.println("count is "+count);
        return count == tokens.length;
    }

    public int contains(String str) {
        for (int i = 0; i < this.EpisodeSet.size(); i++) {
            Episode_preinsertion e = this.EpisodeSet.get(i);
            if (e.getName().get(0).equals(str)) {
                return i;
            }
        }
        return -1;
    }

}
