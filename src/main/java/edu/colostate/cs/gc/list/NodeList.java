package edu.colostate.cs.gc.list;

import java.util.List;

/**
 * this class gives an illusion of a list to the higher level classes.
 * Users can add nodes, change values and move and remove nodes from this list for key value pairs
 * and always queries the top 10 nodes
 * according to a compare function they have defined. All operations takes log(n) time.
 */
public class NodeList {

    private OrderedList list;
    private DynamicHeap heap;

    public NodeList(int capacity) {
        this.heap = new DynamicHeap(capacity);
        this.list = new OrderedList();
    }

    public boolean containsKey(Object key) {
        return this.heap.containsKey(key) || this.list.containsKey(key);
    }

    public NodeValue get(Object key) {
        if (this.heap.containsKey(key)) {
            return this.heap.get(key);
        } else {
            return this.list.get(key);
        }
    }

    public void add(Object key, NodeValue value) {
        if (this.list.isBelongs(value)) {
            this.list.add(key, value);
            this.list.decrementPosition(key);
        } else {
            this.heap.add(key, value);
        }
        // if this value greater than the tail of the list and originally list was full
        // then we need to move the tail to heap.
        moveTailToHeap();
    }

    private void moveTailToHeap() {
        // if the tail position is 10 i.e this list is over sized.
        if (this.list.getTailPosition() >= OrderedList.MAX_NODES) {
            Object key = this.list.getTailKey();
            NodeValue nodeValue = this.list.remove(key);
            this.heap.add(key, nodeValue);
        }
    }

    public void remove(Object key) {

        if (this.heap.containsKey(key)) {
            this.heap.remove(key);
        } else {
            this.list.remove(key);
            // if there elements at heap now we need to get it and put the list
            Object maxKey = this.heap.getMaxKey();
            if (maxKey != null) {
                NodeValue heapMax = this.heap.remove(maxKey);
                this.list.add(maxKey, heapMax);
                // we don't need to decrement since value from the heap will be the smallest
            }
        }
    }

    /**
     * user of this class ask to decrease the position. i.e the value of this node has increased.
     * this means we may need to decrement position in list or move up in heap
     *
     * @param key
     */
    public void decrementPosition(Object key) {
        if (this.heap.containsKey(key)) {
            // if there is an object in the heap list must be full and hence tail can not be null.
            Object tailKey = this.list.getTailKey();
            NodeValue tailNode = this.list.get(tailKey);
            NodeValue node = this.heap.get(key);
            if (node.compare(tailNode) == 1) {
                // i.e current node is greater than the tail.
                // so remove the tail and put that to heap.
                // remove this node and put that to list.
                NodeValue listNode = this.list.remove(tailKey);
                this.heap.add(tailKey, listNode);
                NodeValue nodeValue = this.heap.remove(key);
                this.list.add(key, nodeValue);
                // this value may have increased beyond tail
                this.list.decrementPosition(key);
            } else {
                // this node won't go to to 10. just update the heap
                this.heap.moveUp(key);
            }
        } else {
            //decrementing a value in list won't change global setting.
            this.list.decrementPosition(key);
        }

    }

    /**
     * user of this class ask to increase the position. i.e the value of this node has decreased.
     * this means we need to increment position in the list or move down in the heap.
     *
     * @param key
     */
    public void incrementPosition(Object key) {
        if (this.heap.containsKey(key)) {
            this.heap.moveDown(key);
        } else {
            Object maxHeapKey = this.heap.getMaxKey();
            if (maxHeapKey != null) {
                NodeValue maxHeapNode = this.heap.get(maxHeapKey);
                NodeValue node = this.list.get(key);
                if (maxHeapNode.compare(node) == 1) {
                    // i.e current max heap is greater than this node.
                    NodeValue heapNode = this.heap.remove(maxHeapKey);
                    NodeValue nodeValue = this.list.remove(key);
                    this.heap.add(key, nodeValue);
                    this.list.add(maxHeapKey, heapNode);
                    // this node should be less than existing values. so no need to decrement.
                } else {
                    this.list.incrementPosition(key);
                }
            } else {
                // if no elements in the heap just increment the list element
                this.list.incrementPosition(key);
            }

        }
    }

    public boolean checkConsistency() {
        if (!this.list.checkListConsistency()) {
            return false;
        }

        if (!this.heap.checkHeap()) {
            return false;
        }

        Object tailKey = this.list.getTailKey();
        if (tailKey != null) {
            Object heapKey = this.heap.getMaxKey();
            if (heapKey != null) {
                return (this.list.get(tailKey).compare(this.heap.get(heapKey)) != -1);
            }
        }
        return true;
    }

    public List<NodeValue> getTopValues() {
        return this.list.getTopValues();
    }
}
