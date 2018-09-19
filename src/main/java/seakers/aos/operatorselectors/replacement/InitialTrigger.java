/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.operatorselectors.replacement;

import seakers.aos.aos.AOS;

/**
 * This trigger checks to see if a certain number of function evaluations has
 * been exceeded. This trigger is used only once. After the specified number of
 * function evaluations has been exceeded, this trigger does not contribute to
 * operator replacement
 *
 * @author nozomihitomi
 */
public class InitialTrigger implements ReplacementTrigger {

    /**
     * The number of function evaluations that must be exceeded to set off this
     * trigger
     */
    private final int nfe;
    
    /**
     * Flag to keep track of if this trigger has been set off or not
     */
    private boolean used;

    public InitialTrigger(int nfe) {
        this.nfe = nfe;
        this.used = false;
    }

    @Override
    public boolean checkTrigger(AOS aos) {
        int nevals = aos.getNumberOfEvaluations();
        if (!used && nevals >= nfe) {
            this.used = true;
            return true;
        } else {
            return false;
        }
    }

}
