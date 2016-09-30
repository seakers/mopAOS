/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operatorselectors.replacement;

import java.util.Collection;
import java.util.HashMap;
import org.moeaframework.core.Variation;

/**
 * Classes implementing OperatorRemover will be responsible for selecting the
 * operators from a set to remove
 *
 * @author nozomihitomi
 */
public interface OperatorRemover {

    /**
     * Method to select the operators that will be removed from a set. The
     * implemented method will rely on operator qualities as an objective to
     * decide which operators should be removed
     *
     * @param nevals the current number of evaluations used
     * @param qualities the qualities of each operator
     * @return The collection of operators to remove
     */
    public Collection<Variation> selectOperators(int nevals, HashMap<Variation, Double> qualities);
}
