/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassigment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * A category of credit assignment strategies that compares the offspring to its
 * parent solution to assign credit
 *
 * @author nozomihitomi
 */
public abstract class AbstractOffspringParent implements ICreditAssignment {
    
    /**
     * Computes the credit by comparing the offspring solution to its parent
     * @param offspring offspring solution
     * @param parent the parent of the offspring solution
     * @return the credit to assign
     */
    public abstract double compute(Solution offspring, Solution parent);

    @Override
    public  Map<String, Double> compute(Solution[] offspring, Solution[] parent,
            Population population, NondominatedPopulation paretoFront, 
            NondominatedPopulation archive, Set<String> operators){
        HashMap<String, Double> credits = new HashMap<>();
        for (Solution o : offspring) {
            for (Solution p : parent) {
                String name = String.valueOf(o.getAttribute("operator"));
                if (!operators.contains(name)) {
                    continue;
                }
                double c = compute(o, p);
                if (!credits.containsKey(name)) {
                    credits.put(name, c);
                } else {
                    credits.put(name, credits.get(name) + c);
                }
            }
        }
        return credits;
    }
    
    
}
