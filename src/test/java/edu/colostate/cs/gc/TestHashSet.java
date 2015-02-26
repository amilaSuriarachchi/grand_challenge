package edu.colostate.cs.gc;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestHashSet {

    public static void main(String[] args) {
        List<TestClass> messages = new ArrayList<TestClass>();

        messages.add(new TestClass(5));
        messages.add(new TestClass(4));
        messages.add(new TestClass(10));
        messages.add(new TestClass(9));

        Collections.sort(messages);
        System.out.println("OK");
    }
}
