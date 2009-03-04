package com.yahoo.search;

import com.yahoo.rest.RestRequest;

/**
 * Context search request object.
 *
 * @author Ryan Kennedy
 */
public class ContextSearchRequest extends RestRequest {
    /**
     * Constructs a new image search request.
     *
     * @param context  The context to extract meaning from. This is the only required attribute.
     */
    public ContextSearchRequest(String context) {
        super("http://search.yahooapis.com/WebSearchService/V1/contextSearch");
        setContext(context);
    }

    /**
     *  The context to extract meaning from.
     *
     * @param context  The context to extract meaning from.
     */
    public void setContext(String context) {
        setParameter("context", context);
    }

    /**
     * The query to search for.
     *
     * @param query The query to search for.
     */
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

    /**
     * The starting result position. This number should increment by the value of the results
     * argument each time the user chooses the next page of results. The default is 1.
     *
     * @param start The starting result position.
     */
    public void setStart(int start) {
        setParameter("start", Integer.toString(start));
    }

    /**
     * Specifies the kind of content to search for. At the time of writing, the following
     * options are available: any, html, msword, pdf, ppt, rss, txt and xls.
     *
     * @param format Specifies the kind of content to search for.
     */
    public void setFormat(String format) {
        setParameter("format", format);
    }

    /**
     * Specifies if adult related results are permitted. Defaults to false.
     *
     * @param adultOk Specifies if adult related results are permitted.
     */
    public void setAdultOk(boolean adultOk) {
        if(adultOk) {
            setParameter("adult_ok", "1");
        }
        else {
            clearParameter("adult_ok");
        }
    }

    /**
     * Specifies whether to allow multiple results with similar content.
     *
     * @param similarOk  Specifies whether to allow multiple results with similar content.
     */
    public void setSimilarOk(boolean similarOk) {
        if(similarOk) {
            setParameter("similar_ok", "1");
        }
        else {
            clearParameter("similar_ok");
        }
    }

    /**
     * The language the results are written in.
     *
     * @param language  The language the results are written in.
     */
    public void setLanguage(String language) {
        setParameter("languate", language);
    }

    /**
     * The country code for the country the website is located in.
     *
     * @param country The country code for the country the website is located in.
     */
    public void setCountry(String country) {
        setParameter("country", country);
    }

    /**
     * A domain to restrict your searches to (e.g. www.yahoo.com). You may submit up to 30 values.
     *
     * @param site The domain to restrict your searches to.
     */
    public void addSite(String site) {
        addParameter("site", site);
    }

    /**
     * The Creative Commons license that the contents are licensed under. You may submit multiple values.
     *
     * @param license  The Creative Commons license that the contents are licensed under. You may submit multiple values.
     */
    public void addLicense(String license) {
        addParameter("license", license);
    }
}