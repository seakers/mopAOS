/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassignment.fitnessindicator;

import java.util.List;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * Used for binary indicators
 *
 * @author nozomihitomi
 */
public interface IIndicator {

    /**
     * Computes the contributions of each of the solutions in the
     * population. Returns a list of contributions in the order of the
     * population's index from get() method
     *
     * @param popA the population of solutions 
     * @return a list of contributions in the order of the population's Iterator
     */
    public List<Double> computeContributions(Population popA);

    /**
     * Computes the indicator value when comparing solution A to solution B.
     * Used in IBEA setting
     *
     * @param solnA solution A
     * @param solnB solution B
     * @return the binary indicator value 
     */
    public double compute(Solution solnA, Solution solnB);

    /**
     * Computes the contributions of specified solution to the
     * population if inserted. It is assumed that the solution has already been
     * inserted into the population. Returns the contribution of the offspring
     * to the indicator value
     *
     * @param pop the population with the solution of interest.
     * @param solution the solution just inserted into the population.
     * @return indicator value representing the solution's contribution
     */
    public double computeContribution(Population pop, Solution solution);

    /**
     * Gets the reference point for the indicator
     * @return the reference point for the indicator
     */
    public Solution getReferencePoint();
    
    /**
     * Sets the reference point for the indicator
     * @param solution the reference point for the indicator
     */
    public void setReferencePoint(Solution solution);
}
