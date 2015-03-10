package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.EventWriter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopRouteWriter extends EventWriter {

    public TopRouteWriter(String fileName) {
        super(fileName);
    }

    @Override
    public void writeLine(long startTime, String pickUpTime, long dropOffTime, List<NodeValue> nodeValues) {
        try {
            long delay = System.currentTimeMillis() - startTime;
            this.eventWriter.write(pickUpTime + ",");
            this.eventWriter.write(this.simpleDateFormat.format(new Date(dropOffTime)) + ",");
            for (NodeValue nodeValue : nodeValues) {
                TopRouteCount routeCount = (TopRouteCount) nodeValue;
                this.eventWriter.write(routeCount.getRoute().toString());
            }
            this.eventWriter.write(delay + "");
            this.eventWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
