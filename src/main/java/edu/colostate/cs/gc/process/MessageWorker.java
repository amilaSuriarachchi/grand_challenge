package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.TripEvent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

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

    private CyclicBarrier barrier;
    private CountDownLatch latch;

    public MessageWorker(TripProcessor processor, CyclicBarrier barrier, CountDownLatch latch) {
        this.messages = new LinkedList<TripEvent>();
        this.isFinished = false;
        this.processor = processor;
        this.barrier = barrier;
        this.latch = latch;
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

        try {
            this.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // record will be thread executions is over.
        while ((event = getEvent()) != null) {
            this.processor.processEvent(event);
        }

        this.latch.countDown();

    }
}
