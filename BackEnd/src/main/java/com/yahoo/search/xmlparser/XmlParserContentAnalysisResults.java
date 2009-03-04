package com.yahoo.search.xmlparser;

import com.yahoo.search.ContentAnalysisResults;
import com.yahoo.xml.XmlParser;

import java.util.Map;
import java.util.List;

public class XmlParserContentAnalysisResults implements ContentAnalysisResults {
    private String terms[];

    public XmlParserContentAnalysisResults(Map results) {
        List termMap = XmlParser.getList(results, "/ResultSet/Result");

        if (termMap != null) {
            terms = new String[termMap.size()];
            for (int i = 0; i < terms.length; i++) {
                terms[i] = XmlParser.getString((Map) termMap.get(i), "/value");
            }
        }
        else {
            terms = new String[0];
        }
    }

    public String[] getExtractedTerms() {
        return terms;
    }
}
