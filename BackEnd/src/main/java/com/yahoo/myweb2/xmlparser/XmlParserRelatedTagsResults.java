package com.yahoo.myweb2.xmlparser;

import com.yahoo.myweb2.RelatedTagsResults;
import com.yahoo.myweb2.RelatedTagsResult;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.math.BigInteger;

public class XmlParserRelatedTagsResults implements RelatedTagsResults {
    private Map root;
    private RelatedTagsResult[] results;

    public XmlParserRelatedTagsResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new RelatedTagsResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserRelatedTagsResult(map);
            }
        }
        else {
            results = new RelatedTagsResult[0];
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

    public RelatedTagsResult[] listResults() {
        return results;
    }

    private class XmlParserRelatedTagsResult implements RelatedTagsResult {
        private Map result;

        public XmlParserRelatedTagsResult(Map result) {
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