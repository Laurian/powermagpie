package com.yahoo.search;

import com.yahoo.rest.RestRequest;

/**
 * Content analyis request.
 *
 * @author Ryan Kennedy
 */
public class ContentAnalysisRequest extends RestRequest {
    private static final String DEFAULT_REQUEST_URL = "http://search.yahooapis.com/ContentAnalysisService/V1/termExtraction";

    /**
     * Constructs a new content analysis request.
     *
     * @param context The context to extract terms from.
     */
    public ContentAnalysisRequest(String context) {
        super(DEFAULT_REQUEST_URL);
        setContext(context);
    }

    /**
     * The context to extract terms from.
     *
     * @param context The context to extract terms from.
     */
    public void setContext(String context) {
        setParameter("context", context);
    }

    /**
     * An optional query to help with the extraction process.
     *
     * @param query An optional query to help with the extraction process.
     */
    public void setQuery(String query) {
        setParameter("query", query);
    }
}