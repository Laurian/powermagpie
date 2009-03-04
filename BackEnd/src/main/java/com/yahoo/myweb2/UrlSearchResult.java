package com.yahoo.myweb2;

import java.math.BigInteger;

public interface UrlSearchResult {
    String getTitle();

    String getSummary();

    String getUrl();

    String getClickUrl();

    String getUser();

    String getNote();

    BigInteger getDate();

    String[] listTags();
}