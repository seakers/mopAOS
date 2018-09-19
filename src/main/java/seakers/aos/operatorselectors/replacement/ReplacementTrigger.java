/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.operatorselectors.replacement;

import seakers.aos.aos.AOS;

/**
 * The criteria that triggers the replacement of operators in the AOS
 * @author nozomihitomi
 */
public interface ReplacementTrigger {

    /**
     * Checks the replacement trigger to see if operators should be replaced
     * based on the quality of the operators and the number of evaluations so
     * far
     *
     * @param aos the adaptive operator selector
     * @return true if the replacement criteria is triggered. Else false
     */
    public boolean checkTrigger(AOS aos);
}
