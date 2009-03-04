package com.yahoo.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Simple REST client.
 *
 * TODO: Handle error codes (http://developer.yahoo.net/documentation/errors.html).
 * TODO: Parse error XML when error occurs.
 * TODO: Add request/response logging.
 *
 * @author Ryan Kennedy
 */
public class RestClient {
    private static final int MAX_URI_LENGTH_FOR_GET = 255;
    private static final int ERROR_READ_BUFFER_SIZE = 1024;
    private static final String USER_AGENT_STRING;
    private static final String DEFAULT_USER_AGENT = "Yahoo-Java-SDK/unknown-version";

    static {
        String ua = DEFAULT_USER_AGENT;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("com.yahoo.rest.RestClient");
            if(bundle != null) {
                ua = bundle.getString("useragent");
            }
        }
        catch (MissingResourceException mre) {
            // It's okay, the bundle just wasn't found. We'll use the default.
        }
        finally {
            USER_AGENT_STRING = ua;
        }
    }

    private RestClient() {
    }

    /**
     * Calls the given REST resource with the given parameters.
     *
     * @param serviceUrl The URL of the REST resource to call.
     * @param parameters The parameters to pass to the call.
     * @return The InputStream to the content returned by the resource.
     * @throws IOException Thrown if a networking issue occurs.
     * @throws RestException Thrown if the call fails. Could be caused by the service or the client. Check the error message for more information.
     */
    public static InputStream call(String serviceUrl, Map parameters) throws IOException, RestException {
        StringBuffer urlString = new StringBuffer(serviceUrl);
        String query = RestClient.buildQueryString(parameters);

        HttpURLConnection conn;
        if((urlString.length() + query.length() + 1) > MAX_URI_LENGTH_FOR_GET) {
            // Request is too big, do a POST
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT_STRING);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.getOutputStream().write(query.getBytes());
        }
        else {
            // Request is small enough it should fit in a GET
            if(query.length() > 0) {
                urlString.append("?").append(query);
            }

            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT_STRING);

            conn.setRequestMethod("GET");
        }

        int responseCode = conn.getResponseCode();
        if (HttpURLConnection.HTTP_OK != responseCode) {
            ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();

            int read;
            byte[] readBuffer = new byte[ERROR_READ_BUFFER_SIZE];
            InputStream errorStream = conn.getErrorStream();
            while (-1 != (read = errorStream.read(readBuffer))) {
                errorBuffer.write(readBuffer, 0, read);
            }

            throw new RestException("Request failed, HTTP " + responseCode + ": " + conn.getResponseMessage(), errorBuffer.toByteArray());
        }

        return conn.getInputStream();
    }

    private static String buildQueryString(Map parameters) throws UnsupportedEncodingException {
        Set parameterNames = parameters.keySet();
        StringBuffer buffer = new StringBuffer();

        for (Iterator iterator = parameterNames.iterator(); iterator.hasNext();) {
            String parameterName = (String) iterator.next();
            Object value = parameters.get(parameterName);
            if(value instanceof String) {
                buffer.append(URLEncoder.encode(parameterName, "UTF-8"));
                buffer.append('=');
                buffer.append(URLEncoder.encode((String) parameters.get(parameterName), "UTF-8"));
            }
            else if(value instanceof List) {
                for (Iterator values = ((List) value).iterator(); values.hasNext();) {
                    String currentValue = (String) values.next();
                    buffer.append(URLEncoder.encode(parameterName, "UTF-8"));
                    buffer.append('=');
                    buffer.append(URLEncoder.encode(currentValue, "UTF-8"));

                    if(values.hasNext()) {
                        buffer.append('&');
                    }
                }
            }

            if (iterator.hasNext()) {
                buffer.append('&');
            }
        }

        return buffer.toString();
    }
}