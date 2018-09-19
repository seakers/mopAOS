/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.creditassignment.setimprovement;

import seakers.aos.creditassignment.AbstractSetImprovement;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * This credit definition gives credit if the specified solution improves a set
 * of solutions using a dominance-based fitness.
 *
 * @author Nozomi
 */
public class SetImprovementDominance extends AbstractSetImprovement {

    /**
     * Credit received if a new solution is nondominated with respect to the set
     */
    protected final double inSet;

    /**
     * Credit received if a new solution is dominated with respect to the set
     */
    protected final double notInSet;

    /**
     * Constructor to specify the credits that are assigned when a solution is
     * nondominated or dominated with respect to the given population
     *
     * @param solutionSet The solution set to compute improvements
     * @param inSet credit to assign when solution is nondominated with respect
     * to the given population
     * @param notInSet credit to assign when solution is dominated with respect
     * to the given population
     */
    public SetImprovementDominance(Population solutionSet, double inSet, double notInSet) {
        super(solutionSet);
        this.notInSet = notInSet;
        this.inSet = inSet;
    }

    @Override
    public double computeCredit(Solution offspring) {
        //assumes that the newly created offspring has already been inserted
        //(or tried to be inserted into the archive). New solutions that enter 
        //archive are put at the end of the population so check there for any changes
        for (int i = solutionSet.size() - 1; i >= 0; i--) {
            if (solutionSet.get(i).equals(offspring)) {
                return inSet;
            }
        }
        return notInSet;
    }
}
