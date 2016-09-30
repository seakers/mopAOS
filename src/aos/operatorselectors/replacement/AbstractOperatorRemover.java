/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors.replacement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.moeaframework.core.Variation;

/**
 * This abstract class implements operator remover but ensures that any
 * operators that cannot be removed shall not be selected for removal
 *
 * @author nozomihitomi
 */
public abstract class AbstractOperatorRemover implements OperatorRemover {

    /**
     * Collection of operators that cannot be removed. For example, may want to
     * keep certain knowledge-independent operators
     */
    private final Collection<Variation> permanentOperators;

    /**
     * Collection of operators that cannot be removed. For example, may want to
     * keep certain knowledge-independent operators
     *
     * @param permanentOperators
     */
    public AbstractOperatorRemover(Collection<Variation> permanentOperators) {
        this.permanentOperators = permanentOperators;
    }

    /**
     * Method to select the operators that will be removed from a set. The
     * method relies on operator qualities as an objective to decide which
     * operators should be removed. This method ensures that the returned
     * collection of operators do not contain any permanent operators that
     * cannot be removed from the adaptive operator selector.
     *
     * @param nevals the current number of evaluations used
     * @param qualities the qualities of each operator
     * @return The collection of operators to remove
     */
    @Override
    public Collection<Variation> selectOperators(int nevals, HashMap<Variation, Double> qualities) {
        ArrayList<Variation> out = new ArrayList();

        HashMap<Variation, Double> operatorMetric = new HashMap<>(qualities.size());
        for (Variation operator : qualities.keySet()) {
            if (permanentOperators.contains(operator)) {
                operatorMetric.put(operator, computeMetric(nevals, qualities.get(operator)));
            }
        }

        while (!finished()) {
            Variation operatorToRemove = getNextLowest(operatorMetric);
            out.add(operatorToRemove);
            operatorMetric.remove(operatorToRemove);
        }

        return out;
    }

    /**
     * Check to see when the method is done selecting operators that need to be
     * replaced
     *
     * @return true if the selection of operators is finished. False otherwise.
     */
    protected abstract boolean finished();

    /**
     * Computes a performance metric of an operator based on the current number
     * of function evaluations used so far and the quality of the operator. The
     * operator remover will remove the operators with the lowest valued metrics
     *
     * @param nevals the number of evaluations used so far
     * @param quality the quality of the operator
     * @return the operator selected for removal
     */
    protected abstract double computeMetric(int nevals, double quality);

    /**
     * Scans over the metrics of each operator and retrieves the one with the
     * lowest metric value
     *
     * @param metric
     * @return
     */
    private Variation getNextLowest(HashMap<Variation, Double> metric) {
        double minMetric = Double.POSITIVE_INFINITY;
        Variation minOperator = null;
        for (Variation operator : metric.keySet()) {
            if (metric.get(operator) < minMetric) {
                minMetric = metric.get(operator);
                minOperator = operator;
            }
        }
        return minOperator;
    }

    /**
     * Gets the collection of permanent operators that are prohibited from being
     * selected for removal
     *
     * @return
     */
    public Collection<Variation> getPermanentOperators() {
        return permanentOperators;
    }
}
