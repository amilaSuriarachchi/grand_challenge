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

    public void testRemoveNodes(){

        NodeList nodeList = new NodeList(100);

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

    public void testDecrementPosition(){

        NodeList nodeList = new NodeList(100);

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


        CountNode countNode = (CountNode) nodeList.get("route12");
        countNode.setValue(85);
        nodeList.decrementPosition("route12");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route10");
        countNode.setValue(85);
        nodeList.decrementPosition("route10");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route11");
        countNode.setValue(27);
        nodeList.decrementPosition("route11");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route7");
        countNode.setValue(15);
        nodeList.decrementPosition("route7");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route1");
        countNode.setValue(70);
        nodeList.decrementPosition("route1");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

    }

    public void testIncrementPosition(){
        NodeList nodeList = new NodeList(100);

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


        CountNode countNode = (CountNode) nodeList.get("route12");
        countNode.setValue(4);
        nodeList.incrementPosition("route12");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route3");
        countNode.setValue(6);
        nodeList.incrementPosition("route3");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route3");
        countNode.setValue(5);
        nodeList.incrementPosition("route3");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route3");
        countNode.setValue(4);
        nodeList.incrementPosition("route3");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route14");
        countNode.setValue(3);
        nodeList.incrementPosition("route14");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route5");
        countNode.setValue(20);
        nodeList.incrementPosition("route5");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route3");
        countNode.setValue(0);
        nodeList.incrementPosition("route3");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

        countNode = (CountNode) nodeList.get("route8");
        countNode.setValue(-1);
        nodeList.incrementPosition("route8");
        assertTrue(nodeList.checkConsistency());
        printList(nodeList.getTopValues());

    }

    public void testIncrementWithoutHeap(){
        NodeList nodeList = new NodeList(100);

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

        CountNode countNode = (CountNode) nodeList.get("route5");
        countNode.setValue(20);
        nodeList.incrementPosition("route5");
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
