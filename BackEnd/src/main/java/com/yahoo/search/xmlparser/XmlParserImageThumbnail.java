package com.yahoo.search.xmlparser;

import com.yahoo.search.ImageThumbnail;
import com.yahoo.xml.XmlParser;

import java.math.BigInteger;
import java.util.Map;

public class XmlParserImageThumbnail implements ImageThumbnail {
    private Map thumbnail;

    public XmlParserImageThumbnail(Map thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return XmlParser.getString(thumbnail, "/Url/value");
    }

    public BigInteger getHeight() {
        return XmlParser.getBigInteger(thumbnail, "/Height/value");
    }

    public BigInteger getWidth() {
        return XmlParser.getBigInteger(thumbnail, "/Width/value");
    }
}