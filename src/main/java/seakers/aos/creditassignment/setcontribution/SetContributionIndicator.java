/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.creditassignment.setcontribution;

import seakers.aos.creditassignment.AbstractSetContribution;
import seakers.aos.creditassignment.fitnessindicator.IIndicator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * This credit assignment rewards an operator proportional to the sum of the
 * fitness values of the solutions it has created in the set. The credit values
 * are normalized such that the lowest credit received by an operator is 0 while
 * the highest is 1.0
 *
 * @author SEAK2
 */
public class SetContributionIndicator extends AbstractSetContribution {
    private final IIndicator indicator;
    /**
     *
     * @param solutionSet The solution set to compute contributions
     * @param indicator the indicator to compute the indicator metric
     */
    public SetContributionIndicator(Population solutionSet, IIndicator indicator) {
        super(solutionSet, 0);
        this.indicator = indicator;
    }

    @Override
    public Map<String, Double> computeCredit(Set<String> operators){
        indicator.computeContributions(solutionSet);

        HashMap<String, Double> credits = new HashMap<>();
        for (String name : operators) {
            credits.put(name, 0.0);
        }

        double minCredit = Double.POSITIVE_INFINITY;
        double maxCredit = Double.NEGATIVE_INFINITY;
        
        for (Solution soln : solutionSet) {
            double contribution = (double) soln.getAttribute("contribution");
            if (soln.hasAttribute("operator")) {
                String operator = String.valueOf(soln.getAttribute("operator"));
                credits.put(operator, credits.get(operator) + contribution);
            }
        }
        
        for (String name : operators) {
            minCredit = Math.min(minCredit, credits.get(name));
            maxCredit = Math.max(maxCredit, credits.get(name));
        }

        //normalize the reward values
        for (String name : operators) {
            double normalizedCredit = Math.max(0.0,(credits.get(name) - minCredit) / (maxCredit - minCredit));
            credits.put(name, normalizedCredit);
        }
        return credits;
    }


}
