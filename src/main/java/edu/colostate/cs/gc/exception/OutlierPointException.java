package edu.colostate.cs.gc.exception;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/19/15
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class OutlierPointException extends Exception {

    public OutlierPointException() {
    }

    public OutlierPointException(String message) {
        super(message);
    }

    public OutlierPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutlierPointException(Throwable cause) {
        super(cause);
    }

    public OutlierPointException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
