package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentEvent {

    private String medallion;
    private double fare;
    private boolean isPayEvent;
    private long dropOffTime;
    private long pickUpTime;

    private Cell pickUpCell;
    private Cell dropOffCell;

    private long startTime;

    public boolean isExpired(long lastEventTime, long windowSize) {
        return (lastEventTime - this.dropOffTime) > windowSize;
    }

    public String getMedallion() {
        return medallion;
    }

    public void setMedallion(String medallion) {
        this.medallion = medallion;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public boolean isPayEvent() {
        return isPayEvent;
    }

    public void setPayEvent(boolean payEvent) {
        isPayEvent = payEvent;
    }

    public long getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(long dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public long getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(long pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Cell getPickUpCell() {
        return pickUpCell;
    }

    public void setPickUpCell(Cell pickUpCell) {
        this.pickUpCell = pickUpCell;
    }

    public Cell getDropOffCell() {
        return dropOffCell;
    }

    public void setDropOffCell(Cell dropOffCell) {
        this.dropOffCell = dropOffCell;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
