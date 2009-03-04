package com.yahoo.search;

/**
 * Related search suggestion results.
 *
 * @author Ryan Kennedy
 */
public interface RelatedSuggestionResults {
    /**
     * The suggestions found for the given search query.
     *
     * @return The suggestions found for the given search query.
     */
    String[] getSuggestions();
}