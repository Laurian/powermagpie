package com.yahoo.search.xmlparser;

import com.yahoo.search.ImageSearchResult;
import com.yahoo.search.ImageSearchResults;
import com.yahoo.search.ImageThumbnail;
import com.yahoo.xml.XmlParser;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class XmlParserImageSearchResults implements ImageSearchResults {
    private Map root;
    private ImageSearchResult results[];

    public XmlParserImageSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new ImageSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserImageSearchResult(map);
            }
        }
        else {
            results = new ImageSearchResult[0];
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

    public ImageSearchResult[] listResults() {
        return results;
    }

    public static class XmlParserImageSearchResult implements ImageSearchResult {
        private Map result;
        private ImageThumbnail thumbnail;

        public XmlParserImageSearchResult(Map result) {
            this.result = result;
            thumbnail = new XmlParserImageThumbnail((Map) result.get("Thumbnail"));
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

        public String getRefererUrl() {
            return XmlParser.getString(result, "/RefererUrl/value");
        }

        public BigInteger getFileSize() {
            return XmlParser.getBigInteger(result, "/FileSize/value");
        }

        public String getFileFormat() {
            return XmlParser.getString(result, "/FileFormat/value");
        }

        public BigInteger getHeight() {
            return XmlParser.getBigInteger(result, "/Height/value");
        }

        public BigInteger getWidth() {
            return XmlParser.getBigInteger(result, "/Width/value");
        }

        public ImageThumbnail getThumbnail() {
            return thumbnail;
        }

        public String getPublisher() {
            return XmlParser.getString(result, "/Publisher/value");
        }

        public String getRestrictions() {
            return XmlParser.getString(result, "/Restrictions/value");
        }

        public String getCopyright() {
            return XmlParser.getString(result, "/Copyright/value");
        }
    }
}