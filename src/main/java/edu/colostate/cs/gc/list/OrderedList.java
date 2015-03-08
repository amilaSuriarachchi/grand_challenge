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

    public static final int MAX_NODES = 10;

    private Map<Object, ListNode> keyMap = new HashMap<Object, ListNode>(600);

    // head and tail of the doubly linked list. this doubly linked list keep a list sorted by
    // the compare function defined by the user.
    // always header position will be 0 and it increases towards the tail.
    // increment position refers to moving towards the tail and decrement position refers to moving towards the head.
    private ListNode tail;
    private ListNode head;

    //use a node node count instead of the position at each place to efficient element removal.
    private int count = 0;

    /**
     * Adds a NodeValue to the map.
     *
     * @param key
     * @param value
     * @return - true if this node within first 10 nodes.
     */
    public void add(Object key, NodeValue value) {
        if (this.tail == null) {
            this.tail = new ListNode(value, key);
            this.head = this.tail;
            this.keyMap.put(key, this.tail);
        } else {
            ListNode listNode = new ListNode(value, key);
            listNode.setPreNode(this.tail);
            this.tail.setNextNode(listNode);
            this.keyMap.put(key, listNode);
            this.tail = listNode;
        }
        this.count++;
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

        this.count--;
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

        // this list will keep only maximum of ten values.
        while (currentNode != null) {
            nodeValues.add(currentNode.getNodeValue().getClone());
            currentNode = currentNode.getNextNode();
        }
        return nodeValues;
    }

    /**
     * this method only for testing purposes.
     * @param key
     * @return
     */
    public int getPosition(Object key){
        // this method assumes this key is there. i.e there is at least one object
        ListNode listNode = this.keyMap.get(key);
        int position = 0;
        ListNode currentNode = this.head;
        while (!currentNode.equals(listNode)){
            position++;
            currentNode = currentNode.getNextNode();

        }
        return position;
    }

    public boolean isBelongs(NodeValue value){
        // if the tail is null or current position is less than 10 surely we can.
        if (this.count < MAX_NODES){
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
        boolean isCorrect = true;
        while (currentNode != null) {
            if (currentNode.compare(currentNode.getNextNode()) == -1) {
                isCorrect = false;
            }
            if ((currentNode.getNextNode() != null) && (currentNode.getNextNode().getPreNode() != currentNode)) {
                isCorrect = false;
            }
            currentNode = currentNode.getNextNode();
        }

        currentNode = this.tail;
        while (currentNode != null) {
            if ((currentNode.getPreNode() != null) && (currentNode.compare(currentNode.getPreNode()) == 1)) {
                isCorrect = false;
            }
            currentNode = currentNode.getNextNode();
        }
        return isCorrect;
    }

    public void displayDetails(){
        System.out.println("key count ==> " + this.keyMap.size());
        if (!checkListConsistency()){
            System.out.println("An error");
        }
        System.out.println("Tail position ==> " + (this.count - 1));
    }

    public int getMapSize(){
        return this.keyMap.size();
    }

    public int getTailPosition(){
        if (this.tail == null){
            return 0;
        } else {
            return this.count - 1;
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
