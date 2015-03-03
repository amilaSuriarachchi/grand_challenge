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
    private long updatedTime;
    private Route route;

    public TopRouteCount() {
    }

    public TopRouteCount(int count, Route route, long updatedTime) {
        this.count = count;
        this.route = route;
        this.updatedTime = updatedTime;
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

    public int compare(NodeValue value) {
        TopRouteCount topRouteCount = (TopRouteCount) value;
        if (this.count > topRouteCount.count){
            return 1;
        } else if (this.count < topRouteCount.count){
            return -1;
        } else {
            // i.e counts are equal we need to check for updated times
            if (this.updatedTime > topRouteCount.updatedTime){
                return 1;
            } else if (this.updatedTime < topRouteCount.updatedTime){
                return -1;
            }  else {
                return 0;
            }
        }
    }


    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.count);
            dataOutput.writeLong(this.updatedTime);
            this.route.serialize(dataOutput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }


    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.count = dataInput.readInt();
            this.updatedTime = dataInput.readLong();
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

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
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



}
