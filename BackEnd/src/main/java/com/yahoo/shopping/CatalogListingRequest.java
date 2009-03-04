package com.yahoo.shopping;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

public class CatalogListingRequest extends RestRequest {
    public CatalogListingRequest(BigInteger catalogId) {
        super("http://api.shopping.yahoo.com/ShoppingService/V1/catalogListing");

        setCatalogId(catalogId);
    }

    public void setCatalogId(BigInteger id) {
        setParameter("catalogid", id.toString(10));
    }

    public void setUpc(String upc) {
        setParameter("upc", upc);
    }

    public void setZipCode(String zip) {
        setParameter("zip", zip);
    }

    public void setOnlyNew(boolean onlyNew) {
        if(onlyNew) {
            setParameter("onlynew", "1");
        }
        else {
            setParameter("onlynew", "0");
        }
    }
}