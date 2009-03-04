package com.yahoo.myweb2;

import java.math.BigInteger;

public interface TagSearchResult {
    String getTag();

    BigInteger getFrequency();

    BigInteger getDate();
}