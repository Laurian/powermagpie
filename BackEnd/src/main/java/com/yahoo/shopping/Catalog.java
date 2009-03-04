package com.yahoo.shopping;

import java.math.BigInteger;
import java.math.BigDecimal;

public interface Catalog {
    String getUrl();

    String getProductName();

    BigDecimal getPriceFrom();

    BigDecimal getPriceTo();

    Thumbnail getThumbnail();

    String getDescription();

    String getSummary();

    UserRating getUserRating();

    Specification[] listSpecifications();

    BigInteger getId();
}