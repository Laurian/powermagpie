package com.yahoo.search.xmlparser;

import com.yahoo.search.RelatedSuggestionResults;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;

public class XmlParserRelatedSuggestionResults implements RelatedSuggestionResults {
    private String suggestions[];

    public XmlParserRelatedSuggestionResults(Map root) {
        List suggestionList = XmlParser.getList(root, "/ResultSet/Result");

        if (suggestionList != null) {
            suggestions = new String[suggestionList.size()];
            for (int i = 0; i < suggestions.length; i++) {
                suggestions[i] = XmlParser.getString((Map) suggestionList.get(i), "/value");
            }
        }
        else {
            suggestions = new String[0];
        }
    }

    public String[] getSuggestions() {
        return suggestions;
    }
}