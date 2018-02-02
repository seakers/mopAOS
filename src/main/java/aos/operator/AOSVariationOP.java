/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operator;

import aos.creditassigment.AbstractOffspringParent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.moeaframework.core.Solution;
import aos.operatorselectors.OperatorSelector;

/**
 * AOS strategy for offspring-parent type credit assignment strategies.
 *
 * @author nozomihitomi
 */
public class AOSVariationOP extends AbstractAOSVariation {
    
    private final Collection<OffspringParentGroup> solutionHistory;
    
    /**
     * Creates a new AOS strategy for a offspring-parent type credit assignment strategy
     * @param operatorSelector the operator selection strategy
     * @param creditAssignment the credit assignment strategy
     * @param initialNFE The number of function evaluation used for the initial population
     */
    public AOSVariationOP(OperatorSelector operatorSelector, AbstractOffspringParent creditAssignment, int initialNFE) {
        super(operatorSelector, creditAssignment, initialNFE);
        this.solutionHistory = new ArrayList<>();
    }

    @Override
    public Solution[] evolve(Solution[] parents) {
        Solution[] offspring = super.evolve(parents);
        this.solutionHistory.add(new OffspringParentGroup(offspring, parents));
        return offspring;
    }

    @Override
    protected Map<String, Double> computeCredits() {
        Map<String, Double> credits = new HashMap<>();
        AbstractOffspringParent creditAssignment = (AbstractOffspringParent)getCreditAssignment();
        for(OffspringParentGroup opg : solutionHistory){            
            Solution[] offspring = opg.getOffspring();
            String operatorName = String.valueOf(offspring[0].getAttribute("operator"));
            if(!credits.containsKey(operatorName)){
                credits.put(operatorName, 0.0);
            }
            
            //updated total credit for each operator
            credits.put(operatorName, 
                    credits.get(operatorName) + creditAssignment.compute(offspring, opg.getParents()));
        }
        return credits;
    }

    @Override
    protected void internalReset() {
       //clear the history for the next iteration
        solutionHistory.clear();
    }
    
    /**
     * Container to keep offspring and parents together
     */
    private class OffspringParentGroup{
        
        /**
         * Offspring solutions
         */
        private final Solution[] offspring;
        
        /**
         * parent solutions
         */
        private final Solution[] parents;
        

        public OffspringParentGroup(Solution[] offspring, Solution[] parents) {
            this.offspring = offspring;
            this.parents = parents;
        }
        /**
         * Gets the offspring solutions
         * @return the offspring solutions
         */
        public Solution[] getOffspring() {
            return offspring;
        }

        /**
         * Gets the parent solutions
         * @return the parent solutions
         */
        public Solution[] getParents() {
            return parents;
        }
    }

}
