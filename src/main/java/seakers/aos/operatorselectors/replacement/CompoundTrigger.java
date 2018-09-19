/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.operatorselectors.replacement;

import java.util.Collection;
import seakers.aos.aos.AOS;

/**
 * A class to hold multiple triggers . Each trigger within the compound trigger
 * is checked to see if operators should be replaced.
 *
 * @author nozomihitomi
 */
public class CompoundTrigger implements ReplacementTrigger {

    private final Collection<ReplacementTrigger> triggers;

    public CompoundTrigger(Collection<ReplacementTrigger> triggers) {
        this.triggers = triggers;
    }

    @Override
    public boolean checkTrigger(AOS aos) {
        boolean out = false;
        for (ReplacementTrigger rt : triggers) {
            if (rt.checkTrigger(aos)) {
                out = true;
            }
        }
        return out;
    }

}
