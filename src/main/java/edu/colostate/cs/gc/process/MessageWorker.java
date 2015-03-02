package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.TripEvent;

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

    private Queue<TripEvent> messages;
    private boolean isFinished;

    private TripProcessor processor;

    public MessageWorker(TripProcessor processor) {
        this.messages = new LinkedList<TripEvent>();
        this.isFinished = false;
        this.processor = processor;
    }

    public synchronized void addEvents(TripEvent[] events) {
        if (this.messages.size() >= MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addEvents(events);
        } else {
            for (TripEvent event : events) {
                this.messages.add(event);
            }
            this.notify();
        }

    }

    public synchronized TripEvent getEvent() {

        TripEvent event = this.messages.poll();
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
        TripEvent event = null;
        // record will be thread executions is over.
        while ((event = getEvent()) != null) {
            this.processor.processEvent(event);
        }

        this.processor.close();
    }
}
