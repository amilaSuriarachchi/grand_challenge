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
 * Date: 3/2/15
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopRouteCount implements NodeValue {

    private int count;
    //this is the event sequence number this pirticular route is updated last.
    private int seqNo;
    private Route route;

    public TopRouteCount() {
    }

    public TopRouteCount(int count, Route route, int seqNo) {
        this.count = count;
        this.route = route;
        this.seqNo = seqNo;
    }

    public Object getKey() {
        return this.route;
    }

    public void update(NodeValue value) {
        TopRouteCount newValue = (TopRouteCount) value;
        this.count = newValue.count;
        this.seqNo = newValue.seqNo;
    }

    public int compare(NodeValue value) {
        TopRouteCount topRouteCount = (TopRouteCount) value;
        if (this.count > topRouteCount.count){
            return 1;
        } else if (this.count < topRouteCount.count){
            return -1;
        } else {
            // i.e counts are equal we need to check for updated times
            if (this.seqNo > topRouteCount.seqNo){
                return 1;
            } else if (this.seqNo < topRouteCount.seqNo){
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

    public void setCount(int count) {
        this.count = count;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopRouteCount that = (TopRouteCount) o;

        if (!route.equals(that.route)) return false;

        return true;
    }

    public int hashCode() {
        return route.hashCode();
    }

    public NodeValue getClone() {
        return new TopRouteCount(this.count, this.route, this.seqNo);
    }

    public boolean isValid() {
        return true;
    }
}
