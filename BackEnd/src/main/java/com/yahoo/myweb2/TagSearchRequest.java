package com.yahoo.myweb2;

import com.yahoo.rest.RestRequest;

public class TagSearchRequest extends RestRequest {
    public static final String SORT_BY_DATE = "date";
    public static final String SORT_BY_TAG = "tag";
    public static final String SORT_BY_POPULARITY = "popularity";

    public TagSearchRequest() {
        super("http://search.yahooapis.com/MyWebService/V1/tagSearch");
    }

    public void setUrl(String url) {
        setParameter("url", url);
    }

    public void setYahooId(String yahooId) {
        setParameter("yahooid", yahooId);
    }

    public void setSort(String sort) {
        setParameter("sort", sort);
    }

    public void setSortReverse(boolean reverse) {
        if(reverse) {
            setParameter("reverse_sort", "1");
        }
        else {
            setParameter("reverse_sort", "0");
        }
    }

    public void setResults(int results) {
        setParameter("results", Integer.toString(results));
    }

    public void setStart(int start) {
        setParameter("start", Integer.toString(start));
    }
}