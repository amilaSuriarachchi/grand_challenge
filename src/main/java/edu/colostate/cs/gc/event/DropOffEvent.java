package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropOffEvent extends TripEvent {

    private String pickUpTime;
    private String dropOffTime;
    private long startTime;
    private long dropOffTimeMillis;

    private int seqNo;

    private Route route;

    public boolean isExpired(long lastEventTime) {
        return lastEventTime - this.dropOffTimeMillis > Constants.LARGE_WINDOW_SIZE;
    }

    @Override
    public Object getKey() {
        return this.route;
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeUTF(this.pickUpTime);
            dataOutput.writeUTF(this.dropOffTime);
            dataOutput.writeLong(this.startTime);
            dataOutput.writeInt(this.seqNo);
            this.route.serialize(dataOutput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }

    @Override
    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.pickUpTime = dataInput.readUTF();
            this.dropOffTime = dataInput.readUTF();
            this.startTime = dataInput.readLong();
            this.seqNo = dataInput.readInt();
            this.route = new Route();
            this.route.parse(dataInput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }

    public void processDropOffTime(){
        this.dropOffTimeMillis = Util.getTime(this.dropOffTime);
    }

    public long getDropOffTimeMillis() {
        return dropOffTimeMillis;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(String dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
}
