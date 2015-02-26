package edu.colostate.cs.gc.heap;

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/26/15
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPriorityHeap extends TestCase {

    public void testAddElements(){

        HeapElement heapElement1 = new HeapElement(new CountNode(5));
        HeapElement heapElement2 = new HeapElement(new CountNode(10));
        HeapElement heapElement3 = new HeapElement(new CountNode(21));
        HeapElement heapElement4 = new HeapElement(new CountNode(4));
        HeapElement heapElement5 = new HeapElement(new CountNode(17));
        HeapElement heapElement6 = new HeapElement(new CountNode(15));
        HeapElement heapElement7 = new HeapElement(new CountNode(13));
        HeapElement heapElement8 = new HeapElement(new CountNode(8));
        HeapElement heapElement9 = new HeapElement(new CountNode(55));
        HeapElement heapElement10 = new HeapElement(new CountNode(31));

        PriorityHeap priorityHeap = new PriorityHeap();
        priorityHeap.add(heapElement1);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement2);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement3);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement4);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement5);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement6);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement7);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement8);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement9);
        assertTrue(priorityHeap.checkHeap());
        priorityHeap.add(heapElement10);
        assertTrue(priorityHeap.checkHeap());

        // change
        CountNode countNode = (CountNode) heapElement4.getNodeValue();
        countNode.setValue(2);
        priorityHeap.moveDown(heapElement4);
        assertTrue(priorityHeap.checkHeap());

        countNode = (CountNode) heapElement4.getNodeValue();
        countNode.setValue(21);
        priorityHeap.moveUp(heapElement4);
        assertTrue(priorityHeap.checkHeap());

        countNode = (CountNode) heapElement5.getNodeValue();
        countNode.setValue(80);
        priorityHeap.moveUp(heapElement5);
        assertTrue(priorityHeap.checkHeap());

        priorityHeap.remove(heapElement5);
        assertTrue(priorityHeap.checkHeap());

        countNode = (CountNode) heapElement7.getNodeValue();
        countNode.setValue(1);
        priorityHeap.moveDown(heapElement7);
        assertTrue(priorityHeap.checkHeap());


        HeapElement sortedElement = null;
        while ((sortedElement = priorityHeap.extractMax()) != null ){
            System.out.print(sortedElement.getNodeValue() + ",");
        }



    }
}
