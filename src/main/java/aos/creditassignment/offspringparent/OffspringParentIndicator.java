/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassignment.offspringparent;

import aos.creditassigment.AbstractOffspringParent;
import aos.creditassignment.fitnessindicator.IIndicator;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

/**
 * These indicators take the form of the indicators used in IBEA. It uses the
 * population to compute the fitness of individuals via quality indicators. It
 * does not compute the quality of the population
 *
 * Indicator fitness calculation based on Zitzler, E., and Simon, K. (2004).
 * Indicator-Based Selection in Multiobjective Search. 8th International
 * Conference on Parallel Problem Solving from Nature (PPSN VIII), 832–842.
 * doi:10.1007/978-3-540-30217-9_84
 *
 * @author nozomihitomi
 */
public class OffspringParentIndicator extends AbstractOffspringParent {

    /**
     * The binary indicator to use
     */
    private final IIndicator indicator;

    /**
     * Creates a new indicator-based credit assignment strategy
     * @param indicator The indicator to use
     * @param prob the problem being solved
     */
    public OffspringParentIndicator(IIndicator indicator, Problem prob) {
        super();
        this.indicator = indicator;
    }
    
    @Override
    public double computeCredit(Solution offspring, Solution parent) {
        return Math.max(0.0, indicator.compute(parent, offspring));
    }

}
