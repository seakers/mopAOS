/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.operatorselectors.replacement;

import java.util.Collection;
import org.moeaframework.core.Variation;
import seakers.aos.aos.AOS;

/**
 * The operator replacement strategy relies on an operator remover that selects
 * operators to remove and a replacement trigger that defines when to remove
 * operators during the search
 *
 * @author nozomihitomi
 */
public class OperatorReplacementStrategy {

    /**
     * The strategy to select operators for removal
     */
    private final OperatorRemover operatorRemover;

    /**
     * The strategy that will indicate when to replace operators.
     */
    private final ReplacementTrigger trigger;
    
    /**
     * Operator creator will create new operators during the optimization
     */
    private final OperatorCreator operatorCreator;

    /**
     * Constructor to build a new operator replacement strategy
     *
     * @param trigger The strategy that will indicate when to replace operators.
     * @param operatorRemover The strategy to select operators for removal
     * @param operatorCreator Operator creator will create new operators during the optimization
     */
    public OperatorReplacementStrategy(ReplacementTrigger trigger, OperatorRemover operatorRemover, OperatorCreator operatorCreator) {
        this.operatorRemover = operatorRemover;
        this.trigger = trigger;
        this.operatorCreator = operatorCreator;
    }

    /**
     * Checks to see if the operators should be removed at this point in time.
     * @param aos the adaptive operator selector
     * @return true if the replacement criteria is triggered. Else false
     */
    public boolean checkTrigger(AOS aos) {
        return this.trigger.checkTrigger(aos);
    }

    /**
     * Removes the operators from the selector's set of operators
     * @param aos the adaptive operator selector
     * @return The operators that were removed
     */
    public Collection<Variation> removeOperators(AOS aos) {
        Collection<Variation> operatorsToRemove = operatorRemover.selectOperators(aos);
        for (Variation operator : operatorsToRemove) {
            aos.getOperatorSelector().removeOperator(operator);
        }
        return operatorsToRemove;
    }

    /**
     * Adds a desired number of new operators to the selector's set of operators
     * @param aos the adaptive operator selector
     * @param nOperators the desired number of operators to add
     * @return  The operators that were added
     */
    public Collection<Variation> addNewOperator(AOS aos, int nOperators){
        Collection<Variation> newOperators = operatorCreator.createOperator(nOperators);
        for(Variation newOp : newOperators){
            aos.getOperatorSelector().addOperator(newOp);
        }
        return newOperators;
    }

    /**
     * Gets the method to remove operators
     * @return the method to remove operators
     */
    public OperatorRemover getOperatorRemover() {
        return operatorRemover;
    }

    /**
     * Gets trigger used to indicate the replacement of operators
     * @return trigger used to indicate the replacement of operators
     */
    public ReplacementTrigger getTrigger() {
        return trigger;
    }

    /**
     * Gets the method to create operators
     * @return the method to create operators
     */
    public OperatorCreator getOperatorCreator() {
        return operatorCreator;
    }
    
    
}
