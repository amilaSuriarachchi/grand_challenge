package edu.colostate.cs.gc.profit;

import junit.framework.TestCase;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/23/15
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestProfitCellNode extends TestCase {

    public void testFare() {
        ProfitCellNode profitCellNode = new ProfitCellNode(20.0);
        assertEquals(profitCellNode.getMidFare(), 20.0);

        profitCellNode.addFare(25.0);
        assertEquals(profitCellNode.getMidFare(), 22.5);

        profitCellNode.addFare(17.0);
        assertEquals(profitCellNode.getMidFare(), 20.0);

        profitCellNode.addFare(14.0);
        assertEquals(profitCellNode.getMidFare(), 18.5);

        profitCellNode.addFare(22.0);
        profitCellNode.addFare(18.0);
        assertEquals(profitCellNode.getMidFare(), 19.0);

        profitCellNode.removeFare(14.0);
        assertEquals(profitCellNode.getMidFare(), 20.0);

        profitCellNode.removeFare(22.0);
        assertEquals(profitCellNode.getMidFare(), 19.0);

        profitCellNode.removeFare(18.0);
        assertEquals(profitCellNode.getMidFare(), 20.0);

        profitCellNode.removeFare(20.0);
        assertEquals(profitCellNode.getMidFare(), 21.0);

        profitCellNode.removeFare(17.0);
        assertEquals(profitCellNode.getMidFare(), 25.0);

        profitCellNode.removeFare(25.0);
        assertEquals(profitCellNode.getMidFare(), 0.0);





    }


}
