package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.list.NodeValue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/23/15
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCount implements NodeValue {

    private int count;
    private Route route;

    public RouteCount(int count, Route route) {
        this.count = count;
        this.route = route;
    }

    public void incrementCount(){
        this.count++;
    }

    public void decrementCount(){
        this.count--;
    }

    public boolean isEmpty(){
        return this.count == 0;
    }

    public int compare(NodeValue value) {
        RouteCount routeCount = (RouteCount) value;
        if (this.count > routeCount.count){
            return 1;
        } else if (this.count < routeCount.count){
            return -1;
        } else {
            return 0;
        }
    }

    public Route getRoute() {
        return route;
    }
}
