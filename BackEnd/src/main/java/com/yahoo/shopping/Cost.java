package com.yahoo.shopping;

import java.math.BigDecimal;

public interface Cost {
    boolean checkMerchant();

    BigDecimal getAmount();
}