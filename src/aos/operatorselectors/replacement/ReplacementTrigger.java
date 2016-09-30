/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors.replacement;

import java.util.HashMap;
import org.moeaframework.core.Variation;

/**
 *
 * @author nozomihitomi
 */
public interface ReplacementTrigger {

    /**
     * Checks the replacement trigger to see if operators should be replaced
     * based on the quality of the operators and the number of evaluations so
     * far
     *
     * @param nevals number of evaluations so far
     * @param qualities the qualities of the operators
     * @return
     */
    public boolean checkTrigger(int nevals,HashMap<Variation, Double> qualities);
}
