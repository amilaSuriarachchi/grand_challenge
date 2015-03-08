package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.TripProcessor;
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
public class RouteProcessor extends TripProcessor {

    private Queue<DropOffEvent> window = new LinkedList<DropOffEvent>();

    NodeList nodeList = new NodeList();

    private boolean isStarted = false;
    private long startTime;

    private double windowAvg = 0;
    private long numOfMessages = 0;


    private Set<Route> lastRouteSet;

    private int numOfProcessors;

    public RouteProcessor() {
        this.lastRouteSet = new HashSet<Route>();
    }

    public RouteProcessor(TripProcessor processor, int numOfProcessors) {
        this.processor = processor;
        this.lastRouteSet = new HashSet<Route>();
        this.numOfProcessors = numOfProcessors;
    }

    public void processEvent(TripEvent event) {

        DropOffEvent dropOffEvent = (DropOffEvent) event;
        // Upto here drop off time comes as a string. convert it to milliseconds.
        dropOffEvent.processDropOffTime();

        if (!this.isStarted) {
            this.isStarted = true;
            this.startTime = dropOffEvent.getDropOffTimeMillis();
        }

        List<NodeValue> tempPreList = nodeList.getTopValues();

        List<NodeValue> preList = new ArrayList<NodeValue>();
        for (NodeValue nodeValue : tempPreList){
            RouteCount routeCount = (RouteCount) nodeValue;
            preList.add(new RouteCount(routeCount.getCount(), routeCount.getRoute(), routeCount.getSeqNo()));
        }

        this.window.add(dropOffEvent);
        // whether the current top ten events get changed.
        if (!this.nodeList.containsKey(dropOffEvent.getRoute())) {
            this.nodeList.add(dropOffEvent.getRoute(), new RouteCount(1, dropOffEvent.getRoute(), dropOffEvent.getSeqNo()));
        } else {
            RouteCount routeCount = (RouteCount) this.nodeList.get(dropOffEvent.getRoute());
            routeCount.incrementCount();
            routeCount.setSeqNo(dropOffEvent.getSeqNo());
            // after increasing the count we need to move this element towards the head. i.e reduce position.
            this.nodeList.decrementPosition(dropOffEvent.getRoute());
        }

        // pull out expired events
        while (!this.window.isEmpty() && this.window.peek().isExpired(dropOffEvent.getDropOffTimeMillis())) {
            DropOffEvent expiredEvent = this.window.poll();
            RouteCount routeCount = (RouteCount) this.nodeList.get(expiredEvent.getRoute());
            routeCount.decrementCount();
            // we don't need to change the update time. since
            if (routeCount.isEmpty()) {
                this.nodeList.remove(expiredEvent.getRoute());
            } else {
                this.nodeList.incrementPosition(expiredEvent.getRoute());
            }
        }

        List<NodeValue> currentList = this.nodeList.getTopValues();

        if (!Util.isSame(preList, currentList) && (dropOffEvent.getDropOffTimeMillis() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
            Set<Route> currentRoutSet = getRouteSet(currentList);
            this.lastRouteSet.removeAll(currentRoutSet);
            generateRouteChangeEvent(dropOffEvent.getStartTime(),
                    dropOffEvent.getPickUpTime(),
                    dropOffEvent.getDropOffTimeMillis(),
                    this.lastRouteSet,
                    currentList,
                    dropOffEvent.getKey().hashCode() % this.numOfProcessors,
                    dropOffEvent.getSeqNo());

            this.lastRouteSet = getRouteSet(currentList);

        }

        this.windowAvg = (this.windowAvg * this.numOfMessages + this.window.size()) / (this.numOfMessages + 1);
        this.numOfMessages++;


    }


    public void generateRouteChangeEvent(long startTime,
                                         String pickUpTime,
                                         long dropOffTime,
                                         Set<Route> removedRoutes,
                                         List<NodeValue> newRoutes,
                                         int processID,
                                         int seqNo) {
        TopRoutesEvent topRoutesEvent = new TopRoutesEvent(startTime, pickUpTime, dropOffTime);
        topRoutesEvent.setRemovedRoutes(removedRoutes);
        topRoutesEvent.setProcessorID(processID);
        topRoutesEvent.setSeqNo(seqNo);
        for (NodeValue nodeValue : newRoutes){
            RouteCount routeCount = (RouteCount) nodeValue;
            topRoutesEvent.addRouteCount(
                    new TopRouteCount(routeCount.getCount(), routeCount.getRoute(), routeCount.getSeqNo()));
        }
        this.processor.processEvent(topRoutesEvent);
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
    }
}
