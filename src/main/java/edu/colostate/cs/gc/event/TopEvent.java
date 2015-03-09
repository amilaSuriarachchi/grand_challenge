package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.list.NodeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TopEvent extends TripEvent {

    protected String pickUpTime;
    protected long dropOffTime;
    protected long startTime;

    protected int processorID;

    protected Set removedKeys;
    protected List<NodeValue> newValueList;

    public TopEvent() {
        this.newValueList = new ArrayList<NodeValue>();
    }

    public TopEvent(long startTime, String pickUpTime, long dropOffTime) {
        this();
        this.startTime = startTime;
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
    }

    public void addNodeValue(NodeValue nodeValue) {
        this.newValueList.add(nodeValue);
    }

    public Set getRemovedKeys() {
        return removedKeys;
    }

    public void setRemovedKeys(Set removedKeys) {
        this.removedKeys = removedKeys;
    }

    public List<NodeValue> getNewValueList() {
        return newValueList;
    }

    public void setNewValueList(List<NodeValue> newValueList) {
        this.newValueList = newValueList;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public long getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(long dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getProcessorID() {
        return processorID;
    }

    public void setProcessorID(int processorID) {
        this.processorID = processorID;
    }
}
