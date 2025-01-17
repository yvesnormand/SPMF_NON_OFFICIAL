package ca.pfv.spmf.algorithms.sequentialpatterns.prosecco;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.SequentialPatterns;

/**
 * Interface for progressive outputs produced by ProSecCo
 * <p>
 * Copyright (c) 2008-2019 Philippe Fournier-Viger and Sacha Servan-Schreiber
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
 */


public interface ProseccoCallbacks {

    void blockUpdate(
            SequentialPatterns patterns,
            int numTransactionsProcessed,
            long blockRuntime,
            double blockErrorBound);
}

