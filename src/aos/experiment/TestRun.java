/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.experiment;

import aos.IO.IOCreditHistory;
import aos.IO.IOQualityHistory;
import aos.IO.IOSelectionHistory;
import aos.aos.AOSFactory;
import aos.aos.IAOS;
import aos.aos.AOSMOEAD;
import aos.nextoperator.INextOperator;
import aos.creditassigment.CreditFitnessFunctionType;
import aos.creditassigment.ICreditAssignment;
import aos.creditassigment.CreditDefFactory;
import aos.aos.AOSIBEA;
import aos.aos.AOSNSGAII;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.analysis.sensitivity.EpsilonHelper;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.Population;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.FitnessComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.fitness.R2FitnessEvaluator;
import org.moeaframework.core.indicator.InvertedGenerationalDistance;
import org.moeaframework.core.indicator.jmetal.FastHypervolume;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.util.TypedProperties;

/**
 * This sets up the experimental run. Need to define the problem, the
 * hyperheuristic components, and the search parameters
 *
 * @author nozomihitomi
 */
public class TestRun implements Callable {

    protected TypedProperties properties;
    protected Problem problem;
    protected String probName;
    protected String path;
    private ICreditAssignment creditDef;
    protected double[] epsilonDouble;
    protected int maxEvaluations;
    private final Collection<Variation> operators;
    protected final NondominatedPopulation referenceSet;
    protected Solution refPointObj;

    /**
     *
     * @param path
     * @param problem
     * @param probName
     * @param referenceSet
     * @param properties
     * @param heuristics
     * @param maxEvaluations
     */
    public TestRun(String path, Problem problem, String probName, NondominatedPopulation referenceSet, TypedProperties properties,
            Collection<Variation> heuristics, int maxEvaluations) {

        this.operators = heuristics;
        this.properties = properties;
        this.problem = problem;
        this.epsilonDouble = properties.getDoubleArray("ArchiveEpsilon",
                new double[]{EpsilonHelper.getEpsilon(problem)});
        this.probName = probName;
        this.maxEvaluations = maxEvaluations;
        this.path = path;

        this.referenceSet = referenceSet;
    }
    
    /**
     * Returns a new instance of AOSIBEA
     *
     * @return
     */
    private IAOS newAOSIBEA() {
        int populationSize = (int) properties.getDouble("populationSize", 600);

        Initialization initialization = new RandomInitialization(problem,
                populationSize);

        Population population = new Population();

        int numVectors = (int) properties.getDouble("r2.numberVectors", 100);
        R2FitnessEvaluator fitnesseval = new R2FitnessEvaluator(problem,numVectors,1.0);

        FitnessComparator fitnessComparator = new FitnessComparator(fitnesseval.areLargerValuesPreferred());
        TournamentSelection selection = new TournamentSelection(fitnessComparator);

        //all other properties use default parameters
        INextOperator selector = AOSFactory.getInstance().getOperatorSelector(properties.getString("AOS", null), properties, operators);

        AOSIBEA aosibea = new AOSIBEA(problem, population, null, selection,
                initialization, fitnesseval, selector, creditDef);

        return aosibea;
    }

    
    /**
     * Returns a new instance of AOSNSGAII
     *
     * @return
     */
    private IAOS newAOSNSGAII() {
        int populationSize = (int) properties.getDouble("populationSize", 600);

        Initialization initialization = new RandomInitialization(problem,
                populationSize);

        NondominatedSortingPopulation population = new NondominatedSortingPopulation();

        TournamentSelection selection = new TournamentSelection(2,
                new ChainedComparator(new ParetoDominanceComparator(),
                        new CrowdingComparator()));

        //all other properties use default parameters
        INextOperator selector = AOSFactory.getInstance().getOperatorSelector(properties.getString("AOS", null), properties, operators);

        AOSNSGAII aosnsgaii = new AOSNSGAII(problem, population, null, selection,
                initialization, selector, creditDef);

        return aosnsgaii;
    }
    
    /**
     * Returns a new Hyper eMOEA instance.
     *
     * @param properties the properties for customizing the new {@code eMOEA}
     * instance
     * @param problem the problem
     * @return a new {@code eMOEA} instance
     */
    private IAOS newAOSMOEAD() throws IOException {

        int populationSize = (int) properties.getDouble("populationSize", 600);

        Initialization initialization = new RandomInitialization(problem,
                populationSize);

        int neighborhoodSize = properties.getInt("neighborhood", 20);

        double delta = properties.getDouble("delta", 0.9);

        double eta = properties.getDouble("eta", 2.0);

        int updateUtility = properties.getInt("updateUtility", 50);

        INextOperator selector = AOSFactory.getInstance().getOperatorSelector(properties.getString("AOS", null), properties, operators);

        AOSMOEAD moeadhh = new AOSMOEAD(problem, neighborhoodSize, initialization,
                delta, eta, updateUtility, selector, creditDef);

        return moeadhh;
    }

