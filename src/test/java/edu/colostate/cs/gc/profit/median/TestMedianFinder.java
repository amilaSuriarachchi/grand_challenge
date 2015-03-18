package edu.colostate.cs.gc.profit.median;

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/18/15
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMedianFinder extends TestCase {

    public void testAddUnique(){

        MedianFinder medianFinder = new MedianFinder();
        medianFinder.add(20.0);
        assertEquals(medianFinder.getMedian(), 20.0);

        medianFinder.add(25.0);
        assertEquals(medianFinder.getMedian(), 22.5);

        medianFinder.add(17.0);
        assertEquals(medianFinder.getMedian(), 20.0);

        medianFinder.add(14.0);
        assertEquals(medianFinder.getMedian(), 18.5);

        medianFinder.add(22.0);
        medianFinder.add(18.0);
        assertEquals(medianFinder.getMedian(), 19.0);
    }

    public void testAddDuplicate(){
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.add(25.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(25.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(26.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(25.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(26.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(26.0);
        assertEquals(medianFinder.getMedian(), 25.5);
        medianFinder.add(26.0);
        assertEquals(medianFinder.getMedian(), 26.0);
        medianFinder.add(26.0);
        assertEquals(medianFinder.getMedian(), 26.0);
        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 26.0);
        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 25.5);
        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);

        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);

        medianFinder.add(23.0);
        assertEquals(medianFinder.getMedian(), 24.5);
        medianFinder.add(23.0);
        assertEquals(medianFinder.getMedian(), 24.0);
        medianFinder.add(23.0);
        assertEquals(medianFinder.getMedian(), 24.0);

        medianFinder.add(28.0);
        assertEquals(medianFinder.getMedian(), 24.0);
        medianFinder.add(28.0);
        assertEquals(medianFinder.getMedian(), 24.5);
        medianFinder.add(28.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.add(28.0);
        assertEquals(medianFinder.getMedian(), 25.0);
    }

    public void testRemoveUnique(){
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.add(20.0);
        medianFinder.add(25.0);
        medianFinder.add(17.0);
        medianFinder.add(14.0);
        medianFinder.add(22.0);
        medianFinder.add(18.0);

        medianFinder.remove(14.0);
        assertEquals(medianFinder.getMedian(), 20.0);

        medianFinder.remove(22.0);
        assertEquals(medianFinder.getMedian(), 19.0);

        medianFinder.remove(18.0);
        assertEquals(medianFinder.getMedian(), 20.0);

        medianFinder.remove(20.0);
        assertEquals(medianFinder.getMedian(), 21.0);

        medianFinder.remove(17.0);
        assertEquals(medianFinder.getMedian(), 25.0);

        medianFinder.remove(25.0);
        assertEquals(medianFinder.getMedian(), 0.0);
    }

    public void testRemoveDuplicate(){
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.add(25.0);
        medianFinder.add(25.0);

        medianFinder.add(26.0);

        medianFinder.add(25.0);

        medianFinder.add(26.0);
        medianFinder.add(26.0);
        medianFinder.add(26.0);
        medianFinder.add(26.0);

        medianFinder.add(24.0);
        medianFinder.add(24.0);
        medianFinder.add(24.0);

        medianFinder.add(24.0);
        medianFinder.add(24.0);
        medianFinder.add(24.0);
        medianFinder.add(24.0);

        medianFinder.add(23.0);
        medianFinder.add(23.0);
        medianFinder.add(23.0);

        medianFinder.add(28.0);
        medianFinder.add(28.0);
        medianFinder.add(28.0);
        medianFinder.add(28.0);

        medianFinder.remove(26.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.remove(28.0);
        assertEquals(medianFinder.getMedian(), 24.5);
        medianFinder.remove(28.0);
        assertEquals(medianFinder.getMedian(), 24.0);
        medianFinder.remove(28.0);
        assertEquals(medianFinder.getMedian(), 24.0);
        medianFinder.remove(24.0);
        assertEquals(medianFinder.getMedian(), 24.0);
        medianFinder.remove(24.0);
        assertEquals(medianFinder.getMedian(), 24.5);
        medianFinder.remove(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.remove(25.0);
        assertEquals(medianFinder.getMedian(), 24.5);



        medianFinder.remove(23.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.remove(23.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.remove(24.0);
        assertEquals(medianFinder.getMedian(), 25.0);
        medianFinder.remove(24.0);
        assertEquals(medianFinder.getMedian(), 25.5);
        medianFinder.remove(24.0);
        assertEquals(medianFinder.getMedian(), 26.0);

        medianFinder.remove(26.0);
        assertEquals(medianFinder.getMedian(), 25.5);

        medianFinder.remove(25.0);
        assertEquals(medianFinder.getMedian(), 26.0);

        medianFinder.remove(25.0);
        assertEquals(medianFinder.getMedian(), 26.0);

        medianFinder.remove(26.0);
        assertEquals(medianFinder.getMedian(), 26.0);

        medianFinder.remove(26.0);
        assertEquals(medianFinder.getMedian(), 25.0);

        medianFinder.remove(26.0);
        assertEquals(medianFinder.getMedian(), 24.0);



    }
}
