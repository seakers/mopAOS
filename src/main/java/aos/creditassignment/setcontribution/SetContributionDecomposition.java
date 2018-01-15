/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aos.creditassignment.setcontribution;

import org.moeaframework.core.Population;

/**
 *
 * @author nozomihitomi
 */
public class SetContributionDecomposition extends SetContributionDominance{
    
    /**
     * Constructor to specify the rewards to give to the heuristic responsible 
     * for each solution in a given neighborhood
     * @param solutionSet The solution set to compute contributions
     * @param rewardInN reward to assign to each solution in the neighborhood that the heuristic created
     * @param rewardNotInN reward to assign if there are no solutions in the neighborhood created by the heuristic 
     */
    public SetContributionDecomposition(Population solutionSet, double rewardInN,double rewardNotInN) {
        super(solutionSet, rewardInN, rewardNotInN);
    }
}
