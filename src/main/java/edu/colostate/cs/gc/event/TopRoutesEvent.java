package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.route.RouteCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/18/15
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopRoutesEvent extends Event {

    private long startTime;
    private long pickUpTime;
    private long dropOffTime;

    private Set<Route> removedRoutes;
    private List<NodeValue> newRoutes;

    public TopRoutesEvent(long startTime, long pickUpTime, long dropOffTime) {
        this.startTime = startTime;
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
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
