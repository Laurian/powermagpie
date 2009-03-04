package com.yahoo.shopping;

import java.math.BigInteger;

public interface DetailedMerchant {
    String getName();

    MerchantRating getRating();

    boolean isCertifiedMerchant();

    String getInfoUrl();

    BigInteger getId();
}