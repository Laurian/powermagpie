package com.yahoo.shopping;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

public class MerchantSearchRequest extends RestRequest {
    public MerchantSearchRequest(BigInteger merchantId) {
        super("http://api.shopping.yahoo.com/ShoppingService/V1/merchantSearch");

        setMerchantId(merchantId);
    }

    public void setMerchantId(BigInteger id) {
        setParameter("merchantid", id.toString(10));
    }
}