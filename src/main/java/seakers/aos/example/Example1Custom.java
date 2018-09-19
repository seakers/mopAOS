package seakers.aos.example;

/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.Properties;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Population;
import org.moeaframework.core.Selection;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.ParetoConstraintComparator;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.operator.real.SBX;

/**
 * Demonstrates using an Executor to solve the UF1 test problem with NSGA-II but
 * with custom algorithm parameter and an operator that is not SBX.
 */
public class Example1Custom {

    public static void main(String[] args) {
        //declare custom parameters
        Properties properties = new Properties();
        properties.setProperty("populationSize", "200");
        properties.setProperty("operator", "1x+pm");
        properties.setProperty("1x.rate", "0.9");
        
        //configure and run this experiment
        NondominatedPopulation result = new Executor()
                .withProblem("UF1")
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(10000)
                .withProperties(properties)
                .run();

        //display the results
        System.out.format("Objective1  Objective2%n");

        for (Solution solution : result) {
            System.out.format("%.4f      %.4f%n",
                    solution.getObjective(0),
                    solution.getObjective(1));
        }
        
        Variation variation = new SBX(0.9, 20);
        Population population = new Population();
        Selection selection = new TournamentSelection(2, new ParetoConstraintComparator());
        Solution[] parents = selection.select(variation.getArity(), population);
    }

}
