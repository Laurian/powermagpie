package com.yahoo.myweb2;

import com.yahoo.myweb2.xmlparser.XmlParserUrlSearchResults;
import com.yahoo.myweb2.xmlparser.XmlParserTagSearchResults;
import com.yahoo.myweb2.xmlparser.XmlParserRelatedTagsResults;
import com.yahoo.rest.RestClient;
import com.yahoo.rest.RestException;
import com.yahoo.xml.XmlParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Map;

/**
 * The MyWeb2Client class handles all calls to/from the Yahoo MyWeb2 Web Service. Additionally,
 * it translates the XML responses into Java objects before returning results to the caller.
 *
 * Each MyWeb2Client instance has an associated application ID ("appid"). You will need to
 * register an application ID with <a href="http://developer.yahoo.net/">Yahoo</a> prior to using MyWeb2Client.
 *
 * See http://developer.yahoo.net/myweb/index.html for more details.
 *
 * @author Ryan Kennedy
 */
public class MyWeb2Client {
    private static final String APPID_KEY = "appid";

    private String appId;

    /**
     * Constructs a new MyWeb2Client with the given application ID using the default settings.
     *
     * @param appId The ID of the calling application.
     */
    public MyWeb2Client(String appId) {
        this.appId = appId;
    }

    /**
     * Allows you to search for URLs that have been tagged by particular tags.
     *
     * @param request The search criteria.
     * @return The list of URL's found.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws MyWeb2Exception Thrown if the request is invalid or if the service is malfunctioning.
     */
    public UrlSearchResults urlSearch(UrlSearchRequest request) throws IOException, MyWeb2Exception {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserUrlSearchResults(results);
    }

    /**
     * Allows you to search for tags that have been created by a particular user.
     *
     * @param request The search criteria.
     * @return The list of tagss found.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws MyWeb2Exception Thrown if the request is invalid or if the service is malfunctioning.
     */
    public TagSearchResults tagSearch(TagSearchRequest request) throws IOException, MyWeb2Exception {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserTagSearchResults(results);
    }

    /**
     * Allows you to search for tags related to another, given tag.
     *
     * @param request The search criteria.
     * @return The list of tagss found.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws MyWeb2Exception Thrown if the request is invalid or if the service is malfunctioning.
     */
    public RelatedTagsResults relatedTags(RelatedTagsRequest request) throws IOException, MyWeb2Exception {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserRelatedTagsResults(results);
    }

    private Map executeAndParse(String serviceUrl, Map parameters) throws IOException, MyWeb2Exception {
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
            throw new MyWeb2Exception("Error parsing XML response", e);
        }
        catch (RestException ye) {
            throw new MyWeb2Exception("Error calling service\n" + new String(ye.getErrorMessage(), "UTF-8"), ye);
        }

        return xmlParser.getRoot();
    }
}