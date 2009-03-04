package com.yahoo.shopping;

public interface Offer {
    String getUrl();

    String getProductName();

    Thumbnail getThumbnail();

    String getSummary();

    Merchant getMerchant();
}