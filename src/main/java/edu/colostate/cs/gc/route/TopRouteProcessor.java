package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.list.TopMap;
import edu.colostate.cs.gc.util.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class TopRouteProcessor {

    private TopMap topMap;

    private BufferedWriter eventWriter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int eventReceived = 0;

    public TopRouteProcessor() {
        this.topMap = new TopMap();
        try {
            this.eventWriter = new BufferedWriter(new FileWriter("data/top_routs.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public synchronized void processEvent(TopRoutesEvent event) {

        // remove old routes
        RouteCount routeCount = null;
        boolean isChanged = false;

        List<NodeValue> preList = this.topMap.getTopValues();

        for (Route route : event.getRemovedRoutes()) {
            isChanged = this.topMap.remove(route) || isChanged;
        }

        // add new values
        for (NodeValue nodeValue : event.getNewRoutes()) {
            routeCount = (RouteCount) nodeValue;
            if (!this.topMap.containsKey(routeCount.getRoute())) {
                // need to create a new object to avoid conflits with earlier process objects.
                isChanged = this.topMap.add(routeCount.getRoute(),
                        new RouteCount(routeCount.getCount(), routeCount.getRoute())) || isChanged;
                isChanged = this.topMap.decrementPosition(routeCount.getRoute()) || isChanged;
            } else {
                RouteCount existingValue = (RouteCount) this.topMap.get(routeCount.getRoute());
                if (routeCount.getCount() < existingValue.getCount()) {
                    existingValue.setCount(routeCount.getCount());
                    //if the new value is less it has to move further down.
                    isChanged = this.topMap.incrementPosition(routeCount.getRoute()) || isChanged;
                } else if (routeCount.getCount() > existingValue.getCount()) {
                    existingValue.setCount(routeCount.getCount());
                    isChanged = this.topMap.decrementPosition(routeCount.getRoute()) || isChanged;
                }
            }
        }

        this.eventReceived++;

        if (!isSame(preList, this.topMap.getTopValues())) {
            generateRouteChangeEvent(event.getStartTime(),
                    event.getPickUpTime(), event.getDropOffTime(), topMap.getTopValues());
        }
    }

    private boolean isSame(List<NodeValue> list1, List<NodeValue> list2){
        Set<Route> s1 = getRouteSet(list1);
        Set<Route> s2 = getRouteSet(list2);
        return s1.containsAll(s2);
    }

    private Set<Route> getRouteSet(List<NodeValue> routeList) {
        Set<Route> routes = new HashSet<Route>();
        for (NodeValue nodeValue : routeList) {
            routes.add(((RouteCount) nodeValue).getRoute());
        }
        return routes;
    }

    public void generateRouteChangeEvent(long startTime, long pickUpTime, long dropOffTime, List<NodeValue> nodeValues) {

        try {

            this.eventWriter.write(this.simpleDateFormat.format(new Date(pickUpTime)) + ",");
            this.eventWriter.write(this.simpleDateFormat.format(new Date(dropOffTime)) + ",");
            for (NodeValue nodeValue : nodeValues) {
                RouteCount routeCount = (RouteCount) nodeValue;
                this.eventWriter.write(routeCount.getRoute().toString());
            }
            this.eventWriter.write((System.currentTimeMillis() - startTime) + "");
            this.eventWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void close() {

        System.out.println("Event received ==> " + this.eventReceived);
        System.out.println("Map size ==> " + topMap.getMapSize());
        try {
            this.eventWriter.flush();
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
