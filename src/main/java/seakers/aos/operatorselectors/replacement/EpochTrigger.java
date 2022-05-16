/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seakers.aos.operatorselectors.replacement;

import seakers.aos.aos.AOS;

/**
 * This replacement trigger will detect when an epoch has passed. Each epoch is
 * defined with a fixed number of function evaluations
 *
 * @author nozomihitomi
 */
public class EpochTrigger implements ReplacementTrigger {

    /**
     * The number of evaluations that define one epoch
     */
    private final int epochLength;
    
    /**
     * offset trigger by the number of function evaluations
     */
    private final int offset;

    /**
     * The evaluation count when the epochtrigger was triggered last
     */
    private int lastTriggeredEpoch;

    /**
     * The constructor to create a new EpochTrigger that detects if an epoch has
     * elapsed.
     *
     * @param epochLength The number of evaluations that define one epoch
     */
    public EpochTrigger(int epochLength) {
        this.epochLength = epochLength;
        this.lastTriggeredEpoch = 0;
        this.offset = 0;
    }
    
    /**
     * The constructor to create a new EpochTrigger that detects if an epoch has
     * elapsed.
     *
     * @param epochLength The number of evaluations that define one epoch
     * @param offset the number of function evaluations to offset the trigger by
     */
    public EpochTrigger(int epochLength, int offset) {
        this.epochLength = epochLength;
        this.lastTriggeredEpoch = 0;
        this.offset = offset;
    }

    @Override
    public boolean checkTrigger(AOS aos) {
        int nevals = aos.getNumberOfEvaluations();
        if (Math.floorDiv(nevals-offset, epochLength) > lastTriggeredEpoch) {
            this.lastTriggeredEpoch++;
            return true;
        } else {
            return false;
        }
    }
}
