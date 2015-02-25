package edu.colostate.cs.gc.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/18/15
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopRoutesEvent {

    private long pickUpTime;
    private long dropOffTime;

    private List<Route> routes = new ArrayList<Route>();

    public TopRoutesEvent(long pickUpTime, long dropOffTime) {
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
    }

    public void addRoute(Route route){
        this.routes.add(route);
    }

    @Override
    public String toString() {
        String result = this.pickUpTime + "," + this.dropOffTime + ",";
        for (Route route : this.routes){
            result += route.toString();
        }
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

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
