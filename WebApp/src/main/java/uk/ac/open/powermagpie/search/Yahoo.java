package uk.ac.open.powermagpie.search;

import com.tek271.memoize.Remember;
import com.tek271.memoize.cache.TimeUnitEnum;
import com.yahoo.search.ContentAnalysisRequest;
import com.yahoo.search.ContentAnalysisResults;
import com.yahoo.search.SearchClient;
import com.yahoo.search.SearchException;
import com.yahoo.search.WebSearchRequest;
import com.yahoo.search.WebSearchResults;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.open.powermagpie.context.Context;

/**
 *
 * @author Laurian Gridinoc
 */
public class Yahoo {
    
    @Remember(maxSize=1024, timeToLive=4, timeUnit=TimeUnitEnum.HOUR)
    public int hits(String query) {
        SearchClient client = new SearchClient("WIsykmzV34GpQo23EGZ0F38QqYu2y8wrq.hLmYjXjNpkA5L4MCzCeL1mniq82m9w5oSMbXHi");
        WebSearchRequest request = new WebSearchRequest(query);
 
        try {
            WebSearchResults results = client.webSearch(request);
            return  results.getTotalResultsAvailable().intValue();
        } catch (Exception exception) {
            Logger.getLogger(Yahoo.class.getName()).log(Level.SEVERE, null, exception);
        }
        
        return 0;
    }

    @Remember(maxSize=1024, timeToLive=4, timeUnit=TimeUnitEnum.HOUR)
    public void terms(Context context, String text) {
        try {
            SearchClient client = new SearchClient("WIsykmzV34GpQo23EGZ0F38QqYu2y8wrq.hLmYjXjNpkA5L4MCzCeL1mniq82m9w5oSMbXHi");
            ContentAnalysisRequest request = new ContentAnalysisRequest(text);
            ContentAnalysisResults result = client.termExtractionSearch(request);
            String[] yterms = result.getExtractedTerms();
            for (int i = 0; i < yterms.length; i++) {
                context.add(yterms[i]);
            }
        } catch (IOException ex) {
            Logger.getLogger(Yahoo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SearchException ex) {
            Logger.getLogger(Yahoo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
