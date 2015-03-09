package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.PaymentEvent;
import edu.colostate.cs.gc.exception.OutlierPointException;
import edu.colostate.cs.gc.process.MessageBuffer;
import edu.colostate.cs.gc.route.RouteProcessor;
import edu.colostate.cs.gc.route.TopRouteProcessor;
import edu.colostate.cs.gc.util.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/24/15
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitEventEmitter {


    public ProfitEventEmitter() {
    }

    private void loadData() {

        String fileName = "data/sorted_data.csv";
        try {

            int numberOfBuffers = 2;
            //initialize buffers
            MessageBuffer[] messageBuffers = new MessageBuffer[numberOfBuffers];
            for (int i = 0; i < messageBuffers.length; i++) {
                messageBuffers[i] = new MessageBuffer(new ProfitCalculator());
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int seqNo = 0;

            long currentTime = System.currentTimeMillis();

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    PaymentEvent pickUpEvent = new PaymentEvent();
                    pickUpEvent.setSeqNo(seqNo);
                    seqNo++;

                    pickUpEvent.setStartTime(System.currentTimeMillis());
                    pickUpEvent.setMedallion(values[0].trim());
                    pickUpEvent.setPickUpTime(dateFormat.parse(values[2]).getTime());
                    pickUpEvent.setDropOffTime(dateFormat.parse(values[3]).getTime());
                    pickUpEvent.setPickUpCell(getCell(Double.parseDouble(values[6]), Double.parseDouble(values[7])));
                    pickUpEvent.setDropOffCell(getCell(Double.parseDouble(values[8]), Double.parseDouble(values[9])));
                    pickUpEvent.setFare(Double.parseDouble(values[11]) + Double.parseDouble(values[14]));
                    pickUpEvent.setPayEvent(true);
                    int bufferNumber = pickUpEvent.getPickUpCell().hashCode() % numberOfBuffers;
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

                    bufferNumber = dropOffEvent.getDropOffCell().hashCode() % numberOfBuffers;
                    messageBuffers[bufferNumber].addMessage(dropOffEvent);

                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (OutlierPointException e) {
                }
            }

            for (int i = 0; i < messageBuffers.length; i++) {
                messageBuffers[i].setFinish();
            }

            System.out.println("Total time (ms) " + (System.currentTimeMillis() - currentTime));

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private Cell getCell(double longitude, double latitude) throws OutlierPointException {
        if ((longitude < Constants.RIGHT_LONGITUDE) && (longitude > Constants.LEFT_LONGITUDE) &&
                (latitude < Constants.TOP_LATITUDE) && (latitude > Constants.BOTTOM_LATITUDE)) {

            int column = (int) Math.floor((longitude - Constants.LEFT_LONGITUDE) / Constants.EAST_CELL_SIZE_250);
            int row = (int) Math.floor((Constants.TOP_LATITUDE - latitude) / Constants.SOUTH_CELL_SIZE_250);
            return new Cell(column, row);

        } else {
            throw new OutlierPointException(" longitude " + latitude + " latitude " + latitude + " lies out side");
        }
    }

    public static void main(String[] args) {
        new ProfitEventEmitter().loadData();
    }
}
