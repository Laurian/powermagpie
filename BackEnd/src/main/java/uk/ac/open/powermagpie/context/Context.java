/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.open.powermagpie.context;

import com.yahoo.search.ContentAnalysisRequest;
import com.yahoo.search.ContentAnalysisResults;
import com.yahoo.search.SearchClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lg3388
 */
public class Context {

    private String name;
    private StringBuffer text;

    private HashMap<String, Term> terms;

    private boolean OpenK = false;

    public Context() {
        text = new StringBuffer();
        terms = new HashMap<String, Term>();
    }

    public Context(String name) {
        this();
        this.name = name;
    }

    //TODO move to script, paralelize addTerm!
    public void process(String text) throws Exception {
        this.text.append(' ').append(text);

        SearchClient client = new SearchClient("WIsykmzV34GpQo23EGZ0F38QqYu2y8wrq.hLmYjXjNpkA5L4MCzCeL1mniq82m9w5oSMbXHi");
        ContentAnalysisRequest request = new ContentAnalysisRequest(text);

        ContentAnalysisResults result = client.termExtractionSearch(request);

        String[] yterms = result.getExtractedTerms();
        for (int i = 0; i < yterms.length; i++) {
            addTerm(yterms[i]);
        }
    }

    public Term addTerm(String lexical) {
        System.out.println("Context.addTerm " + lexical);
        
        Term term = new Term(lexical, OpenK);
        terms.put(lexical, term);
        return term;
    }

    public HashMap<String, Term> getTerms() {
        return terms;
    }
}
