/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.operator;

import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;

/**
 * An abstract variation that checks the parents solutions for some
 * pre-specified pattern. If the pattern is not satisfied in both parents, this
 * variation cannot modify the parents
 *
 * @author nozomihitomi
 */
public abstract class AbstractCheckParent implements CheckParents, Variation {

    /**
     * In this implementation, a check is performed to see if the parent
     * solutions meet some criteria. if they meet these criteria, they are
     * operated and evolved. Else a CheckParentException is thrown.
     *
     * @param parents the parent solutions
     * @return the offspring solution
     * @throws CheckParentException if the parent solutions don't meet the
     * criteria
     */
    @Override
    public Solution[] evolve(Solution[] parents) throws CheckParentException {
        if (!check(parents)) {
            throw new CheckParentException();
        } else {
            return evolveParents(parents);
        }
    }

    /**
     * Evolves the parents after they have passed through the Parent check.
     *
     * @param parents parent solutions
     * @return the offspring solutions after operation is applied
     */
    public abstract Solution[] evolveParents(Solution[] parents);

}
