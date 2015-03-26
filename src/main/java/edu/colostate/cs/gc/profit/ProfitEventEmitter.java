package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.PaymentEvent;
import edu.colostate.cs.gc.process.MessageBuffer;
import edu.colostate.cs.gc.route.StreamEmitter;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.gc.util.Util;
import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/24/15
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitEventEmitter implements Adaptor {

    private Container container;
    private String fileName;
    private int numberOfThreads;

    public ProfitEventEmitter() {
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
        Util.displayStatistics("top_profit_cells.txt");
    }

    public void initialise(Container container, Map<String, String> parameterMap) {
        this.container = container;
        this.fileName = parameterMap.get("fileName");
        this.numberOfThreads = Integer.parseInt(parameterMap.get("threads"));
    }

    private void loadData(String fileName, MessageBuffer[] messageBuffers, CyclicBarrier barrier, CountDownLatch latch) {

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            int seqNo = 1;
            int numberOfEvents = 0;

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

                    PaymentEvent pickUpEvent = new PaymentEvent();
                    pickUpEvent.setSeqNo(seqNo);
                    seqNo++;

                    pickUpEvent.setStartTime(System.currentTimeMillis());
                    pickUpEvent.setMedallion(values[0].trim());
                    pickUpEvent.setPickUpTime(values[2]);
                    pickUpEvent.setDropOffTime(values[3]);
                    pickUpEvent.setPickUpCell(getCell(Double.parseDouble(values[6]), Double.parseDouble(values[7])));
                    pickUpEvent.setDropOffCell(getCell(Double.parseDouble(values[8]), Double.parseDouble(values[9])));

                    if ((pickUpEvent.getPickUpCell() != null) && (pickUpEvent.getDropOffCell() != null)) {

                        double fare = Double.parseDouble(values[11]) + Double.parseDouble(values[14]);
                        pickUpEvent.setFare(Math.round(fare * 100)/ 100.0);
                        pickUpEvent.setPayEvent(true);

                        int bufferNumber;

                        if (pickUpEvent.getFare() > 0){
                            bufferNumber = pickUpEvent.getPickUpCell().hashCode() % messageBuffers.length;
                            messageBuffers[bufferNumber].addMessage(pickUpEvent);
                            numberOfEvents++;

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

                            bufferNumber = dropOffEvent.getDropOffCell().hashCode() % messageBuffers.length;
                            messageBuffers[bufferNumber].addMessage(dropOffEvent);
                        }


                    }

                } catch (Exception e) {
                    // catch number format exceptions and do nothing.
                }
            }

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
            System.out.println("Execution time (ms) " + totalTime);
            System.out.println("Execution time (Min) " + totalTime * 1.0 / 60000);
            System.out.println("Throughput (Msg/s) " + numberOfEvents * 1000.0 / totalTime);

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

    private Cell getCell(double longitude, double latitude) {
        if ((longitude < Constants.RIGHT_LONGITUDE) && (longitude > Constants.LEFT_LONGITUDE) &&
                (latitude < Constants.TOP_LATITUDE) && (latitude > Constants.BOTTOM_LATITUDE)) {

            int column = (int) Math.floor((longitude - Constants.LEFT_LONGITUDE) / Constants.EAST_CELL_SIZE_250);
            int row = (int) Math.floor((Constants.TOP_LATITUDE - latitude) / Constants.SOUTH_CELL_SIZE_250);
            return new Cell(column, row);

        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        int numberOfBuffers = Integer.parseInt(args[1]);
        int windowSize = Integer.parseInt(args[2]);

        CyclicBarrier barrier = new CyclicBarrier(numberOfBuffers + 1);
        CountDownLatch latch = new CountDownLatch(numberOfBuffers);

        TopProfitProcessor topProfitProcessor = new TopProfitProcessor(numberOfBuffers);
        //initialize buffers
        MessageBuffer[] messageBuffers = new MessageBuffer[numberOfBuffers];
        for (int i = 0; i < messageBuffers.length; i++) {
            messageBuffers[i] = new MessageBuffer(
                                new ProfitCalculator(topProfitProcessor, numberOfBuffers, windowSize), barrier, latch);
        }
        new ProfitEventEmitter().loadData(args[0], messageBuffers, barrier, latch);
        topProfitProcessor.close();
        Util.displayStatistics("top_profit_cells.txt");
    }
}
