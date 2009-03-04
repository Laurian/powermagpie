package com.yahoo.search;

import java.math.BigInteger;

/**
 * Image thumbnail.
 *
 * @author Ryan Kennedy.
 */
public interface ImageThumbnail {
    /**
     * The URL for the image thumbnail.
     *
     * @return The URL for the image thumbnail.
     */
    String getUrl();

    /**
     * The height of the image thumbnail in pixels. <b>This field is optional.</b>
     *
     * @return The height of the image thumbnail in pixels.
     */
    BigInteger getHeight();

    /**
     * The width of the image thumbnail in pixels. <b>This field is optional.</b>
     *
     * @return The width of the image thumbnail in pixels.
     */
    BigInteger getWidth();
}