    /**
     * Goes through one run of the algorithm. Returns the algorithm object. Can
     * get the population from the algorithm object
     *
     * @return the algorithm object. Can get the population from the algorithm
     * object
     * @throws Exception
     */
    @Override
    public IAOS call() throws Exception {
        IAOS aos;
        try {
            creditDef = CreditDefFactory.getInstance().getCreditDef(properties.getString("CredDef", null), properties, problem);
        } catch (IOException ex) {
            Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (creditDef.getFitnessType() == CreditFitnessFunctionType.De) {
            aos = newAOSMOEAD();
        } else if (creditDef.getFitnessType() == CreditFitnessFunctionType.Do) {
            aos = newAOSNSGAII();
        } else if (creditDef.getFitnessType() == CreditFitnessFunctionType.I) {
            aos = newAOSIBEA();
        } else {
            throw new IllegalArgumentException("Credit fitness type " + creditDef.getFitnessType() + "not recognized");
        }

        InstrumentedAlgorithm instAlgorithm = instrument(aos);

        // run the executor using the listener to collect results
        System.out.println("Starting " + aos.getNextHeuristicSupplier() + creditDef + " on " + probName + " with pop size: " + properties.getDouble("populationSize", 600));
        long startTime = System.currentTimeMillis();
        try {
            while (!instAlgorithm.isTerminated() && (instAlgorithm.getNumberOfEvaluations() < maxEvaluations)) {
                instAlgorithm.step();
                if (instAlgorithm.getNumberOfEvaluations() % 1000 == 0) {
                    System.out.println(instAlgorithm.getNumberOfEvaluations());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
        }

        aos.terminate();
        long finishTime = System.currentTimeMillis();
        System.out.println("Done with optimization. Execution time: " + ((finishTime - startTime) / 1000) + "s");

        aos.setName(String.valueOf(System.nanoTime()));

        String filename = path + File.separator + properties.getProperties().getProperty("saveFolder") + File.separator + probName + "_" // + problem.getNumberOfObjectives()+ "_"
                + aos.getNextHeuristicSupplier() + "_" + aos.getCreditDefinition() + "_" + aos.getName();

        saveIndicatorValues(instAlgorithm, filename);

        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveFinalPopulation"))) {
            NondominatedPopulation ndPop = instAlgorithm.getResult();
            try {
                PopulationIO.writeObjectives(new File(filename + ".NDpop"), ndPop);
            } catch (IOException ex) {
                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveOperatorCreditHistory"))) {
            IOCreditHistory ioch = new IOCreditHistory();
            ioch.saveHistory(((IAOS) aos).getCreditHistory(), filename + ".creditcsv",",");
        }

        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveOperatorSelectionHistory"))) {
            IOSelectionHistory iosh = new IOSelectionHistory();
            iosh.saveHistory(((IAOS) aos).getSelectionHistory(), filename + ".hist");
        }

        //save operator quality history
        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveOperatorQualityHistory"))) {
            IOQualityHistory ioqh = new IOQualityHistory();
            ioqh.saveHistory(((IAOS) aos).getQualityHistory(), filename + ".qual");
        }

        return aos;
    }

    /**
     * Attaches the collectors to the instrumented algorithm
     *
     * @param alg
     * @return
     */
    protected InstrumentedAlgorithm instrument(Algorithm alg) {
        refPointObj = problem.newSolution();
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            refPointObj.setObjective(i, 1.1);
        }

        Instrumenter instrumenter = new Instrumenter().withFrequency(maxEvaluations / 100)
                .withProblem(probName)
                .attachAdditiveEpsilonIndicatorCollector()
                .attachGenerationalDistanceCollector()
                .attachInvertedGenerationalDistanceCollector()
                .attachHypervolumeJmetalCollector(refPointObj)
                .withEpsilon(epsilonDouble)
                .withReferenceSet(referenceSet)
                .attachElapsedTimeCollector();

        return instrumenter.instrument(alg);
    }

    /**
     * Saves the indicator values collected during the run.
     *
     * @param instAlgorithm
     * @param filename
     */
    protected void saveIndicatorValues(InstrumentedAlgorithm instAlgorithm, String filename) {
        Accumulator accum = instAlgorithm.getAccumulator();
        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveIndicators"))) {
            File results = new File(filename + ".res");
            System.out.println("Saving results");

            try (FileWriter writer = new FileWriter(results)) {
                Set<String> keys = accum.keySet();
                Iterator<String> keyIter = keys.iterator();
                while (keyIter.hasNext()) {
                    String key = keyIter.next();
                    int dataSize = accum.size(key);
                    writer.append(key).append(",");
                    for (int i = 0; i < dataSize; i++) {
                        writer.append(accum.get(key, i).toString());
                        if (i + 1 < dataSize) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }

                //also record the final HV
                NondominatedPopulation ndPop = instAlgorithm.getResult();
                FastHypervolume fHV = new FastHypervolume(problem, referenceSet, refPointObj);
                double hv = fHV.evaluate(ndPop);
                writer.append("Final HV, " + hv + "\n");

                //also record the final IGD
                InvertedGenerationalDistance igd = new InvertedGenerationalDistance(problem, referenceSet);
                double figd = igd.evaluate(ndPop);
                writer.append("Final IGD, " + figd + "\n");

                writer.flush();
            } catch (Exception ex) {
                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
