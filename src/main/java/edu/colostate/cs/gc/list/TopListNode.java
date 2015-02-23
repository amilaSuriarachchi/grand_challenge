package edu.colostate.cs.gc.list;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopListNode {

    private int position;
    private NodeValue nodeValue;

    private TopListNode nextNode;
    private TopListNode preNode;

    public TopListNode(int position, NodeValue nodeValue) {
        this.position = position;
        this.nodeValue = nodeValue;
    }

    public TopListNode incrementPosition(TopListNode head) {

        while ((this.preNode != null) && (this.isBefore(this.preNode))) {
            //swap two nodes.
            TopListNode currentPreNode = this.preNode;
            TopListNode currentNextNode = this.nextNode;

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


    public TopListNode decrementPosition(TopListNode tail) {
        while ((this.nextNode != null) && (this.nextNode.isBefore(this))) {

            TopListNode currentNext = this.nextNode;
            TopListNode currentPre = this.preNode;

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

    public boolean isBefore(TopListNode topListNode) {
        return (topListNode == null) || this.nodeValue.isBefore(topListNode.nodeValue);
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

    public TopListNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(TopListNode nextNode) {
        this.nextNode = nextNode;
    }

    public TopListNode getPreNode() {
        return preNode;
    }

    public void setPreNode(TopListNode preNode) {
        this.preNode = preNode;
    }

    public NodeValue getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(NodeValue nodeValue) {
        this.nodeValue = nodeValue;
    }
}
