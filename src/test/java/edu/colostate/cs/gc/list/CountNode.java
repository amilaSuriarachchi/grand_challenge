package edu.colostate.cs.gc.list;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/23/15
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountNode implements NodeValue {

    private int value;

    public CountNode(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isBefore(NodeValue value) {
        CountNode countNode = (CountNode) value;
        return this.value > countNode.value;
    }

    @Override
    public String toString() {
        return this.value + ",";
    }
}
