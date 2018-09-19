/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.creditassignment.setcontribution;

import seakers.aos.creditassignment.AbstractSetContribution;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * This Credit definition gives credit to all solutions created by the specified
 * operator, including the solution given, that are in the set of solutions
 *
 * @author Nozomi
 */
public class SetContributionDominance extends AbstractSetContribution {

    /**
     * Credit value for being in the set of solutions
     */
    private final double rewardInSet;

    /**
     * Constructor to specify the rewards to give to the operator responsible
     * for each solution in the Population
     *
     * @param solutionSet The solution set to compute contributions
     * @param rewardInSet reward to assign to each solution in the set of solutions that
     * the operator created
     * @param rewardNotInSet reward to assign if there are no solutions in the
     * set of solutions created by the operator
     */
    public SetContributionDominance(Population solutionSet, double rewardInSet, double rewardNotInSet) {
        super(solutionSet, rewardNotInSet);
        this.rewardInSet = rewardInSet;
    }

    @Override
    public Map<String, Double> computeCredit(Set<String> operators) {
        HashMap<String, Double> credits = new HashMap<>();
        for (Solution o : solutionSet) {
            String name = String.valueOf(o.getAttribute("operator"));
            if (!operators.contains(name)) {
                continue;
            }
            if (!credits.containsKey(name)) {
                credits.put(name, rewardInSet);
            } else {
                credits.put(name, credits.get(name) + rewardInSet);
            }
        }
        return credits;
    }

}
