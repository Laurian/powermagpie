package com.yahoo.search;

/**
 * Web search result.
 *
 * @author Ryan Kennedy
 */
public interface WebSearchResult {
    /**
     * The title of the web page.
     *
     * @return The title of the web page.
     */
    String getTitle();

    /**
     * Summary text associated with the web page.
     *
     * @return Summary text associated with the web page.
     */
    String getSummary();

    /**
     * The URL for the web page.
     *
     * @return The URL for the web page.
     */
    String getUrl();

    /**
     * The URL for linking to the page. See <a href="http://developer.yahoo.net/faq/index.html#clickurl">URL linking</a> for more information.
     *
     * @return The URL for linking to the page.
     */
    String getClickUrl();

    /**
     * The MIME type of the page. <b>This field is optional.</b>
     *
     * @return The MIME type of the page.
     */
    String getMimeType();

    /**
     * The date the page was last modified, in unix timestamp format. <b>This field is optional.</b>
     *
     * @return The date the page was last modified, in unix timestamp format.
     */
    String getModificationDate();

    /**
     * The URL of the cached result, and its size in bytes. <b>This field is optional.</b>
     *
     * @return The URL of the cached result, and its size in bytes.
     */
    CacheInfo getCache();

}