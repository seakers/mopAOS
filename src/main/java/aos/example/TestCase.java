/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.example;

import aos.IO.AOSHistoryIO;
import aos.aos.AOSMOEA;
import aos.creditassignment.offspringparent.OffspringParentDomination;
import aos.nextoperator.IOperatorSelector;
import aos.operator.AOSVariationOP;
import aos.operatorselectors.ProbabilityMatching;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.EpsilonMOEA;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.PopulationIO;
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
        NondominatedSortingPopulation population = new NondominatedSortingPopulation();
        EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.01);
        TournamentSelection selection = new TournamentSelection(2);
        RandomInitialization initialization = new RandomInitialization(uf1, populationSize);

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
        OffspringParentDomination creditAssignment = new OffspringParentDomination(1, 0, 0);
        
        //create AOS strategy
        AOSVariationOP aosStrategy = new AOSVariationOP(operatorSelector,creditAssignment,populationSize);

        EpsilonMOEA emoea = new EpsilonMOEA(uf1, population, archive, selection, null, initialization);
        AOSMOEA aos = new AOSMOEA(emoea, aosStrategy, true);

        //attach collectors
        Instrumenter instrumenter = new Instrumenter().withFrequency(5)
                .attachElapsedTimeCollector();

        InstrumentedAlgorithm instAlgorithm = instrumenter.instrument(aos);

        //conduct search
        int maxEvaluations = 1000;
        while (!instAlgorithm.isTerminated()
                && (instAlgorithm.getNumberOfEvaluations() < maxEvaluations)) {
            instAlgorithm.step();

            try {
                //one way to save current population
                PopulationIO.writeObjectives(new File("results.txt"), aos.getPopulation());
            } catch (IOException ex) {
                Logger.getLogger(TestCase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //save AOS results
        AOSHistoryIO.saveSelectionHistory(aos.getSelectionHistory(), new File("selection.csv"), ",");
        AOSHistoryIO.saveCreditHistory(aos.getCreditHistory(), new File("credit.csv"), ",");
        AOSHistoryIO.saveQualityHistory(aos.getQualityHistory(), new File("quality.csv"), ",");

    }

}
