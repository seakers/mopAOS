/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassigment;

import org.moeaframework.core.Solution;

/**
 * A category of credit assignment strategies that compares the offspring to its
 * parent solution to assign credit
 *
 * @author nozomihitomi
 */
public abstract class AbstractOffspringParent implements ICreditAssignment {
    
    /**
     * Computes the credit by comparing the offspring solution to its parent
     * @param offspring offspring solution
     * @param parent the parent of the offspring solution
     * @return the credit to assign
     */
    public abstract double computeCredit(Solution offspring, Solution parent);

    public double compute(Solution[] offspring, Solution[] parent){
        double credit = 0;
        for (Solution o : offspring) {
            for (Solution p : parent) {
                credit += computeCredit(o, p);
            }
        }
        return credit;
    }
    
    
}
