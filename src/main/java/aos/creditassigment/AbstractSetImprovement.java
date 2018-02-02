/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassigment;

import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * A category of credit assignment strategies that compares the offspring to a
 * set of solutions to assess if the offspring improves the set
 *
 * @author nozomihitomi
 */
public abstract class AbstractSetImprovement implements CreditAssignment {
    
    /**
     * The solution set to compute improvements
     */
    protected final Population solutionSet;

    /**
     * 
     * @param solutionSet The solution set to compute improvements
     */
    public AbstractSetImprovement(Population solutionSet) {
        this.solutionSet = solutionSet;
    }
    
    /**
     * Computes the credit by comparing the offspring solution to a set of
     * solutions
     *
     * @param offspring offspring solution
     * @return the credit to assign
     */
    public abstract double computeCredit(Solution offspring);
    
    public double compute(Solution offspring) {
        return computeCredit(offspring);
    }
    
    public double compute(Solution[] offspring) {
        double credit = 0;
        for (Solution o : offspring) {
            credit += compute(o);
        }
        return credit;
    }

}
