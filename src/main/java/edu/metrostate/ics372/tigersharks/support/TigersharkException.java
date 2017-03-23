package edu.metrostate.ics372.tigersharks.support;

/**
 * this really isnt implemented. I wanted to have a custom exception class for logging and stuff but logging isnt really a thing either
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
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
