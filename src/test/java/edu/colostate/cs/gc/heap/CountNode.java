package edu.colostate.cs.gc.heap;

import edu.colostate.cs.gc.list.NodeValue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountNode implements NodeValue {

    private int value;

    public CountNode(int value) {
        this.value = value;
    }

    public int compare(NodeValue value) {
        CountNode newNode = (CountNode) value;
        if (this.value > newNode.value){
            return 1;
        } else if (this.value < newNode.value){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
