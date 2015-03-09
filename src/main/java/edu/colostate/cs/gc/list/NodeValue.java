package edu.colostate.cs.gc.list;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/21/15
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NodeValue   {

    public int compare(NodeValue value);

    public Object getKey();

    public void update(NodeValue value);

    public NodeValue getClone();

}
