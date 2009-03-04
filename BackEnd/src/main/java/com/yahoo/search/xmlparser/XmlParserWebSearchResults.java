package com.yahoo.search.xmlparser;

import com.yahoo.search.WebSearchResults;
import com.yahoo.search.WebSearchResult;
import com.yahoo.search.CacheInfo;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.math.BigInteger;

public class XmlParserWebSearchResults implements WebSearchResults {
    private Map root;
    private WebSearchResult results[];

    public XmlParserWebSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new WebSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserWebSearchResult(map);
            }
        }
        else {
            results = new WebSearchResult[0];
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

    public WebSearchResult[] listResults() {
        return results;
    }

    private class XmlParserWebSearchResult implements WebSearchResult {
        private Map result;
        private CacheInfo cache;

        public XmlParserWebSearchResult(Map result) {
            this.result = result;

            Map cacheMap = (Map) result.get("Cache");
            if (cacheMap != null) {
                cache = new XmlParserCacheInfo(cacheMap);
            }
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

        public String getMimeType() {
            return XmlParser.getString(result, "/MimeType/value");
        }

        public String getModificationDate() {
            return XmlParser.getString(result, "/ModificationDate/value");
        }

        public CacheInfo getCache() {
            return cache;
        }
    }
}