package edu.colostate.cs.gc.util;

import edu.colostate.cs.gc.list.NodeValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static boolean isSame(List<NodeValue> list1, List<NodeValue> list2) {
        if (list1.size() != list2.size()) {
            return false;
        } else {
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).equals(list2.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method gets the date strings in the format of yyyy-MM-dd HH:mm:ss
     *
     * @param dateString
     * @return
     */
    public static long getTime(String dateString) {

        String[] parts = dateString.split(" ");
        String[] dateParts = parts[0].trim().split("-");
        String[] hourParts = parts[1].trim().split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateParts[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[2]));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourParts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourParts[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(hourParts[2]));

        return calendar.getTime().getTime();
    }

    public static void displayStatistics(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = null;
            String delayString = null;
            int totalLines = 0;
            long totalDelay = 0;
            while ((line = bufferedReader.readLine()) != null) {
                try{
                    delayString = line.substring(line.lastIndexOf(",") + 1);
                    totalDelay = totalDelay + Long.parseLong(delayString);
                    totalLines++;
                } catch (Exception e){
                     break;
                }
            }
            System.out.println("Total lines " + totalLines);
            System.out.println("Average delay " + totalDelay * 1.0 / totalLines);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
