/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors.replacement;

import aos.nextoperator.INextOperator;
import java.util.Collection;
import org.moeaframework.core.Variation;

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
     * Constructor to build a new operator replacement strategy
     *
     * @param operatorRemover The strategy to select operators for removal
     * @param trigger The strategy that will indicate when to replace operators.
     */
    public OperatorReplacementStrategy(OperatorRemover operatorRemover, ReplacementTrigger trigger) {
        this.operatorRemover = operatorRemover;
        this.trigger = trigger;
    }

    public boolean checkTrigger(INextOperator selector) {
        return this.trigger.checkTrigger(selector.getNumberOfIterations(), selector.getQualities());
    }

    public void removeOperators(INextOperator selector) {
        Collection<Variation> operatorsToRemove = operatorRemover.selectOperators(selector.getNumberOfIterations(), selector.getQualities());
        for (Variation operator : operatorsToRemove) {
            selector.removeOperator(operator);
        }
    }

}
