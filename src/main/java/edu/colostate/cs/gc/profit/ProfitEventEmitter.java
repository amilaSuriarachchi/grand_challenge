package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.PaymentEvent;
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
 * Date: 2/24/15
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitEventEmitter {

    private ProfitCalculator profitCalculator;

    public ProfitEventEmitter() {
        this.profitCalculator = new ProfitCalculator();
    }

    private void loadData() {

        String fileName = "data/sorted_data.csv";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long currentTime = System.currentTimeMillis();

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    PaymentEvent paymentEvent = new PaymentEvent();
                    paymentEvent.setStartTime(System.currentTimeMillis());
                    paymentEvent.setMedallion(values[0].trim());
                    paymentEvent.setPickUpTime(dateFormat.parse(values[2]).getTime());
                    paymentEvent.setDropOffTime(dateFormat.parse(values[3]).getTime());
                    paymentEvent.setPickUpCell(getCell(Double.parseDouble(values[6]), Double.parseDouble(values[7])));
                    paymentEvent.setDropOffCell(getCell(Double.parseDouble(values[8]), Double.parseDouble(values[9])));
                    paymentEvent.setFare(Double.parseDouble(values[11]) + Double.parseDouble(values[14]));
                    paymentEvent.setPayEvent(true);
                    this.profitCalculator.processEvent(paymentEvent);
                    paymentEvent.setPayEvent(false);
                    this.profitCalculator.processEvent(paymentEvent);

                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (OutlierPointException e) {
//                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Total time (ms) " + (System.currentTimeMillis() - currentTime));
            this.profitCalculator.close();

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
