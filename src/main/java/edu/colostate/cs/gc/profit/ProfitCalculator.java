package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.PaymentEvent;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.list.TopMap;
import edu.colostate.cs.gc.util.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/20/15
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitCalculator {

    private Queue<PaymentEvent> paymentWindow = new LinkedList<PaymentEvent>();
    private Queue<PaymentEvent> dropWindow = new LinkedList<PaymentEvent>();
    private TopMap topMap = new TopMap();

    private long startTime;
    private boolean isStarted;

    private BufferedWriter eventWriter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProfitCalculator() {
        try {
            this.eventWriter = new BufferedWriter(new FileWriter("data/top_profits.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void processEvent(PaymentEvent event) {

        if (!this.isStarted) {
            this.isStarted = true;
            this.startTime = event.getDropOffTime();
        }

        boolean isChanged = false;
        if (event.isPayEvent()) {
            this.paymentWindow.add(event);
            Cell pickUpNode = event.getPickUpCell();
            if (!this.topMap.containsKey(pickUpNode)) {
                // if there is no entry in the topMap then any taxi drop has not been arrived at this position. So
                // no need to reduce the empty taxis
                ProfitCellNode profitCellNode = new ProfitCellNode(event.getFare(), pickUpNode);
                //this node does not have a profitability so it won't make any change to top nodes.
                this.topMap.add(pickUpNode, profitCellNode);
            } else {
                ProfitCellNode profitCellNode = (ProfitCellNode) this.topMap.get(pickUpNode);
                double preProfitability = profitCellNode.getProfitability();
                profitCellNode.addFare(event.getFare());

                //we should reduce number of taxi if the drop in time has not expired 30 mints
                if (profitCellNode.containsTaxi(event.getMedallion())) {
                    profitCellNode.reduceEmptyTaxi();
                    profitCellNode.removeTaxi(event.getMedallion());
                }

                if (preProfitability > profitCellNode.getProfitability()) {
                    isChanged = this.topMap.incrementPosition(pickUpNode) || isChanged;
                } else {
                    isChanged = this.topMap.decrementPosition(pickUpNode) || isChanged;
                }

            }

            while ((this.paymentWindow.size() > 0) &&
                    (this.paymentWindow.peek().isExpired(event.getDropOffTime(), Constants.SMALL_WINDOW_SIZE))) {
                PaymentEvent paymentEvent = this.paymentWindow.poll();
                ProfitCellNode profitCellNode = (ProfitCellNode) this.topMap.get(paymentEvent.getPickUpCell());
                double preProfitability = profitCellNode.getProfitability();
                profitCellNode.removeFare(paymentEvent.getFare());
                if (profitCellNode.isEmpty()) {
                    this.topMap.remove(paymentEvent.getPickUpCell());
                } else {
                    if (preProfitability > profitCellNode.getProfitability()) {
                        isChanged = this.topMap.incrementPosition(paymentEvent.getPickUpCell()) || isChanged;
                    } else {
                        isChanged = this.topMap.decrementPosition(paymentEvent.getPickUpCell()) || isChanged;
                    }

                }
            }
        } else {
            // this is just the drop off information. i.e an empty taxi has arrived to this location.
            this.dropWindow.add(event);

            Cell dropOffNode = event.getDropOffCell();

            if (!this.topMap.containsKey(dropOffNode)) {
                ProfitCellNode profitCellNode = new ProfitCellNode(dropOffNode);
                profitCellNode.addTaxi(event.getMedallion());
                isChanged = this.topMap.add(dropOffNode, profitCellNode) || isChanged;
                // we need to decrement the position since it added as last node and there can be -1 profitability nodes
                isChanged = this.topMap.decrementPosition(dropOffNode) || isChanged;
            } else {
                ProfitCellNode profitCellNode = (ProfitCellNode) this.topMap.get(dropOffNode);
                profitCellNode.addTaxi(event.getMedallion());
                profitCellNode.increaseEmptyTaxi();
                isChanged = this.topMap.incrementPosition(dropOffNode) || isChanged;
            }

            while ((this.dropWindow.size() > 0)
                    && (this.dropWindow.peek().isExpired(event.getDropOffTime(), Constants.LARGE_WINDOW_SIZE))) {
                PaymentEvent paymentEvent = this.dropWindow.poll();

                if (this.topMap.containsKey(paymentEvent.getDropOffCell())) {
                    //this cell may have been removed by earlier processing of dropping event due to taxi leaving.
                    //or by payment reduction due to same reasoning.
                    ProfitCellNode profitCellNode = (ProfitCellNode) this.topMap.get(paymentEvent.getDropOffCell());

                    if (profitCellNode.containsTaxi(paymentEvent.getMedallion())) {
                        // there is a possibility that the taxi for this drop event already has gone and received a
                        // payment event.
                        profitCellNode.reduceEmptyTaxi();
                        profitCellNode.removeTaxi(paymentEvent.getMedallion());

                        if (profitCellNode.isEmpty()) {
                            this.topMap.remove(paymentEvent.getDropOffCell());
                        } else {
                            isChanged = this.topMap.decrementPosition(paymentEvent.getDropOffCell()) || isChanged;
                        }

                    }
                }
            }
        }

        if (isChanged && (event.getDropOffTime() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
            generateProfitChangeEvent(event.getStartTime(), event.getPickUpTime(),
                                            event.getDropOffTime(), this.topMap.getTopValues());
        }

    }

    public void generateProfitChangeEvent(long startTime, long pickTime, long dropTime, List<NodeValue> nodes) {

        try {
            this.eventWriter.write(this.simpleDateFormat.format(new Date(pickTime)) + ",");
            this.eventWriter.write(this.simpleDateFormat.format(new Date(dropTime)) + ",");
            for (NodeValue nodeValue : nodes) {
                ProfitCellNode profitCellNode = (ProfitCellNode) nodeValue;
                this.eventWriter.write(profitCellNode.getCell().toString() + ",");
                this.eventWriter.write(profitCellNode.getNumOfEmptyTaxis() + ",");
                this.eventWriter.write(profitCellNode.getMidFare() + ",");
                this.eventWriter.write(profitCellNode.getProfitability() + ",");
            }
            this.eventWriter.write((System.currentTimeMillis() - startTime) + "");
            this.eventWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void close(){
        try {
            this.eventWriter.flush();
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
