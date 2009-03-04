package com.yahoo.shopping;

import com.yahoo.xml.XmlParser;
import com.yahoo.rest.RestClient;
import com.yahoo.rest.RestException;
import com.yahoo.shopping.xmlparser.XmlParserProductSearchResults;
import com.yahoo.shopping.xmlparser.XmlParserCatalogListingResults;
import com.yahoo.shopping.xmlparser.XmlParserMerchantSearchResults;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

/**
 * The ShoppingClient class handles all calls to/from the Yahoo Shopping Search Web Service. Additionally,
 * it translates the XML responses into Java objects before returning results to the caller.
 * <p/>
 * Each ShoppingClient instance has an associated application ID ("appid"). You will need to
 * register an application ID with <a href="http://developer.yahoo.net/">Yahoo</a> prior to using ShoppingClient.
 * <p/>
 * See http://developer.yahoo.net/shopping/index.html for more details.
 *
 * @author Ryan Kennedy
 */
public class ShoppingClient {
    private static final String APPID_KEY = "appid";

    private String appId;

    /**
     * Constructs a new ShoppingClient with the given application ID using the default settings.
     *
     * @param appId The ID of the calling application.
     */
    public ShoppingClient(String appId) {
        this.appId = appId;
    }

    /**
     * Allows you to search for products offerings and buyers' guides that correspond to a particular query.
     *
     * @param request The search criteria.
     * @return The list of products found.
     * @throws IOException       Thrown if any network issues occur while making the call.
     * @throws ShoppingException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public ProductSearchResults productSearch(ProductSearchRequest request) throws IOException, ShoppingException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserProductSearchResults(results);
    }

    /**
     * Allows you to find the individual offers inside a Yahoo! Shopping Buyers' Guide catalog.
     *
     * @param request The search criteria.
     * @return The catalog listing found.
     * @throws IOException       Thrown if any network issues occur while making the call.
     * @throws ShoppingException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public CatalogListingResults catalogListing(CatalogListingRequest request) throws IOException, ShoppingException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserCatalogListingResults(results);
    }

    /**
     * Allows you to retrive data about the merchants making offers on Yahoo! Shopping.
     *
     * @param request The search criteria.
     * @return The list of merchants found.
     * @throws IOException       Thrown if any network issues occur while making the call.
     * @throws ShoppingException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public MerchantSearchResults merchantSearch(MerchantSearchRequest request) throws IOException, ShoppingException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());

        return new XmlParserMerchantSearchResults(results);
    }

    private Map executeAndParse(String serviceUrl, Map parameters) throws IOException, ShoppingException {
        XmlParser xmlParser = null;

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            xmlParser = new XmlParser();
            parser.parse(RestClient.call(serviceUrl, parameters), xmlParser);
        } catch (ParserConfigurationException e) {
            throw new com.yahoo.java.ExtendedError("XML parser not properly configured", e);
        } catch (SAXException e) {
            throw new ShoppingException("Error parsing XML response", e);
        } catch (RestException ye) {
            throw new ShoppingException("Error calling service\n" + new String(ye.getErrorMessage(), "UTF-8"), ye);
        }

        return xmlParser.getRoot();
    }
}