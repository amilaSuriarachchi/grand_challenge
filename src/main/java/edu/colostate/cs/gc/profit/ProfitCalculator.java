package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.*;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.TripProcessor;
import edu.colostate.cs.gc.route.RouteCount;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.api.Container;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/20/15
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitCalculator extends TripProcessor {

    private Queue<PaymentEvent> paymentWindow = new LinkedList<PaymentEvent>();
    private Queue<PaymentEvent> dropWindow = new LinkedList<PaymentEvent>();
    private NodeList nodeList = new NodeList(2048);

    private long startTime;
    private boolean isStarted;

    private Set<Cell> lastCellSet;
    private List<NodeValue> lastCellList;

    private int numOfProcessors;

    public ProfitCalculator() {
        this.lastCellList = new ArrayList<NodeValue>();
        this.lastCellSet = new HashSet<Cell>();
    }

    public ProfitCalculator(TripProcessor tripProcessor, int numOfProcessors) {
        this();
        this.processor = tripProcessor;
        this.numOfProcessors = numOfProcessors;
    }

    @Override
    public void initialise(Container container, Map<String, String> parameters) {
        super.initialise(container, parameters);
        this.numOfProcessors = Integer.parseInt(parameters.get("processors"));
    }

    public void processEvent(TripEvent event) {

        PaymentEvent paymentEvent = (PaymentEvent) event;

        paymentEvent.processDropOffTime();

        if (!this.isStarted) {
            this.isStarted = true;
            this.startTime = paymentEvent.getDropOffTimeMillis();
        }

        if (paymentEvent.isPayEvent()) {

            this.paymentWindow.add(paymentEvent);
            Cell pickUpNode = paymentEvent.getPickUpCell();
            if (!this.nodeList.containsKey(pickUpNode)) {
                // if there is no entry in the nodeList then any taxi drop has not been arrived at this position. So
                // no need to reduce the empty taxis
                ProfitCellNode profitCellNode = new ProfitCellNode(paymentEvent.getFare(), pickUpNode, paymentEvent.getSeqNo());
                //this node does not have a profitability so it won't make any change to top nodes.
                this.nodeList.add(pickUpNode, profitCellNode);
            } else {
                ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(pickUpNode);
                profitCellNode.setSeqNo(paymentEvent.getSeqNo());
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
                    // we need to decrement the position if profitability is higher or equal since
                    // sequence number has changed.
                    this.nodeList.decrementPosition(pickUpNode);
                }

            }

            while ((this.paymentWindow.size() > 0) &&
                    (this.paymentWindow.peek().isExpired(paymentEvent.getDropOffTimeMillis(), Constants.SMALL_WINDOW_SIZE))) {
                PaymentEvent expiredEvent = this.paymentWindow.poll();
                ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(expiredEvent.getPickUpCell());
                double preProfitability = profitCellNode.getProfitability();
                profitCellNode.removeFare(expiredEvent.getFare());

                // here we calculate the profit for the pick up cell. we don't need to do any thing to empty taxis at
                // this point.since this taxi already gone from this cell.

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
                ProfitCellNode profitCellNode = new ProfitCellNode(dropOffNode, paymentEvent.getSeqNo());
                profitCellNode.addTaxi(paymentEvent.getMedallion());
                this.nodeList.add(dropOffNode, profitCellNode);
                // when adding a node node list structure take care about the position.
            } else {
                ProfitCellNode profitCellNode = (ProfitCellNode) this.nodeList.get(dropOffNode);
                profitCellNode.setSeqNo(paymentEvent.getSeqNo());
                profitCellNode.addTaxi(paymentEvent.getMedallion());
                profitCellNode.increaseEmptyTaxi();
                this.nodeList.incrementPosition(dropOffNode);
            }

            while ((this.dropWindow.size() > 0)
                    && (this.dropWindow.peek().isExpired(paymentEvent.getDropOffTimeMillis(), Constants.LARGE_WINDOW_SIZE))) {
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

        if (!Util.isSame(this.lastCellList, currentList) &&
                (paymentEvent.getDropOffTimeMillis() - this.startTime > Constants.SMALL_WINDOW_SIZE)) {

            Set<Cell> currentCellSet = getCellSet(currentList);
            this.lastCellSet.removeAll(currentCellSet);

            TopProfitsEvent topProfitsEvent = new TopProfitsEvent(
                                                paymentEvent.getStartTime(),
                                                paymentEvent.getPickUpTime(),
                                                paymentEvent.getDropOffTimeMillis());

            topProfitsEvent.setSeqNo(paymentEvent.getSeqNo());

            int processorID;
            if (paymentEvent.isPayEvent()){
                processorID = paymentEvent.getPickUpCell().hashCode() % this.numOfProcessors;
            } else {
                processorID = paymentEvent.getDropOffCell().hashCode() % this.numOfProcessors;
            }
            topProfitsEvent.setProcessorID(processorID);
            topProfitsEvent.setRemovedKeys(this.lastCellSet);

            for (NodeValue nodeValue : currentList){
               ProfitCellNode profitCellNode = (ProfitCellNode) nodeValue;
                topProfitsEvent.addNodeValue(
                        new TopProfitCellNode(profitCellNode.getProfitability(),
                                              profitCellNode.getMidFare(),
                                              profitCellNode.getNumOfEmptyTaxis(),
                                              profitCellNode.getSeqNo(),
                                              profitCellNode.getCell()));
            }

            this.processor.processEvent(topProfitsEvent);
            this.lastCellSet = currentCellSet;
        }
        this.lastCellList = currentList;

    }

    private Set<Cell> getCellSet(List<NodeValue> cellList) {
        Set<Cell> cells = new HashSet<Cell>();
        for (NodeValue nodeValue : cellList) {
            cells.add(((ProfitCellNode) nodeValue).getCell());
        }
        return cells;
    }

    public void close() {
    }

}
