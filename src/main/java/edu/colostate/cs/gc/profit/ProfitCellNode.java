package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.list.NodeValue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitCellNode implements NodeValue {

    public static final int UNDEFINED_PROFITABILITY = -1;

    private double profitability;
    private double midFare;
    private int numOfEmptyTaxis;
    private int numOfFares;

    /**
     * this constructor is being created from an drop off event.
     */
    public ProfitCellNode() {
        this.profitability = 0;
        this.numOfEmptyTaxis = 1;
        this.midFare = 0;
        this.numOfFares = 0;
    }

    /**
     * this constructor is being called when a details of taxi leaving this place and arrive at some other place comes
     * we start the cell with existing fare.
     * in this case since we don't know the number of empty taxis in the area profitability is undefined.
     *
     * @param fare
     */
    public ProfitCellNode(double fare) {
        this.midFare = fare;
        this.numOfFares = 1;
        this.numOfEmptyTaxis = 0;
        this.profitability = UNDEFINED_PROFITABILITY;
    }

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

    public void reduceEmptyTaxi() {
        if (this.numOfEmptyTaxis > 0) {
            this.numOfEmptyTaxis--;
        }
        setProfitability();
    }

    public void increaseEmptyTaxi() {
        this.numOfEmptyTaxis++;
        setProfitability();
    }


    public void addFare(double fare) {

        // this means this taxi has leaved from this location. i.e we need to reduce the empty taxis.
        this.numOfFares++;

        if (this.numOfFares % 2 == 0) {
            if (fare <= this.midFare) {
                this.lowQueue.add(fare);
                double highOfLow = this.lowQueue.peek();
                this.highQueue.add(this.midFare);
                this.midFare = (this.midFare + highOfLow) / 2;
            } else {
                this.highQueue.add(fare);
                double lowOfHigh = this.highQueue.peek();
                this.lowQueue.add(this.midFare);
                this.midFare = (this.midFare + lowOfHigh) / 2;
            }
        } else {
            // now existing median is a fake median which is equals to mid point of two medians
            if (fare <= this.midFare) {
                this.lowQueue.add(fare);
                this.midFare = this.lowQueue.poll();
            } else {
                this.highQueue.add(fare);
                this.midFare = this.highQueue.poll();
            }
        }
        setProfitability();
    }

    public void removeFare(double fare) {

        this.numOfFares--;

        if (this.numOfFares == 0) {
            this.midFare = 0;
        } else {
            if (this.numOfFares % 2 == 0) {
                if (fare == this.midFare){
                    this.midFare = (this.lowQueue.peek() + this.highQueue.peek()) / 2;
                } else if (fare > this.midFare) {
                    this.highQueue.remove(fare);
                    this.highQueue.add(this.midFare);
                    this.midFare = (this.midFare + this.lowQueue.peek()) / 2;
                } else {
                    this.lowQueue.remove(fare);
                    this.lowQueue.add(this.midFare);
                    this.midFare = (this.midFare + this.highQueue.peek()) / 2;
                }
            } else {
                if (fare > this.midFare){
                    this.highQueue.remove(fare);
                    this.midFare = this.lowQueue.poll();
                } else {
                    this.lowQueue.remove(fare);
                    this.midFare = this.highQueue.poll();
                }
            }
        }
        setProfitability();
    }

    private void setProfitability() {
        if (this.numOfEmptyTaxis == 0) {
            this.profitability = UNDEFINED_PROFITABILITY;
        } else {
            this.profitability = this.midFare / this.numOfEmptyTaxis;
        }
    }

    public boolean isEmpty() {
        return (this.numOfFares == 0) && (this.numOfEmptyTaxis == 0);
    }

    public int compare(NodeValue value) {
        double newProfitability = ((ProfitCellNode) value).profitability;
        if (this.profitability > newProfitability){
            return 1;
        } else if (this.profitability < newProfitability){
            return -1;
        }  else {
            return 0;
        }
    }

    public double getProfitability() {
        return profitability;
    }

    public double getMidFare() {
        return midFare;
    }
}
