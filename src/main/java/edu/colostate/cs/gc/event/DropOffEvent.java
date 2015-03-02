package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.util.Constants;
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

    private long pickUpTime;
    private long dropOffTime;
    private long startTime;

    private Route route;

    public boolean isExpired(long lastEventTime) {
        return lastEventTime - this.dropOffTime > Constants.LARGE_WINDOW_SIZE;
    }

    @Override
    public Object getKey() {
        return this.route;
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeLong(this.pickUpTime);
            dataOutput.writeLong(this.dropOffTime);
            dataOutput.writeLong(this.startTime);
            this.route.serialize(dataOutput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }

    @Override
    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.pickUpTime = dataInput.readLong();
            this.dropOffTime = dataInput.readLong();
            this.startTime = dataInput.readLong();
            this.route = new Route();
            this.route.parse(dataInput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }

    public long getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(long pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public long getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(long dropOffTime) {
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
}
