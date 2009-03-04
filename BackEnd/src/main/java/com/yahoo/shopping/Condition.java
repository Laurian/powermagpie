package com.yahoo.shopping;

public class Condition {
    public static final Condition NEW = new Condition("NEW");
    public static final Condition USED = new Condition("USED");
    public static final Condition REFURBISHED = new Condition("REFURBISHED");
    public static final Condition MERCHANT_MARKETPLACE = new Condition("MERCHANT_MARKETPLACE");

    private final String myName; // for debug only

    private Condition(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}