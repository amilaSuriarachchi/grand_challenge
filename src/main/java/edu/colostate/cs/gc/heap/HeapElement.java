package edu.colostate.cs.gc.heap;

import edu.colostate.cs.gc.list.NodeValue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class HeapElement {

    private int arrayPosition;
    private NodeValue nodeValue;

    public HeapElement(NodeValue nodeValue) {
        this.nodeValue = nodeValue;
    }

    public int compare(HeapElement heapElement){
        return this.nodeValue.compare(heapElement.nodeValue);
    }

    public int getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(int arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public NodeValue getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(NodeValue nodeValue) {
        this.nodeValue = nodeValue;
    }
}
