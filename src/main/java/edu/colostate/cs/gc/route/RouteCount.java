package edu.colostate.cs.gc.route;

import edu.colostate.cs.gc.event.Route;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/23/15
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCount implements NodeValue {

    private int count;
    private int seqNo;
    private Route route;

    public RouteCount() {
    }

    public RouteCount(int count, Route route, int seqNo) {
        this.count = count;
        this.route = route;
        this.seqNo = seqNo;
    }

    public void incrementCount(){
        this.count++;
    }

    public void decrementCount(){
        this.count--;
    }

    public boolean isEmpty(){
        return this.count == 0;
    }

    public Object getKey() {
        return this.route;
    }

    public void update(NodeValue value) {
        RouteCount newValue = (RouteCount) value;
        this.count = newValue.count;
        this.seqNo = newValue.seqNo;
    }

    public int compare(NodeValue value) {
        RouteCount routeCount = (RouteCount) value;
        if (this.count > routeCount.count){
            return 1;
        } else if (this.count < routeCount.count){
            return -1;
        } else {
            // i.e counts are equal we need to check for updated times
            if (this.seqNo > routeCount.seqNo){
                return 1;
            } else if (this.seqNo < routeCount.seqNo){
                return -1;
            }  else {
                return 0;
            }
        }
    }


    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.count);
            dataOutput.writeInt(this.seqNo);
            this.route.serialize(dataOutput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }


    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.count = dataInput.readInt();
            this.seqNo = dataInput.readInt();
            this.route = new Route();
            this.route.parse(dataInput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }


    public int getCount() {
        return count;
    }

    public Route getRoute() {
        return route;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteCount that = (RouteCount) o;

        if (count != that.count) return false;
        if (seqNo != that.seqNo) return false;
        if (!route.equals(that.route)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (int) (seqNo ^ (seqNo >>> 32));
        result = 31 * result + route.hashCode();
        return result;
    }

    public NodeValue getClone() {
        return new RouteCount(this.count, this.route, this.seqNo);
    }

    public boolean isValid() {
        return true;
    }
}
