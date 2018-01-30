/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.aos;

import aos.creditassigment.ICreditAssignment;
import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.operator.AbstractAOSVariation;
import java.util.HashSet;
import java.util.Set;
import org.moeaframework.algorithm.AbstractEvolutionaryAlgorithm;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import aos.operatorselectors.OperatorSelector;

/**
 * An MOEA with an adaptive operator selector controlling the use of the
 * operators
 *
 * @author nozomihitomi
 */
public class AOSMOEA extends AbstractEvolutionaryAlgorithm implements AOS {

    /**
     * The solution attribute to keep track of when the solution was created
     */
    private final String NFE_ATTR = "NFE";

    /**
     * The AOS strategy
     */
    private final AbstractAOSVariation aosStrategy;

    /**
     * Name to id the AOS
     */
    private String name;

    /**
     * The underlying evolutionary algorithm
     */
    private final AbstractEvolutionaryAlgorithm ea;

    /**
     * Option to record all solutions that are ever created for post-run
     * analysis
     */
    private final boolean recordAllSolutions;

    private final HashSet<Solution> allSolutions;

    /**
     * Var
     *
     * @param ea the evolutionary algorithm
     * @param aosStrategy The AOS strategy
     * @param recordAllSolutions Option to record all solutions that are ever
     * created for post-run analysis
     */
    public AOSMOEA(AbstractEvolutionaryAlgorithm ea,
            AbstractAOSVariation aosStrategy, boolean recordAllSolutions) {
        super(ea.getProblem(), ea.getPopulation(), ea.getArchive(), null);
        this.ea = ea;
        this.aosStrategy = aosStrategy;
        this.recordAllSolutions = recordAllSolutions;
        this.allSolutions = new HashSet<>();
    }

    @Override
    protected void initialize() {
        if (!ea.isInitialized() && !ea.isTerminated()) {
            ea.step();
        }
        for (Solution soln : ea.getPopulation()) {
            soln.setAttribute(NFE_ATTR, 0);
            allSolutions.add(soln);
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
        ea.step();
        aosStrategy.update();
        
        //record solutions if desired
        if (recordAllSolutions) {
            for (Solution soln : ea.getPopulation()) {
                //use a copy in case there are other objects referencing the solutions
                allSolutions.add(soln.deepCopy());
            }
        }
    }

    @Override
    public OperatorSelectionHistory getSelectionHistory() {
        return aosStrategy.getSelectionHistory();
    }

    @Override
    public OperatorQualityHistory getQualityHistory() {
        return aosStrategy.getQualityHistory();
    }

    @Override
    public CreditHistory getCreditHistory() {
        return aosStrategy.getCreditHistory();
    }

    @Override
    public ICreditAssignment getCreditAssignment() {
        return aosStrategy.getCreditAssignment();
    }

    @Override
    public OperatorSelector getOperatorSelector() {
        return aosStrategy.getOperatorSelector();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the recorded solutions that were ever created and evaluated
     * during the search.
     *
     * @return the unique solutions that were evaluated during the search
     */
    public Set<Solution> getAllSolutions() {
        return allSolutions;
    }

}
