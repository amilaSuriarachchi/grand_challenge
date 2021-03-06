package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.process.TripProcessor;
import edu.colostate.cs.worker.api.Container;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocalProfitProcessor extends TripProcessor {

    private ProfitCalculator[] processors;
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
        int windowSize = Integer.parseInt(parameters.get("windowSize"));
        this.processors = new ProfitCalculator[this.numOfProcessors];
        for (int i = 0; i < this.numOfProcessors; i++) {
            this.processors[i] = new ProfitCalculator(this.processor, this.numOfProcessors, windowSize);
        }
    }
}
