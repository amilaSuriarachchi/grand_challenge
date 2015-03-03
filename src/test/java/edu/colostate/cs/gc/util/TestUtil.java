package edu.colostate.cs.gc.util;

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

    public void testDateParsing(){

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


}
