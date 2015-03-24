package edu.colostate.cs.gc.profit.median;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/23/15
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MedianFinder {

    protected double median;

    public abstract void add(double value);

    public abstract void remove(double value);

    public double getMedian() {
        return median;
    }

}
