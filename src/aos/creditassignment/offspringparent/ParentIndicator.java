/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassignment.offspringparent;

import aos.creditassigment.CreditDefinedOn;
import aos.creditassigment.CreditFitnessFunctionType;
import org.moeaframework.core.FitnessEvaluator;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.fitness.HypervolumeFitnessEvaluator;

/**
 * This credit assignment strategies compares the offspring indicator-based
 * fitness to that of its parents
 *
 * @author nozomihitomi
 */
public class ParentIndicator extends AbstractOffspringParent {
    
    private final HypervolumeFitnessEvaluator hvFitnessEvaluator;

    public ParentIndicator(Problem problem) {
        super();
        operatesOn = CreditDefinedOn.PARENT;
        fitType = CreditFitnessFunctionType.I;
        this.hvFitnessEvaluator = new HypervolumeFitnessEvaluator(problem);
    }
    

    /**
     * The offspring vs parent indicator-based credit assignment assigns the
     * difference between the offspring fitness over its parent. If it is
     * negative, it returns zero.
     *
     * @param offspring
     * @param parent
     * @param removedSolution
     * @return
     */
    @Override
    public double compute(Solution offspring, Solution parent) {
        
        double hv1 = hvFitnessEvaluator.calculateIndicator(parent, offspring);
        double hv2 = hvFitnessEvaluator.calculateIndicator(offspring, parent);
        return Math.max((hv1-hv2)/hv1, 0.0);
    }

    @Override
    public String toString() {
        return "OP-I";
    }
}
