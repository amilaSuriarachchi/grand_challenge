package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Event;
import edu.colostate.cs.gc.event.TopProfitableEvent;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.Processor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/28/15
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitEventWriter {

    private BufferedWriter eventWriter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int numOfEvents = 0;
    private double avgDelay = 0;

    public ProfitEventWriter() {
        try {
            this.eventWriter = new BufferedWriter(new FileWriter("data/top_profits.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void processEvent(Event event) {
        TopProfitableEvent topProfitableEvent = (TopProfitableEvent) event;
        long delay = System.currentTimeMillis() - topProfitableEvent.getStartTime();
        this.avgDelay = (this.avgDelay * this.numOfEvents + delay) / (this.numOfEvents + 1);
        this.numOfEvents++;
        try {
            this.eventWriter.write(this.simpleDateFormat.format(new Date(topProfitableEvent.getPickTime())) + ",");
            this.eventWriter.write(this.simpleDateFormat.format(new Date(topProfitableEvent.getDropTime())) + ",");
            for (NodeValue nodeValue : topProfitableEvent.getProfitCells()) {
                ProfitCellNode profitCellNode = (ProfitCellNode) nodeValue;
                this.eventWriter.write(profitCellNode.getCell().toString() + ",");
                this.eventWriter.write(profitCellNode.getNumOfEmptyTaxis() + ",");
                this.eventWriter.write(profitCellNode.getMidFare() + ",");
                this.eventWriter.write(profitCellNode.getProfitability() + ",");
            }
            this.eventWriter.write(delay + "");
            this.eventWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

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
