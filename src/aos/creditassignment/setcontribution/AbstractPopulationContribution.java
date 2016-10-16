/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aos.creditassignment.setcontribution;

import aos.creditassigment.AbstractRewardDefintion;
import aos.creditassigment.CreditFunctionInputType;
import aos.creditassigment.Credit;
import java.util.Collection;
import java.util.HashMap;
import org.moeaframework.core.Population;
import org.moeaframework.core.Variation;

/**
 *
 * @author nozomihitomi
 */
public abstract class AbstractPopulationContribution extends AbstractRewardDefintion{
    
    public AbstractPopulationContribution(){
        super();
        inputType = CreditFunctionInputType.CS;
    }
    
    /**
     * Computes all the credits received for each heuristic and returns the Credits they earn
     * @param population
     * @param heuristics
     * @param iteration
     * @return 
     */
    public abstract HashMap<Variation, Credit> compute(Population population,
            Collection<Variation> heuristics,int iteration);
}
