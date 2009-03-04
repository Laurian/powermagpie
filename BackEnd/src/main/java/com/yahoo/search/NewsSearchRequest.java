package com.yahoo.search;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

/**
 * News search request object.
 *
 * @author Ryan Kennedy
 */
public class NewsSearchRequest extends RestRequest {
    /**
     * Constructs a new news search request.
     *
     * @param query The query to search for. This is the only required attribute.
     */
    public NewsSearchRequest(String query) {
        super("http://search.yahooapis.com/NewsSearchService/V1/newsSearch");
        setQuery(query);
    }

    public void setQuery(String query) {
        setParameter("query", query);
    }

    /**
     * Sets the type of the search. At the time of writing, three options are available:
     * <p/>
     * <ul>
     * <li><b>all (default)</b>: returns results with all query terms</li>
     * <li><b>any</b>: returns results with one or more of the query terms</li>
     * <li><b>phrase</b>: returns results containing the query terms as a phrase</li>
     * </ul>
     *
     * @param type The type of search to execute.
     */
    public void setType(String type) {
        setParameter("type", type);
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
    public void setStart(BigInteger start) {
        setParameter("start", start.toString(10));
    }

    /**
     * The sort order for news articles. At the time of writing, two options were available:
     * rank (default) or date.
     *
     * @param sort The sort order for news articles.
     */
    public void setSort(String sort) {
        setParameter("sort", sort);
    }

    /**
     * The language the results are written in. At the time of writing, the following options
     * are available: 'default', 'ar', 'bg', 'ca', 'szh', 'tzh', 'hr', 'cs', 'da',  'nl',  'en',
     * 'et', 'fi', 'fr', 'de', 'el', 'he', 'hu', 'is', 'it', 'ja', 'ko',  'lv', 'lt', 'no', 'fa',
     * 'pl', 'pt', 'ro', 'ru', 'sk', 'sl', 'es', 'sv',  'th', 'tr' .
     *
     * @param language The language the results are written in.
     */
    public void setLanguage(String language) {
        setParameter("language", language);
    }

    /**
     * A domain to restrict your searches to (e.g. www.yahoo.com). You may submit up to 30 values.
     *
     * @param site The site to restrict your search to.
     */
    public void addSite(String site) {
        addParameter("site", site);
    }
}