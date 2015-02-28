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
public class OrderedList {

    public static final int HEAD_SIZE = 10;

    private Map<Object, ListNode> keyMap = new HashMap<Object, ListNode>(600);

    // head and tail of the doubly linked list. this doubly linked list keep a list sorted by
    // the compare function defined by the user.
    // always header position will be 0 and it increases towards the tail.
    // increment position refers to moving towards the tail and decrement position refers to moving towards the head.
    private ListNode tail;
    private ListNode head;

    /**
     * Adds a NodeValue to the map.
     *
     * @param key
     * @param value
     * @return - true if this node within first 10 nodes.
     */
    public void add(Object key, NodeValue value) {
        if (this.tail == null) {
            this.tail = new ListNode(0, value, key);
            this.head = this.tail;
            this.keyMap.put(key, this.tail);
        } else {
            ListNode listNode = new ListNode(this.tail.getPosition() + 1, value, key);
            listNode.setPreNode(this.tail);
            this.tail.setNextNode(listNode);
            this.keyMap.put(key, listNode);
            this.tail = listNode;
        }
    }

    /**
     * Removes event from map. This removes the event from the node list as well. When removing from the nodes list
     * first it has to move until the end and remove to make position of the list consistent
     *
     * @param key
     * @return - true if this node were in top 10 nodes
     */
    public NodeValue remove(Object key) {

        ListNode listNode = this.keyMap.remove(key);
        // increment positions of next set of nodes
        ListNode currentNode = listNode.getNextNode();
        while (currentNode != null) {
            // move the position one up.
            currentNode.decrementPosition();
            currentNode = currentNode.getNextNode();
        }

        if ((listNode != this.head) && (listNode != this.tail)) {
            listNode.getPreNode().setNextNode(listNode.getNextNode());
            listNode.getNextNode().setPreNode(listNode.getPreNode());
        }

        if (listNode == this.head) {
            this.head = listNode.getNextNode();
            if (this.head != null) {
                this.head.setPreNode(null);
            }

        }

        if (listNode == this.tail) {
            this.tail = listNode.getPreNode();
            if (this.tail != null) {
                this.tail.setNextNode(null);
            }
        }
        return listNode.getNodeValue();
    }

    public NodeValue get(Object key) {
        return this.keyMap.get(key).getNodeValue();
    }

    public boolean containsKey(Object key) {
        return this.keyMap.containsKey(key);
    }

    /**
     * Increment the position of node with this key.
     *
     * @param key
     * @return - true if position changed and it was within top 10
     */
    public void incrementPosition(Object key) {

        ListNode listNode = this.keyMap.get(key);
        if ((listNode == this.head) &&
                (listNode.getNextNode() != null) &&
                (listNode.compare(listNode.getNextNode()) == -1)) {
            this.head = listNode.getNextNode();

        }

        this.tail = listNode.incrementPosition(this.tail);
        this.head.setPreNode(null);

    }

    /**
     * Decrement the position of node with this key.
     *
     * @param key
     * @return - true if position get changed and it is within top 10
     */
    public void decrementPosition(Object key) {
        ListNode listNode = this.keyMap.get(key);
        if ((listNode == this.tail) &&
                (listNode.getPreNode() != null) &&
                (listNode.compare(listNode.getPreNode()) == 1)) {
            this.tail = listNode.getPreNode();
        }
        this.head = listNode.decrementPosition(this.head);
        this.tail.setNextNode(null);
    }

    public List<NodeValue> getTopValues() {
        List<NodeValue> nodeValues = new ArrayList<NodeValue>();
        ListNode currentNode = this.head;

        while ((currentNode != null) && (currentNode.getPosition() < HEAD_SIZE)) {
            nodeValues.add(currentNode.getNodeValue());
            currentNode = currentNode.getNextNode();
        }
        return nodeValues;
    }

    public int getPosition(Object key){
        return this.keyMap.get(key).getPosition();
    }

    public boolean isBelongs(NodeValue value){
        // if the tail is null or current position is less than 10 surely we can.
        if ((this.tail == null) || (tail.getPosition() < (HEAD_SIZE - 1))){
            return true;
        } else {
            // if this value is greater than the tail value then we need to insert this to this list a well.
            if (value.compare(this.tail.getNodeValue()) == 1){
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean checkListConsistency() {
        ListNode currentNode = this.head;
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

    public int getMapSize(){
        return this.keyMap.size();
    }

    public int getTailPosition(){
        if (this.tail == null){
            return 0;
        } else {
            return this.tail.getPosition();
        }
    }

    public Object getTailKey(){
        if (tail == null){
            return null;
        } else {
            return this.tail.getKey();
        }
    }



}
