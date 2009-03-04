package com.yahoo.search.xmlparser;

import com.yahoo.search.LocalSearchResult;
import com.yahoo.search.LocalSearchResults;
import com.yahoo.xml.XmlParser;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;


public class XmlParserLocalSearchResults implements LocalSearchResults {
    public static final Map TYPE_MAP;

    static {
        TYPE_MAP = new HashMap();
        TYPE_MAP.put("/ResultSet/totalResultsAvailable", BigInteger.class);
        TYPE_MAP.put("/ResultSet/totalResultsReturned", BigInteger.class);
        TYPE_MAP.put("/ResultSet/firstResultPosition", BigInteger.class);
    }

    private Map root;
    private LocalSearchResult results[];

    public XmlParserLocalSearchResults(Map root) {
        this.root = root;

        List resultsList = XmlParser.getList(root, "/ResultSet/Result");
        if (resultsList != null) {
            results = new LocalSearchResult[resultsList.size()];
            for (int i = 0; i < results.length; i++) {
                Map map = (Map) resultsList.get(i);
                results[i] = new XmlParserLocalSearchResult(map);
            }
        }
        else {
            results = new LocalSearchResult[0];
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

    public String getResultsMapUrl() {
        return XmlParser.getString(root, "/ResultSet/ResultSetMapUrl/value");
    }

    public LocalSearchResult[] listResults() {
        return results;
    }

    public static class XmlParserLocalSearchResult implements LocalSearchResult {
        private Map result;
        private BigInteger id;
        private LocalSearchCategory categories[] = null;
        private LocalSearchRating rating = null;

        public XmlParserLocalSearchResult(Map result) {
            this.result = result;

            List resultMap = XmlParser.getList(result, "/ResultSet/Result");

            if(resultMap != null) {
                for(Iterator iterator = resultMap.iterator(); iterator.hasNext();) {
                    Map currentResult = (Map) iterator.next();

                    List categoryMap = XmlParser.getList(currentResult, "/Categories/Category");
                    categories = new LocalSearchCategory[categoryMap.size()];
                    for(int i = 0; i < categories.length; i++) {
                        categories[i] = new LocalSearchCategory(XmlParser.getString((Map) categoryMap.get(i), "/value"),
                                XmlParser.getBigInteger((Map) categoryMap.get(i), "/id/value"));
                    }
                }
            }
            else {
                categories = new LocalSearchCategory[0];
            }

            Map ratingMap = (Map) XmlParser.get(result, "/Rating");
            if(ratingMap != null) {
                rating = new LocalSearchRating(XmlParser.getBigDecimal(ratingMap, "/AverageRating/value"),
                        XmlParser.getBigInteger(ratingMap, "/TotalRatings/value"),
                        XmlParser.getBigInteger(ratingMap, "/TotalReviews/value"),
                        XmlParser.getBigInteger(ratingMap, "/LastReviewDate/value"),
                        XmlParser.getString(ratingMap, "/LastReviewIntro/value"));
            }
        }

        public BigInteger getId() {
            return XmlParser.getBigInteger(result, "/id");
        }

        public String getTitle() {
            return XmlParser.getString(result, "/Title/value");
        }

        public String getAddress() {
            return XmlParser.getString(result, "/Address/value");
        }

        public String getCity() {
            return XmlParser.getString(result, "/City/value");
        }

        public String getState() {
            return XmlParser.getString(result, "/State/value");
        }

        public String getPhone() {
            return XmlParser.getString(result, "/Phone/value");
        }

        public BigDecimal getLatitude() {
            return XmlParser.getBigDecimal(result, "/Latitude/value");
        }

        public BigDecimal getLongitude() {
            return XmlParser.getBigDecimal(result, "/Longitude/value");
        }

        public LocalSearchRating getRating() {
            return rating;
        }

        public String getDistance() {
            return XmlParser.getString(result, "/Distance/value");
        }

        public String getUrl() {
            return XmlParser.getString(result, "/Url/value");
        }

        public String getClickUrl() {
            return XmlParser.getString(result, "/ClickUrl/value");
        }

        public String getMapUrl() {
            return XmlParser.getString(result, "/MapUrl/value");
        }

        public String getBusinessUrl() {
            return XmlParser.getString(result, "/BusinessUrl/value");
        }

        public String getBusinessClickUrl() {
            return XmlParser.getString(result, "/BusinessClickUrl/value");
        }

        public LocalSearchCategory[] listCategories() {
            return categories;
        }
    }
}