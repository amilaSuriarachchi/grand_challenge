package edu.colostate.cs.gc.event;

import edu.colostate.cs.worker.data.Event;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/28/15
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TripEvent extends Event {

    // this is the sequence number of the event which causes this event to fire from event generator
    protected int seqNo;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
}
