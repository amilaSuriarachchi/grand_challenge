package edu.colostate.cs.gc.event;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import junit.framework.TestCase;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/2/15
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDropOffEvent extends TestCase {

    public void testParsing(){
        DropOffEvent dropOffEvent = new DropOffEvent();

        dropOffEvent.setPickUpTime("2013-01-01 00:01:00");
        dropOffEvent.setDropOffTime("2013-01-01 00:02:00");
        dropOffEvent.setStartTime(234);

        Cell dropOffCell = new Cell(23,345);
        Cell pickUpCell = new Cell(341,34);
        Route route = new Route(pickUpCell, dropOffCell);

        dropOffEvent.setRoute(route);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);

        try {
            dropOffEvent.serialize(dataOutput);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            DataInput dataInput = new DataInputStream(byteArrayInputStream);

            DropOffEvent result = new DropOffEvent();
            result.parse(dataInput);

            assertEquals(dropOffEvent.getRoute(), result.getRoute());

        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
