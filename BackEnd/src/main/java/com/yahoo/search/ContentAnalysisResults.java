package com.yahoo.search;

/**
 * Content analysis (term extraction) results.
 *
 * @author Ryan Kennedy
 */
public interface ContentAnalysisResults {
    /**
     * The terms extracted from the content.
     *
     * @return The terms extracted from the content.
     */
    String[] getExtractedTerms();
}