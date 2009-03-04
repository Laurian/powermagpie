package com.yahoo.myweb2;

import java.math.BigInteger;

public interface UrlSearchResults {
    /**
     * The number of URLs available.
     *
     * @return The number of URLs found by the search.
     */
    BigInteger getTotalResultsAvailable();

    /**
     * The number of URLs returned. This may be lower than the number of
     * URLs requested if there were fewer URLs available.
     *
     * @return The number of URLs returned.
     */
    BigInteger getTotalResultsReturned();

    /**
     * The position of the first URL.
     *
     * @return The position of the first URL.
     */
    BigInteger getFirstResultPosition();

    /**
     * The list of URLs from the request.
     *
     * @return The list of URLs from the request.
     */
    UrlSearchResult[] listResults();
}