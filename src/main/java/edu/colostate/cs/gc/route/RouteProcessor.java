package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.list.TopMap;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    TopMap topMap = new TopMap();

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

        List<NodeValue> preList = topMap.getTopValues();

        this.window.add(event);
        // whether the current top ten events get changed.
        boolean isChanged = false;
        if (!this.topMap.containsKey(event.getRoute())) {
            isChanged = this.topMap.add(event.getRoute(), new RouteCount(1, event.getRoute()));
        } else {
            RouteCount routeCount = (RouteCount) this.topMap.get(event.getRoute());
            routeCount.incrementCount();
            // after increasing the count we need to move this element towards the head. i.e reduce position.
            isChanged = this.topMap.decrementPosition(event.getRoute()) || isChanged;
        }

        // pull out expired events
        while (!this.window.isEmpty() && this.window.peek().isExpired(event.getDropOffTime())) {
            DropOffEvent expiredEvent = this.window.poll();
            RouteCount routeCount = (RouteCount) this.topMap.get(expiredEvent.getRoute());
            routeCount.decrementCount();
            if (routeCount.isEmpty()) {
                isChanged = this.topMap.remove(expiredEvent.getRoute()) || isChanged;
            } else {
                isChanged = this.topMap.incrementPosition(expiredEvent.getRoute()) || isChanged;
            }
        }

        List<NodeValue> currentList = this.topMap.getTopValues();
        if (!Util.isSame(preList, currentList) && (event.getDropOffTime() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
//            Set<Route> currentRoutSet = getRouteSet(currentList);
//            this.lastRouteSet.removeAll(currentRoutSet);
            generateRouteChangeEvent(event.getStartTime(), event.getPickUpTime(),
                    event.getDropOffTime(), this.lastRouteSet, currentList);
            this.lastRouteSet = getRouteSet(currentList);

        }

        //TODO: check the isChanged variable. when there are many changes there is a possibility that one
        //change would revert earlier one making lists same :use isSame instead of the change method?

//        if (isChanged && (event.getDropOffTime() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
//            Set<NodeValue> currentSet = new HashSet<NodeValue>(this.topMap.getTopValues());
//            this.lastRouteSet.removeAll(currentSet);
//            generateRouteChangeEvent(event.getStartTime(), event.getPickUpTime(),
//                    event.getDropOffTime(), this.lastRouteSet, currentSet);
//            this.lastRouteSet = currentSet;
//        }

        this.windowAvg = (this.windowAvg * this.numOfMessages + this.window.size()) / (this.numOfMessages + 1);
        this.mapAvg = (this.mapAvg * this.numOfMessages + this.topMap.getMapSize()) / (this.numOfMessages + 1);
        this.listAvg = (this.listAvg * this.numOfMessages + this.topMap.getTailPosition()) / (this.numOfMessages + 1);
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
        this.topMap.displayDetails();
    }
}
