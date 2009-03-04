package com.yahoo.search.xmlparser;

import com.yahoo.search.WebSearchResult;
import com.yahoo.search.CacheInfo;
import com.yahoo.xml.XmlParser;

import java.util.Map;

public class XmlParserCacheInfo implements CacheInfo {
    private Map cache;

    public XmlParserCacheInfo(Map cache) {
        this.cache = cache;
    }

    public String getUrl() {
        return XmlParser.getString(cache, "/Url/value");
    }

    public String getSize() {
        return XmlParser.getString(cache, "/Size/value");
    }
}
