package com.yahoo.shopping;

import java.math.BigInteger;

public interface UserRating {
    BigInteger getNumberOfRatings();

    float getMaxRating();

    float getAverageRating();

    String getRatingUrl();

    String getCreateRatingUrl();
}