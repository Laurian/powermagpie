package com.yahoo.myweb2;

import java.math.BigInteger;

public interface RelatedTagsResults {
    /**
     * The number of tags available.
     *
     * @return The number of tags found by the search.
     */
    BigInteger getTotalResultsAvailable();

    /**
     * The number of tags returned. This may be lower than the number of
     * tags requested if there were fewer tags available.
     *
     * @return The number of tags returned.
     */
    BigInteger getTotalResultsReturned();

    /**
     * The position of the first tag.
     *
     * @return The position of the first tag.
     */
    BigInteger getFirstResultPosition();

    /**
     * The list of tags from the request.
     *
     * @return The list of tags from the request.
     */
    RelatedTagsResult[] listResults();
}