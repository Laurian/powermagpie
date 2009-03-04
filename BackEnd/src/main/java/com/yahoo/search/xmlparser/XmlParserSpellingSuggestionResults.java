package com.yahoo.search.xmlparser;

import com.yahoo.search.SpellingSuggestionResults;
import com.yahoo.xml.XmlParser;

import java.util.Map;

public class XmlParserSpellingSuggestionResults implements SpellingSuggestionResults {
    private String suggestion;

    public XmlParserSpellingSuggestionResults(Map root) {
        suggestion = XmlParser.getString(root, "/ResultSet/Result/value");
    }

    public String getSuggestion() {
        return suggestion;
    }
}