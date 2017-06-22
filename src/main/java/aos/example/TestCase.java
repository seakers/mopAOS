/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.example;

import aos.aos.AOSMOEA;
import aos.aos.AOSStrategy;
import aos.creditassigment.ICreditAssignment;
import aos.creditassignment.offspringparent.ParentDomination;
import aos.nextoperator.IOperatorSelector;
import aos.operator.AOSVariation;
import aos.operatorselectors.ProbabilityMatching;
import java.util.ArrayList;
import java.util.Properties;
import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.AbstractEvolutionaryAlgorithm;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.StandardAlgorithms;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.Population;
import org.moeaframework.core.Variation;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.operator.real.PM;
import org.moeaframework.core.operator.real.SBX;
import org.moeaframework.problem.CEC2009.UF1;

/**
 *
 * @author nozomihitomi
 */
public class TestCase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        //create the desired problem
        UF1 uf1 = new UF1();
        
        //create the desired algorithm
        int populationSize = 100;
        AOSVariation variation = new AOSVariation(); 
        NondominatedSortingPopulation population = new NondominatedSortingPopulation();
        EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.01);
        TournamentSelection selection = new TournamentSelection(2);
        RandomInitialization initialization = new RandomInitialization(uf1, populationSize);
        NSGAII nsgaii = new NSGAII(uf1, population, archive, selection, variation, initialization);

        //example of operators you might use
        ArrayList<Variation> operators = new ArrayList();
        double crossProbability = 0.8;
        double crossDistributionIndex = 20;
        operators.add(new SBX(crossProbability, crossDistributionIndex));
        double mutProbability = 0.1;
        double mutDistributionIndex = 20;
        operators.add(new PM(mutProbability, mutDistributionIndex));

        //create operator selector
        IOperatorSelector operatorSelector = new ProbabilityMatching(operators, 0.8, 0.8);

        //create credit assignment
        ICreditAssignment creditAssignment = new ParentDomination(1, 0, 0);

        //create AOS
        AOSStrategy aosStrategy = new AOSStrategy(creditAssignment, operatorSelector);
        AOSMOEA aos = new AOSMOEA(nsgaii,variation, aosStrategy);

        //attach collectors
        Instrumenter instrumenter = new Instrumenter().withFrequency(5)
                .attachElapsedTimeCollector();

        InstrumentedAlgorithm instAlgorithm = instrumenter.instrument(aos);

        //conduct search
        int maxEvaluations = 1000;
        while (!instAlgorithm.isTerminated() && 
                (instAlgorithm.getNumberOfEvaluations() < maxEvaluations)) {
            instAlgorithm.step();
        }

    }

}
