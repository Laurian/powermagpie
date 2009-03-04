package com.yahoo.search;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Local search result.
 *
 * @author Ryan Kennedy
 */
public interface LocalSearchResult {
    /**
     * Listing ID of the result.
     *
     * @return The listing ID of the result.
     */
    BigInteger getId();

    /**
     * Name of the result.
     *
     * @return Name of the result.
     */
    String getTitle();

    /**
     * Street address of the result.
     *
     * @return Street address of the result.
     */
    String getAddress();

    /**
     * City in which the result is located.
     *
     * @return City in which the result is located.
     */
    String getCity();

    /**
     * State in which the result is located.
     *
     * @return State in which the result is located.
     */
    String getState();

    /**
     * Phone number of the business, if known.
     *
     * @return Phone number of the business, if known.
     */
    String getPhone();

    /**
     * The latitude of the location.
     *
     * @return The latitude of the location.
     */
    BigDecimal getLatitude();

    /**
     * The longitude of the location.
     *
     * @return The longitude of the location.
     */
    BigDecimal getLongitude();

    /**
     * End-user ratings for the business or service. <b>This field is optional.</b>
     *
     * @return End-user ratings for the business or service.
     */
    LocalSearchRating getRating();

    /**
     * Distance in miles.
     * <p/>
     * The distance as calculated by one of the following methods:
     * <ul>
     * <li>When you enter a street address along with your search term, this is the distance from that
     * particular street address to each result.</li>
     * <li>When you enter your search term along with a city or a city and state, this is the distance from the
     * city center to the business you're looking for.</li>
     * </ul>
     *
     * @return Distance in miles.
     */
    String getDistance();

    /**
     * The URL to the detailed page for a business.
     *
     * @return The URL to the detailed page for a business.
     */
    String getUrl();

    /**
     * The URL for linking to the detailed page for a business. See <a href="http://developer.yahoo.net/faq/index.html#clickurl">URL linking</a> for more information.
     *
     * @return The URL for linking to the detailed page for a business.
     */
    String getClickUrl();

    /**
     * The URL of a map for the address.
     *
     * @return The URL of a map for the address.
     */
    String getMapUrl();

    /**
     * The URL of the businesses website, if known. <b>This field is optional.</b>
     *
     * @return The URL of the businesses website, if known.
     */
    String getBusinessUrl();

    /**
     * The URL for linking to the businesses website if known. See <a href="http://developer.yahoo.net/faq/index.html#clickurl">URL linking</a> for more information. <b>This field is optional.</b>
     *
     * @return The URL for linking to the businesses website if known.
     */
    String getBusinessClickUrl();

    /**
     * All the categories in which this listing is classified. It may be fed back
     * into the search using the category parameter to retrieve listings from this category.
     *
     * @return All the categories in which this listing is classified.
     */
    LocalSearchCategory[] listCategories();

    /**
     * Local search category type.
     */
    public class LocalSearchCategory {
        private String name = null;
        private BigInteger id = null;


        public LocalSearchCategory(String name, BigInteger id) {
            this.name = name;
            this.id = id;
        }


        public String getName() {
            return name;
        }

        public BigInteger getId() {
            return id;
        }
    }

    /**
     * Local search rating type.
     */
    public class LocalSearchRating {
        private BigDecimal averageRating;
        private BigInteger totalRatings;
        private BigInteger totalReviews;
        private BigInteger lastReviewDate;
        private String lastReviewIntro;


        public LocalSearchRating(BigDecimal averageRating, BigInteger totalRatings, BigInteger totalReviews, BigInteger lastReviewDate, String lastReviewIntro) {
            this.averageRating = averageRating;
            this.totalRatings = totalRatings;
            this.totalReviews = totalReviews;
            this.lastReviewDate = lastReviewDate;
            this.lastReviewIntro = lastReviewIntro;
        }


        public BigDecimal getAverageRating() {
            return averageRating;
        }

        public BigInteger getTotalRatings() {
            return totalRatings;
        }

        public BigInteger getTotalReviews() {
            return totalReviews;
        }

        public BigInteger getLastReviewDate() {
            return lastReviewDate;
        }

        public String getLastReviewIntro() {
            return lastReviewIntro;
        }
    }
}