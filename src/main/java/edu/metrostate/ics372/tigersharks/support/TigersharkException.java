package edu.metrostate.ics372.tigersharks.support;

/**
 * Created by sleig on 3/21/2017.
 */
public class TigersharkException extends Exception {
    public TigersharkException() {
    }

    public TigersharkException(String message) {
        super(message);
    }

    public TigersharkException(Throwable cause) {
        super(cause);
    }

    public TigersharkException(String message, Throwable cause) {
        super(message, cause);
    }

    public TigersharkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
