package edu.colostate.cs.gc.util;

import edu.colostate.cs.gc.list.NodeValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/25/15
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static boolean isSame(List<NodeValue> list1, List<NodeValue> list2) {
        if (list1.size() != list2.size()) {
            return false;
        } else {
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).equals(list2.get(i))){
                    return false;
                }
            }
        }
        return true;
    }
}
