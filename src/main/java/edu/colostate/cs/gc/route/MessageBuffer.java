package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.DropOffEvent;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBuffer {

    public static final int MAX_BUFFER_SIZE = 500;

    private MessageWorker messageWorker;
    private DropOffEvent[] buffer;
    private int bufferCount;

    public MessageBuffer(TopRouteProcessor topRouteProcessor) {

        this.messageWorker = new MessageWorker(topRouteProcessor);
        Thread thread = new Thread(this.messageWorker);
        thread.start();

        this.buffer = new DropOffEvent[MAX_BUFFER_SIZE];
        this.bufferCount = 0;

    }

    public void addMessage(DropOffEvent dropOffEvent){

        this.buffer[this.bufferCount] = dropOffEvent;
        this.bufferCount++;
        if (this.bufferCount == MAX_BUFFER_SIZE){
            this.messageWorker.addEvents(this.buffer);
            this.bufferCount = 0;
        }
    }

    public void setFinish(){
        this.messageWorker.setFinish();
    }
}
