package com.yahoo.search;

/**
 * Represents the cached copy of the URL content.
 */
public interface CacheInfo {
    /**
     * The URL of the cached result.
     *
     * @return The URL of the cached result.
     */
    String getUrl();

    /**
     * Size of the cached result, in bytes.
     * <p/>
     * <b>NOTE:</b> The schema for web search indicates this field is a string. Do not assume this data
     * will be numeric as it is perfectly valid for the search to return non-numeric contents.
     *
     * @return Size of the cached result, in bytes.
     */
    String getSize();
}
