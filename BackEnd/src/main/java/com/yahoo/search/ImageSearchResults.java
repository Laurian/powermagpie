package com.yahoo.search;

import java.math.BigInteger;

/**
 * Image search results and results metadata.
 *
 * @author Ryan Kennedy
 */ 
public interface ImageSearchResults {
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
    ImageSearchResult[] listResults();
}