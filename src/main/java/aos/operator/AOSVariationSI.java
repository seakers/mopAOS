/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operator;

import aos.creditassigment.AbstractSetImprovement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.moeaframework.core.Solution;
import aos.operatorselectors.OperatorSelector;

/**
 * AOS strategy for set-improvement type credit assignment strategies.
 *
 * @author nozomihitomi
 */
public class AOSVariationSI extends AbstractAOSVariation {
    
    private final Collection<Solution> solutionHistory; 
   
    public AOSVariationSI(OperatorSelector operatorSelector, AbstractSetImprovement creditAssignment, int initialNFE) {
        super(operatorSelector, creditAssignment, initialNFE);
        this.solutionHistory = new ArrayList<>();
    }

    @Override
    public Solution[] evolve(Solution[] parents) {
        Solution[] offspring = super.evolve(parents);
        this.solutionHistory.addAll(Arrays.asList(offspring));
        return offspring;
    }

    @Override
    protected Map<String, Double> computeCredits() {
        Map<String, Double> credits = new HashMap<>();
        AbstractSetImprovement creditAssignment = (AbstractSetImprovement)getCreditAssignment();
        for(Solution solution : solutionHistory){            
            String operatorName = String.valueOf(solution.getAttribute("operator"));
            if(!credits.containsKey(operatorName)){
                credits.put(operatorName, 0.0);
            }
            
            //updated total credit for each operator
            credits.put(operatorName,
                    credits.get(operatorName) + creditAssignment.compute(solution));
        }
        return credits;
    }

    @Override
    protected void internalReset() {
       //clear the history for the next iteration
        solutionHistory.clear();
    }

}
