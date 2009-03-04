package com.yahoo.search;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

/**
 * Web search request object.
 *
 * @author Ryan Kennedy
 */
public class WebSearchRequest extends RestRequest {
    /**
     * Constructs a new web search request.
     *
     * @param query The query to search for. This is the only required attribute.
     */
    public WebSearchRequest(String query) {
        super("http://search.yahooapis.com/WebSearchService/V1/webSearch");
        setQuery(query);
    }

    public void setQuery(String query) {
        setParameter("query", query);
    }

    /**
     * Sets the regional search engine on which the service performs the search. For example,
     * "uk" will give you the search engine at uk.search.yahoo.com.
     * <a href="http://developer.yahoo.com/search/regions.html">Supported Regions</a>.
     *
     * @param region The region's search engine to use.
     */
    public void setRegion(String region) {
        setParameter("region", region);
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
     * Specifies the kind of web document to search for. At the time of writing, the following
     * options are available: all (default), html, msword, pdf, ppt, rss, txt, xls.
     *
     * @param format Specifies the kind of web document to search for.
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
        if (adultOk) {
            setParameter("adult_ok", "1");
        }
        else {
            clearParameter("adult_ok");
        }
    }

    /**
     * Specifies whether to allow multiple results with similar content. Defaults to false.
     *
     * @param similarOk Specifies whether to allow multiple results with similar content.
     */
    public void setSimilarOk(boolean similarOk) {
        if (similarOk) {
            setParameter("similar_ok", "1");
        }
        else {
            clearParameter("similar_ok");
        }
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
     * Restricts the results to a given country.
     *
     * @param country The country code of the country to restrict search results to.
     */
    public void setCountry(String country) {
        setParameter("country", country);
    }

    /**
     * A domain to restrict your searches to (e.g. www.yahoo.com). You may submit up to 30 values.
     *
     * @param site A site to restrict search results to.
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

    /**
     * The subscription source that should be included in the search results. You may submit multiple values. For
     * a list of supported subscription services, see <a href="http://developer.yahoo.net/documentation/subscriptions.html">
     * http://developer.yahoo.net/documentation/subscriptions.html</a>.
     *
     * @param subscription The subscription source to be included in the search results. You may submit multiple values.
     */
    public void addSubscription(String subscription) {
        addParameter("subscription", subscription);
    }
}