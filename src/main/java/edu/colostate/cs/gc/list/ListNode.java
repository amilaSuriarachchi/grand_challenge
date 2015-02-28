package edu.colostate.cs.gc.list;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListNode {

    private int position;
    private NodeValue nodeValue;
    private Object key;

    private ListNode nextNode;
    private ListNode preNode;

    public ListNode(int position, NodeValue nodeValue, Object key) {
        this.position = position;
        this.nodeValue = nodeValue;
        this.key = key;
    }

    public ListNode decrementPosition(ListNode head) {

        while ((this.preNode != null) && (this.compare(this.preNode) == 1)) {
            //swap two nodes.
            ListNode currentPreNode = this.preNode;
            ListNode currentNextNode = this.nextNode;

            if (currentNextNode != null) {
                currentNextNode.preNode = currentPreNode;
            }

            if ((currentPreNode != null) && (currentPreNode.preNode != null)) {
                currentPreNode.preNode.nextNode = this;
            }

            this.preNode = currentPreNode.preNode;
            this.nextNode = currentPreNode;

            currentPreNode.nextNode = currentNextNode;
            currentPreNode.preNode = this;

            decrementPosition();
            this.nextNode.incrementPosition();

        }

        if (this.preNode == null) {
            return this;
        } else {
            return head;
        }
    }


    public ListNode incrementPosition(ListNode tail) {
        while ((this.nextNode != null) && (this.compare(this.nextNode) == -1)) {

            ListNode currentNext = this.nextNode;
            ListNode currentPre = this.preNode;

            if (currentPre != null) {
                currentPre.nextNode = currentNext;
            }

            if ((currentNext != null) && (currentNext.nextNode != null)) {
                currentNext.nextNode.preNode = this;
            }

            this.nextNode = currentNext.nextNode;
            this.preNode = currentNext;

            currentNext.preNode = currentPre;
            currentNext.nextNode = this;

            incrementPosition();
            currentNext.decrementPosition();
        }

        if (this.nextNode == null) {
            return this;
        } else {
            return tail;
        }
    }

    public int compare(ListNode node) {
        if (node == null){
            return 1;
        } else {
            return this.nodeValue.compare(node.nodeValue);
        }
    }

    public Object getKey(){
        return key;
    }

    public void incrementPosition() {
        this.position++;
    }

    public void decrementPosition() {
        this.position--;
    }

    public int getPosition() {
        return position;
    }

    public ListNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(ListNode nextNode) {
        this.nextNode = nextNode;
    }

    public ListNode getPreNode() {
        return preNode;
    }

    public void setPreNode(ListNode preNode) {
        this.preNode = preNode;
    }

    public NodeValue getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(NodeValue nodeValue) {
        this.nodeValue = nodeValue;
    }
}
