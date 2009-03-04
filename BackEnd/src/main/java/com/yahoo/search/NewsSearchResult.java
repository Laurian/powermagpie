package com.yahoo.search;

/**
 * News search result.
 *
 * @author Ryan Kennedy
 */
public interface NewsSearchResult {
    /**
     * The title of the article.
     *
     * @return The title of the article.
     */
    String getTitle();

    /**
     * Summary text associated with the article.
     *
     * @return Summary text associated with the article.
     */
    String getSummary();

    /**
     * The URL for the article.
     *
     * @return The URL for the article.
     */
    String getUrl();

    /**
     * The URL for linking to the article. See <a href="http://developer.yahoo.net/faq/index.html#clickurl">URL linking</a> for more information.
     *
     * @return The URL for linking to the article.
     */
    String getClickUrl();

    /**
     * The company that distributed the news article, such as API or BBC.
     *
     * @return The company that distributed the news article, such as API or BBC.
     */
    String getNewsSource();

    /**
     * The URL for the news source.
     *
     * @return The URL for the news source.
     */
    String getNewsSourceUrl();

    /**
     * The language the article is written in.
     *
     * @return The language the article is written in.
     */
    String getLanguage();

    /**
     * The date the article was first published, in unix timestamp format.
     *
     * @return The date the article was first published, in unix timestamp format.
     */
    String getPublishDate();

    /**
     * The date the article was last modified, in unix timestamp format. <b>This field is optional.</b>
     *
     * @return The date the article was last modified, in unix timestamp format.
     */
    String getModificationDate();

    /**
     * The URL of a thumbnail file associated with the article, if present, and its height and width in pixels.
     * <b>This field is optional.</b>
     *
     * @return The URL of a thumbnail file associated with the article, if present, and its height and width in pixels.
     */
    ImageThumbnail getThumbnail();
}