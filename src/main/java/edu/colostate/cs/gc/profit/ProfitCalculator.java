package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.Event;
import edu.colostate.cs.gc.event.PaymentEvent;
import edu.colostate.cs.gc.event.TopProfitableEvent;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.Processor;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/20/15
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitCalculator extends Processor {

    private Queue<PaymentEvent> paymentWindow = new LinkedList<PaymentEvent>();
    private Queue<PaymentEvent> dropWindow = new LinkedList<PaymentEvent>();
    private NodeList nodeList = new NodeList();

    private long startTime;
    private boolean isStarted;


    private int numOfEvents = 0;
    private double avgDelay = 0;

    private ProfitEventWriter profitEventWriter;

    public ProfitCalculator() {
        this.profitEventWriter = new ProfitEventWriter();
    }

    public void processEvent(Event event) {

        PaymentEvent paymentEvent = (PaymentEvent) event;

        if (!this.isStarted) {
            this.isStarted = true;
            this.startTime = paymentEvent.getDropOffTime();
        }

        List<NodeValue> preList = nodeList.getTopValues();
        if (paymentEvent.isPayEvent()) {
            this.paymentWindow.add(paymentEvent);
            Cell pickUpNode = paymentEvent.getPickUpCell();
            if (!this.nodeList.containsKey(pickUpNode)) {
                // if there is no entry in the nodeList then any taxi drop has not been arrived at this position. So
                // no need to reduce the empty taxis
                ProfitCellNode profitCellNode = new ProfitCellNode(paymentEvent.getFare(), pickUpNode);
                //this node does not have a profitability so it won't make any change to top nodes.
                this.nodeList.add(pickUpNode, profitCellNode);
            } else {
                ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(pickUpNode);
                double preProfitability = profitCellNode.getProfitability();
                profitCellNode.addFare(paymentEvent.getFare());

                //we should reduce number of taxi if the drop in time has not expired 30 mints
                if (profitCellNode.containsTaxi(paymentEvent.getMedallion())) {
                    profitCellNode.reduceEmptyTaxi();
                    profitCellNode.removeTaxi(paymentEvent.getMedallion());
                }

                if (preProfitability > profitCellNode.getProfitability()) {
                    this.nodeList.incrementPosition(pickUpNode);
                } else {
                    this.nodeList.decrementPosition(pickUpNode);
                }

            }

            while ((this.paymentWindow.size() > 0) &&
                    (this.paymentWindow.peek().isExpired(paymentEvent.getDropOffTime(), Constants.SMALL_WINDOW_SIZE))) {
                PaymentEvent expiredEvent = this.paymentWindow.poll();
                ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(expiredEvent.getPickUpCell());
                double preProfitability = profitCellNode.getProfitability();
                profitCellNode.removeFare(expiredEvent.getFare());
                if (profitCellNode.isEmpty()) {
                    this.nodeList.remove(expiredEvent.getPickUpCell());
                } else {
                    if (preProfitability > profitCellNode.getProfitability()) {
                        this.nodeList.incrementPosition(expiredEvent.getPickUpCell());
                    } else {
                        this.nodeList.decrementPosition(expiredEvent.getPickUpCell());
                    }

                }
            }
        } else {
            // this is just the drop off information. i.e an empty taxi has arrived to this location.
            this.dropWindow.add(paymentEvent);

            Cell dropOffNode = paymentEvent.getDropOffCell();

            if (!this.nodeList.containsKey(dropOffNode)) {
                ProfitCellNode profitCellNode = new ProfitCellNode(dropOffNode);
                profitCellNode.addTaxi(paymentEvent.getMedallion());
                this.nodeList.add(dropOffNode, profitCellNode);
                // we need to decrement the position since it added as last node and there can be -1 profitability nodes
                this.nodeList.decrementPosition(dropOffNode);
            } else {
                ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(dropOffNode);
                profitCellNode.addTaxi(paymentEvent.getMedallion());
                profitCellNode.increaseEmptyTaxi();
                this.nodeList.incrementPosition(dropOffNode);
            }

            while ((this.dropWindow.size() > 0)
                    && (this.dropWindow.peek().isExpired(paymentEvent.getDropOffTime(), Constants.LARGE_WINDOW_SIZE))) {
                PaymentEvent expiredEvent = this.dropWindow.poll();

                if (this.nodeList.containsKey(expiredEvent.getDropOffCell())) {
                    //this cell may have been removed by earlier processing of dropping event due to taxi leaving.
                    //or by payment reduction due to same reasoning.
                    ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(expiredEvent.getDropOffCell());

                    if (profitCellNode.containsTaxi(expiredEvent.getMedallion())) {
                        // there is a possibility that the taxi for this drop event already has gone and received a
                        // payment event.
                        profitCellNode.reduceEmptyTaxi();
                        profitCellNode.removeTaxi(expiredEvent.getMedallion());

                        if (profitCellNode.isEmpty()) {
                            this.nodeList.remove(expiredEvent.getDropOffCell());
                        } else {
                            this.nodeList.decrementPosition(expiredEvent.getDropOffCell());
                        }

                    }
                }
            }
        }

        List<NodeValue> currentList = this.nodeList.getTopValues();
        if (!Util.isSame(preList, currentList) &&
                (paymentEvent.getDropOffTime() - this.startTime > Constants.LARGE_WINDOW_SIZE)) {
            long delay = System.currentTimeMillis() - paymentEvent.getStartTime();
            this.avgDelay = (this.avgDelay * this.numOfEvents + delay) / (this.numOfEvents + 1);
            this.numOfEvents++;
            this.profitEventWriter.processEvent(new TopProfitableEvent(paymentEvent.getStartTime(),
                    paymentEvent.getPickUpTime(), paymentEvent.getDropOffTime(), this.nodeList.getTopValues()));
        }

    }

    public void close() {
        System.out.println("Average delay ==> " + this.avgDelay);
        this.profitEventWriter.close();
    }

}
