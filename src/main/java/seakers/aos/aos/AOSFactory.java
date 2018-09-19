/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.aos;

import seakers.aos.operatorselectors.AdaptivePursuit;
import seakers.aos.operatorselectors.FRRMAB;
import seakers.aos.operatorselectors.ProbabilityMatching;
import seakers.aos.operatorselectors.RandomSelect;
import seakers.aos.operatorselectors.AbstractOperatorSelector;
import seakers.aos.operatorselectors.AllOperators;
import java.util.Collection;
import org.moeaframework.core.Variation;
import org.moeaframework.util.TypedProperties;

/**
 * Factory methods for creating an instance of IHyperHeuristicc
 * @author SEAK2
 */
public class AOSFactory {
    
    public static AbstractOperatorSelector getOperatorSelector(String name, TypedProperties properties, Collection<Variation> operators){
        AbstractOperatorSelector operatorSelector = null;
        
        switch(name){
            case "Random": //uniform random selection
                operatorSelector = new RandomSelect(operators);
                break;
            case "All": //use all operators in a random order
                operatorSelector = new AllOperators(operators);
                break;
            case "PM":{ //Probability matching
                double pmin = properties.getDouble("pm.pmin", 0.1);
                double alpha = properties.getDouble("pm.alpha", 0.8);
                operatorSelector = new ProbabilityMatching(operators,alpha,pmin);
                }
                break;
            case "AP":{ //Adaptive Pursuit
                double pmin = properties.getDouble("ap.pmin", 0.1);
                
                double alpha = properties.getDouble("ap.alpha", 0.8);
                double beta = properties.getDouble("ap.beta", 0.8);
                operatorSelector = new AdaptivePursuit(operators, alpha, beta,pmin);
                }
                break;
            case "FRRMAB":{
                double c = properties.getDouble("frrmab.c", 0.5);
                int windowSize = properties.getInt("frrmab.windowsize",100);
                double d = properties.getDouble("frrmab.d", 1);
                operatorSelector = new FRRMAB(operators, c, windowSize,d);
            }
            break;
            default: throw new IllegalArgumentException("Invalid operator selector specified:" + name);
        }
        
        return operatorSelector;
    }
}
