package com.yahoo.java;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Extension of the java.lang.Error class to provide the Java 1.4
 * embedded cause functionality on platforms that don't already provide it.
 *
 * @author Ryan Kennedy
 */
public class ExtendedError extends Error {
    /**
     * The underlying cause of the error.
     */
    private Throwable cause;

    /**
     * Constructs an error with an underlying cause.
     *
     * @param cause The underlying cause of the error.
     */
    public ExtendedError(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Constructs an error with an underlying cause and a message
     *
     * @param message The message of the error.
     * @param cause The underlying cause of the error.
     */
    public ExtendedError(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Implements printStackTrace so that it prints the stack trace for
     * both this error and the underyling cause, if there is one.
     */
    public void printStackTrace() {
        super.printStackTrace();
        if(cause != null) {
            System.err.print("Underlying cause: ");
            cause.printStackTrace();
        }
    }

    /**
     * Implements printStackTrace so that it prints the stack trace for
     * both this error and the underyling cause, if there is one.
     *
     * @param s The print stream to print the stack trace to.
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if(cause != null) {
            s.print("Underlying cause: ");
            cause.printStackTrace(s);
        }
    }

    /**
     * Implements printStackTrace so that it prints the stack trace for
     * both this error and the underyling cause, if there is one.
     *
     * @param s The print writer to write the stack trace to. 
     */
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if(cause != null) {
            s.print("Underlying cause: ");
            cause.printStackTrace(s);
        }
    }
}