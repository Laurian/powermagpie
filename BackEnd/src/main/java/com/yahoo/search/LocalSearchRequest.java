package com.yahoo.search;

import com.yahoo.rest.RestRequest;

import java.math.BigInteger;

/**
 * Local search request object.
 *
 * @author Ryan Kennedy
 */
public class LocalSearchRequest extends RestRequest {
    /**
     * Constructs a new local search request.
     */
    public LocalSearchRequest() {
        super("http://api.local.yahoo.com/LocalSearchService/V3/localSearch");
    }

    /**
     * The query to search for. Using a query of "*" returns all values that match other
     * criteria in the search (category, radius, and so on). At least one of query or
     * listing id must be specified.
     *
     * @param query The query to search for.
     */
    public void setQuery(String query) {
        setParameter("query", query);
    }

    /**
     * The id associated with a specific business listing. It corresponds with the id
     * attribute of Result entities. At least one of query or listing id must be specified.
     *
     * @param listingId The listing ID to search for.
     */
    public void setListingId(String listingId) {
        setParameter("listing_id", listingId);
    }

    /**
     * The maximum number of results to return. May return fewer results if there aren't enough results
     * in the database. At the time of writing, the default value is 10, the maximum value is 50.
     *
     * @param results The maximum number of results to return.
     */
    public void setResults(int results) {
        setParameter("results", Integer.toString(results));
    }

    /**
     * The starting result position. This number should increment by the value of the results
     * argument each time the user chooses the next page of results. The default is 1.
     *
     * @param start The starting result position.
     */
    public void setStart(BigInteger start) {
        setParameter("start", start.toString(10));
    }

    /**
     * How far from the specified location to search for the query terms. The default radius
     * varies according to the location given.
     *
     * @param radius How far from the specified location to search for the query terms.
     */
    public void setRadius(float radius) {
        setParameter("radius", Float.toString(radius));
    }

    /**
     * Street name, the number is optional.
     *
     * @param street Street name, the number is optional.
     */
    public void setStreet(String street) {
        setParameter("street", street);
    }

    /**
     * City name.
     *
     * @param city City name.
     */
    public void setCity(String city) {
        setParameter("city", city);
    }

    /**
     * The United States state. You can spell out the full state name or you can use the two-letter abbreviation.
     *
     * @param state The United States state.
     */
    public void setState(String state) {
        setParameter("state", state);
    }

    /**
     * The five-digit zip code, or the five-digit code plus four-digit extension. If this location
     * contradicts the city and state specified, the zip code takes priority.
     *
     * @param zip The five-digit zip code, or the five-digit code plus four-digit extension.
     */
    public void setZip(String zip) {
        setParameter("zip", zip);
    }

    /**
     * Sets the location portion of the search as a string. This free field lets
     * users enter any of the following:
     * <p/>
     * <ul>
     * <li>city, state</li>
     * <li>city, state, zip</li>
     * <li>zip</li>
     * <li>street, city, state</li>
     * <li>street, city, state, zip</li>
     * <li>street, zip</li>
     * </ul>
     *
     * @param location Free text location field.
     */
    public void setLocation(String location) {
        setParameter("location", location);
    }

    /**
     * Sets the latitude of the local area to search.
     *
     * @param latitude The latitude of the local area to search.
     */
    public void setLatitude(String latitude) {
        setParameter("latitude", latitude);
    }

    /**
     * Sets the longitude of the local area to search.
     *
     * @param longitude The longitude of the local area to search.
     */
    public void setLongitude(String longitude) {
        setParameter("longitude", longitude);
    }

    /**
     * Sets the sort order for local search results. The options at the time
     * of writing are "distance", "rating" and "name", while the default value is
     * to sort by relevance.
     *
     * @param sort The sort order for local search results.
     */
    public void setSort(String sort) {
        setParameter("sort", sort);
    }

    /**
     * The id of a category to search in. This id corresponds to the id attribute of
     * the Category entity. If you specify multiple categories , results are taken
     * from entries that appear in all of the specified categories.
     *
     * @param category The ID of the category to search in.
     */
    public void addCategory(int category) {
        addParameter("category", Integer.toString(category));
    }

    /**
     * The id of a category to omit results from. Multiple categories may be omitted,
     * and a result will not be returned if it appears in any of the specified categories.
     *
     * @param category The ID of a category to omit results from.
     */
    public void addOmitCategory(int category) {
        addParameter("omit_category", Integer.toString(category));
    }

    /**
     * The minimum average rating (on a five point scale) for a result. If this is
     * specified, no results without ratings will be returned.
     *
     * @param rating The minimum average rating for a result.
     */
    public void setMinimumRating(int rating) {
        setParameter("minimum_rating", Integer.toString(rating));
    }
}