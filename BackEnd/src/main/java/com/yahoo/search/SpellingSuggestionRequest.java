package com.yahoo.search;

import com.yahoo.rest.RestRequest;

/**
 * Spelling suggestion request object.
 *
 * @author Ryan Kennedy
 */
public class SpellingSuggestionRequest extends RestRequest {
    /**
     * Constructs a new spelling suggestion request.
     *
     * @param query The query to request suggestions for.
     */
    public SpellingSuggestionRequest(String query) {
        super("http://search.yahooapis.com/WebSearchService/V1/spellingSuggestion");
        setQuery(query);
    }

    public void setQuery(String query) {
        setParameter("query", query);
    }
}