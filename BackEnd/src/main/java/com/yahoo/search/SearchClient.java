package com.yahoo.search;

import com.yahoo.rest.RestClient;
import com.yahoo.rest.RestException;
import com.yahoo.search.xmlparser.*;
import org.xml.sax.SAXException;
import com.yahoo.xml.XmlParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Map;

/**
 * The SearchClient class handles all calls to/from the Yahoo Search Web Service. Additionally,
 * it translates the XML responses into Java objects before returning results to the caller.
 *
 * Each SearchClient instance has an associated application ID ("appid"). You will need to
 * register an application ID with <a href="http://developer.yahoo.net/">Yahoo</a> prior to using SearchClient.
 *
 * @author Ryan Kennedy
 */
public class SearchClient {
    private static final String APPID_KEY = "appid";

    private String appId;

    /**
     * Constructs a new SearchClient with the given application ID using the default settings.
     *
     * @param appId The ID of the calling application.
     */
    public SearchClient(String appId) {
        this.appId = appId;
    }

    /**
     * Searches the Yahoo database for image files.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public ImageSearchResults imageSearch(ImageSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserImageSearchResults(results);
    }

    /**
     * Searches the Yahoo database for local results.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public LocalSearchResults localSearch(LocalSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserLocalSearchResults(results);
    }

    /**
     * Searches the Yahoo database for news results.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public NewsSearchResults newsSearch(NewsSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserNewsSearchResults(results);
    }

    /**
     * Searches the Yahoo database for video files.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public VideoSearchResults videoSearch(VideoSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserVideoSearchResults(results);
    }

    /**
     * Searches the Yahoo database for web results.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public WebSearchResults webSearch(WebSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserWebSearchResults(results);
    }

    /**
     * Requests a possible spelling correction for the given query.
     *
     * @param request The search query to request a suggestion for.
     * @return Result of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public SpellingSuggestionResults spellingSuggestion(SpellingSuggestionRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserSpellingSuggestionResults(results);
    }

    /**
     * Requests search terms related to the given search term.
     *
     * @param request The search term to request suggestions for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public RelatedSuggestionResults relatedSuggestion(RelatedSuggestionRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserRelatedSuggestionResults(results);
    }

    /**
     * Searches the Yahoo database for web results using a context-based query.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public WebSearchResults contextWebSearch(ContextSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserWebSearchResults(results);
    }

    /**
     * Searches a bit of content for relevant, related search terms.
     *
     * @param request The request to extract terms from.
     * @return Results of the term extraction.
     * @throws IOException Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public ContentAnalysisResults termExtractionSearch(ContentAnalysisRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserContentAnalysisResults(results);
    }

    private Map executeAndParse(String serviceUrl, Map parameters) throws IOException, SearchException {
        XmlParser xmlParser = null;

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            xmlParser = new XmlParser();
            parser.parse(RestClient.call(serviceUrl, parameters), xmlParser);
        }
        catch (ParserConfigurationException e) {
            throw new com.yahoo.java.ExtendedError("XML parser not properly configured", e);
        }
        catch (SAXException e) {
            throw new SearchException("Error parsing XML response", e);
        }
        catch (RestException ye) {
            throw new SearchException("Error calling service\n" + new String(ye.getErrorMessage(), "UTF-8"), ye);
        }

        return xmlParser.getRoot();
    }
}