package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentEvent extends TripEvent {

    private String medallion;
    private double fare;
    private boolean isPayEvent;
    private String dropOffTime;
    private String pickUpTime;

    private long dropOffTimeMillis;

    private Cell pickUpCell;
    private Cell dropOffCell;

    private long startTime;

    public boolean isExpired(long lastEventTime, long windowSize) {
        return (lastEventTime - this.dropOffTimeMillis) > windowSize;
    }

    public void processDropOffTime(){
        this.dropOffTimeMillis = Util.getTime(this.dropOffTime);
    }

    @Override
    public Object getKey() {
        if (isPayEvent){
            return this.pickUpCell;
        } else {
            return this.dropOffCell;
        }
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.seqNo);
            dataOutput.writeUTF(this.medallion);
            dataOutput.writeDouble(this.fare);
            dataOutput.writeBoolean(this.isPayEvent);
            dataOutput.writeUTF(this.dropOffTime);
            dataOutput.writeUTF(this.pickUpTime);
            this.pickUpCell.serialize(dataOutput);
            this.dropOffCell.serialize(dataOutput);
            dataOutput.writeLong(this.startTime);

        } catch (IOException e) {
            throw new MessageProcessingException("Can not write data ");
        }
    }

    @Override
    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.seqNo = dataInput.readInt();
            this.medallion = dataInput.readUTF();
            this.fare = dataInput.readDouble();
            this.isPayEvent = dataInput.readBoolean();
            this.dropOffTime = dataInput.readUTF();
            this.pickUpTime = dataInput.readUTF();
            this.pickUpCell = new Cell();
            this.pickUpCell.parse(dataInput);
            this.dropOffCell = new Cell();
            this.dropOffCell.parse(dataInput);
            this.startTime = dataInput.readLong();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read data");
        }
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

    public String getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(String dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
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

    public long getDropOffTimeMillis() {
        return dropOffTimeMillis;
    }

    public void setDropOffTimeMillis(long dropOffTimeMillis) {
        this.dropOffTimeMillis = dropOffTimeMillis;
    }
}
