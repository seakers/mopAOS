/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aos.aos;

import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.nextoperator.INextOperator;
import aos.creditassigment.ICreditAssignment;
import org.moeaframework.core.Algorithm;

/**
 * AOS is the framework using a credit assignment and operator selection strategy 
 * @author nozomihitomi
 */
public interface IAOS extends Algorithm{
    
    /**
     * Returns the selection history stored in the hyper-operator
     * @return 
     */
    public OperatorSelectionHistory getSelectionHistory();
    
    /**
     * Resets the adaptive operator selector so that it can run again for another seed.
     */
    public void reset();
    
    /**
     * gets the quality history stored for each operator in the hyper-operator
     * @return 
     */
    public OperatorQualityHistory getQualityHistory();
    
    /**
     * gets the credit history stored for each operator in the hyper-operator
     *
     * @return
     */
    public CreditHistory getCreditHistory();
    
    /**
     * Gets the credit definition being used.
     * @return 
     */
    public ICreditAssignment getCreditDefinition();
    
    /**
     * Gets the strategy that is used to generate or select the next operator 
     * @return 
     */
    public INextOperator getNextHeuristicSupplier();
    
    
    /**
     * Sets the adaptive operator selector's name
     * @param name
     */
    public void setName(String name);
    
    /**
     * Gets the adaptive operator selector's name
     * @return 
     */
    public String getName();
}
