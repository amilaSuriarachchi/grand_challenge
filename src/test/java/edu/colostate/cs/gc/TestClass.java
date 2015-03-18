package edu.colostate.cs.gc;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestClass implements Comparable<TestClass> {

    private int count;

    public TestClass(int count) {
        this.count = count;
    }

    public int compareTo(TestClass o) {
        if (this.count > o.count) {
            return 1;
        } else if (this.count < o.count) {
            return -1;
        } else {
            return 0;
        }
    }


}
