package edu.colostate.cs.gc.list;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/23/15
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestOrderedList extends TestCase {

    public void testTopMap() {
        OrderedList orderedList = new OrderedList();
        orderedList.add("route1", new CountNode(5, "route1"));
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route2", new CountNode(17, "route2"));
        orderedList.decrementPosition("route2");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route3", new CountNode(9, "route3"));
        orderedList.decrementPosition("route3");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route4", new CountNode(10, "route4"));
        orderedList.decrementPosition("route4");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route5", new CountNode(4, "route5"));
        orderedList.decrementPosition("route5");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route6", new CountNode(21, "route6"));
        orderedList.decrementPosition("route6");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route7", new CountNode(12, "route7"));
        orderedList.decrementPosition("route7");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route8", new CountNode(16, "route8"));
        orderedList.decrementPosition("route8");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route9", new CountNode(51, "route9"));
        orderedList.decrementPosition("route9");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route10", new CountNode(3, "route10"));
        orderedList.decrementPosition("route10");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route11", new CountNode(31, "route11"));
        orderedList.decrementPosition("route11");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route12", new CountNode(40, "route12"));
        orderedList.decrementPosition("route12");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        CountNode countNode = (CountNode) orderedList.get("route10");
        countNode.setValue(62);
        orderedList.decrementPosition("route10");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route7");
        countNode.setValue(30);
        orderedList.decrementPosition("route7");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route7");
        countNode.setValue(65);
        orderedList.decrementPosition("route7");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route7");
        countNode.setValue(65);
        orderedList.decrementPosition("route7");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route7");
        countNode.setValue(2);
        orderedList.incrementPosition("route7");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route12");
        countNode.setValue(12);
        orderedList.incrementPosition("route12");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route12");
        countNode.setValue(1);
        orderedList.incrementPosition("route12");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.remove("route2");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.remove("route10");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.remove("route12");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

    }

    public void testEmptyMaps() {
        OrderedList orderedList = new OrderedList();
        orderedList.add("route1", new CountNode(5, "route1"));
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route2", new CountNode(17, "route2"));
        orderedList.decrementPosition("route2");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.remove("route1");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.remove("route2");
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());
    }

    public void testSameValues() {
        OrderedList orderedList = new OrderedList();

        orderedList.add("route1", new CountNode(1, "route1"));
        assertTrue(orderedList.checkListConsistency());
        printList(orderedList.getTopValues());

        orderedList.add("route2", new CountNode(1, "route2"));
        orderedList.decrementPosition("route2");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route2"), 1);
        printList(orderedList.getTopValues());

        orderedList.add("route3", new CountNode(1, "route3"));
        orderedList.decrementPosition("route3");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route3"), 2);
        printList(orderedList.getTopValues());

        orderedList.add("route4", new CountNode(1, "route4"));
        orderedList.decrementPosition("route4");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route4"), 3);
        printList(orderedList.getTopValues());

        orderedList.add("route5", new CountNode(1, "route5"));
        orderedList.decrementPosition("route5");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route5"), 4);
        printList(orderedList.getTopValues());

        orderedList.add("route6", new CountNode(1, "route6"));
        orderedList.decrementPosition("route6");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route6"), 5);
        printList(orderedList.getTopValues());

        CountNode countNode = (CountNode) orderedList.get("route5");
        countNode.incrementValue();
        orderedList.decrementPosition("route5");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route5"), 0);
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route6");
        countNode.incrementValue();
        orderedList.decrementPosition("route6");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route6"), 1);
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route6");
        countNode.decrementValue();
        orderedList.remove("route6");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route1"), 1);
        printList(orderedList.getTopValues());

        countNode = (CountNode) orderedList.get("route5");
        countNode.decrementValue();
        orderedList.incrementPosition("route5");
        assertTrue(orderedList.checkListConsistency());
        assertEquals(orderedList.getPosition("route5"), 0);
        printList(orderedList.getTopValues());



    }

    private void printList(List<NodeValue> nodeValues) {
        for (NodeValue nodeValue : nodeValues) {
            System.out.print(nodeValue);
        }
        System.out.println("");
    }
}
