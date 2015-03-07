package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.process.TripProcessor;
import edu.colostate.cs.gc.route.RouteProcessor;
import edu.colostate.cs.worker.api.Container;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/3/15
 * Time: 6:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocalRouteProcessor extends TripProcessor {

    private RouteProcessor[] processors;
    private int numOfProcessors;

    @Override
    public void processEvent(TripEvent event) {
        int processorNumber = event.getKey().hashCode() % this.numOfProcessors;
        this.processors[processorNumber].processEvent(event);
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initialise(Container container, Map<String, String> parameters) {
        super.initialise(container, parameters);
        this.numOfProcessors = Integer.parseInt(parameters.get("processors"));
        this.processors = new RouteProcessor[this.numOfProcessors];
        for (int i = 0; i < this.numOfProcessors; i++) {
            this.processors[i] = new RouteProcessor(this.processor);
        }
    }
}
