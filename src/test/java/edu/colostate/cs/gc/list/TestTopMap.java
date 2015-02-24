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
public class TestTopMap extends TestCase {

    public void testTopMap() {
        TopMap topMap = new TopMap();
        topMap.add("route1", new CountNode(5, "route1"));
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route2", new CountNode(17, "route2"));
        topMap.incrementPosition("route2");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route3", new CountNode(9, "route3"));
        topMap.incrementPosition("route3");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route4", new CountNode(10, "route4"));
        topMap.incrementPosition("route4");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route5", new CountNode(4, "route5"));
        topMap.incrementPosition("route5");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route6", new CountNode(21, "route6"));
        topMap.incrementPosition("route6");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route7", new CountNode(12, "route7"));
        topMap.incrementPosition("route7");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route8", new CountNode(16, "route8"));
        topMap.incrementPosition("route8");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route9", new CountNode(51, "route9"));
        topMap.incrementPosition("route9");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route10", new CountNode(3, "route10"));
        topMap.incrementPosition("route10");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route11", new CountNode(31, "route11"));
        topMap.incrementPosition("route11");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route12", new CountNode(40, "route12"));
        topMap.incrementPosition("route12");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        CountNode countNode = (CountNode) topMap.get("route10");
        countNode.setValue(62);
        topMap.incrementPosition("route10");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route7");
        countNode.setValue(30);
        topMap.incrementPosition("route7");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route7");
        countNode.setValue(65);
        topMap.incrementPosition("route7");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route7");
        countNode.setValue(65);
        topMap.incrementPosition("route7");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route7");
        countNode.setValue(2);
        topMap.decrementPosition("route7");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route12");
        countNode.setValue(12);
        topMap.decrementPosition("route12");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route12");
        countNode.setValue(1);
        topMap.decrementPosition("route12");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.remove("route2");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.remove("route10");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.remove("route12");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

    }

    public void testEmptyMaps() {
        TopMap topMap = new TopMap();
        topMap.add("route1", new CountNode(5, "route1"));
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route2", new CountNode(17, "route2"));
        topMap.incrementPosition("route2");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.remove("route1");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.remove("route2");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());
    }

    public void testSameValues() {
        TopMap topMap = new TopMap();

        topMap.add("route1", new CountNode(1, "route1"));
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route2", new CountNode(1, "route2"));
        topMap.incrementPosition("route2");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route3", new CountNode(1, "route3"));
        topMap.incrementPosition("route3");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route4", new CountNode(1, "route4"));
        topMap.incrementPosition("route4");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route5", new CountNode(1, "route5"));
        topMap.incrementPosition("route5");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        topMap.add("route6", new CountNode(1, "route6"));
        topMap.incrementPosition("route6");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        CountNode countNode = (CountNode) topMap.get("route5");
        countNode.incrementValue();
        topMap.incrementPosition("route5");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route6");
        countNode.incrementValue();
        topMap.incrementPosition("route6");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

        countNode = (CountNode) topMap.get("route6");
        countNode.decrementValue();
        topMap.remove("route6");
        assertTrue(topMap.checkListConsistency());
        printList(topMap.getTopValues());

    }

    private void printList(List<NodeValue> nodeValues) {
        for (NodeValue nodeValue : nodeValues) {
            System.out.print(nodeValue);
        }
        System.out.println("");
    }
}
