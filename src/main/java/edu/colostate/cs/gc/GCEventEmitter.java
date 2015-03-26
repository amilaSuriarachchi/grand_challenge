package edu.colostate.cs.gc;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.PaymentEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.process.MessageBuffer;
import edu.colostate.cs.gc.profit.ProfitCalculator;
import edu.colostate.cs.gc.profit.TopProfitProcessor;
import edu.colostate.cs.gc.route.RouteProcessor;
import edu.colostate.cs.gc.route.TopRouteProcessor;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/24/15
 * Time: 9:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class GCEventEmitter {

    private void loadData(String fileName,
                          MessageBuffer[] routeBuffers,
                          MessageBuffer[] profitBuffers,
                          CyclicBarrier barrier,
                          CountDownLatch latch) {

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

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

                try {
                    String[] values = line.split(",");

                    // first process the longitude and latitude coordinates.
                    double pickUpLongitude = Double.parseDouble(values[6]);
                    double pickUpLatitude = Double.parseDouble(values[7]);
                    double dropOffLongitude = Double.parseDouble(values[8]);
                    double dropOffLatitude = Double.parseDouble(values[9]);

                    // processing route event.
                    DropOffEvent routeEvent = new DropOffEvent();
                    routeEvent.setStartTime(System.currentTimeMillis());
                    routeEvent.setPickUpTime(values[2]);
                    routeEvent.setDropOffTime(values[3]);
                    routeEvent.setSeqNo(seqNo);
                    seqNo++;

                    Route route = new Route();
                    //this cell processing seems to be taking some time. But lets keep it since this is required
                    //to calculate event.
                    route.setPickUpCell(getCell(pickUpLongitude, pickUpLatitude));
                    route.setDropOffCell(getCell(dropOffLongitude, dropOffLatitude));
                    if ((route.getDropOffCell() != null) && (route.getPickUpCell() != null)) {
                        routeEvent.setRoute(route);
                        int bufferNumber = route.hashCode() % routeBuffers.length;
                        routeBuffers[bufferNumber].addMessage(routeEvent);
                    }

                    // processing payment event.
                    PaymentEvent pickUpEvent = new PaymentEvent();
                    pickUpEvent.setSeqNo(seqNo);
                    seqNo++;

                    pickUpEvent.setStartTime(System.currentTimeMillis());
                    pickUpEvent.setMedallion(values[0].trim());
                    pickUpEvent.setPickUpTime(values[2]);
                    pickUpEvent.setDropOffTime(values[3]);
                    pickUpEvent.setPickUpCell(getSmallCell(pickUpLongitude, pickUpLatitude));
                    pickUpEvent.setDropOffCell(getSmallCell(dropOffLongitude, dropOffLatitude));

                    if ((pickUpEvent.getPickUpCell() != null) && (pickUpEvent.getDropOffCell() != null)) {

                        double fare = Double.parseDouble(values[11]) + Double.parseDouble(values[14]);
                        pickUpEvent.setFare(Math.round(fare * 100) / 100.0);
                        pickUpEvent.setPayEvent(true);

                        int bufferNumber;

                        if (pickUpEvent.getFare() > 0) {
                            bufferNumber = pickUpEvent.getPickUpCell().hashCode() % profitBuffers.length;
                            profitBuffers[bufferNumber].addMessage(pickUpEvent);

                            PaymentEvent dropOffEvent = new PaymentEvent();
                            dropOffEvent.setSeqNo(seqNo);
                            seqNo++;

                            dropOffEvent.setStartTime(System.currentTimeMillis());
                            dropOffEvent.setMedallion(pickUpEvent.getMedallion());
                            dropOffEvent.setPickUpTime(pickUpEvent.getPickUpTime());
                            dropOffEvent.setDropOffTime(pickUpEvent.getDropOffTime());
                            dropOffEvent.setPickUpCell(pickUpEvent.getPickUpCell());
                            dropOffEvent.setDropOffCell(pickUpEvent.getDropOffCell());
                            dropOffEvent.setFare(pickUpEvent.getFare());
                            dropOffEvent.setPayEvent(false);

                            bufferNumber = dropOffEvent.getDropOffCell().hashCode() % profitBuffers.length;
                            profitBuffers[bufferNumber].addMessage(dropOffEvent);
                        }
                    }

                } catch (Exception e) {
                    // catch number format exceptions and do nothing.
                }
            }

            for (int i = 0; i < routeBuffers.length; i++) {
                routeBuffers[i].setFinish();
            }

            for (int i = 0; i < profitBuffers.length; i++) {
                profitBuffers[i].setFinish();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            long totalTime = System.currentTimeMillis() - currentTime;
            System.out.println("Execution time (both Queries) ...");
            System.out.println("Execution time (ms) " + totalTime);
            System.out.println("Execution time (Min) " + totalTime * 1.0 / 60000);

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

    private Cell getSmallCell(double longitude, double latitude) {
        if ((longitude < Constants.RIGHT_LONGITUDE) && (longitude > Constants.LEFT_LONGITUDE) &&
                (latitude < Constants.TOP_LATITUDE) && (latitude > Constants.BOTTOM_LATITUDE)) {

            int column = (int) Math.floor((longitude - Constants.LEFT_LONGITUDE) / Constants.EAST_CELL_SIZE_250);
            int row = (int) Math.floor((Constants.TOP_LATITUDE - latitude) / Constants.SOUTH_CELL_SIZE_250);
            return new Cell(column, row);

        } else {
            return null;
        }
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
        int numOfRouteBuffers = Integer.parseInt(args[1]);
        int numOfProfitBuffers = Integer.parseInt(args[2]);

        CyclicBarrier barrier = new CyclicBarrier(numOfRouteBuffers + numOfProfitBuffers + 1);
        CountDownLatch latch = new CountDownLatch(numOfRouteBuffers + numOfProfitBuffers);

        TopRouteProcessor topRouteProcessor = new TopRouteProcessor(numOfRouteBuffers);
        TopProfitProcessor topProfitProcessor = new TopProfitProcessor(numOfProfitBuffers);
        //initialize buffers
        MessageBuffer[] routeBuffers = new MessageBuffer[numOfRouteBuffers];
        for (int i = 0; i < routeBuffers.length; i++) {
            routeBuffers[i] = new MessageBuffer(
                    new RouteProcessor(topRouteProcessor, numOfRouteBuffers), barrier, latch);
        }

        MessageBuffer[] profitBuffers = new MessageBuffer[numOfProfitBuffers];
        for (int i = 0; i < profitBuffers.length; i++) {
            profitBuffers[i] = new MessageBuffer(
                    new ProfitCalculator(topProfitProcessor, numOfProfitBuffers, 1), barrier, latch);
        }


        new GCEventEmitter().loadData(args[0], routeBuffers, profitBuffers, barrier, latch);

        topRouteProcessor.close();
        System.out.println("Route query delay ...");
        Util.displayStatistics("top_routes.txt");
        System.out.println("Profit cell query delay ...");
        topProfitProcessor.close();
        Util.displayStatistics("top_profit_cells.txt");

    }
}
