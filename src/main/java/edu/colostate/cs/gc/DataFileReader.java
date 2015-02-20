package edu.colostate.cs.gc;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.DropOffEvent;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.exception.OutlierPointException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataFileReader {

    private int[][] gridLocations;

    private int outsidePoints;
    private int total;

    // http://andrew.hedges.name/experiments/haversine/
    double topLatitude = 41.47943;
    double bottomLatitude = 40.13087;
    double leftLongitude = -74.91952;
    double rightLongitude = -73.13789;

    private EventDataProcessor eventDataProcessor;


    public DataFileReader() {
        this.gridLocations = new int[300][300];
        this.eventDataProcessor = new EventDataProcessor();
    }


    private void loadData() {

        String fileName = "data/sorted_data.csv";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                try {

                    DropOffEvent dropOffEvent = new DropOffEvent();
                    dropOffEvent.setPickUpTime(dateFormat.parse(values[2]).getTime());
                    dropOffEvent.setDropOffTime(dateFormat.parse(values[3]).getTime());
                    Route route = new Route();
                    route.setDropOffCell(getCell(Double.parseDouble(values[6]),Double.parseDouble(values[7])));
                    route.setPickUpCell(getCell(Double.parseDouble(values[8]),Double.parseDouble(values[9])));
                    dropOffEvent.setRoute(route);
                    this.eventDataProcessor.processEvent(dropOffEvent);

                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (OutlierPointException e) {
//                    System.out.println(e.getMessage());
                }
            }

            this.eventDataProcessor.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private Cell getCell(double longitude, double latitude) throws OutlierPointException {
        if ((longitude < this.rightLongitude) && (longitude > this.leftLongitude) &&
                (latitude < this.topLatitude) && (latitude > this.bottomLatitude)) {

            int column = (int) Math.floor(300 * (longitude - this.leftLongitude) / (this.rightLongitude - this.leftLongitude));
            int row = (int) Math.floor(300 * (this.topLatitude - latitude) / (this.topLatitude - this.bottomLatitude));
            return new Cell(column, row);

        } else {
            throw new OutlierPointException(" longitude " + latitude + " latitude " + latitude + " lies out side");
        }
    }


    public static void main(String[] args) {
        new DataFileReader().loadData();
    }
}
