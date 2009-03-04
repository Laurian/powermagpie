package com.yahoo.shopping;

import java.math.BigInteger;
import java.util.List;

public interface ProductSearchResults {
    /**
     * The number of products available.
     *
     * @return The number of products found by the search.
     */
    BigInteger getTotalResultsAvailable();

    /**
     * The number of products returned. This may be lower than the number of
     * products requested if there were fewer products available.
     *
     * @return The number of products returned.
     */
    BigInteger getTotalResultsReturned();

    /**
     * The position of the first product.
     *
     * @return The position of the first product.
     */
    BigInteger getFirstResultPosition();

    /**
     * Gets any spelling suggestions for the query given.
     *
     * @return Spelling suggestions for the query given or null if
     *         none were given.
     */
    String getSpellSuggestions();

    /**
     * The list of products from the request. The list returned may contain
     * a mixture of com.yahoo.shopping.Catalog and com.yahoo.shopping.Offer
     * objects.
     *
     * @return The list of products from the request.
     */
    List listResults();
}