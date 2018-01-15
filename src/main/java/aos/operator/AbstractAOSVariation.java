/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operator;

import aos.creditassigment.Credit;
import aos.creditassigment.ICreditAssignment;
import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.nextoperator.IOperatorSelector;
import java.util.Collection;
import java.util.Map;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;

/**
 * Abstract class for an AOS variation
 *
 * @author nozomihitomi
 */
public abstract class AbstractAOSVariation implements AOSVariation {

    /**
     * The solution attribute to keep track of which operator created it
     */
    public static final String OPERATOR_ATTRIBUTE = "operator";
    
    /**
     * The solution attribute to keep track of when the solution was created
     */
    private final String NFE_ATTR = "NFE";

    /**
     * The operator selection strategy
     */
    private final IOperatorSelector operatorSelector;

    /**
     * Credit assignment strategy
     */
    private final ICreditAssignment creditAssignment;

    /**
     * The history that stores all the operators selected by the hyper
     * operators. History can be extracted by getSelectionHistory(). Used for
     * analyzing the results to see the dynamics of operators selected
     */
    private final OperatorSelectionHistory operatorSelectionHistory;

    /**
     * The history that stores all the rewards received by the operators. Used
     * for analyzing the results to see the dynamics in rewards
     */
    private final CreditHistory creditHistory;

    /**
     * The history of the operators' qualities over time. Used for analyzing the
     * results to see the dynamics of the operator qualities
     */
    private final OperatorQualityHistory qualityHistory;

    /**
     * An internal counter for the number of function evaluations
     */
    private int nfeCounter;

    public AbstractAOSVariation(IOperatorSelector operatorSelector, ICreditAssignment creditAssignment, int initialNFE) {
        this.operatorSelector = operatorSelector;
        this.creditAssignment = creditAssignment;
        this.operatorSelectionHistory = new OperatorSelectionHistory();
        this.creditHistory = new CreditHistory();
        this.qualityHistory = new OperatorQualityHistory();
        this.nfeCounter = initialNFE;
    }

    @Override
    public int getArity() {
        if (getOperatorSelector().getOperators().isEmpty()) {
            throw new IllegalStateException("no operators added");
        }

        int arity = 0;
        for (Variation operator : getOperatorSelector().getOperators()) {
            arity = Math.max(arity, operator.getArity());
        }

        return arity;
    }

    @Override
    public Solution[] evolve(Solution[] parents) {
        if (getOperatorSelector().getOperators().isEmpty()) {
            throw new IllegalStateException("no operators added");
        }
        //Some operators might need to check some parent property in order to operate on them
        //These operators must extend AbstractCheckParent and throw a CheckParentException if the parents don't fulfill the given criteria
        //If the parents fail to fulfill the criteria, the operator is given 0 credit and a new operator is selected
        Solution[] offspring = null;
        Variation nextOperator = null;
        while (nextOperator == null) {
            try {
                nextOperator = getOperatorSelector().nextOperator();
                offspring = nextOperator.evolve(parents);

                operatorSelectionHistory.add(nextOperator, nfeCounter);
                nfeCounter += offspring.length;

                //keep a record of who made which solution
                for (Solution soln : offspring) {
                    soln.setAttribute(OPERATOR_ATTRIBUTE, nextOperator.toString());
                    soln.setAttribute(NFE_ATTR, nfeCounter);
                }

            } catch (CheckParentException e) {
                nextOperator = null;
            }
        }

        return offspring;
    }

    @Override
    public void update() {
        Map<String, Double> credits = computeCredits();
        for (String name : credits.keySet()) {
            Credit reward = new Credit(nfeCounter, credits.get(name));
            operatorSelector.update(reward, operatorSelector.getOperator(name));
            creditHistory.add(operatorSelector.getOperator(name), reward);
        }

        //update the quality history
        Map<Variation, Double> currentQualities = operatorSelector.getQualities();
        for (Variation operator : operatorSelector.getOperators()) {
            qualityHistory.add(operator, currentQualities.get(operator), nfeCounter);
        }
    }

    /**
     * Computes the credits for any operators used since the last time credits
     * were computed
     *
     * @return
     */
    protected abstract Map<String, Double> computeCredits();

    /**
     * A internal reset that is recommended when using AOS strategies for
     * generational algorithms. Reset on internal fields might be necessary
     * after each generation.
     */
    protected abstract void internalReset();

    @Override
    public void reset() {
        operatorSelector.reset();
    }

    @Override
    public void reset(Collection<Variation> operators) {
        operatorSelector.reset();
        operatorSelector.removeOperators();
        for (Variation var : operators) {
            operatorSelector.addOperator(var);
        }
    }

    /**
     * Gets the operator selection strategy
     *
     * @return the operator selection strategy
     */
    public IOperatorSelector getOperatorSelector() {
        return operatorSelector;
    }

    /**
     * Gets the credit assignment strategy
     *
     * @return the credit assignment strategy
     */
    public ICreditAssignment getCreditAssignment() {
        return creditAssignment;
    }

    /**
     * Gets the operator selection history
     *
     * @return the operator selection history
     */
    public OperatorSelectionHistory getSelectionHistory() {
        return operatorSelectionHistory;
    }

    /**
     * Gets the operator quality history
     *
     * @return the operator quality history
     */
    public OperatorQualityHistory getQualityHistory() {
        return qualityHistory;
    }

    /**
     * Gets the credit history for all operators
     *
     * @return the credit history
     */
    public CreditHistory getCreditHistory() {
        return creditHistory;
    }

}
