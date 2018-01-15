/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operator;

import java.util.Collection;
import org.moeaframework.core.Variation;

/**
 * Interface for the AOS strategies contained in a Variation
 *
 * @author nozomihitomi
 */
public interface AOSVariation extends Variation {

    /**
     * Resets the operator selection strategy
     */
    public void reset();

    /**
     * Resets the operator selection strategy and replaces the current operators
     * with the given operators.
     *
     * @param operators the new operators.
     */
    public void reset(Collection<Variation> operators);
    
    /**
     * Computes the credits and updates the selection probabilities
     */
    public void update();
}
