package edu.colostate.cs.gc.profit.median;

import edu.colostate.cs.gc.list.DynamicHeap;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/18/15
 * Time: 9:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicFieldMF extends MedianFinder {

    private DynamicHeap minHeap = new DynamicHeap(8);
    private DynamicHeap maxHeap = new DynamicHeap(8);

    // this value is equals to twice of the median field size
    private int medianCount;

    // position of the median field
    private int medianPosition;

    // this means median is in middle of two buckets. so real median will be the middle value.
    private boolean isInMiddle;

    public DynamicFieldMF() {
        this.median = 0;
        this.isInMiddle = true;
    }

    public void add(double value) {
        if (this.isInMiddle) {
            // in this case we don't care about the equality since the median is a fake value. and there is no
            // possibility that both are equal since we have unique buckets.
            if (value > this.median) {
                addToMaxHeap(value);
                // new median will be the minimum value of this map
                pullMedianFromMaxHeap();
            } else {
                addToMinHeap(value);
                pullMedianFromMinHeap();
            }
            this.isInMiddle = false;
        } else {
            // i.e there is a median class available.
            // when adding an element we assume it adds to end of the bucket. so need to remove from end as well.
            if (value == this.median) {
                this.medianCount = this.medianCount + 2;
                this.medianPosition++;
            } else if (value > this.median) {
                addToMaxHeap(value);
                // median position should advance by one.
                this.medianPosition++;
                if (this.medianPosition > this.medianCount) {
                    //i.e median should in between.
                    pushMedianToMinHeap();
                    this.isInMiddle = true;
                }
            } else {
                // i.e current value is less than median.
                addToMinHeap(value);
                this.medianPosition--;
                if (this.medianPosition < 2) {
                    // i.e median should be in between
                    pushMedianToMaxHeap();
                    this.isInMiddle = true;
                }
            }
        }

    }

    private void pushMedianToMinHeap() {
        this.minHeap.add(this.median, new MinHeapValue(this.medianCount / 2, this.median));
        MaxHeapValue maxHeapValue = (MaxHeapValue) this.maxHeap.getMaxValue();
        this.median = (this.median + maxHeapValue.getValue()) / 2;
    }

    private void pullMedianFromMaxHeap() {
        MaxHeapValue maxHeapValue = (MaxHeapValue) this.maxHeap.extractMax();
        this.median = maxHeapValue.getValue();
        this.medianCount = maxHeapValue.getCount() * 2;
        this.medianPosition = 2;
    }

    private void pullMedianFromMinHeap() {
        MinHeapValue minHeapValue = (MinHeapValue) this.minHeap.extractMax();
        this.median = minHeapValue.getValue();
        this.medianCount = minHeapValue.getCount() * 2;
        this.medianPosition = this.medianCount;
    }

    private void pushMedianToMaxHeap() {
        this.maxHeap.add(this.median, new MaxHeapValue(this.medianCount / 2, this.median));
        MinHeapValue minHeapValue = (MinHeapValue) this.minHeap.getMaxValue();
        this.median = (this.median + minHeapValue.getValue()) / 2;
    }

    public void remove(double value) {

        //we don't consider removing an element when it is non exists.
        if (this.isInMiddle) {
            // similar to adding we don't consider the case where equal since it can not exits.
            if (value > this.median) {
                removeFromMaxHeap(value);
                //pull the value from min Heap
                pullMedianFromMinHeap();
            } else {
                // i.e if value less than or equal
                removeFromMinHeap(value);
                pullMedianFromMaxHeap();
            }
            this.isInMiddle = false;
        } else {
            if (this.median == value) {
                if (this.medianCount == 2) {
                    // this is the only element.
                    // just remove this element i.e set the median from two heaps.
                    if (!this.maxHeap.isEmpty() && !this.minHeap.isEmpty()) {
                        MaxHeapValue maxHeapValue = (MaxHeapValue) this.maxHeap.getMaxValue();
                        MinHeapValue minHeapValue = (MinHeapValue) this.minHeap.getMaxValue();
                        this.median = (maxHeapValue.getValue() + minHeapValue.getValue()) / 2;
                    } else {
                        this.median = 0;
                    }
                    this.isInMiddle = true;

                } else {
                    this.medianCount = this.medianCount - 2;
                    this.medianPosition--;
                    if (this.medianPosition < 2) {
                        pushMedianToMaxHeap();
                        this.isInMiddle = true;
                    }

                    if (this.medianPosition > this.medianCount) {
                        pushMedianToMinHeap();
                        this.isInMiddle = true;
                    }
                }
            } else if (value > this.median) {
                removeFromMaxHeap(value);
                this.medianPosition--;
                if (this.medianPosition < 2) {
                    pushMedianToMaxHeap();
                    this.isInMiddle = true;
                }
            } else {
                // i.e value is less than the median
                removeFromMinHeap(value);
                this.medianPosition++;
                if (this.medianPosition > this.medianCount) {
                    pushMedianToMinHeap();
                    this.isInMiddle = true;
                }
            }
        }
    }

    private void removeFromMaxHeap(double value) {
        // this value should exits.
        MaxHeapValue maxHeapValue = (MaxHeapValue) this.maxHeap.get(value);
        maxHeapValue.decrementCount();
        if (maxHeapValue.getCount() == 0) {
            // i.e we need to remove this value
            this.maxHeap.remove(value);
        }
    }

    private void removeFromMinHeap(double value) {
        MinHeapValue minHeapValue = (MinHeapValue) this.minHeap.get(value);
        minHeapValue.decrementCount();
        if (minHeapValue.getCount() == 0) {
            // i.e we need to remove this value
            this.minHeap.remove(value);
        }
    }

    private void addToMaxHeap(double value) {
        if (this.maxHeap.containsKey(value)) {
            MaxHeapValue maxHeapValue = (MaxHeapValue) this.maxHeap.get(value);
            maxHeapValue.incrementCount();
        } else {
            this.maxHeap.add(value, new MaxHeapValue(1, value));
        }
    }

    private void addToMinHeap(double value) {
        if (this.minHeap.containsKey(value)) {
            MinHeapValue minHeapValue = (MinHeapValue) this.minHeap.get(value);
            minHeapValue.incrementCount();
        } else {
            this.minHeap.add(value, new MinHeapValue(1, value));
        }
    }


}
