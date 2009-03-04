package com.yahoo.shopping;

import com.yahoo.rest.RestRequest;

public class ProductSearchRequest extends RestRequest {
    public static final String SORT_BY_RELEVANCE = "relevance";
    public static final String SORT_BY_PRICE = "price";

    public ProductSearchRequest(String query) {
        super("http://api.shopping.yahoo.com/ShoppingService/V1/productSearch");
        setQuery(query);
    }

    public void setQuery(String query) {
        setParameter("query", query);
    }

    public void setResults(int results) {
        setParameter("results", Integer.toString(results));
    }

    public void setStart(int start) {
        setParameter("start", Integer.toString(start));
    }

    public void setMerchantId(int id) {
        setParameter("merchantid", Integer.toString(id));
    }

    public void setHighestPrice(float price) {
        setParameter("highestprice", Float.toString(price));
    }

    public void setLowestPrice(float price) {
        setParameter("lowestprice", Float.toString(price));
    }

    public void setSort(String sort) {
        setParameter("sort", sort);
    }

    public void setDepartment(int department) {
        setParameter("department", Integer.toString(department));
    }
}