package com.yahoo.rest;

/**
 * Class encapsulating a wide range of REST-related errors.
 *
 * @author Ryan Kennedy
 */
public class RestException extends Exception {
    private byte[] errorMessage;

    private RestException() {
    }

    /**
     * Constructs a rest exception with a message and the error content
     * returned by the server.
     *
     * @param message The message to wrap inside the exception.
     * @param errorMessage The error content sent back by the server.
     */
    public RestException(String message, byte[] errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the error content sent back by the server.
     *
     * @return The error content sent back by the server.
     */
    public byte[] getErrorMessage() {
        return errorMessage;
    }
}