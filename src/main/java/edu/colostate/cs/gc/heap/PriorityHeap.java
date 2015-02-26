package edu.colostate.cs.gc.heap;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityHeap {

    private HeapElement[] heapElements = new HeapElement[20000];

    private int heapSize = 0;

    public void add(HeapElement heapElement) {
        this.heapElements[this.heapSize] = heapElement;
        this.heapElements[this.heapSize].setArrayPosition(this.heapSize);
        this.heapSize++;
        moveUp(heapElement);

    }

    public void remove(HeapElement heapElement) {
        int currentPosition = heapElement.getArrayPosition();
        this.heapElements[currentPosition] = this.heapElements[this.heapSize - 1];
        this.heapElements[currentPosition].setArrayPosition(currentPosition);
        this.heapSize--;
        if (heapElement.compare(this.heapElements[currentPosition]) == 1) {
            moveDown(this.heapElements[currentPosition]);
        } else {
            moveUp(this.heapElements[currentPosition]);
        }

    }

    public HeapElement extractMax() {
        if (this.heapSize == 0) {
            return null;
        }
        HeapElement maxElement = this.heapElements[0];
        this.heapElements[0] = this.heapElements[this.heapSize - 1];
        this.heapElements[0].setArrayPosition(0);
        this.heapSize--;
        moveDown(this.heapElements[0]);
        return maxElement;
    }

    public void moveUp(HeapElement heapElement) {
        // if current position is zero it is already parent.
        int currentPosition = heapElement.getArrayPosition();
        if (currentPosition != 0) {
            int parent = getParent(currentPosition);
            // check this element is greater than parent.
            if (heapElement.compare(this.heapElements[parent]) == 1) {
                // swap two elements
                this.heapElements[currentPosition] = this.heapElements[parent];
                this.heapElements[currentPosition].setArrayPosition(currentPosition);

                this.heapElements[parent] = heapElement;
                this.heapElements[parent].setArrayPosition(parent);

                moveUp(heapElement);
            }
        }
    }


    public void moveDown(HeapElement heapElement) {

        int currentPosition = heapElement.getArrayPosition();
        int left = getLeft(currentPosition);
        int right = getRight(currentPosition);

        int largest = 0;
        if ((left < this.heapSize) && (this.heapElements[left].compare(heapElement) == 1)) {
            largest = left;
        } else {
            largest = currentPosition;
        }

        if ((right < this.heapSize) && (this.heapElements[right].compare(this.heapElements[largest]) == 1)) {
            largest = right;
        }

        if (largest != currentPosition) {
            // exchange elements
            this.heapElements[currentPosition] = this.heapElements[largest];
            this.heapElements[currentPosition].setArrayPosition(currentPosition);

            this.heapElements[largest] = heapElement;
            this.heapElements[largest].setArrayPosition(largest);

            moveDown(heapElement);
        }
    }

    public boolean checkHeap(){
        return checkHeap(0);
    }

    private boolean checkHeap(int i) {
        int right = getRight(i);
        int left = getLeft(i);
        if (right < this.heapSize){
            if (this.heapElements[i].compare(this.heapElements[right]) == -1){
                return false;
            } else {
                return checkHeap(right);
            }

        }

        if (left < this.heapSize){
            if (this.heapElements[i].compare(this.heapElements[left]) == -1){
                return false;
            } else {
                return checkHeap(left);
            }
        }

        return true;
    }

    private int getParent(int i) {
        return (i + 1) / 2 -1;
    }

    private int getLeft(int i) {
        return (i + 1) * 2 - 1;
    }

    private int getRight(int i) {
        return (i + 1) * 2;
    }

}
