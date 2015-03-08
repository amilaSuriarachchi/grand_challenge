package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.TripEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.event.TopRoutesEvent;
import edu.colostate.cs.gc.list.NodeList;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.TripProcessor;
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
public class TopRouteProcessor extends TripProcessor {

    private NodeList nodeList;

    private BufferedWriter eventWriter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private double avgDelay = 0;
    private int numOfEvents = 0;

    private int eventsWritten = 0;

    private int numbOfProcessors;
    private List<Queue<TopRoutesEvent>> queues;

    private List<NodeValue> lastRoutesList;

    public TopRouteProcessor() {
        this.nodeList = new NodeList();
        this.lastRoutesList = new ArrayList<NodeValue>();
        try {
            this.eventWriter = new BufferedWriter(new FileWriter("top_routs.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public TopRouteProcessor(int numbOfProcessors) {
        this();
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

        this.queues = new ArrayList<Queue<TopRoutesEvent>>(this.numbOfProcessors);
        for (int i = 0; i < this.numbOfProcessors; i++) {
            this.queues.add(new LinkedList<TopRoutesEvent>());
        }
    }

    public synchronized void processEvent(TripEvent event) {
        TopRoutesEvent topRoutesEvent = (TopRoutesEvent) event;
        this.queues.get(topRoutesEvent.getProcessorID()).add(topRoutesEvent);

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
        for (Queue<TopRoutesEvent> queue : this.queues) {
            if (queue.isEmpty()) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }

    private void processOrderedMessage(TopRoutesEvent topRoutesEvent) {

        //check the sequence
//        List<NodeValue> preList = this.nodeList.getTopValues();

        // remove old routes
        for (Route route : topRoutesEvent.getRemovedRoutes()) {
            this.nodeList.remove(route);
        }

        // add new values
        for (TopRouteCount routeCount : topRoutesEvent.getNewRoutes()) {

            if (!this.nodeList.containsKey(routeCount.getRoute())) {
                // need to create a new object to avoid conflicts with earlier process objects.
                this.nodeList.add(routeCount.getRoute(), routeCount);
            } else {
                TopRouteCount existingValue = (TopRouteCount) this.nodeList.get(routeCount.getRoute());
                if (routeCount.getCount() < existingValue.getCount()) {
                    existingValue.setCount(routeCount.getCount());
                    existingValue.setSeqNo(routeCount.getSeqNo());
                    //if the new value is less it has to move further down.
                    this.nodeList.incrementPosition(routeCount.getRoute());
                } else if (routeCount.getCount() > existingValue.getCount()) {
                    existingValue.setCount(routeCount.getCount());
                    existingValue.setSeqNo(routeCount.getSeqNo());
                    this.nodeList.decrementPosition(routeCount.getRoute());
                } else {
                    // still need to set the last updated time.
                    existingValue.setSeqNo(routeCount.getSeqNo());
                    this.nodeList.decrementPosition(routeCount.getRoute());
                }
            }
        }

        List<NodeValue> newList = this.nodeList.getTopValues();
        if (!Util.isSame(this.lastRoutesList, newList)) {
            generateRouteChangeEvent(topRoutesEvent.getStartTime(),
                    topRoutesEvent.getPickUpTime(), topRoutesEvent.getDropOffTime(), newList);
            this.eventsWritten++;
        }
        this.lastRoutesList = newList;
    }

    public void generateRouteChangeEvent(long startTime, String pickUpTime, long dropOffTime, List<NodeValue> nodeValues) {

        try {
            long delay = System.currentTimeMillis() - startTime;
            this.avgDelay = (this.avgDelay * this.numOfEvents + delay) / (this.numOfEvents + 1);
            this.numOfEvents++;

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

    public void close() {

        System.out.println("Avg Delay ==> " + this.avgDelay);
        flushExistingMessages();
        System.out.println("Event received ==> " + this.eventsWritten);

        try {
            this.eventWriter.flush();
            this.eventWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
        for (Queue<TopRoutesEvent> queue : this.queues) {
            if (!queue.isEmpty()) {
                isOneNotEmpty = true;
                break;
            }
        }
        return isOneNotEmpty;
    }
}
