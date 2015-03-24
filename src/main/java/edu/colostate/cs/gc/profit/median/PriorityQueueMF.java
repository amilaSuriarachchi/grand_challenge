package edu.colostate.cs.gc.profit.median;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/23/15
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityQueueMF extends MedianFinder {

    private PriorityQueue<Double> lowQueue = new PriorityQueue<Double>(20, new Comparator<Double>() {

        public int compare(Double o1, Double o2) {
            return o2.compareTo(o1);
        }
    });
    private PriorityQueue<Double> highQueue = new PriorityQueue<Double>(20, new Comparator<Double>() {

        public int compare(Double o1, Double o2) {
            return o1.compareTo(o2);
        }
    });

    private int numOfFares;

    public PriorityQueueMF() {
        this.numOfFares = 0;
        this.median = 0;
    }

    @Override
    public void add(double fare) {
        this.numOfFares++;

        if (this.numOfFares % 2 == 0) {
            if (fare <= this.median) {
                this.lowQueue.add(fare);
                double highOfLow = this.lowQueue.peek();
                this.highQueue.add(this.median);
                this.median = (this.median + highOfLow) / 2;
            } else {
                this.highQueue.add(fare);
                double lowOfHigh = this.highQueue.peek();
                this.lowQueue.add(this.median);
                this.median = (this.median + lowOfHigh) / 2;
            }
        } else {
            // now existing median is a fake median which is equals to mid point of two medians
            if (fare <= this.median) {
                this.lowQueue.add(fare);
                this.median = this.lowQueue.poll();
            } else {
                this.highQueue.add(fare);
                this.median = this.highQueue.poll();
            }
        }
    }

    @Override
    public void remove(double fare) {
        this.numOfFares--;

        if (this.numOfFares == 0) {
            this.median = 0;
        } else {
            if (this.numOfFares % 2 == 0) {
                if (fare == this.median) {
                    this.median = (this.lowQueue.peek() + this.highQueue.peek()) / 2;
                } else if (fare > this.median) {
                    this.highQueue.remove(fare);
                    this.highQueue.add(this.median);
                    this.median = (this.median + this.lowQueue.peek()) / 2;
                } else {
                    this.lowQueue.remove(fare);
                    this.lowQueue.add(this.median);
                    this.median = (this.median + this.highQueue.peek()) / 2;
                }
            } else {
                if (fare > this.median) {
                    this.highQueue.remove(fare);
                    this.median = this.lowQueue.poll();
                } else {
                    this.lowQueue.remove(fare);
                    this.median = this.highQueue.poll();
                }
            }
        }
    }
}
