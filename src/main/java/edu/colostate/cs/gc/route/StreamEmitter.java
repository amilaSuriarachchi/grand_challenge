package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.process.TripProcessor;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/2/15
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class StreamEmitter extends TripProcessor {

    private Container container;

    public StreamEmitter(Container container) {
        this.container = container;
    }

    @Override
    public void processEvent(TripEvent event) {
        try {
            this.container.emit(event);
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void close() {
        System.out.println("Closing the Stream Emitter");
    }
}
