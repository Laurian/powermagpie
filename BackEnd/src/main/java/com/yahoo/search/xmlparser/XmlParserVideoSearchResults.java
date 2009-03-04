package com.yahoo.search.xmlparser;

import com.yahoo.search.VideoSearchResults;
import com.yahoo.search.VideoSearchResult;
import com.yahoo.search.ImageThumbnail;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.math.BigInteger;
import java.math.BigDecimal;

public class XmlParserVideoSearchResults implements VideoSearchResults {
    private Map root;
    private VideoSearchResult results[];

    public XmlParserVideoSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new VideoSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserVideoSearchResult(map);
            }
        }
        else {
            results = new VideoSearchResult[0];
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

    public VideoSearchResult[] listResults() {
        return results;
    }

    private class XmlParserVideoSearchResult implements VideoSearchResult {
        private Map result;
        private ImageThumbnail thumbnail;

        public XmlParserVideoSearchResult(Map result) {
            this.result = result;

            Map thumbnailMap = (Map) result.get("Thumbnail");
            if (thumbnailMap != null) {
                thumbnail = new XmlParserImageThumbnail(thumbnailMap);
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

        public BigDecimal getDuration() {
            return XmlParser.getBigDecimal(result, "/Duration/value");
        }

        public boolean isStreaming() {
            // TODO: It's a parsing error if this is missing.
            Boolean streaming = XmlParser.getBoolean(result, "/Streaming/value");
            return (streaming != null) ? streaming.booleanValue() : false;
        }

        public String getChannels() {
            return XmlParser.getString(result, "/Channels/value");
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