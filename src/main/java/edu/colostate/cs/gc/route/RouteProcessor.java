package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.list.OrderedList;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteProcessor {

    private Queue<DropOffEvent> window = new LinkedList<DropOffEvent>();

    OrderedList orderedList = new OrderedList();

    private boolean isStarted = false;
    private long startTime;

    private double windowAvg = 0;
    private long numOfMessages = 0;

    private double mapAvg = 0;
    private double listAvg = 0;

    private TopRouteProcessor topRouteProcessor;
    private Set<Route> lastRouteSet;

    public RouteProcessor(TopRouteProcessor topRouteProcessor) {
        this.topRouteProcessor = topRouteProcessor;
        this.lastRouteSet = new HashSet<Route>();
    }

    public void processEvent(DropOffEvent event) {

        if (!this.isStarted) {
            this.isStarted = true;
            this.startTime = event.getDropOffTime();
        }

        List<NodeValue> preList = orderedList.getTopValues();

        this.window.add(event);
        // whether the current top ten events get changed.
        boolean isChanged = false;
        if (!this.orderedList.containsKey(event.getRoute())) {
            isChanged = this.orderedList.add(event.getRoute(), new RouteCount(1, event.getRoute(), event.getDropOffTime()));
            isChanged = this.orderedList.decrementPosition(event.getRoute()) || isChanged;
        } else {
            RouteCount routeCount = (RouteCount) this.orderedList.get(event.getRoute());
            routeCount.incrementCount();
            routeCount.setUpdatedTime(event.getDropOffTime());
            // after increasing the count we need to move this element towards the head. i.e reduce position.
            isChanged = this.orderedList.decrementPosition(event.getRoute()) || isChanged;
        }

        // pull out expired events
        while (!this.window.isEmpty() && this.window.peek().isExpired(event.getDropOffTime())) {
            DropOffEvent expiredEvent = this.window.poll();
            RouteCount routeCount = (RouteCount) this.orderedList.get(expiredEvent.getRoute());
            routeCount.decrementCount();
            if (routeCount.isEmpty()) {
                this.orderedList.remove(expiredEvent.getRoute());
            } else {
                isChanged = this.orderedList.incrementPosition(expiredEvent.getRoute()) || isChanged;
            }
        }

        List<NodeValue> currentList = this.orderedList.getTopValues();
        if (!Util.isSame(preList, currentList) && (event.getDropOffTime() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
            Set<Route> currentRoutSet = getRouteSet(currentList);
            this.lastRouteSet.removeAll(currentRoutSet);
            generateRouteChangeEvent(event.getStartTime(), event.getPickUpTime(),
                    event.getDropOffTime(), this.lastRouteSet, currentList);
            this.lastRouteSet = getRouteSet(currentList);

        }

        this.windowAvg = (this.windowAvg * this.numOfMessages + this.window.size()) / (this.numOfMessages + 1);
        this.mapAvg = (this.mapAvg * this.numOfMessages + this.orderedList.getMapSize()) / (this.numOfMessages + 1);
        this.listAvg = (this.listAvg * this.numOfMessages + this.orderedList.getTailPosition()) / (this.numOfMessages + 1);
        this.numOfMessages++;

    }


    public void generateRouteChangeEvent(long startTime,
                                         long pickUpTime,
                                         long dropOffTime,
                                         Set<Route> removedRoutes,
                                         List<NodeValue> newRoutes) {
        TopRoutesEvent topRoutesEvent = new TopRoutesEvent(startTime, pickUpTime, dropOffTime);
        topRoutesEvent.setRemovedRoutes(removedRoutes);
        topRoutesEvent.setNewRoutes(newRoutes);
        this.topRouteProcessor.processEvent(topRoutesEvent);
    }


    private Set<Route> getRouteSet(List<NodeValue> routeList) {
        Set<Route> routes = new HashSet<Route>();
        for (NodeValue nodeValue : routeList) {
            routes.add(((RouteCount) nodeValue).getRoute());
        }
        return routes;
    }

    public void close() {
        System.out.println("Average window size ==> " + this.windowAvg);
        System.out.println("Average map size ==> " + this.mapAvg);
        System.out.println("Average tail position size ==> " + this.listAvg);
        this.orderedList.displayDetails();
    }
}
