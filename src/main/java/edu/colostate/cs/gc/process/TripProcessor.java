package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.route.StreamEmitter;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.data.Event;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/28/15
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TripProcessor implements Processor {

    private Container container;
    protected TripProcessor processor;

    public abstract void processEvent(TripEvent event);

    public abstract void close();

    public void onEvent(Event event) {
        processEvent((TripEvent)event);
    }

    public void initialise(Container container, Map<String, String> stringStringMap) {
        this.container = container;
        this.processor = new StreamEmitter(this.container);

    }
}
