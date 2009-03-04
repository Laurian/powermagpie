package com.yahoo.rest;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * REST request base class.
 *
 * @author Ryan Kennedy
 */
public class RestRequest {
    private Map parameters;
    private String requestUrl;

    /**
     * Constructs a REST request with the given URL.
     *
     * @param defaultRequestUrl The URL of the REST service.
     */
    public RestRequest(String defaultRequestUrl) {
        parameters = new HashMap();
        setRequestUrl(defaultRequestUrl);
    }

    /**
     * Gets the list of parameters to send to the REST service.
     *
     * @return The list of parameters to send to the REST service.
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * Sets a parameter within the request. Overwrites any preexisting
     * value.
     *
     * @param name The name of the parameter to set.
     * @param value The value of the parameter to set.
     */
    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    /**
     * Adds a parameter value to a list.
     *
     * @param name The name of the parameter list to add to.
     * @param value The value to add to the parameter list.
     */
    public void addParameter(String name, String value) {
        List params = (List) parameters.get(name);
        if(params == null) {
            params = new ArrayList();
            parameters.put(name, params);
        }
        params.add(value);
    }

    /**
     * Removes a parameter from the list.
     *
     * @param name The name of the parameter to remove from the list.
     */
    public void clearParameter(String name) {
        parameters.remove(name);
    }

    /**
     * Gets the request URL for the REST request.
     *
     * @return The request URL for the REST request.
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * Sets the URL for the REST request, overriding the default
     * set in the constructor.
     *
     * @param requestUrl The new URL for the REST request.
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}