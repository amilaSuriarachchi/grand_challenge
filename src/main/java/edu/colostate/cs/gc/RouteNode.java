package edu.colostate.cs.gc;

import edu.colostate.cs.gc.event.Route;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteNode {

    private int count;
    private int position;
    private Route route;

    public RouteNode(int count, int position, Route route) {
        this.count = count;
        this.position = position;
        this.route = route;
    }

    private RouteNode nextNode;
    private RouteNode preNode;

    public RouteNode incrementCount(RouteNode head){
        this.count++;
        while ((this.preNode != null) && (this.preNode.getCount() < this.count)){
            //swap two nodes.
            RouteNode currentPreNode = this.preNode;
            RouteNode currentNextNode = this.nextNode;

            if (currentNextNode != null){
                currentNextNode.preNode = currentPreNode;
            }

            if ((currentPreNode != null) && (currentPreNode.preNode != null)){
                currentPreNode.preNode.nextNode = this;
            }

            this.preNode = currentPreNode.preNode;
            this.nextNode = currentPreNode;

            currentPreNode.nextNode = currentNextNode;
            currentPreNode.preNode = this;

            decrementPosition();
            this.nextNode.incrementPosition();

        }

        if (this.preNode == null){
            return this;
        } else {
            return head;
        }
    }

    public boolean isCorrect(RouteNode head){
        boolean isCorrect = true;
        RouteNode routeNode = head;
        int currentPosition = 0;

        while (routeNode != null){
            if (currentPosition != routeNode.getPosition()){
                System.out.println("Fail at " + currentPosition + " Node position " + routeNode.getPosition());
                isCorrect = false;
                break;
            }
            currentPosition++;
            routeNode = routeNode.getNextNode();
        }
        return isCorrect;
    }

    public RouteNode decrementCount(RouteNode tail){
        this.count--;
        while ((this.nextNode != null) && (this.nextNode.getCount() > this.count)){

            RouteNode currentNext = this.nextNode;
            RouteNode currentPre = this.preNode;

            if (currentPre != null){
                currentPre.nextNode = currentNext;
            }

            if ((currentNext != null) && (currentNext.nextNode != null)){
                currentNext.nextNode.preNode = this;
            }

            this.nextNode = currentNext.nextNode;
            this.preNode = currentNext;

            currentNext.preNode = currentPre;
            currentNext.nextNode = this;

            incrementPosition();
            currentNext.decrementPosition();
        }

        if (this.nextNode == null){
            return this;
        } else {
            return tail;
        }


    }

    public void incrementPosition(){
        this.position++;
    }

    public void decrementPosition(){
        this.position--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public RouteNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(RouteNode nextNode) {
        this.nextNode = nextNode;
    }

    public RouteNode getPreNode() {
        return preNode;
    }

    public void setPreNode(RouteNode preNode) {
        this.preNode = preNode;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
