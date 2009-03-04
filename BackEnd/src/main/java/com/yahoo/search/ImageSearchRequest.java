package com.yahoo.search;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

/**
 * Image search request object.
 *
 * @author Ryan Kennedy
 */
public class ImageSearchRequest extends RestRequest {
    private static final String DEFAULT_REQUEST_URL = "http://search.yahooapis.com/ImageSearchService/V1/imageSearch";

    /**
     * Constructs a new image search request.
     *
     * @param query The query to search for. This is the only required attribute.
     */
    public ImageSearchRequest(String query) {
        super(DEFAULT_REQUEST_URL);
        setQuery(query);
    }

    /**
     * The image query to search for.
     *
     * @param query The image query to search for.
     */
    public void setQuery(String query) {
        setParameter("query", query);
    }

    /**
     * Sets the type of the search. At the time of writing, three options are available:
     * <p/>
     * <ul>
     * <li><b>all (default)</b>: returns results with all query terms</li>
     * <li><b>any</b>: returns results with one or more of the query terms</li>
     * <li><b>phrase</b>: returns results containing the query terms as a phrase</li>
     * </ul>
     *
     * @param type The type of search to execute.
     */
    public void setType(String type) {
        setParameter("type", type);
    }

    /**
     * The maximum number of results to return. May return fewer results if there aren't enough results
     * in the database. At the time of writing, the default value is 10, the maximum value is 50.
     *
     * @param results The maximum number of results to return.
     */
    public void setResults(int results) {
        setParameter("results", Integer.toString(results));
    }

    /**
     * The starting result position. This number should increment by the value of the results
     * argument each time the user chooses the next page of results. The default is 1.
     *
     * @param start The starting result position.
     */
    public void setStart(BigInteger start) {
        setParameter("start", start.toString(10));
    }

    /**
     * Specifies the kind of image file to search for. At the time of writing, the following
     * options are available: all (default), bmp, gif, jpeg, png.
     *
     * @param format Specifies the kind of image file to search for.
     */
    public void setFormat(String format) {
        setParameter("format", format);
    }

    /**
     * Specifies if adult related results are permitted. Defaults to false.
     *
     * @param adultOk Specifies if adult related results are permitted.
     */
    public void setAdultOk(boolean adultOk) {
        if (adultOk) {
            setParameter("adult_ok", "1");
        } else {
            clearParameter("adult_ok");
        }
    }

    /**
     * The service returns only the images of the coloration specified (color or black-and-white).
     *
     * @param coloration The coloration of the results to return. Default is any.
     */
    public void setColoration(String coloration) {
        setParameter("coloration", coloration);
    }

    /**
     * A domain to restrict your searches to (e.g. www.yahoo.com). You may submit up to
     * 30 values.
     *
     * @param site The domain to restrict your searches to.
     */
    public void addSite(String site) {
        addParameter("site", site);
    }
}