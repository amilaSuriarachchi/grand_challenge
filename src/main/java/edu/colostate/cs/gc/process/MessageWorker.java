package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.Event;

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

    private Queue<Event> messages;
    private boolean isFinished;

    private Processor processor;

    public MessageWorker(Processor processor) {
        this.messages = new LinkedList<Event>();
        this.isFinished = false;
        this.processor = processor;
    }

    public synchronized void addEvents(Event[] events) {
        if (this.messages.size() >= MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addEvents(events);
        } else {
            for (Event record : events) {
                this.messages.add(record);
            }
            this.notify();
        }

    }

    public synchronized Event getEvent() {

        Event event = this.messages.poll();
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
        Event event = null;
        // record will be thread executions is over.
        while ((event = getEvent()) != null) {
            this.processor.processEvent(event);
        }

        this.processor.close();
    }
}
