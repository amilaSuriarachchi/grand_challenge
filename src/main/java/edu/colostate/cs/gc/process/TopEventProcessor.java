package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.TopEvent;
import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.api.Container;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopEventProcessor extends TripProcessor {

    private NodeList nodeList;

    private EventWriter eventWriter;

    private int numbOfProcessors;
    private List<Queue<TopEvent>> queues;

    private List<NodeValue> lastRoutesList;

    public TopEventProcessor(EventWriter eventWriter) {
        this.nodeList = new NodeList(64);
        this.lastRoutesList = new ArrayList<NodeValue>();
        this.eventWriter = eventWriter;

    }

    public TopEventProcessor(int numbOfProcessors, EventWriter eventWriter) {
        this(eventWriter);
        this.numbOfProcessors = numbOfProcessors;
        initializeQueues();
    }

    @Override
    public void initialise(Container container, Map<String, String> parameters) {
        super.initialise(container, parameters);
        this.numbOfProcessors = Integer.parseInt(parameters.get("processors"));
        initializeQueues();
    }

    private void initializeQueues() {

        this.queues = new ArrayList<Queue<TopEvent>>(this.numbOfProcessors);
        for (int i = 0; i < this.numbOfProcessors; i++) {
            this.queues.add(new LinkedList<TopEvent>());
        }
    }

    public synchronized void processEvent(TripEvent event) {

        TopEvent topEvent = (TopEvent) event;
        this.queues.get(topEvent.getProcessorID()).add(topEvent);

        int minIndex = 0;
        int minSeq = 0;
        while (!isEmpty()) {
            minIndex = 0;
            minSeq = this.queues.get(0).peek().getSeqNo();
            for (int i = 1; i < this.numbOfProcessors; i++) {
                if (this.queues.get(i).peek().getSeqNo() < minSeq) {
                    minIndex = i;
                    minSeq = this.queues.get(i).peek().getSeqNo();
                }
            }
            processOrderedMessage(this.queues.get(minIndex).poll());
        }
    }

    private boolean isEmpty() {
        boolean isEmpty = false;
        for (Queue<TopEvent> queue : this.queues) {
            if (queue.isEmpty()) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }

    private void processOrderedMessage(TopEvent topEvent) {


        // remove old routes
        for (Object key : topEvent.getRemovedKeys()) {
            this.nodeList.remove(key);
        }

        // add new values
        for (NodeValue nodeValue : topEvent.getNewValueList()) {

            if (!this.nodeList.containsKey(nodeValue.getKey())) {
                // need to create a new object to avoid conflicts with earlier process objects.
                this.nodeList.add(nodeValue.getKey(), nodeValue);
            } else {
                NodeValue existingValue = this.nodeList.get(nodeValue.getKey());
                if (nodeValue.compare(existingValue) == -1) {
                    existingValue.update(nodeValue);
                    //if the new value is less it has to move further down.
                    this.nodeList.incrementPosition(nodeValue.getKey());
                } else {
                    // if it is not less either can same or increased.
                    existingValue.update(nodeValue);
                    this.nodeList.decrementPosition(nodeValue.getKey());
                }
            }
        }

        List<NodeValue> newList = this.nodeList.getTopValues();
        if (!Util.isSame(this.lastRoutesList, newList)) {
            this.eventWriter.writeLine(topEvent.getStartTime(), topEvent.getPickUpTime(), topEvent.getDropOffTime(), newList);
        }
        this.lastRoutesList = newList;
    }

    public void close() {

        flushExistingMessages();
        this.eventWriter.close();

    }

    private void flushExistingMessages() {

        int minIndex;
        int minSeq;
        while (isOneNotEmpty()) {
            minIndex = Integer.MIN_VALUE;
            minSeq = Integer.MAX_VALUE;
            for (int i = 0; i < this.numbOfProcessors; i++) {
                if (!this.queues.get(i).isEmpty() && (this.queues.get(i).peek().getSeqNo() < minSeq)) {
                    minIndex = i;
                    minSeq = this.queues.get(i).peek().getSeqNo();
                }
            }
            processOrderedMessage(this.queues.get(minIndex).poll());
        }
    }

    private boolean isOneNotEmpty() {
        boolean isOneNotEmpty = false;
        for (Queue<TopEvent> queue : this.queues) {
            if (!queue.isEmpty()) {
                isOneNotEmpty = true;
                break;
            }
        }
        return isOneNotEmpty;
    }

}
