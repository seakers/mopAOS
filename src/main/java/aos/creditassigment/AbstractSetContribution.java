/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassigment;

import java.util.Map;
import java.util.Set;
import org.moeaframework.core.Population;

/**
 * A category of credit assignment strategies that computes an operator's
 * contribution to a set of solutions.
 *
 * @author nozomihitomi
 */
public abstract class AbstractSetContribution implements CreditAssignment {

    /**
     * Credit assigned if an operator does not contributed to the set;
     */
    private final double noContribution;
    
    /**
     * The solution set to compute contributions
     */
    protected final Population solutionSet;

    /**
     * Constructs a abstract set contribution credit assignment
     *
     * @param solutionSet The solution set to compute contributions
     * @param noContribution Credit assigned if an operator does not contributed
     * to the set;
     */
    public AbstractSetContribution(Population solutionSet, double noContribution) {
        this.noContribution = noContribution;
        this.solutionSet = solutionSet;
    }

    /**
     * Computes the credit by seeing how each operator contributes to a set of solutions
     *
     * @param operators the operators involved in the credit assignment
     * @return the credit to assign to each operator for their contribution
     */
    public abstract Map<String, Double> computeCredit(Set<String> operators);

    /**
     * * Computes the credit by seeing how each operator contributes to a set of solutions
     * @param operators the operators involved in the credit assignment
     * @return the credit to assign to each operator for their contribution
     */
    public Map<String, Double> compute(Set<String> operators) {
        Map<String, Double> credits = computeCredit(operators);
        for (String op : operators) {
            if (!credits.containsKey(op)) {
                credits.put(op, noContribution);
            }
        }
        return credits;
    }

}
