package com.yahoo.shopping;

import java.math.BigInteger;

public interface CatalogListingResults {
    BigInteger getId();

    BigInteger getNumberOfOffers();

    String getZipCode();

    CatalogOffer[] listOffers();
}