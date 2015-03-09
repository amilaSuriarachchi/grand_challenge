package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.list.NodeValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EventWriter {

    protected BufferedWriter eventWriter;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected int numOfEvents = 0;
    protected double avgDelay = 0;

    public EventWriter(String fileName) {
        try {
            this.eventWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public abstract void writeLine(long startTime, String pickUpTime, long dropOffTime, List<NodeValue> nodeValues);

    public void close() {
        System.out.println("Number of events " + this.numOfEvents);
        System.out.println("Average delay " + this.avgDelay);
        try {
            this.eventWriter.flush();
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
