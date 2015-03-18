package edu.colostate.cs.gc.profit.median;

import edu.colostate.cs.gc.list.NodeValue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/18/15
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaxHeapValue implements NodeValue {

    private int count;
    private double value;

    public MaxHeapValue(int count, double value) {
        this.count = count;
        this.value = value;
    }

    public int compare(NodeValue value) {
        MaxHeapValue newValue = (MaxHeapValue) value;
        if (this.value < newValue.value){
            return 1;
        } else if (this.value > newValue.value){
            return -1;
        } else {
            return 0;
        }

    }

    public void incrementCount(){
        this.count++;
    }

    public void decrementCount(){
        this.count--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Object getKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void update(NodeValue value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public NodeValue getClone() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
