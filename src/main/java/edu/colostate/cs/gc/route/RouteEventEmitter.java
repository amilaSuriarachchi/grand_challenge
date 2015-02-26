package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.exception.OutlierPointException;
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
 * Date: 2/16/15
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteEventEmitter {


    private void loadData() {

        String fileName = "data/sorted_data.csv";
        try {

            int numberOfBuffers = 4;
            //initialize buffers
            TopRouteProcessor topRouteProcessor = new TopRouteProcessor();
            MessageBuffer[] messageBuffers = new MessageBuffer[numberOfBuffers];
            for (int i = 0; i < messageBuffers.length; i++) {
                 messageBuffers[i] = new MessageBuffer(topRouteProcessor);
            }

            long currentTime = System.currentTimeMillis();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                try {

                    DropOffEvent dropOffEvent = new DropOffEvent();
                    dropOffEvent.setStartTime(System.currentTimeMillis());
                    dropOffEvent.setPickUpTime(dateFormat.parse(values[2]).getTime());
                    dropOffEvent.setDropOffTime(dateFormat.parse(values[3]).getTime());
                    Route route = new Route();
                    route.setPickUpCell(getCell(Double.parseDouble(values[6]), Double.parseDouble(values[7])));
                    route.setDropOffCell(getCell(Double.parseDouble(values[8]), Double.parseDouble(values[9])));
                    dropOffEvent.setRoute(route);
                    int bufferNumber = route.hashCode() % numberOfBuffers;
                    messageBuffers[bufferNumber].addMessage(dropOffEvent);
//                    this.eventDataProcessor.processEvent(dropOffEvent);

                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (OutlierPointException e) {
//                    System.out.println(e.getMessage());
                }
            }

            // finish all buffers
            for (int i = 0; i < messageBuffers.length; i++) {
                messageBuffers[i].setFinish();
            }

            System.out.println("Total time (ms) " + (System.currentTimeMillis() - currentTime));

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            topRouteProcessor.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private Cell getCell(double longitude, double latitude) throws OutlierPointException {
        if ((longitude < Constants.RIGHT_LONGITUDE) && (longitude > Constants.LEFT_LONGITUDE) &&
                (latitude < Constants.TOP_LATITUDE) && (latitude > Constants.BOTTOM_LATITUDE)) {

            int column = (int) Math.floor((longitude - Constants.LEFT_LONGITUDE) / Constants.EAST_CELL_SIZE_500);
            int row = (int) Math.floor((Constants.TOP_LATITUDE - latitude) / Constants.SOUTH_CELL_SIZE_500);
            return new Cell(column, row);

        } else {
            throw new OutlierPointException(" longitude " + latitude + " latitude " + latitude + " lies out side");
        }
    }


    public static void main(String[] args) {
        new RouteEventEmitter().loadData();
    }
}
