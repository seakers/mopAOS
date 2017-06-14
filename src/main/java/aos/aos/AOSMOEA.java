/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.aos;

import aos.creditassigment.Credit;
import aos.creditassigment.ICreditAssignment;
import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.nextoperator.IOperatorSelector;
import java.util.Map;
import org.moeaframework.algorithm.AbstractEvolutionaryAlgorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;

/**
 * An MOEA with an adaptive operator selector controlling the use of the
 * operators
 *
 * @author nozomihitomi
 */
public class AOSMOEA extends AbstractEvolutionaryAlgorithm implements IAOS {

    private final String nfeStr = "NFE";

    private final String creatorStr = "operator";

    /**
     * The type of operator selection method
     */
    private final IOperatorSelector operatorSelector;

    /**
     * The Credit assignment strategy to be used that defines how much credit to
     * receive for certain types of solutions
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
     * Name to id the AOS
     */
    private String name;

    private final AOSVariation adaptiveOperator;

    /**
     * The underlying evolutionary algorithm
     */
    private final AbstractEvolutionaryAlgorithm ea;

    /**
     * The current paretofront
     */
    private final NondominatedPopulation paretofront;

    /**
     * Var
     *
     * @param var a dummy variation that must be given to the EA. It is
     * reassigned to the given operators during the search by the AOS
     * @param ea the evolutionary algorithm
     * @param strategy the credit assignment and operator selection strategies
     */
    public AOSMOEA(Variation var, AbstractEvolutionaryAlgorithm ea,
            AOSStrategy strategy) {
        super(ea.getProblem(), ea.getPopulation(), ea.getArchive(), null);
        this.creditAssignment = strategy.getCreditAssignment();
        this.operatorSelector = strategy.getOperatorSelection();
        this.ea = ea;
        this.creditHistory = new CreditHistory();
        this.operatorSelectionHistory = new OperatorSelectionHistory();
        this.qualityHistory = new OperatorQualityHistory();
        this.adaptiveOperator = new AOSVariation();
        var = this.adaptiveOperator;
        this.paretofront = new NondominatedPopulation();
    }

    @Override
    protected void initialize() {
        if (!ea.isInitialized() && !ea.isTerminated()) {
            ea.step();
        }
        for (Solution soln : ea.getPopulation()) {
            soln.setAttribute(nfeStr, 0);
        }
    }

    @Override
    public void terminate() {
        ea.terminate(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isTerminated() {
        return ea.isTerminated(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInitialized() {
        return ea.isInitialized(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Problem getProblem() {
        return ea.getProblem(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumberOfEvaluations() {
        return ea.getNumberOfEvaluations(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluate(Solution solution) {
        ea.evaluate(solution); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateAll(Solution[] solutions) {
        ea.evaluateAll(solutions); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateAll(Iterable<Solution> solutions) {
        ea.evaluateAll(solutions); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void iterate() {
        Variation nextOperator = operatorSelector.nextOperator();
        this.adaptiveOperator.setVariation(nextOperator);
        ea.step();
        Solution[] offspring = this.adaptiveOperator.getOffspring();
        Solution[] parents = this.adaptiveOperator.getParents();

        //set attributes to all newly created offspring
        for (Solution soln : offspring) {
            soln.setAttribute(nfeStr, ea.getNumberOfEvaluations());
            soln.setAttribute(creatorStr, nextOperator.toString());
        }

        paretofront.addAll(offspring);

        Map<String, Double> credits = creditAssignment.compute(
                offspring, parents, population, paretofront, archive,
                operatorSelector.getOperatorNames());

        for (String name : credits.keySet()) {
            Credit reward = new Credit(this.numberOfEvaluations, credits.get(name));
            operatorSelector.update(reward, operatorSelector.getOperator(name));
            creditHistory.add(operatorSelector.getOperator(name), reward);
        }
        
        //update the quality history
        Map<Variation, Double> currentQualities = operatorSelector.getQualities();
        for (Variation operator : operatorSelector.getOperators()) {
            qualityHistory.add(operator, currentQualities.get(operator));
        }
        
    }

    @Override
    public OperatorSelectionHistory getSelectionHistory() {
        return operatorSelectionHistory;
    }

    @Override
    public OperatorQualityHistory getQualityHistory() {
        return qualityHistory;
    }

    @Override
    public CreditHistory getCreditHistory() {
        return creditHistory;
    }

    @Override
    public ICreditAssignment getCreditAssignment() {
        return creditAssignment;
    }

    @Override
    public IOperatorSelector getOperatorSelector() {
        return operatorSelector;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    private class AOSVariation implements Variation {

        private Variation var;

        private Solution[] parents;

        private Solution[] offspring;

        @Override
        public int getArity() {
            return var.getArity();
        }

        @Override
        public Solution[] evolve(Solution[] parents) {
            this.parents = parents;
            this.offspring = var.evolve(parents);
            return this.offspring;
        }

        public Solution[] getParents() {
            return parents;
        }

        public Solution[] getOffspring() {
            return offspring;
        }

        public void setVariation(Variation variation) {
            this.var = variation;
        }

        public Variation getVariation(Variation variation) {
            return var;
        }
    }

}
