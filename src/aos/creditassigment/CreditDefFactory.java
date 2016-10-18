/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassigment;

//import hh.creditassignment.fitnessindicator.HypervolumeIndicator;
import aos.creditassignment.fitnessindicator.R2Indicator;
import aos.creditassignment.offspringparent.OPBinaryIndicator;
import aos.creditassignment.offspringparent.ParentDecomposition;
import aos.creditassignment.offspringparent.ParentDomination;
import aos.creditassignment.setimprovement.OffspringNeighborhood;
import aos.creditassignment.setimprovement.OffspringParetoFrontDominance;
import aos.creditassignment.setimprovement.IndicatorImprovement;
//import hh.creditassignment.offspringpopulation.OffspringPopulationIndicator;
import aos.creditassignment.setcontribution.DecompositionContribution;
import aos.creditassignment.setcontribution.IndicatorContribution;
import aos.creditassignment.setcontribution.ParetoFrontContribution;
import java.io.IOException;
import java.util.Arrays;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.util.TypedProperties;

/**
 * Factory methods for creating instances of ICreditDefinition
 *
 * @author SEAK2
 */
public class CreditDefFactory {

    /**
     * The default problem factory.
     */
    private static CreditDefFactory instance;

    /**
     * private constructor to enforce singleton
     */
    private CreditDefFactory() {
        super();
    }

    /**
     * Returns an instance of the hyper-heuristic factory
     *
     * @return
     */
    public static CreditDefFactory getInstance() {
        if (instance == null) {
            return new CreditDefFactory();
        } else {
            return instance;
        }
    }

    public ICreditAssignment getCreditDef(String name, TypedProperties prop, Problem problem) throws IOException {
        ICreditAssignment credDef = null;
        //Get values from properties or use default values
        double satisfy = prop.getDouble("satisfy", 1.0);
        double disatisfy = prop.getDouble("disatisfy", 0.0);
        double neither = prop.getDouble("neither", 0.0);
        //reference point used to compute hypervolume
        double[] defRef = new double[problem.getNumberOfObjectives()];
        Arrays.fill(defRef, 2.0);
        double[] refPoint = prop.getDoubleArray("ref point", defRef);
        //ideal point used in R family indicators
        double[] defIdeal = new double[problem.getNumberOfObjectives()];
        Arrays.fill(defIdeal, -1.0);
        double[] idealPoint = prop.getDoubleArray("ref point", defIdeal);
        
        int numVec=0;
        if(problem.getNumberOfObjectives()==2){
            numVec = prop.getInt("numVec", 50);
        }else if(problem.getNumberOfObjectives()==3){
            numVec = prop.getInt("numVec", 91);
        }
        switch (name) {
            case "OPDe": //offspring improves parent in subproblem
                credDef = new ParentDecomposition();
                break;
            case "OPDo": //offspring dominates parent
                credDef = new ParentDomination(satisfy, neither, disatisfy);
                break;
            case "OPI" :
                credDef = new OPBinaryIndicator(new R2Indicator(problem, numVec, new Solution(idealPoint)),problem);
                break;
            case "SIDe": //
                credDef = new OffspringNeighborhood();
                break;
            case "SIDo": //in pareto front
                credDef = new OffspringParetoFrontDominance(satisfy, disatisfy);
                break;
            case "SII" : //offspring improvement to the mean indicator-based fitness value
                credDef = new IndicatorImprovement(new R2Indicator(problem, numVec, new Solution(idealPoint)));
                break;
            case "CSDe": //contribution to the subproblem's neighborhood
                credDef = new DecompositionContribution(satisfy, disatisfy);
                break;
            case "CSDo": //contribution to pareto front
                credDef = new ParetoFrontContribution(satisfy, disatisfy);
                break;
            case "CSI": //Contribution to indicator fitness of entire population
                credDef = new IndicatorContribution(new R2Indicator(problem, numVec, new Solution(idealPoint)));
                break;
            default:
                throw new IllegalArgumentException("No such credit defintion: " + name);
        }

        return credDef;
    }
}
