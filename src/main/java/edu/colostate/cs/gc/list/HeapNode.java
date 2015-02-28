package edu.colostate.cs.gc.list;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class HeapNode {

    private int arrayPosition;
    private NodeValue nodeValue;
    private Object key;

    public HeapNode(NodeValue nodeValue, Object key) {
        this.nodeValue = nodeValue;
        this.key = key;
    }

    public int compare(HeapNode heapNode){
        return this.nodeValue.compare(heapNode.nodeValue);
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

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }
}
