/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassignment.offspringparent;

import aos.creditassigment.AbstractOffspringParent;
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

    private final HVFitnessEvaluator hvFitnessEvaluator;

    public ParentIndicator(Problem problem) {
        super();
        this.hvFitnessEvaluator = new HVFitnessEvaluator(problem);
    }

    @Override
    public double compute(Solution offspring, Solution parent) {
        double hv1 = hvFitnessEvaluator.calculate(parent, offspring);
        double hv2 = hvFitnessEvaluator.calculate(offspring, parent);
        return Math.max((hv1 - hv2) / hv1, 0.0);
    }

    /**
     * A wrapper for the fitness evaluation that exposes the calculation of indicators on two solutions
     */
    private class HVFitnessEvaluator extends HypervolumeFitnessEvaluator{

        public HVFitnessEvaluator(Problem problem) {
            super(problem);
        }

        public double calculate(Solution solution1, Solution solution2) {
            return super.calculateIndicator(solution1, solution2); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
