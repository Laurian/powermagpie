package com.yahoo.shopping;

import java.math.BigInteger;

public interface MerchantRating {
    float getMaxRating();

    BigInteger getNumberOfRatings();

    float getOverallRating();

    float getPriceSatisfactionRating();

    float getShippingOptionsRating();

    float getTimelyDeliveryRating();

    float getEaseOfPurchaseRating();

    float getCustomerServiceRating();

    String getRatingUrl();
}