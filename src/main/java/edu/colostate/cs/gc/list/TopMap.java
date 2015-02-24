package edu.colostate.cs.gc.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopMap {

    public static final int HEAD_SIZE = 10;

    private Map<Object, TopListNode> keyMap = new HashMap<Object, TopListNode>(5000);
    private TopListNode tail;
    private TopListNode head;

    /**
     * Adds a NodeValue to the map.
     *
     * @param key
     * @param value
     * @return - true if this node within first 10 nodes.
     */
    public boolean add(Object key, NodeValue value) {
        boolean isChanged = false;
        if (this.tail == null) {
            this.tail = new TopListNode(0, value);
            this.head = this.tail;
            this.keyMap.put(key, this.tail);
            isChanged = true;
        } else {
            TopListNode topListNode = new TopListNode(this.tail.getPosition() + 1, value);
            topListNode.setPreNode(this.tail);
            this.tail.setNextNode(topListNode);
            this.keyMap.put(key, topListNode);
            this.tail = topListNode;
            if (this.tail.getPosition() < HEAD_SIZE) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    /**
     * Removes event from map. This removes the event from the node list as well. When removing from the nodes list
     * first it has to move until the end and remove to make position of the list consistent
     *
     * @param key
     * @return - true if this node were in top 10 nodes
     */
    public boolean remove(Object key) {

        TopListNode topListNode = this.keyMap.remove(key);
        int prePosition = topListNode.getPosition();
        // increment positions of next set of nodes
        TopListNode currentNode = topListNode.getNextNode();
        while (currentNode != null) {
            currentNode.decrementPosition();
            currentNode = currentNode.getNextNode();
        }

        if ((topListNode != this.head) && (topListNode != this.tail)) {
            topListNode.getPreNode().setNextNode(topListNode.getNextNode());
            topListNode.getNextNode().setPreNode(topListNode.getPreNode());
        }

        if (topListNode == this.head) {
            this.head = topListNode.getNextNode();
            if (this.head != null) {
                this.head.setPreNode(null);
            }

        }

        if (topListNode == this.tail) {
            this.tail = topListNode.getPreNode();
            if (this.tail != null) {
                this.tail.setNextNode(null);
            }
        }

        return prePosition < HEAD_SIZE;

    }

    public NodeValue get(Object key) {
        return this.keyMap.get(key).getNodeValue();
    }

    public boolean containsKey(Object key) {
        return this.keyMap.containsKey(key);
    }

    /**
     * Decrement the position of node with this key.
     *
     * @param key
     * @return - true if position changed and it was within top 10
     */
    public boolean decrementPosition(Object key) {

        TopListNode topListNode = this.keyMap.get(key);
        int prePosition = topListNode.getPosition();
        if ((topListNode == this.head) &&
                (topListNode.getNextNode() != null) &&
                (topListNode.compare(topListNode.getNextNode()) == -1)) {
            this.head = topListNode.getNextNode();

        }

        this.tail = topListNode.decrementPosition(this.tail);
        this.head.setPreNode(null);
        int currentPosition = topListNode.getPosition();
        return (prePosition != currentPosition) && (prePosition < HEAD_SIZE);

    }

    /**
     * Increment the position of node with this key.
     *
     * @param key
     * @return - true if position get changed and it is within top 10
     */
    public boolean incrementPosition(Object key) {
        TopListNode topListNode = this.keyMap.get(key);
        int prePosition = topListNode.getPosition();

        if ((topListNode == this.tail) &&
                (topListNode.getPreNode() != null) &&
                (topListNode.compare(topListNode.getPreNode()) == 1)) {
            this.tail = topListNode.getPreNode();
        }

        this.head = topListNode.incrementPosition(this.head);
        this.tail.setNextNode(null);
        int currentPosition = topListNode.getPosition();
        return (prePosition != currentPosition) && (currentPosition < HEAD_SIZE);
    }

    public List<NodeValue> getTopValues() {
        List<NodeValue> nodeValues = new ArrayList<NodeValue>();
        TopListNode currentNode = this.head;

        while ((currentNode != null) && (currentNode.getPosition() < HEAD_SIZE)) {
            nodeValues.add(currentNode.getNodeValue());
            currentNode = currentNode.getNextNode();
        }
        return nodeValues;
    }

    public boolean checkListConsistency() {
        TopListNode currentNode = this.head;
        int currentPosition = 0;
        boolean isCorrect = true;
        while (currentNode != null) {
            if ((currentNode.getPosition() != currentPosition) || currentNode.compare(currentNode.getNextNode()) == -1) {
                isCorrect = false;
            }
            if ((currentNode.getNextNode() != null) && (currentNode.getNextNode().getPreNode() != currentNode)) {
                isCorrect = false;
            }
            currentNode = currentNode.getNextNode();
            currentPosition++;
        }

        currentPosition--;
        currentNode = this.tail;
        while (currentNode != null) {
            if (currentNode.getPosition() != currentPosition) {
                System.out.println("Error at position " + currentPosition);
                isCorrect = false;
            }
            currentNode = currentNode.getNextNode();
            currentPosition--;
        }
        return isCorrect;
    }

    public void displayDetails(){
        System.out.println("key count ==> " + this.keyMap.size());
        if (!checkListConsistency()){
            System.out.println("An error");
        }
        System.out.println("Tail position ==> " + this.tail.getPosition());
    }

}
