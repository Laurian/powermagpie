package com.yahoo.search;

/**
 * Spelling suggestion results.
 *
 * @author Ryan Kennedy
 */
public interface SpellingSuggestionResults {
    /**
     * The text of the suggested correction.
     *
     * @return The text of the suggested correction.
     */
    String getSuggestion();
}