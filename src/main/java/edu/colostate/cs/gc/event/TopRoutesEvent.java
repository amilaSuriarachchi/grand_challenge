package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.route.RouteCount;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/18/15
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopRoutesEvent extends TripEvent {

    private long pickUpTime;
    private long dropOffTime;
    private long startTime;

    private Set<Route> removedRoutes;
    private List<NodeValue> newRoutes;

    public TopRoutesEvent() {
    }

    public TopRoutesEvent(long startTime, long pickUpTime, long dropOffTime) {
        this.startTime = startTime;
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
    }

    @Override
    public Object getKey() {
        return new Long(this.dropOffTime);
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeLong(this.pickUpTime);
            dataOutput.writeLong(this.dropOffTime);
            dataOutput.writeLong(this.startTime);

            dataOutput.writeInt(this.removedRoutes.size());
            for (Route route : this.removedRoutes) {
                route.serialize(dataOutput);
            }
            dataOutput.writeInt(this.newRoutes.size());
            for (NodeValue nodeValue : this.newRoutes) {
                RouteCount routeCount = (RouteCount) nodeValue;
                routeCount.serialize(dataOutput);
            }

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

            int length = dataInput.readInt();
            this.removedRoutes = new HashSet<Route>();
            for (int i = 0; i < length; i++) {
                Route route = new Route();
                route.parse(dataInput);
                this.removedRoutes.add(route);
            }

            length = dataInput.readInt();
            this.newRoutes = new ArrayList<NodeValue>();
            for (int i = 0; i < length; i++) {
                RouteCount routeCount = new RouteCount();
                routeCount.parse(dataInput);
                this.newRoutes.add(routeCount);
            }

        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopRoutesEvent that = (TopRoutesEvent) o;

        if (dropOffTime != that.dropOffTime) return false;
        if (pickUpTime != that.pickUpTime) return false;
        if (startTime != that.startTime) return false;
        if (!newRoutes.equals(that.newRoutes)) return false;
        if (!removedRoutes.equals(that.removedRoutes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (pickUpTime ^ (pickUpTime >>> 32));
        result = 31 * result + (int) (dropOffTime ^ (dropOffTime >>> 32));
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + removedRoutes.hashCode();
        result = 31 * result + newRoutes.hashCode();
        return result;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Set<Route> getRemovedRoutes() {
        return removedRoutes;
    }

    public void setRemovedRoutes(Set<Route> removedRoutes) {
        this.removedRoutes = removedRoutes;
    }

    public List<NodeValue> getNewRoutes() {
        return newRoutes;
    }

    public void setNewRoutes(List<NodeValue> newRoutes) {
        this.newRoutes = newRoutes;
    }
}
