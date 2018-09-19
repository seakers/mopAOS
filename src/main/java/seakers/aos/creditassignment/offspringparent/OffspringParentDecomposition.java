/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.creditassignment.offspringparent;

import seakers.aos.creditassignment.AbstractOffspringParent;
import org.moeaframework.core.Solution;

/**
 * This credit assignment is decomposition based. Measures the subproblem
 * objective improvement of an offspring over its parent
 *
 * @author nozomihitomi
 */
public class OffspringParentDecomposition extends AbstractOffspringParent {

    /**
     * Weight vector of the current problem
     */
    private double[] weights;

    /**
     * The reference point used to compute the Chebyshev function
     */
    private double[] idealPoint;

    public OffspringParentDecomposition() {
    }

    /**
     * Sets the ideal point used in decomposition methods. Update the ideal
     * point each time compute is called.
     *
     * @param idealPoint the ideal point
     */
    public void setIdealPoint(double[] idealPoint) {
        this.idealPoint = idealPoint;
    }

    /**
     * Sets the weights for the current problem. Update the weights each time
     * compute is called
     *
     * @param weights the weights
     */
    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    /**
     * Evaluates the fitness of the specified solution using the Chebyshev
     * weights.
     *
     * @param solution the solution
     * @param weights the weights
     * @return the fitness of the specified solution using the Chebyshev weights
     */
    private double fitness(Solution solution, double[] weights) {
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            max = Math.max(max, weights[i] * Math.abs(solution.getObjective(i) - idealPoint[i]));
        }

        if (solution.violatesConstraints()) {
            max += Double.POSITIVE_INFINITY;
        }
        return max;
    }

    @Override
    public double computeCredit(Solution offspring, Solution parent) {
        double parentFitness = fitness(parent, weights);
        return (parentFitness - fitness(offspring, weights)) / parentFitness;
    }

}
