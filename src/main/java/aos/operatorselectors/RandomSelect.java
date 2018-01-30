/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aos.operatorselectors;

import aos.creditassigment.Credit;
import java.util.Collection;
import org.moeaframework.core.Variation;

/**
 * RandomSelect randomly selects an operator with uniform probability from the 
 * given set of operators
 * @author nozomihitomi
 */
public class RandomSelect extends AbstractOperatorSelector{
    
    /**
     * RandomSelect does not really utilize the credit repository so any 
     * repository will do
     * @param operators from which to select from 
     */
    public RandomSelect(Collection<Variation> operators) {
        super(operators);
    }

    @Override
    public Variation nextOperator() {
        incrementIterations();
        return super.getRandomOperator(operators);
    }

    @Override
    public String toString() {
        return "RandomSelect";
    }

    @Override
    public void update(Credit reward, Variation heuristic) {
        //no need to do any updates
    }
    
}
