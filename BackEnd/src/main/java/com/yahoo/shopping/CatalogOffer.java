package com.yahoo.shopping;

import java.math.BigDecimal;

public interface CatalogOffer {
    Merchant getMerchant();

    String getUrl();

    Condition getCondition();

    Cost getTaxCost();

    Cost getShippingCost();

    BigDecimal getBasePrice();

    BigDecimal getTotalPrice();

    BigDecimal getStrikethroughPrice();
}