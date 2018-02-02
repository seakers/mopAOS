/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operator;

import aos.creditassigment.CreditAssignment;
import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.operatorselectors.OperatorSelector;
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
    
    /**
     * Returns the selection history stored in the AOS
     * @return the selection history stored in the AOS
     */
    public OperatorSelectionHistory getSelectionHistory();
    
    /**
     * Gets the quality history stored for each operator in the AOS
     * @return the quality history stored for each operator in the AOS
     */
    public OperatorQualityHistory getQualityHistory();
    
    /**
     * Gets the credit history stored for each operator in the AOS
     *
     * @return the credit history stored for each operator in the AOS
     */
    public CreditHistory getCreditHistory();
    
    /**
     * Gets the credit definition being used.
     * @return the credit definition being used.
     */
    public CreditAssignment getCreditAssignment();
    
    /**
     * Gets the strategy that is used to generate or select the next operator 
     * @return the strategy that is used to generate or select the next operator 
     */
    public OperatorSelector getOperatorSelector();

}
