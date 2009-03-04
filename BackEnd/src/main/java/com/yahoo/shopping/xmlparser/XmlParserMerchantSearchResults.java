package com.yahoo.shopping.xmlparser;

import com.yahoo.shopping.MerchantSearchResults;
import com.yahoo.shopping.DetailedMerchant;

import java.util.Map;

public class XmlParserMerchantSearchResults implements MerchantSearchResults {
    private Map root;

    public XmlParserMerchantSearchResults(Map root) {
        this.root = root;
    }

    public DetailedMerchant[] listMerchants() {
        // TODO: Implement.
        return new DetailedMerchant[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}