/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.creditassignment.setimprovement;

import aos.creditassigment.AbstractSetImprovement;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * This credit definition gives credit if the specified solution improves the
 * current neighborhood (i.e., subproblem).
 *
 * @author Nozomi
 */
public class SetImprovementDecomposition extends AbstractSetImprovement {

    public SetImprovementDecomposition(Population solutionSet) {
        super(solutionSet);
    }

    @Override
    public double computeCredit(Solution offspring) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
