/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors.replacement;

import java.util.Collection;
import org.moeaframework.core.Variation;
import aos.aos.AOS;

/**
 * This class will remove n operators that have the lowest quality as seen by
 * the AOS operator selector
 *
 * @author nozomihitomi
 */
public class RemoveNLowest extends AbstractOperatorRemover {

    /**
     * The number of operators to remove when selectOperators is called
     */
    private final int numToRemove;

    /**
     * number of operators selected for removal so far.
     */
    private int nSelected;

    /**
     * Constructor for the operator remover
     *
     * @param permanentOperators the operators that are prohibited from being
     * selected for removal
     * @param numToRemove the number of operators to select for removal at each
     * epoch
     */
    public RemoveNLowest(Collection<Variation> permanentOperators, int numToRemove) {
        super(permanentOperators);
        this.numToRemove = numToRemove;
    }

    @Override
    protected boolean finished(int numOperators) {
        if (numToRemove > numOperators) {
            return nSelected >= numOperators;
        } else {
            return nSelected >= numToRemove;
        }
    }

    @Override
    protected double computeMetric(Variation operator, AOS aos) {
        return aos.getOperatorSelector().getQualities().get(operator);
    }
}
