package com.yahoo.search.xmlparser;

import com.yahoo.search.NewsSearchResults;
import com.yahoo.search.NewsSearchResult;
import com.yahoo.search.ImageThumbnail;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;
import java.math.BigInteger;

public class XmlParserNewsSearchResults implements NewsSearchResults {
    private Map root;
    private NewsSearchResult results[];

    public XmlParserNewsSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new NewsSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserNewsSearchResult(map);
            }
        }
        else {
            results = new NewsSearchResult[0];
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

    public NewsSearchResult[] listResults() {
        return results;
    }

    private class XmlParserNewsSearchResult implements NewsSearchResult {
        private Map result;
        private ImageThumbnail thumbnail;

        public XmlParserNewsSearchResult(Map result) {
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

        public String getNewsSource() {
            return XmlParser.getString(result, "/NewsSource/value");
        }

        public String getNewsSourceUrl() {
            return XmlParser.getString(result, "/NewsSourceUrl/value");
        }

        public String getLanguage() {
            return XmlParser.getString(result, "/Language/value");
        }

        public String getPublishDate() {
            return XmlParser.getString(result, "/PublishDate/value");
        }

        public String getModificationDate() {
            return XmlParser.getString(result, "/ModificationDate/value");
        }

        public ImageThumbnail getThumbnail() {
            return thumbnail;
        }
    }
}