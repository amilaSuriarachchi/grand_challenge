package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropOffEvent {

    private long pickUpTime;
    private long dropOffTime;
    private int tripTime;
    private int tripDistance;

    private Route route;

    public boolean isExpired(long lastEventTime) {
        return lastEventTime - this.dropOffTime > Constants.WINDOW_SIZE;
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

    public int getTripTime() {
        return tripTime;
    }

    public void setTripTime(int tripTime) {
        this.tripTime = tripTime;
    }

    public int getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(int tripDistance) {
        this.tripDistance = tripDistance;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
