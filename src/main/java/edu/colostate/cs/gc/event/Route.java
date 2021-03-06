package edu.colostate.cs.gc.event;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Route implements Comparable<Route> {

    private Cell pickUpCell;
    private Cell dropOffCell;

    public Route() {
    }

    public Route(Cell pickUpCell, Cell dropOffCell) {
        this.pickUpCell = pickUpCell;
        this.dropOffCell = dropOffCell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;

        Route route = (Route) o;

        if (!dropOffCell.equals(route.dropOffCell)) return false;
        if (!pickUpCell.equals(route.pickUpCell)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pickUpCell.hashCode();
        result = 31 * result + dropOffCell.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.pickUpCell.toString() + "," + this.dropOffCell.toString() + ",";
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        this.dropOffCell.serialize(dataOutput);
        this.pickUpCell.serialize(dataOutput);
    }


    public void parse(DataInput dataInput) throws MessageProcessingException {
        this.dropOffCell = new Cell();
        this.dropOffCell.parse(dataInput);
        this.pickUpCell = new Cell();
        this.pickUpCell.parse(dataInput);
    }

    public int compareTo(Route o) {
        if (this.dropOffCell.compareTo(o.dropOffCell) == 0){
            return this.pickUpCell.compareTo(o.pickUpCell);
        } else {
            return this.dropOffCell.compareTo(o.dropOffCell);
        }
    }

    public Cell getPickUpCell() {
        return pickUpCell;
    }

    public void setPickUpCell(Cell pickUpCell) {
        this.pickUpCell = pickUpCell;
    }

    public Cell getDropOffCell() {
        return dropOffCell;
    }

    public void setDropOffCell(Cell dropOffCell) {
        this.dropOffCell = dropOffCell;
    }
}
