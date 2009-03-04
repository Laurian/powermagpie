package com.yahoo.search;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

/**
 * Video search request object.
 *
 * @author Ryan Kennedy
 */
public class VideoSearchRequest extends RestRequest {
    /**
     * Constructs a new video search request.
     *
     * @param query The query to search for. This is the only required attribute.
     */
    public VideoSearchRequest(String query) {
        super("http://search.yahooapis.com/VideoSearchService/V1/videoSearch");
        setQuery(query);
    }

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
     * Specifies the kind of video file to search for. At the time of writing, the following
     * options are available: all (default), avi, flash, mpeg, msmedia, quicktime, realmedia.
     *
     * @param format Specifies the kind of video file to search for.
     */
    public void addFormat(String format) {
        addParameter("format", format);
    }

    /**
     * Specifies if adult related results are permitted. Defaults to false.
     *
     * @param adultOk Specifies if adult related results are permitted.
     */
    public void setAdultOk(boolean adultOk) {
        if (adultOk) {
            setParameter("adult_ok", "1");
        }
        else {
            clearParameter("adult_ok");
        }
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