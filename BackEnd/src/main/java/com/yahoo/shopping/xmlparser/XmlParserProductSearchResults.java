package com.yahoo.shopping.xmlparser;

import com.yahoo.shopping.*;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;
import java.math.BigDecimal;

public class XmlParserProductSearchResults implements ProductSearchResults {
    private Map root;
    private List results;

    public XmlParserProductSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(this.root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new ArrayList(resultsList.size());
            for (int i = 0; i < resultsList.size(); i++) {
                Map map = (Map) resultsList.get(i);
                if(map.get("Catalog") != null) {
                    results.add(new XmlParserCatalogResult((Map)map.get("Catalog")));
                }
                else if(map.get("Offer") != null) {
                    results.add(new XmlParserOfferResult((Map)map.get("Offer")));
                }
            }
        }
        else {
            results = new ArrayList(0);
        }
    }

    public BigInteger getTotalResultsAvailable() {
        return XmlParser.getBigInteger(root, "/ResultSet/totalResultsAvailable");
    }

    public BigInteger getTotalResultsReturned() {
        return XmlParser.getBigInteger(root, "/ResultSet/totalResultsReturned");
    }

    public BigInteger getFirstResultPosition() {
        return XmlParser.getBigInteger(root, "/ResultSet/firstResultPosition");
    }

    public String getSpellSuggestions() {
        return XmlParser.getString(root, "/ResultSet/spellSuggestions");
    }

    public List listResults() {
        return results;
    }

    private class XmlParserCatalogResult implements Catalog {
        private Map catalog;

        public XmlParserCatalogResult(Map catalog) {
            this.catalog = catalog;
        }

        public String getUrl() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getProductName() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public BigDecimal getPriceFrom() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public BigDecimal getPriceTo() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Thumbnail getThumbnail() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getDescription() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getSummary() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public UserRating getUserRating() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Specification[] listSpecifications() {
            return new Specification[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public BigInteger getId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class XmlParserOfferResult implements Offer {
        private Map offer;

        public XmlParserOfferResult(Map offer) {
            this.offer = offer;
        }

        public String getUrl() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getProductName() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Thumbnail getThumbnail() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getSummary() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Merchant getMerchant() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}