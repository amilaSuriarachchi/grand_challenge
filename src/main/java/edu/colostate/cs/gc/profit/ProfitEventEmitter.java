package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.PaymentEvent;
import edu.colostate.cs.gc.process.MessageBuffer;
import edu.colostate.cs.gc.route.StreamEmitter;
import edu.colostate.cs.gc.util.Constants;
import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

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

    private void loadData(String fileName, MessageBuffer[] messageBuffers) {

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            int seqNo = 0;

            long currentTime = System.currentTimeMillis();

            while ((line = bufferedReader.readLine()) != null) {
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

                    pickUpEvent.setFare(Double.parseDouble(values[11]) + Double.parseDouble(values[14]));
                    pickUpEvent.setPayEvent(true);
                    int bufferNumber = pickUpEvent.getPickUpCell().hashCode() % messageBuffers.length;
                    messageBuffers[bufferNumber].addMessage(pickUpEvent);

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

            for (int i = 0; i < messageBuffers.length; i++) {
                messageBuffers[i].setFinish();
            }

            System.out.println("Total time (ms) " + (System.currentTimeMillis() - currentTime));

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
        int numberOfBuffers = 2;
        TopProfitProcessor topProfitProcessor = new TopProfitProcessor(numberOfBuffers);
        //initialize buffers
        MessageBuffer[] messageBuffers = new MessageBuffer[numberOfBuffers];
        for (int i = 0; i < messageBuffers.length; i++) {
            messageBuffers[i] = new MessageBuffer(new ProfitCalculator(topProfitProcessor, numberOfBuffers));
        }
        new ProfitEventEmitter().loadData(args[0], messageBuffers);
        topProfitProcessor.close();
    }
}
