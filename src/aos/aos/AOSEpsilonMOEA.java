/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.aos;

import aos.creditassigment.Credit;
import aos.creditassigment.CreditFunctionInputType;
import aos.creditassigment.ICreditAssignment;
import aos.creditassignment.offspringparent.AbstractOffspringParent;
import aos.creditassignment.setcontribution.AbstractPopulationContribution;
import aos.creditassignment.setimprovement.AbstractOffspringPopulation;
import aos.history.CreditHistory;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.nextoperator.INextOperator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.lang3.ArrayUtils;
import org.moeaframework.algorithm.EpsilonMOEA;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.ParallelPRNG;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Selection;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.DominanceComparator;
import aos.operator.CheckParents;

/**
 * This class extends the epsilon MOEA
 *
 * @author nozomihitomi
 */
public class AOSEpsilonMOEA extends EpsilonMOEA implements IAOS {

    /**
     * The type of heuristic selection method
     */
    private final INextOperator operatorSelector;

    /**
     * The Credit definition to be used that defines how much credit to receive
     * for certain types of solutions
     */
    private final ICreditAssignment creditDef;

    /**
     * The history that stores all the heuristics selected by the hyper
     * heuristics. History can be extracted by getSelectionHistory(). Used for
     * analyzing the results to see the dynamics of heuristics selected
     */
    private final OperatorSelectionHistory operatorSelectionHistory;

    /**
     * The history that stores all the rewards received by the operators. Used
     * for analyzing the results to see the dynamics in rewards
     */
    private final CreditHistory creditHistory;

    /**
     * The set of heuristics that the hyper heuristic is able to work with
     */
    private final Collection<Variation> operators;

    /**
     * The history of the heuristics' qualities over time. Used for analyzing
     * the results to see the dynamics of the heuristic qualities
     */
    private final OperatorQualityHistory qualityHistory;

    /**
     * parallel purpose random generator
     */
    private final ParallelPRNG pprng;

    /**
     * Selection operator to select parent solutions
     */
    private final Selection selection;

    /**
     * Name to id the hyper-heuristic
     */
    private String name;

    public AOSEpsilonMOEA(Problem problem, Population population,
            EpsilonBoxDominanceArchive archive, Selection selection,
            Initialization initialization, INextOperator operatorSelector,
            ICreditAssignment creditDef) {
        super(problem, population, archive, selection, null, initialization);

        this.operators = operatorSelector.getOperators();
        this.operatorSelector = operatorSelector;
        this.creditDef = creditDef;
        this.operatorSelectionHistory = new OperatorSelectionHistory(operators);
        this.qualityHistory = new OperatorQualityHistory(operators);
        this.creditHistory = new CreditHistory(operators);
        this.pprng = new ParallelPRNG();
        this.selection = selection;
    }

    public AOSEpsilonMOEA(Problem problem, Population population,
            EpsilonBoxDominanceArchive archive, Selection selection,
            Initialization initialization, INextOperator operatorSelector,
            ICreditAssignment creditDef, DominanceComparator comparator) {
        super(problem, population, archive, selection, null, initialization, comparator);

        this.operators = operatorSelector.getOperators();
        this.operatorSelector = operatorSelector;
        this.creditDef = creditDef;
        this.operatorSelectionHistory = new OperatorSelectionHistory(operators);
        this.qualityHistory = new OperatorQualityHistory(operators);
        this.creditHistory = new CreditHistory(operators);
        this.pprng = new ParallelPRNG();
        this.selection = selection;
    }

