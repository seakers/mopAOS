/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors;

import aos.nextoperator.AbstractOperatorSelector;
import aos.creditassigment.Credit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.moeaframework.core.Variation;

/**
 * Selects operators based on probability which is proportional to the
 * operators credits. Each operator gets selected with a minimum probability
 * of pmin. If current credits in credit repository becomes negative, zero
 * credit is re-assigned to that operator. For the first iteration, operators
 * are selected with uniform probability.
 *
 * @author nozomihitomi
 */
public class RouletteWheel extends AbstractOperatorSelector {

    /**
     * Hashmap to store the selection probabilities of each operator
     */
    protected HashMap<Variation, Double> probabilities;

    /**
     * The minimum probability for a operator to be selected
     */
    protected final double pmin;

    /**
     * Constructor to initialize probability map for selection
     *
     * @param operators from which to select from
     * @param pmin The minimum probability for a operator to be selected
     */
    public RouletteWheel(Collection<Variation> operators, double pmin) {
        super(operators);
        this.pmin = pmin;
        this.probabilities = new HashMap();
        reset();
    }

    /**
     * Will return the next operator that gets selected based on probability
     * proportional to a operators credits. Each operator gets selected with a
     * minimum probability of pmin
     *
     * @return
     */
    @Override
    public Variation nextOperator() {
        double p = pprng.nextDouble();
        Iterator<Variation> iter = probabilities.keySet().iterator();
        double sum = 0.0;
        Variation operator = null;
        while (iter.hasNext()) {
            operator = iter.next();
            sum += probabilities.get(operator);
            if (sum >= p) {
                break;
            }
        }
        incrementIterations();
        if (operator == null) {
            throw new NullPointerException("No operator was selected by Probability matching operator selector. Check probabilities");
        } else {
            return operator;
        }
    }

    /**
     * calculate the sum of all qualities across the operators
     *
     * @return the sum of the operators' qualities
     */
    protected double sumQualities() {
        double sum = 0.0;
        Iterator<Variation> iter = qualities.keySet().iterator();
        while (iter.hasNext()) {
            sum += qualities.get(iter.next());
        }
        return sum;
    }

    /**
     * Clears the credit repository and resets the selection probabilities
     */
    @Override
    public void reset() {
        super.resetQualities();
        super.reset();
        probabilities.clear();
        Iterator<Variation> iter = operators.iterator();
        while (iter.hasNext()) {
            //all operators get uniform selection probability at beginning
            probabilities.put(iter.next(), 1.0 / (double) operators.size());
        }
    }

    @Override
    public String toString() {
        return "ProbabilityMatching";
    }

    /**
     * Updates the selection probabilities of the operators according to the
     * qualities of each operator.
     */
    protected void updateProbabilities(){
        double sum = sumQualities();

        // if the credits sum up to zero, apply uniform probabilty to  operators
        Iterator<Variation> iter = operators.iterator();
        if (Math.abs(sum) < Math.pow(10.0, -14)) {
            while (iter.hasNext()) {
                Variation operator_i = iter.next();
                probabilities.put(operator_i, 1.0 / (double) operators.size());
            }
        } else { //else update probabilities proportional to quality
            while (iter.hasNext()) {
                Variation operator_i = iter.next();
                double newProb = pmin + (1 - probabilities.size() * pmin)
                        * (qualities.get(operator_i) / sum);
                probabilities.put(operator_i, newProb);
            }
        }
    }

    /**
     * Selection probabilities are updated
     * @param reward given to the operator
     * @param operator to be rewarded
     */
    @Override
    public void update(Credit reward, Variation operator) {
        qualities.put(operator, qualities.get(operator)+reward.getValue());
        super.checkQuality();
        updateProbabilities();
    }

    @Override
    public boolean removeOperator(Variation operator) {
        boolean out = super.removeOperator(operator);
        probabilities.remove(operator);
        return out;
    }
    
    
}
