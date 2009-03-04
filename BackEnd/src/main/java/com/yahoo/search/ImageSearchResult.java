package com.yahoo.search;

import java.math.BigInteger;

/**
 * Individual image search result.
 *
 * @author Ryan Kennedy
 */
public interface ImageSearchResult {
    /**
     * The title of the image file.
     *
     * @return The title of the image file.
     */
    String getTitle();

    /**
     * Summary text associated with the image file.
     *
     * @return Summary text associated with the image file.
     */
    String getSummary();

    /**
     * The URL for the image file.
     *
     * @return The URL for the image file.
     */
    String getUrl();

    /**
     * The URL for linking to the image file. See <a href="http://developer.yahoo.net/faq/index.html#clickurl">URL linking</a> for more information.
     *
     * @return The URL for linking to the image file.
     */
    String getClickUrl();

    /**
     * The URL of the web page hosting the content.
     *
     * @return The URL of the web page hosting the content.
     */
    String getRefererUrl();

    /**
     * The size of the file in bytes.
     *
     * @return The size of the file in bytes.
     */
    BigInteger getFileSize();

    /**
     * One of bmp, gif, jpg, or png.
     * <p/>
     * <b>NOTE:</b> The value returned is not guaranteed to be one of the above options. The XML schema for image
     * search specifies that FileFormat is of type xs:string. It is possible you may receive something other than
     * the previously mentioned formats.
     *
     * @return One of bmp, gif, jpg, or png.
     */
    String getFileFormat();

    /**
     * The height of the image in pixels. <b>This field is optional.</b>
     *
     * @return The height of the image in pixels.
     */
    BigInteger getHeight();

    /**
     * The width of the image in pixels. <b>This field is optional.</b>
     *
     * @return The width of the image in pixels.
     */
    BigInteger getWidth();

    /**
     * The URL of the thumbnail file and its height and width in pixels.
     *
     * @return The URL of the thumbnail file and its height and width in pixels.
     */
    ImageThumbnail getThumbnail();

    /**
     * The creator of the image file. <b>This field is optional.</b>
     *
     * @return The creator of the image file.
     */
    String getPublisher();

    /**
     * Provides any restrictions for this media object. Restrictions include noframe and noinline. <b>This field
     * is optional.</b>
     * <p/>
     * <ul>
     * <li>Noframe means that you should not display it with a framed page on your site.</li>
     * <li>Noinline means that you should not inline the object in the frame up top (it won't work because the
     * site has some protection based on the "referrer" field).</li>
     * </ul>
     * <p/>
     * <b>NOTE:</b> The value returned is not guaranteed to be one of the above options. The XML schema for
     * image search specifies that Restrictions is of type xs:string. It is possible you may receive something other
     * than the previously mentioned numbers.
     *
     * @return Provides any restrictions for this media object. Restrictions include noframe and noinline.
     */
    String getRestrictions();

    /**
     * The copyright owner. <b>This field is optional.</b>
     *
     * @return The copyright owner.
     */
    String getCopyright();

}