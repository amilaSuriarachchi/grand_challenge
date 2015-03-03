package edu.colostate.cs.gc.util;

import edu.colostate.cs.gc.list.NodeValue;

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
                if (!list1.get(i).equals(list2.get(i))){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method gets the date strings in the format of yyyy-MM-dd HH:mm:ss
     * @param dateString
     * @return
     */
    public static long getTime(String dateString){

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
}