    @Override
    public void iterate() {
        //only evaluate if children if it meets some criteria
        boolean flag = false;

        Solution[] parents = null;
        Variation operator = null;

        while (!flag) {
            //select next heuristic
            operator = operatorSelector.nextOperator();

            //select one parent from the archive and the rest from the population
            if (archive.size() <= 1) {
                parents = selection.select(operator.getArity(), population);
            } else {
                parents = ArrayUtils.add(
                        selection.select(operator.getArity() - 1, population),
                        archive.get(pprng.nextInt(archive.size())));
            }
            pprng.shuffle(parents);
            if (operator instanceof CheckParents) {
                if(((CheckParents)operator).check(parents)){
                    flag = true;
                }
            } else {
                flag = true;
            }
        }
        operatorSelectionHistory.add(operator, this.numberOfEvaluations);
        Solution[] children = operator.evolve(parents);
        evaluateAll(children);
        
        //add all children to population and update
        for (Solution child : children) {
            addToPopulation(child);
        }

        if (creditDef.getInputType() == CreditFunctionInputType.OP) {
            double creditValue = 0.0;
            for (Solution child : children) {
                Solution refParent = parents[pprng.nextInt(parents.length)];

                //credit definitions operating on population and archive does 
                //NOT modify the population by adding the child to the population/archive
                switch (creditDef.getOperatesOn()) {
                    case PARENT:
                        creditValue += ((AbstractOffspringParent) creditDef).compute(child, refParent);
                        break;
                    default:
                        throw new NullPointerException("Credit definition not "
                                + "recognized. Used " + creditDef.getInputType() + ".");
                }
            }

            Credit reward = new Credit(this.numberOfEvaluations, creditValue);
            operatorSelector.update(reward, operator);
            creditHistory.add(operator, reward);
        } else if (creditDef.getInputType() == CreditFunctionInputType.SI) {
            double creditValue = 0.0;
            for (Solution child : children) {
                //credit definitions operating on PF and archive will 
                //modify the nondominated population by adding the child to the nondominated population.
                creditValue += ((AbstractOffspringPopulation) creditDef).compute(child, archive);
            }
            Credit reward = new Credit(this.numberOfEvaluations, creditValue);
            operatorSelector.update(reward, operator);
            creditHistory.add(operator, reward);
        } else if (creditDef.getInputType() == CreditFunctionInputType.CS) {
            for (Solution child : children) {
                child.setAttribute("heuristic", new SerializableVal(operator.toString()));
                archive.addAll(children);
            }
            HashMap<Variation, Credit> popContRewards;
            switch (creditDef.getOperatesOn()) {
                case ARCHIVE:

                    popContRewards = ((AbstractPopulationContribution) creditDef).
                            compute(archive, operators, this.numberOfEvaluations);

                    break;
                case POPULATION:

                    popContRewards = ((AbstractPopulationContribution) creditDef).
                            compute(getPopulation(), operators, this.numberOfEvaluations);

                    break;
                default:
                    throw new NullPointerException("Credit definition not "
                            + "recognized. Used " + creditDef.getInputType() + ".");
            }
            Iterator<Variation> iter = popContRewards.keySet().iterator();
            while (iter.hasNext()) {
                Variation operator_i = iter.next();
                operatorSelector.update(popContRewards.get(operator_i), operator_i);
                creditHistory.add(operator_i, new Credit(this.numberOfEvaluations, popContRewards.get(operator_i).getValue()));
            }
        } else {
            throw new UnsupportedOperationException("RewardDefinitionType not recognized ");
        }
        updateQualityHistory();

    }

    /**
     * Updates the quality history every iteration for each heuristic according
     * to the INextHeuristic class used
     */
    private void updateQualityHistory() {
        HashMap<Variation, Double> currentQualities = operatorSelector.getQualities();
        for (Variation heuristic : operators) {
            qualityHistory.add(heuristic, currentQualities.get(heuristic));
        }
    }

    /**
     * Returns the ordered history of operators that were selected
     *
     * @return The ordered history of operators that were selected
     */
    @Override
    public OperatorSelectionHistory getSelectionHistory() {
        return operatorSelectionHistory;
    }

    /**
     * gets the quality history stored for each operator in the hyper-heuristic
     *
     * @return
     */
    @Override
    public OperatorQualityHistory getQualityHistory() {
        return qualityHistory;
    }

    /**
     * gets the credit history stored for each operator in the hyper-heuristic
     *
     * @return
     */
    @Override
    public CreditHistory getCreditHistory() {
        return creditHistory;
    }

    /**
     * Reset the AOS. Clear all selection history and the credit repository.
     * Clears the population and the archive
     */
    @Override
    public void reset() {
        operatorSelectionHistory.reset();
        operatorSelector.reset();
        numberOfEvaluations = 0;
        qualityHistory.clear();
        population.clear();
        archive.clear();
        creditDef.clear();
    }

    @Override
    public ICreditAssignment getCreditDefinition() {
        return creditDef;
    }

    @Override
    public INextOperator getNextHeuristicSupplier() {
        return operatorSelector;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
