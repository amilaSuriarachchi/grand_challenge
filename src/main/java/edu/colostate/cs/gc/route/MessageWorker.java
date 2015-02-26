package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.DropOffEvent;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageWorker implements Runnable {

    public static final int MAX_SIZE = 1000;

    private Queue<DropOffEvent> messages;
    private boolean isFinished;

    private RouteProcessor routeProcessor;

    public MessageWorker(TopRouteProcessor topRouteProcessor) {
        this.messages = new LinkedList<DropOffEvent>();
        this.isFinished = false;
        this.routeProcessor = new RouteProcessor(topRouteProcessor);
    }

    public synchronized void addEvents(DropOffEvent[] events) {
        if (this.messages.size() >= MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addEvents(events);
        } else {
            for (DropOffEvent record : events) {
                this.messages.add(record);
            }
            this.notify();
        }

    }

    public synchronized DropOffEvent getEvent() {

        DropOffEvent event = this.messages.poll();
        while ((event == null) && !this.isFinished) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            event = this.messages.poll();
        }
        this.notify();
        return event;

    }

    public synchronized void setFinish() {
        this.isFinished = true;
        this.notify();
    }


    public void run() {
        DropOffEvent event = null;
        // record will be thread executions is over.
        while ((event = getEvent()) != null) {
            this.routeProcessor.processEvent(event);
        }

        this.routeProcessor.close();
    }
}
