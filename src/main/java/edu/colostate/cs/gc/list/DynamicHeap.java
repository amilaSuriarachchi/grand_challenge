package edu.colostate.cs.gc.list;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicHeap {

    private HeapNode[] heapNodes = new HeapNode[20000];
    private Map<Object, HeapNode> keyMap = new HashMap<Object, HeapNode>();

    private int heapSize = 0;

    public void add(Object key, NodeValue value) {
        HeapNode heapNode = new HeapNode(value, key);
        this.keyMap.put(key, heapNode);
        this.heapNodes[this.heapSize] = heapNode;
        this.heapNodes[this.heapSize].setArrayPosition(this.heapSize);
        this.heapSize++;
        moveUp(heapNode);

    }

    public NodeValue remove(Object key) {

        HeapNode heapNode = this.keyMap.remove(key);
        int currentPosition = heapNode.getArrayPosition();
        //if the current position is the last there is nothing to do other than decreasing the size.
        if (currentPosition != this.heapSize - 1) {
            this.heapNodes[currentPosition] = this.heapNodes[this.heapSize - 1];
            this.heapNodes[currentPosition].setArrayPosition(currentPosition);
            this.heapSize--;
            if (heapNode.compare(this.heapNodes[currentPosition]) == 1) {
                moveDown(this.heapNodes[currentPosition]);
            } else {
                moveUp(this.heapNodes[currentPosition]);
            }
        } else {
            this.heapSize--;
        }

        return heapNode.getNodeValue();
    }

    public Object getMaxKey() {
        if (this.heapSize == 0) {
            return null;
        } else {
            return this.heapNodes[0].getKey();
        }
    }

    public NodeValue extractMax() {
        if (this.heapSize == 0) {
            return null;
        }
        HeapNode maxElement = this.heapNodes[0];
        this.heapNodes[0] = this.heapNodes[this.heapSize - 1];
        this.heapNodes[0].setArrayPosition(0);
        this.heapSize--;
        moveDown(this.heapNodes[0]);
        return maxElement.getNodeValue();
    }

    public void moveUp(Object key) {
        HeapNode heapNode = this.keyMap.get(key);
        moveUp(heapNode);
    }

    private void moveUp(HeapNode heapNode) {
        // if current position is zero it is already parent.
        int currentPosition = heapNode.getArrayPosition();
        if (currentPosition != 0) {
            int parent = getParent(currentPosition);
            // check this element is greater than parent.
            if (heapNode.compare(this.heapNodes[parent]) == 1) {
                // swap two elements
                this.heapNodes[currentPosition] = this.heapNodes[parent];
                this.heapNodes[currentPosition].setArrayPosition(currentPosition);

                this.heapNodes[parent] = heapNode;
                this.heapNodes[parent].setArrayPosition(parent);

                moveUp(heapNode);
            }
        }
    }


    public void moveDown(Object key) {
        HeapNode heapNode = this.keyMap.get(key);
        moveDown(heapNode);
    }

    private void moveDown(HeapNode heapNode) {
        int currentPosition = heapNode.getArrayPosition();
        int left = getLeft(currentPosition);
        int right = getRight(currentPosition);

        int largest = 0;
        if ((left < this.heapSize) && (this.heapNodes[left].compare(heapNode) == 1)) {
            largest = left;
        } else {
            largest = currentPosition;
        }

        if ((right < this.heapSize) && (this.heapNodes[right].compare(this.heapNodes[largest]) == 1)) {
            largest = right;
        }

        if (largest != currentPosition) {
            // exchange elements
            this.heapNodes[currentPosition] = this.heapNodes[largest];
            this.heapNodes[currentPosition].setArrayPosition(currentPosition);

            this.heapNodes[largest] = heapNode;
            this.heapNodes[largest].setArrayPosition(largest);

            moveDown(heapNode);
        }
    }

    public boolean containsKey(Object key) {
        return this.keyMap.containsKey(key);
    }

    public boolean checkHeap() {
        return checkHeap(0);
    }

    public NodeValue get(Object key) {
        return this.keyMap.get(key).getNodeValue();
    }

    private boolean checkHeap(int i) {
        int right = getRight(i);
        int left = getLeft(i);
        if (right < this.heapSize) {
            if (this.heapNodes[i].compare(this.heapNodes[right]) == -1) {
                return false;
            } else {
                return checkHeap(right);
            }

        }

        if (left < this.heapSize) {
            if (this.heapNodes[i].compare(this.heapNodes[left]) == -1) {
                return false;
            } else {
                return checkHeap(left);
            }
        }

        return true;
    }

    private int getParent(int i) {
        return (i + 1) / 2 - 1;
    }

    private int getLeft(int i) {
        return (i + 1) * 2 - 1;
    }

    private int getRight(int i) {
        return (i + 1) * 2;
    }

}