package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.route.RouteCount;
import edu.colostate.cs.gc.route.TopRouteCount;
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

    private String pickUpTime;
    private long dropOffTime;
    private long startTime;

    private Set<Route> removedRoutes;
    private List<TopRouteCount> newRoutes;

    public TopRoutesEvent() {
        this.newRoutes = new ArrayList<TopRouteCount>();
    }

    public TopRoutesEvent(long startTime, String pickUpTime, long dropOffTime) {
        this();
        this.startTime = startTime;
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
    }

    public void addRouteCount(TopRouteCount topRouteCount){
        this.newRoutes.add(topRouteCount);
    }

    @Override
    public Object getKey() {
        return new Long(this.dropOffTime);
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeUTF(this.pickUpTime);
            dataOutput.writeLong(this.dropOffTime);
            dataOutput.writeLong(this.startTime);

            dataOutput.writeInt(this.removedRoutes.size());
            for (Route route : this.removedRoutes) {
                route.serialize(dataOutput);
            }
            dataOutput.writeInt(this.newRoutes.size());
            for (TopRouteCount routeCount : this.newRoutes) {
                routeCount.serialize(dataOutput);
            }

        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }

    @Override
    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.pickUpTime = dataInput.readUTF();
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
            for (int i = 0; i < length; i++) {
                TopRouteCount routeCount = new TopRouteCount();
                routeCount.parse(dataInput);
                this.newRoutes.add(routeCount);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
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

    public Set<Route> getRemovedRoutes() {
        return removedRoutes;
    }

    public void setRemovedRoutes(Set<Route> removedRoutes) {
        this.removedRoutes = removedRoutes;
    }

    public List<TopRouteCount> getNewRoutes() {
        return newRoutes;
    }

    public void setNewRoutes(List<TopRouteCount> newRoutes) {
        this.newRoutes = newRoutes;
    }
}
