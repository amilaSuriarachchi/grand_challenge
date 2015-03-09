package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.list.NodeValue;

import java.util.*;

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

    private Cell cell;

    private Set<String> dropTaxis = new HashSet<String>();

    private int seqNo;

    /**
     * this constructor is being created from an drop off event.
     */
    public ProfitCellNode(Cell cell, int seqNo) {
        this.profitability = 0;
        this.numOfEmptyTaxis = 1;
        this.midFare = 0;
        this.numOfFares = 0;
        this.cell = cell;
        this.seqNo = seqNo;
    }

    /**
     * this constructor is being called when a details of taxi leaving this place and arrive at some other place comes
     * we start the cell with existing fare.
     * in this case since we don't know the number of empty taxis in the area profitability is undefined.
     *
     * @param fare
     */
    public ProfitCellNode(double fare, Cell cell, int seqNo) {
        this.midFare = fare;
        this.numOfFares = 1;
        this.numOfEmptyTaxis = 0;
        this.profitability = UNDEFINED_PROFITABILITY;
        this.cell = cell;
        this.seqNo = seqNo;
    }

    public Object getKey() {
        return this.cell;
    }

    public void update(NodeValue value) {
        ProfitCellNode newValue = (ProfitCellNode) value;
        this.profitability = newValue.profitability;
        this.seqNo = newValue.seqNo;
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
        calculateProfitability();
    }

    public void increaseEmptyTaxi() {
        this.numOfEmptyTaxis++;
        calculateProfitability();
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
        calculateProfitability();
    }

    public void removeFare(double fare) {

        this.numOfFares--;

        if (this.numOfFares == 0) {
            this.midFare = 0;
        } else {
            if (this.numOfFares % 2 == 0) {
                if (fare == this.midFare) {
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
                if (fare > this.midFare) {
                    this.highQueue.remove(fare);
                    this.midFare = this.lowQueue.poll();
                } else {
                    this.lowQueue.remove(fare);
                    this.midFare = this.highQueue.poll();
                }
            }
        }
        calculateProfitability();
    }

    private void calculateProfitability() {
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

        ProfitCellNode profitCellNode = (ProfitCellNode) value;

        if (this.profitability > profitCellNode.profitability) {
            return 1;
        } else if (this.profitability < profitCellNode.profitability) {
            return -1;
        } else {
            if (this.seqNo > profitCellNode.seqNo) {
                return 1;
            } else if (this.seqNo < profitCellNode.profitability) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public NodeValue getClone() {
        ProfitCellNode clone = new ProfitCellNode(this.cell, this.seqNo);
        clone.setProfitability(this.profitability);
        clone.setMidFare(this.midFare);
        clone.setNumOfEmptyTaxis(this.numOfEmptyTaxis);
        clone.setNumOfFares(this.numOfFares);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfitCellNode that = (ProfitCellNode) o;

        if (Double.compare(that.profitability, profitability) != 0) return false;
        if (seqNo != that.seqNo) return false;
        if (!cell.equals(that.cell)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(profitability);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + cell.hashCode();
        result = 31 * result + seqNo;
        return result;
    }

    public boolean containsTaxi(String medallion) {
        return this.dropTaxis.contains(medallion);
    }

    public void removeTaxi(String medallion) {
        this.dropTaxis.remove(medallion);
    }

    public void addTaxi(String medallion) {
        this.dropTaxis.add(medallion);
    }

    public Cell getCell() {
        return cell;
    }

    public int getNumOfEmptyTaxis() {
        return numOfEmptyTaxis;
    }

    public double getMidFare() {
        return midFare;
    }

    public double getProfitability() {
        return profitability;
    }

    public void setProfitability(double profitability) {
        this.profitability = profitability;
    }

    public void setMidFare(double midFare) {
        this.midFare = midFare;
    }

    public void setNumOfEmptyTaxis(int numOfEmptyTaxis) {
        this.numOfEmptyTaxis = numOfEmptyTaxis;
    }

    public void setNumOfFares(int numOfFares) {
        this.numOfFares = numOfFares;
    }

    public void setDropTaxis(Set<String> dropTaxis) {
        this.dropTaxis = dropTaxis;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
}
