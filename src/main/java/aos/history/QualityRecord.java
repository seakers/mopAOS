/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.history;

/**
 * Stores the quality of an operator and the iteration when the quality is
 * recorded
 */
public class QualityRecord {

    /**
     * The quality value
     */
    private final double quality;

    /**
     * The iteration at which the quality is recorded
     */
    private final int iteration;

    /**
     * Creates a new QualityRecord
     *
     * @param quality The quality value
     * @param iteration The iteration at which the quality is recorded
     */
    public QualityRecord(double quality, int iteration) {
        this.quality = quality;
        this.iteration = iteration;
    }

    /**
     * Gets the quality value
     *
     * @return
     */
    public double getQuality() {
        return quality;
    }

    /**
     * Gets the iteration at which the quality is recorded
     *
     * @return
     */
    public int getIteration() {
        return iteration;
    }

}
