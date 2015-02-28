package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Event;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.Processor;
import edu.colostate.cs.gc.util.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class TopRouteProcessor extends Processor {

    private NodeList nodeList;

    private BufferedWriter eventWriter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int eventReceived = 0;

    private double avgDelay = 0;
    private int numOfEvents = 0;

    public TopRouteProcessor() {
        this.nodeList = new NodeList();
        try {
            this.eventWriter = new BufferedWriter(new FileWriter("data/top_routs.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public synchronized void processEvent(Event event) {

        TopRoutesEvent topRoutesEvent = (TopRoutesEvent) event;
        // remove old routes
        RouteCount routeCount = null;

        List<NodeValue> preList = this.nodeList.getTopValues();

        for (Route route : topRoutesEvent.getRemovedRoutes()) {
            this.nodeList.remove(route);
        }

        // add new values
        for (NodeValue nodeValue : topRoutesEvent.getNewRoutes()) {
            routeCount = (RouteCount) nodeValue;
            if (!this.nodeList.containsKey(routeCount.getRoute())) {
                // need to create a new object to avoid conflicts with earlier process objects.
                this.nodeList.add(routeCount.getRoute(),
                        new RouteCount(routeCount.getCount(), routeCount.getRoute(), routeCount.getUpdatedTime()));
            } else {
                RouteCount existingValue = (RouteCount) this.nodeList.get(routeCount.getRoute());
                if (routeCount.getCount() < existingValue.getCount()) {
                    existingValue.setCount(routeCount.getCount());
                    existingValue.setUpdatedTime(routeCount.getUpdatedTime());
                    //if the new value is less it has to move further down.
                    this.nodeList.incrementPosition(routeCount.getRoute());
                } else if (routeCount.getCount() > existingValue.getCount()) {
                    existingValue.setCount(routeCount.getCount());
                    existingValue.setUpdatedTime(routeCount.getUpdatedTime());
                    this.nodeList.decrementPosition(routeCount.getRoute());
                }
            }
        }

        this.eventReceived++;

        if (!Util.isSame(preList, this.nodeList.getTopValues())) {
            generateRouteChangeEvent(topRoutesEvent.getStartTime(),
                    topRoutesEvent.getPickUpTime(), topRoutesEvent.getDropOffTime(), nodeList.getTopValues());

        }
    }

    public void generateRouteChangeEvent(long startTime, long pickUpTime, long dropOffTime, List<NodeValue> nodeValues) {

        try {
            long delay = System.currentTimeMillis() - startTime;
            this.avgDelay = (this.avgDelay * this.numOfEvents + delay) / (this.numOfEvents + 1);
            this.numOfEvents++;

            this.eventWriter.write(this.simpleDateFormat.format(new Date(pickUpTime)) + ",");
            this.eventWriter.write(this.simpleDateFormat.format(new Date(dropOffTime)) + ",");
            for (NodeValue nodeValue : nodeValues) {
                RouteCount routeCount = (RouteCount) nodeValue;
                this.eventWriter.write(routeCount.getRoute().toString());
            }
            this.eventWriter.write(delay + "");
            this.eventWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void close() {

        System.out.println("Event received ==> " + this.eventReceived);
        System.out.println("Avg Delay ==> " + this.avgDelay);
        try {
            this.eventWriter.flush();
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
