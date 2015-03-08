package edu.colostate.cs.gc.util;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.route.RouteCount;
import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/3/15
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestUtil extends TestCase {

    public void testDateParsing() {

        String dateString = "2014-05-01 22:05:06";
        long timeFromUtil = Util.getTime(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long timeFromSimpleFormat = dateFormat.parse(dateString).getTime();
            assertEquals(timeFromUtil, timeFromSimpleFormat);
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public static void main(String[] args) {

        Route route = new Route(new Cell(1, 5), new Cell(2, 7));
        NodeValue routeCount = new RouteCount(5, route, 34);

        RouteCount routeCount1 = (RouteCount) routeCount.getClone();
        System.out.println("OK");

    }


}
