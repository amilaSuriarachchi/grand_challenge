package edu.colostate.cs.gc.list;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/27/15
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNodeList extends TestCase {

    public void testNodes(){

        NodeList nodeList = new NodeList();

        nodeList.add("route1", new CountNode(5, "route1"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route2", new CountNode(25, "route2"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route3", new CountNode(7, "route3"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route4", new CountNode(43, "route4"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route5", new CountNode(31, "route5"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route6", new CountNode(2, "route6"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route7", new CountNode(10, "route7"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route8", new CountNode(12, "route8"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route9", new CountNode(1, "route9"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route10", new CountNode(28, "route10"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route11", new CountNode(9, "route11"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route12", new CountNode(72, "route12"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route13", new CountNode(51, "route13"));
        assertTrue(nodeList.checkConsistency());
        nodeList.add("route14", new CountNode(36, "route14"));
        assertTrue(nodeList.checkConsistency());

        printList(nodeList.getTopValues());

        nodeList.remove("route12");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        nodeList.remove("route2");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        nodeList.remove("route6");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        nodeList.remove("route1");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        nodeList.remove("route4");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        nodeList.remove("route5");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());




    }

    private void printList(List<NodeValue> nodeValues) {
        for (NodeValue nodeValue : nodeValues) {
            System.out.print(nodeValue);
        }
        System.out.println("");
    }
}
