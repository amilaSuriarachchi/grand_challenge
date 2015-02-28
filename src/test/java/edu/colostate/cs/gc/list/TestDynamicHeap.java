package edu.colostate.cs.gc.list;

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDynamicHeap extends TestCase {

    public void testAddElements() {

        CountNode heapNode1 = new CountNode(5, "route1");
        CountNode heapNode2 = new CountNode(10, "route2");
        CountNode heapNode3 = new CountNode(21, "route3");
        CountNode heapNode4 = new CountNode(4, "route4");
        CountNode heapNode5 = new CountNode(17, "route5");
        CountNode heapNode6 = new CountNode(15, "route6");
        CountNode heapNode7 = new CountNode(13, "route7");
        CountNode heapNode8 = new CountNode(8, "route8");
        CountNode heapNode9 = new CountNode(55, "route9");
        CountNode heapNode10 = new CountNode(31, "route10");

        DynamicHeap dynamicHeap = new DynamicHeap();

        dynamicHeap.add("route1", heapNode1);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route2", heapNode2);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route3", heapNode3);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route4", heapNode4);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route5", heapNode5);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route6", heapNode6);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route7", heapNode7);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route8", heapNode8);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route9", heapNode9);
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.add("route10", heapNode10);
        assertTrue(dynamicHeap.checkHeap());

        // change
        CountNode countNode = (CountNode) dynamicHeap.get("route4");
        countNode.setValue(2);
        dynamicHeap.moveDown("route4");
        assertTrue(dynamicHeap.checkHeap());

        countNode = (CountNode) dynamicHeap.get("route4");
        countNode.setValue(21);
        dynamicHeap.moveUp("route4");
        assertTrue(dynamicHeap.checkHeap());

        countNode = (CountNode) dynamicHeap.get("route5");
        countNode.setValue(80);
        dynamicHeap.moveUp("route5");
        assertTrue(dynamicHeap.checkHeap());

        dynamicHeap.remove("route5");
        assertTrue(dynamicHeap.checkHeap());

        countNode = (CountNode) dynamicHeap.get("route7");
        countNode.setValue(1);
        dynamicHeap.moveDown("route7");
        assertTrue(dynamicHeap.checkHeap());


        NodeValue sortedElement = null;
        while ((sortedElement = dynamicHeap.extractMax()) != null) {
            System.out.print(sortedElement + ",");
        }

    }

    public void testRemoveFromZero(){
        DynamicHeap dynamicHeap = new DynamicHeap();
        dynamicHeap.add("route1", new CountNode(13, "route1"));
        dynamicHeap.add("route2", new CountNode(10, "route2"));
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.remove("route2");
        assertTrue(dynamicHeap.checkHeap());
        dynamicHeap.remove("route1");
        assertTrue(dynamicHeap.checkHeap());
    }
}
