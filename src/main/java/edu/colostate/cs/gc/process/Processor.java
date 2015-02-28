package edu.colostate.cs.gc.process;

import edu.colostate.cs.gc.event.Event;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/28/15
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Processor {

    public abstract void processEvent(Event event);

    public abstract void close();
}
