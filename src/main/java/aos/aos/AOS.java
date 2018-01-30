/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aos.aos;

import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.creditassigment.ICreditAssignment;
import org.moeaframework.core.Algorithm;
import aos.operatorselectors.OperatorSelector;

/**
 * AOS is the framework using a credit assignment and operator selection strategy 
 * @author nozomihitomi
 */
public interface AOS extends Algorithm{
    
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
    public ICreditAssignment getCreditAssignment();
    
    /**
     * Gets the strategy that is used to generate or select the next operator 
     * @return the strategy that is used to generate or select the next operator 
     */
    public OperatorSelector getOperatorSelector();
    
    
    /**
     * Sets the adaptive operator selector's name
     * @param name the name of the aos strategy
     */
    public void setName(String name);
    
    /**
     * Gets the adaptive operator selector's name
     * @return the adaptive operator selector's name
     */
    public String getName();
}
