/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.aos;

import aos.heuristicselectors.AdaptivePursuit;
import aos.heuristicselectors.FRRMAB;
import aos.heuristicselectors.ProbabilityMatching;
import aos.heuristicselectors.RouletteWheel;
import aos.heuristicselectors.RandomSelect;
import aos.nextoperator.AbstractOperatorSelector;
import java.util.Collection;
import org.moeaframework.core.Variation;
import org.moeaframework.util.TypedProperties;

/**
 * Factory methods for creating an instance of IHyperHeuristicc
 * @author SEAK2
 */
public class AOSFactory {
    
    /**
     * The default problem factory.
     */
    private static AOSFactory instance;

    /**
     * private constructor to enforce singleton
     */
    private AOSFactory(){
        super();
    }
    
    /**
     * Returns an instance of the hyper-heuristic factory
     * @return 
     */
    public static AOSFactory getInstance(){
        if(instance==null)
            return new AOSFactory();
        else 
            return instance;
    }
    
    public AbstractOperatorSelector getHeuristicSelector(String name, TypedProperties properties,Collection<Variation> heuristics){
        AbstractOperatorSelector heuristicSelector = null;
        
        switch(name){
            case "Random": //uniform random selection
                heuristicSelector = new RandomSelect(heuristics);
                break;
            case "PM":{ //Probability matching
                double pmin = properties.getDouble("pmin", 0.1);
                double alpha = properties.getDouble("alpha", 0.8);
                heuristicSelector = new ProbabilityMatching(heuristics,alpha,pmin);
                }
                break;
            case "AP":{ //Adaptive Pursuit
                double pmin = properties.getDouble("pmin", 0.1);
                
                double alpha = properties.getDouble("alpha", 0.8);
                double beta = properties.getDouble("beta", 0.8);
                heuristicSelector = new AdaptivePursuit(heuristics, alpha, beta,pmin);
                }
                break;
            case "FRRMAB":{
                double c = properties.getDouble("c", 0.5);
                int windowSize = properties.getInt("frrmab.windowsize",100);
                double d = properties.getDouble("d", 1);
                heuristicSelector = new FRRMAB(heuristics, c, windowSize,d);
            }
            break;
            default: throw new IllegalArgumentException("Invalid heuristic selector specified:" + name);
        }
        
        return heuristicSelector;
    }
}
