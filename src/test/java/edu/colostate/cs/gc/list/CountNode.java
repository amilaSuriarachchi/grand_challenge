package edu.colostate.cs.gc.list;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/23/15
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountNode implements NodeValue {

    private int value;
    private String route;

    public CountNode(int value, String route) {
        this.value = value;
        this.route = route;
    }

    public Object getKey() {
        return this.route;
    }

    public void update(NodeValue value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementValue(){
        this.value++;
    }

    public void decrementValue(){
        this.value--;
    }

    public int compare(NodeValue value) {
        CountNode newNode = (CountNode) value;
        if (this.value > newNode.value){
            return 1;
        } else if (this.value < newNode.value){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "(" + this.route + "," + this.value + "),";
    }

    public NodeValue getClone() {
        return new CountNode(this.value, this.route);
    }
}
