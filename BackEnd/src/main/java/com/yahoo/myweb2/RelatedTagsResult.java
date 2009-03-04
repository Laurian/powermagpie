package com.yahoo.myweb2;

import java.math.BigInteger;

public interface RelatedTagsResult {
    String getTag();

    BigInteger getFrequency();

    BigInteger getDate();
}