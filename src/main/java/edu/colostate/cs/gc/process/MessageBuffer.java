package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.Event;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBuffer {

    public static final int MAX_BUFFER_SIZE = 10;

    private MessageWorker messageWorker;
    private Event[] buffer;
    private int bufferCount;

    public MessageBuffer(Processor processor) {

        this.messageWorker = new MessageWorker(processor);
        Thread thread = new Thread(this.messageWorker);
        thread.start();

        this.buffer = new Event[MAX_BUFFER_SIZE];
        this.bufferCount = 0;

    }

    public void addMessage(Event event){

        this.buffer[this.bufferCount] = event;
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
