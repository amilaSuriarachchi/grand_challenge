package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.exception.OutlierPointException;
import edu.colostate.cs.gc.process.MessageBuffer;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteEventEmitter implements Adaptor {

    private Container container;
    private String fileName;
    private int numberOfThreads;


    private void loadData(String fileName, MessageBuffer[] messageBuffers) {

        try {
            int errorLines = 0;
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            long currentTime = System.currentTimeMillis();
            int eventCount = 0;

            int seqNo = 1;

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                try {

                    DropOffEvent dropOffEvent = new DropOffEvent();
                    dropOffEvent.setStartTime(System.currentTimeMillis());
                    dropOffEvent.setPickUpTime(values[2]);
                    dropOffEvent.setDropOffTime(values[3]);
                    dropOffEvent.setSeqNo(seqNo);
                    seqNo++;

                    Route route = new Route();
                    //this cell processing seems to be taking some time. But lets keep it since this is required
                    //to calculate event.
                    route.setPickUpCell(getCell(Double.parseDouble(values[6]), Double.parseDouble(values[7])));
                    route.setDropOffCell(getCell(Double.parseDouble(values[8]), Double.parseDouble(values[9])));
                    if ((route.getDropOffCell() != null) && (route.getPickUpCell() != null)) {
                        dropOffEvent.setRoute(route);
                        int bufferNumber = route.hashCode() % messageBuffers.length;
                        messageBuffers[bufferNumber].addMessage(dropOffEvent);
                    }

                } catch (Exception e) {
                    errorLines++;
                }

                eventCount++;
            }

            // finish all buffers
            for (int i = 0; i < messageBuffers.length; i++) {
                messageBuffers[i].setFinish();
            }

            System.out.println("Total time (ms) " + (System.currentTimeMillis() - currentTime));
            System.out.println("Total error lines " + errorLines);
            System.out.println("Event count " + eventCount);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void start() {
        MessageBuffer[] messageBuffers = new MessageBuffer[this.numberOfThreads];
        StreamEmitter streamEmitter = new StreamEmitter(this.container);
        for (int i = 0; i < this.numberOfThreads; i++) {
            messageBuffers[i] = new MessageBuffer(streamEmitter);
        }
        this.loadData(this.fileName, messageBuffers);
    }

    public void initialise(Container container, Map<String, String> parameterMap) {
        this.container = container;
        this.fileName = parameterMap.get("fileName");
        this.numberOfThreads = Integer.parseInt(parameterMap.get("threads"));
    }

    private Cell getCell(double longitude, double latitude) {
        if ((longitude < Constants.RIGHT_LONGITUDE) && (longitude > Constants.LEFT_LONGITUDE) &&
                (latitude < Constants.TOP_LATITUDE) && (latitude > Constants.BOTTOM_LATITUDE)) {

            int column = (int) Math.floor((longitude - Constants.LEFT_LONGITUDE) / Constants.EAST_CELL_SIZE_500);
            int row = (int) Math.floor((Constants.TOP_LATITUDE - latitude) / Constants.SOUTH_CELL_SIZE_500);
            return new Cell(column, row);

        } else {
            return null;
        }
    }


    public static void main(String[] args) {
        int numberOfBuffers = 4;
        TopRouteProcessor topRouteProcessor = new TopRouteProcessor(numberOfBuffers);
        //initialize buffers
        MessageBuffer[] messageBuffers = new MessageBuffer[numberOfBuffers];
        for (int i = 0; i < messageBuffers.length; i++) {
            messageBuffers[i] = new MessageBuffer(new RouteProcessor(topRouteProcessor, numberOfBuffers));
        }
        new RouteEventEmitter().loadData(args[0], messageBuffers);
        topRouteProcessor.close();
    }
}
