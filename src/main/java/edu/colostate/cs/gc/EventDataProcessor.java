package edu.colostate.cs.gc;

import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.util.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventDataProcessor {

    private Map<Route, RouteNode> routeFrequency = new HashMap<Route, RouteNode>(5000);
    private Queue<DropOffEvent> window = new LinkedList<DropOffEvent>();

    private RouteNode tail;
    private RouteNode head;

    private boolean isStarted = false;
    private long startTime;

    private BufferedWriter eventWriter;

    public EventDataProcessor() {
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
        if (!this.routeFrequency.containsKey(event.getRoute())) {
            if (this.tail == null) {
                this.tail = new RouteNode(1, 0, event.getRoute());
                this.head = this.tail;
                this.routeFrequency.put(event.getRoute(), this.tail);
                isChanged = true;
            } else {
                RouteNode routeNode = new RouteNode(1, this.tail.getPosition() + 1, event.getRoute());
                this.routeFrequency.put(event.getRoute(), routeNode);
                routeNode.setPreNode(this.tail);
                this.tail.setNextNode(routeNode);
                this.tail = routeNode;
                if (this.tail.getPosition() < 10) {
                    isChanged = true;
                }
            }

        } else {
            RouteNode routeNode = this.routeFrequency.get(event.getRoute());
            int prePosition = routeNode.getPosition();
            // if the current node is the tail and if it suppose to move them we better
            // set the new tail correctly.
            if ((routeNode == this.tail) && (routeNode.getPreNode() != null)){
                if (routeNode.getCount() == routeNode.getPreNode().getCount()){
                    // this means this tail will move
                    this.tail = routeNode.getPreNode();
                }
            }

            this.head = routeNode.incrementCount(this.head);
            int currentPosition = routeNode.getPosition();
            // if the position changed and current one is within first 10
            isChanged = (prePosition != currentPosition) && (currentPosition < 10);
        }

        // pull out expired events
        while (!this.window.isEmpty() && this.window.peek().isExpired(event.getDropOffTime())) {
            DropOffEvent expiredEvent = this.window.poll();
            RouteNode routeNode = this.routeFrequency.get(expiredEvent.getRoute());
            if ((routeNode == this.head) && (routeNode.getNextNode() != null)){
                if (routeNode.getCount() == routeNode.getNextNode().getCount()){
                    // next node will be the head
                    this.head = routeNode.getNextNode();
                }
            }

            int prePosition = routeNode.getPosition();
            this.tail = routeNode.decrementCount(this.tail);
            int currentPosition = routeNode.getPosition();
            // remove the node if count is zero.
            if (routeNode.getCount() == 0) {
                this.routeFrequency.remove(routeNode.getRoute());
                this.tail = routeNode.getPreNode();
                this.tail.setNextNode(null);
                //always there should be at least one route in the route list.
            }

            isChanged = (currentPosition != prePosition) && (prePosition < 10);
        }

        if (isChanged && (event.getDropOffTime() - this.startTime > Constants.WINDOW_SIZE)) {
            generateRouteChangeEvent(event.getPickUpTime(), event.getDropOffTime());
        }

        checkList();

    }

    public void generateRouteChangeEvent(long pickUpTime, long dropOffTime) {
        TopRoutesEvent topRoutesEvent = new TopRoutesEvent(pickUpTime, dropOffTime);
        RouteNode currentNode = this.head;
        while ((currentNode != null) && currentNode.getPosition() < 10) {
            topRoutesEvent.addRoute(currentNode.getRoute());
            currentNode = currentNode.getNextNode();
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
        System.out.println("Tail position ==> " + this.tail.getPosition());
        System.out.println("All routes ==> " + this.routeFrequency.size());
        //checking list
        checkList();

        try {
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void checkList() {
        RouteNode routeNode = this.head;
        int currentPosition = 0;
        int currentValue = routeNode.getCount() + 1;
        while (routeNode != null){
            if ((routeNode.getPosition() != currentPosition) || currentValue < routeNode.getCount()){
                System.out.println("Error in list at " + currentPosition);
            }
            currentValue = routeNode.getCount();
            routeNode = routeNode.getNextNode();
            currentPosition++;
        }

        currentPosition--;
        routeNode = this.tail;
        while (routeNode != null){
            if (routeNode.getPosition() != currentPosition){
                System.out.println("Error in list at " + currentPosition);
            }
            routeNode = routeNode.getPreNode();
            currentPosition--;
        }
    }

    public void printListSize(){
        int i = 0;
        RouteNode routeNode = this.head;
        while (routeNode != null){
            i++;
            routeNode = routeNode.getNextNode();
        }
        System.out.println("list size " + i);
    }

    public boolean isCorrect(){
        boolean isCorrect = true;
        RouteNode routeNode = this.head;
        int currentPosition = 0;

        while (routeNode != null){
            if (currentPosition != routeNode.getPosition()){
                System.out.println("Fail at " + currentPosition + " Node position " + routeNode.getPosition());
                isCorrect = false;
            }
            currentPosition++;
            routeNode = routeNode.getNextNode();
        }
        return isCorrect;
    }
}
