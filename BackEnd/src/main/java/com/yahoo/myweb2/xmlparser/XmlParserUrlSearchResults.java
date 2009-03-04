package com.yahoo.myweb2.xmlparser;

import com.yahoo.myweb2.UrlSearchResults;
import com.yahoo.myweb2.UrlSearchResult;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.math.BigInteger;

public class XmlParserUrlSearchResults implements UrlSearchResults {
    private Map root;
    private UrlSearchResult[] results;

    public XmlParserUrlSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new UrlSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserUrlSearchResult(map);
            }
        }
        else {
            results = new UrlSearchResult[0];
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

    public UrlSearchResult[] listResults() {
        return results;
    }

    private class XmlParserUrlSearchResult implements UrlSearchResult {
        private Map result;

        public XmlParserUrlSearchResult(Map result) {
            this.result = result;
        }

        public String getTitle() {
            return XmlParser.getString(result, "/Title/value");
        }

        public String getSummary() {
            return XmlParser.getString(result, "/Summary/value");
        }

        public String getUrl() {
            return XmlParser.getString(result, "/Url/value");
        }

        public String getClickUrl() {
            return XmlParser.getString(result, "/ClickUrl/value");
        }

        public String getUser() {
            return XmlParser.getString(result, "/User/value");
        }

        public String getNote() {
            return XmlParser.getString(result, "/Note/value");
        }

        public BigInteger getDate() {
            return XmlParser.getBigInteger(result, "/Date/value");
        }

        public String[] listTags() {
            return (String[]) XmlParser.getList(result, "/Tags").toArray(new String[XmlParser.getList(result, "/Tags").size()]);
        }
    }
}