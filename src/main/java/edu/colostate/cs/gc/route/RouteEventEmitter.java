package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.exception.OutlierPointException;
import edu.colostate.cs.gc.process.EventWriter;
import edu.colostate.cs.gc.process.MessageBuffer;
import edu.colostate.cs.gc.process.TopEventProcessor;
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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

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


    private void loadData(String fileName,
                          MessageBuffer[] messageBuffers,
                          CyclicBarrier barrier,
                          CountDownLatch latch) {

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            String line = null;
            int seqNo = 1;

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BrokenBarrierException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            System.out.println("Start processing messages ...");
            long currentTime = System.currentTimeMillis();

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
                    // catch number format exceptions and do nothing.
                }
            }

            // finish all buffers
            for (int i = 0; i < messageBuffers.length; i++) {
                messageBuffers[i].setFinish();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            long totalTime = System.currentTimeMillis() - currentTime;
            System.out.println("Display statistics ...");
            System.out.println("Total time (ms) " + totalTime);
            System.out.println("Total time (Min) " + totalTime * 1.0 / 60000 );

            bufferedReader.close();

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

        CyclicBarrier barrier = new CyclicBarrier(this.numberOfThreads + 1);
        CountDownLatch latch = new CountDownLatch(this.numberOfThreads);

        MessageBuffer[] messageBuffers = new MessageBuffer[this.numberOfThreads];
        StreamEmitter streamEmitter = new StreamEmitter(this.container);
        for (int i = 0; i < this.numberOfThreads; i++) {
            messageBuffers[i] = new MessageBuffer(streamEmitter, barrier, latch);
        }
        this.loadData(this.fileName, messageBuffers, barrier, latch);
        Util.displayStatistics("top_routes.txt");
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
        int numberOfBuffers = 2;

        CyclicBarrier barrier = new CyclicBarrier(numberOfBuffers + 1);
        CountDownLatch latch = new CountDownLatch(numberOfBuffers);

        TopRouteProcessor topRouteProcessor = new TopRouteProcessor(numberOfBuffers);
        //initialize buffers
        MessageBuffer[] messageBuffers = new MessageBuffer[numberOfBuffers];
        for (int i = 0; i < messageBuffers.length; i++) {
            messageBuffers[i] = new MessageBuffer(new RouteProcessor(topRouteProcessor, numberOfBuffers), barrier, latch);
        }
        new RouteEventEmitter().loadData(args[0], messageBuffers, barrier, latch);
        topRouteProcessor.close();
        Util.displayStatistics("top_routes.txt");
    }
}
