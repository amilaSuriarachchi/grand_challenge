package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.list.TopMap;
import edu.colostate.cs.gc.util.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private BufferedWriter eventWriter;

    public RouteProcessor() {
        try {
            this.eventWriter = new BufferedWriter(new FileWriter("data/top_routs.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void processEvent(DropOffEvent event) {

        if (!this.isStarted) {
            this.isStarted = true;
            this.startTime = event.getDropOffTime();
        }

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

        if (isChanged && (event.getDropOffTime() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
            generateRouteChangeEvent(event.getPickUpTime(), event.getDropOffTime(), this.topMap.getTopValues());
        }

    }

    public void generateRouteChangeEvent(long pickUpTime, long dropOffTime, List<NodeValue> nodeValues) {
        TopRoutesEvent topRoutesEvent = new TopRoutesEvent(pickUpTime, dropOffTime);
        for (NodeValue nodeValue : nodeValues) {
            topRoutesEvent.addRoute(((RouteCount) nodeValue).getRoute());
        }

        try {
            this.eventWriter.write(topRoutesEvent.toString());
            this.eventWriter.newLine();
            this.eventWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void close() {
        this.topMap.displayDetails();

        try {
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
