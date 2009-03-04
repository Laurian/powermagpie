package com.yahoo.search;

import java.math.BigInteger;

/**
 * Local search results.
 *
 * @author Ryan Kennedy
 */
public interface LocalSearchResults {
    /**
     * The number of query matches in the database.
     *
     * @return The number of query matches in the database.
     */
    BigInteger getTotalResultsAvailable();

    /**
     * The number of query matches returned. This may be lower than the number of results requested if there were
     * fewer total results available.
     *
     * @return The number of query matches returned.
     */
    BigInteger getTotalResultsReturned();

    /**
     * The position of the first result in the overall search.
     *
     * @return The position of the first result in the overall search.
     */
    BigInteger getFirstResultPosition();

    /**
     * The list (in order) of results from the search.
     *
     * @return The list (in order) of results from the search.
     */
    LocalSearchResult[] listResults();

    /**
     *  The URL of a webpage containing a map graphic with all returned results plotted on it.
     *
     * @return  The URL of a webpage containing a map graphic with all returned results plotted on it.
     */
    String getResultsMapUrl();
}