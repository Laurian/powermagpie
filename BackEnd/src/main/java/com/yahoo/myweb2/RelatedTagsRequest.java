package com.yahoo.myweb2;

import com.yahoo.rest.RestRequest;

public class RelatedTagsRequest extends RestRequest {
    public static final String SORT_BY_DATE = "date";
    public static final String SORT_BY_TAG = "tag";
    public static final String SORT_BY_POPULARITY = "popularity";

    public RelatedTagsRequest(String tag) {
        super("http://search.yahooapis.com/MyWebService/V1/relatedTags");

        setTag(tag);
    }

    public void setTag(String tag) {
        setParameter("tag", tag);
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