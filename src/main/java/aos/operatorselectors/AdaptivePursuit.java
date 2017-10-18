/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors;

import aos.creditassigment.Credit;
import java.util.Collection;
import java.util.Iterator;
import org.moeaframework.core.PRNG;
import org.moeaframework.core.Variation;

/**
 * Adaptive pursuit algorithm is based on Thierens, D. (2005). An adaptive
 * pursuit strategy for allocating operator probabilities. Belgian/Netherlands
 * Artificial Intelligence Conference, 385–386. doi:10.1145/1068009.1068251
 *
 * @author nozomihitomi
 */
public class AdaptivePursuit extends ProbabilityMatching {

    /**
     * The maximum probability that the heuristic with the highest credits can
     * be selected. It is implicitly defined as 1.0 - m*pmin where m is the
     * number of operators used and pmin is the minimum selection probability
     */
    double pmax;

    /**
     * The Learning Rate
     */
    private final double beta;

    /**
     * Constructor to initialize adaptive pursuit map for selection. The maximum
     * selection probability is implicitly defined as 1.0 - m*pmin where m is
     * the number of operators defined in the given credit repository and pmin
     * is the minimum selection probability
     *
     * @param operators from which to select from 
     * @param alpha the adaptation rate
     * @param beta the learning rate
     * @param pmin the minimum selection probability
     */
    public AdaptivePursuit(Collection<Variation> operators, double alpha, double beta, double pmin) {
        super(operators, alpha, pmin);
        this.pmax = 1 - (probabilities.size() - 1) * pmin;
        this.beta = beta;
        if (pmax < pmin) {
            throw new IllegalArgumentException("the implicit maxmimm selection "
                    + "probability " + pmax + " is less than the minimum selection probability " + pmin);
        }

        //Initialize the probabilities such that a random heuristic gets the pmax
        int operator_lead = PRNG.nextInt(probabilities.size());
        Iterator<Variation> iter = probabilities.keySet().iterator();
        int count = 0;
        while (iter.hasNext()) {
            if (count == operator_lead) {
                probabilities.put(iter.next(), pmax);
            } else {
                probabilities.put(iter.next(), pmin);
            }
            count++;
        }
    }
    
    /**
     * Updates the probabilities stored in the selector
     */
    @Override
    public void update(Credit reward, Variation operator) {
        super.update(reward, operator);
    }
    
    /**
     * Clears the credit repository and resets the selection probabilities and updates the p_max
     */
    @Override
    public void reset() {
        super.reset();
        this.pmax = 1 - (probabilities.size() - 1) * pmin; 
    }

    /**
     * Updates the selection probabilities of the operators according to the
     * qualities of each operator.
     */
    @Override
    protected void updateProbabilities(){

        Variation leadOperator = argMax(qualities.keySet());

        Iterator<Variation> iter = operators.iterator();
        while (iter.hasNext()) {
            Variation operator_i = iter.next();
            double prevProb = probabilities.get(operator_i);
            if (operator_i == leadOperator) {
                probabilities.put(operator_i, prevProb+beta*(pmax-prevProb));
            } else {
                probabilities.put(operator_i, prevProb+beta*(pmin-prevProb));
            }
        }
    }

    /**
     * Want to find the operator that has the maximum quality
     *
     * @param operator
     * @return the current quality of the specified operator
     */
    @Override
    protected double maximizationFunction(Variation operator) {
        return qualities.get(operator);
    }

    @Override
    public String toString() {
        return "AdaptivePursuit";
    }
    
}
