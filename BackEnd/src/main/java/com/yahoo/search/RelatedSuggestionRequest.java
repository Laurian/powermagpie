package com.yahoo.search;

import com.yahoo.rest.RestRequest;

/**
 * Related search suggestion request.
 *
 * @author Ryan Kennedy
 */
public class RelatedSuggestionRequest extends RestRequest {
    /**
     * Constructs a new related search suggestion request.
     *
     * @param query The query to request related suggestions for. This is the only required attribute.
     */
    public RelatedSuggestionRequest(String query) {
        super("http://search.yahooapis.com/WebSearchService/V1/relatedSuggestion");
        setQuery(query);
    }

    public void setQuery(String query) {
        setParameter("query", query);
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
}