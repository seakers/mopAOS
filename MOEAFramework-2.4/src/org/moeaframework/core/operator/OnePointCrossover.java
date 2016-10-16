/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.core.operator;

import org.moeaframework.core.PRNG;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.Variation;

/**
 * One-point or single-point crossover. A crossover point is selected and all
 * decision variables to the left/right are swapped between the two parents. The
 * two children resulting from this swapping are returned.
 */
public class OnePointCrossover implements Variation {

    /**
     * The probability of applying this operator to solutions.
     */
    private final double probability;

    /**
     * The number of offspring to produce after crossover {1,2}
     */
    private final int numOffspring;

    /**
     * Constructs a one-point crossover operator with the specified probability
     * of applying this operator to solutions. By default the operator creates
     * two offspring
     *
     * @param probability the probability of applying this operator to solutions
     */
    public OnePointCrossover(double probability) {
        this(probability, 2);
    }

    /**
     * Constructs a one-point crossover operator with the specified probability
     * of applying this operator to solutions.
     *
     * @param probability the probability of applying this operator to solutions
     * @param numOffspring The number of offspring to produce after crossover
     * {1,2}
     */
    public OnePointCrossover(double probability, int numOffspring) {
        this.probability = probability;
        this.numOffspring = numOffspring;
        if (!(numOffspring == 1 || numOffspring == 2)) {
            throw new IllegalArgumentException(String.format("Expected number of offspring to be 1 or 2. Found %d", numOffspring));
        }
    }

    /**
     * Returns the probability of applying this operator to solutions.
     *
     * @return the probability of applying this operator to solutions
     */
    public double getProbability() {
        return probability;
    }

    @Override
    public Solution[] evolve(Solution[] parents) {
        Solution result1 = parents[0].copy();
        Solution result2 = parents[1].copy();

        if ((PRNG.nextDouble() <= probability)
                && (result1.getNumberOfVariables() > 1)) {
            int crossoverPoint = PRNG.nextInt(
                    result1.getNumberOfVariables() - 1);

            for (int i = 0; i <= crossoverPoint; i++) {
                Variable temp = result1.getVariable(i);
                result1.setVariable(i, result2.getVariable(i));
                result2.setVariable(i, temp);
            }
        }

        if (numOffspring == 1) {
            return new Solution[]{result1};
        } else {
            return new Solution[]{result1, result2};
        }

    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (Double.doubleToLongBits(this.probability) ^ (Double.doubleToLongBits(this.probability) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OnePointCrossover other = (OnePointCrossover) obj;
        if (Double.doubleToLongBits(this.probability) != Double.doubleToLongBits(other.probability)) {
            return false;
        }
        return true;
    }

}
