/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassignment.setimprovement;

import aos.creditassigment.AbstractSetImprovement;
import aos.creditassignment.fitnessindicator.IIndicator;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * This credit definition gives credit if the specified solution improves the
 * mean fitness value of a solution set
 *
 * @author Nozomi
 */
public class SetImprovementIndicator extends AbstractSetImprovement{
    
    /**
     * the indicator to use to compute credits
     */
    private final IIndicator indicator;
    
    /**
     * Constructor for indicator based set improvement credit assignment
     * @param solutionSet The solution set to compute improvements
     * @param indicator the indicator to use to compute credits
     */
    public SetImprovementIndicator(Population solutionSet, IIndicator indicator) {
        super(solutionSet);
        this.indicator = indicator;
    }

    @Override
    public double computeCredit(Solution offspring) {
        return indicator.computeContribution(solutionSet, offspring);
    }

}
