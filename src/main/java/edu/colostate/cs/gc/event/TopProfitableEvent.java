package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.list.NodeValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/28/15
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopProfitableEvent extends Event {

    private long startTime;
    private long pickTime;
    private long dropTime;
    private List<NodeValue> profitCells;

    public TopProfitableEvent(long startTime, long pickTime, long dropTime, List<NodeValue> profitCells) {
        this.startTime = startTime;
        this.pickTime = pickTime;
        this.dropTime = dropTime;
        this.profitCells = profitCells;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getPickTime() {
        return pickTime;
    }

    public void setPickTime(long pickTime) {
        this.pickTime = pickTime;
    }

    public long getDropTime() {
        return dropTime;
    }

    public void setDropTime(long dropTime) {
        this.dropTime = dropTime;
    }

    public List<NodeValue> getProfitCells() {
        return profitCells;
    }

    public void setProfitCells(List<NodeValue> profitCells) {
        this.profitCells = profitCells;
    }
}
