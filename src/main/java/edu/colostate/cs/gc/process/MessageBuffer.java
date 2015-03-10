package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.TripEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBuffer {

    public static final int MAX_BUFFER_SIZE = 100;

    private MessageWorker messageWorker;
    private TripEvent[] buffer;
    private int bufferCount;

    public MessageBuffer(TripProcessor processor, CyclicBarrier barrier, CountDownLatch latch) {

        this.messageWorker = new MessageWorker(processor, barrier, latch);
        Thread thread = new Thread(this.messageWorker);
        thread.start();

        this.buffer = new TripEvent[MAX_BUFFER_SIZE];
        this.bufferCount = 0;

    }

    public void addMessage(TripEvent event) {

        this.buffer[this.bufferCount] = event;
        this.bufferCount++;
        if (this.bufferCount == MAX_BUFFER_SIZE) {
            this.messageWorker.addEvents(this.buffer);
            this.bufferCount = 0;
        }
    }

    public void setFinish() {
        if (this.bufferCount > 0) {
            TripEvent[] remainder = new TripEvent[this.bufferCount];
            for (int i = 0; i < this.bufferCount; i++) {
                remainder[i] = this.buffer[i];
            }
            this.messageWorker.addEvents(remainder);
        }

        this.messageWorker.setFinish();
    }
}
