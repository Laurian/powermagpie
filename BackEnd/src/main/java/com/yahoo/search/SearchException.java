package com.yahoo.search;

import java.io.PrintStream;
import java.io.PrintWriter;

// TODO: Make the HTTP response code available.

/**
 * Exception class indicating a search failure caused by either a client error
 * (invalid or missing argument) or a service error.
 *
 * @author Ryan Kennedy
 */
public class SearchException extends Exception {
    private Throwable cause;

    /**
     * Constructs a search exception with a message.
     *
     * @param message The message to wrap inside the exception.
     */
    public SearchException(String message) {
        super(message);
    }

    /**
     * Constructs a search exception with a message and an underlying cause.
     *
     * @param message The message to wrap inside the exception.
     * @param cause The underlying cause of the search exception.
     */
    public SearchException(String message, Throwable cause) {
        super(message);

        this.cause = cause;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (cause != null) {
            System.err.print("Underlying cause: ");
            cause.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.print("Underlying cause: ");
            cause.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.print("Underlying cause: ");
            cause.printStackTrace(s);
        }
    }
}