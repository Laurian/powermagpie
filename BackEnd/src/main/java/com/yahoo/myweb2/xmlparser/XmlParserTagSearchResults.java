package com.yahoo.myweb2.xmlparser;

import com.yahoo.myweb2.TagSearchResults;
import com.yahoo.myweb2.TagSearchResult;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.math.BigInteger;

public class XmlParserTagSearchResults implements TagSearchResults {
    private Map root;
    private TagSearchResult[] results;

    public XmlParserTagSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new TagSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserTagSearchResult(map);
            }
        }
        else {
            results = new TagSearchResult[0];
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

    public TagSearchResult[] listResults() {
        return results;
    }

    private class XmlParserTagSearchResult implements TagSearchResult {
        private Map result;

        public XmlParserTagSearchResult(Map result) {
            this.result = result;
        }

        public String getTag() {
            return XmlParser.getString(result, "/Tag/value");
        }

        public BigInteger getFrequency() {
            return XmlParser.getBigInteger(result, "/Frequency/value");
        }

        public BigInteger getDate() {
            return XmlParser.getBigInteger(result, "/Date/value");
        }
    }
}