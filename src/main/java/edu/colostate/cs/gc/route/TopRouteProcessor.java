package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.EventWriter;
import edu.colostate.cs.gc.process.TopEventProcessor;
import edu.colostate.cs.gc.process.TripProcessor;
import edu.colostate.cs.gc.profit.ProfitEventWriter;
import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.api.Container;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class TopRouteProcessor extends TopEventProcessor {

    public TopRouteProcessor() {
        super(new TopRouteWriter("top_routes.txt"));
    }

    public TopRouteProcessor(int numbOfProcessors) {
        super(numbOfProcessors, new TopRouteWriter("top_routes.txt"));
    }
}
