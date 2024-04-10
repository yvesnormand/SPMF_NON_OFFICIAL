/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_l.items.creators;


import ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_l.items.CandidateInSequenceFinder;
import ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_l.items.Item;
import ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_l.items.Sequence;
import ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_l.items.abstractions.Abstraction_Generic;
import ca.pfv.spmf.algorithms.sequentialpatterns.spm_fc_l.items.patterns.Pattern;

import java.util.List;
import java.util.Map;

/**
 * Abstract class that is thought to make it possible the creation of any kind
 * of abstractions.
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
public interface AbstractionCreator {

    Abstraction_Generic CreateDefaultAbstraction();

    List<Pattern> createSize2Sequences(List<Sequence> sequences);

    Pattern getSubpattern(Pattern extension, int i);

    List<Pattern> createSize2Sequences(Map<Integer, Map<Item, List<Integer>>> bbddHorizontal, Map<Item, Pattern> itemsfrecuentes);

    void clear();

    Abstraction_Generic createAbstraction(long timeActual, long timeAnterior);

    int[] findPositionOfItemInSequence(Sequence secuencia, Item itemPar, Abstraction_Generic absPar, Abstraction_Generic absAnterior,
                                       int indexItemset, int indexitem, int indexItemsetAnterior, int indexitemAnterior);

    Pattern generateCandidates(AbstractionCreator creador, Pattern patron1, Pattern patron2, double minSupport);

    void isCandidateInSequence(CandidateInSequenceFinder buscador, Pattern candidato, Sequence secuencia, int k, int i, List<int[]> posicion);

    List<Pattern> generateSize2Candidates(AbstractionCreator creador, Pattern get, Pattern get0);
}
