package com.yahoo.shopping.xmlparser;

import com.yahoo.shopping.*;
import com.yahoo.xml.XmlParser;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

public class XmlParserCatalogListingResults implements CatalogListingResults {
    private Map root;
    private CatalogOffer[] results;

    public XmlParserCatalogListingResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/CatalogListing/Offer");
        if (resultsList != null) {
            results = new CatalogOffer[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserCatalogOffer(map);
            }
        }
        else {
            results = new CatalogOffer[0];
        }
    }

    public BigInteger getId() {
        return XmlParser.getBigInteger(root, "/CatalogListing/ID");
    }

    public BigInteger getNumberOfOffers() {
        return XmlParser.getBigInteger(root, "/CatalogListing/numOffers");
    }

    public String getZipCode() {
        return XmlParser.getString(root, "/CatalogListing/zip");
    }

    public CatalogOffer[] listOffers() {
        return results;
    }

    private class XmlParserCatalogOffer implements CatalogOffer {
        private Map offer;

        public XmlParserCatalogOffer(Map offer) {
            this.offer = offer;
        }

        public Merchant getMerchant() {
            // TODO: Implement
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getUrl() {
            return XmlParser.getString(root, "/Url/value");
        }

        public Condition getCondition() {
            // TODO: Implement
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Cost getTaxCost() {
            // TODO: Implement
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Cost getShippingCost() {
            // TODO: Implement
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public BigDecimal getBasePrice() {
            return XmlParser.getBigDecimal(root, "/BasePrice/value");
        }

        public BigDecimal getTotalPrice() {
            return XmlParser.getBigDecimal(root, "/TotalPrice/value");
        }

        public BigDecimal getStrikethroughPrice() {
            return XmlParser.getBigDecimal(root, "/StrikeThroughPrice/value");
        }
    }
}