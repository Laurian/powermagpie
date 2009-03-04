package com.yahoo.search;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Video search result.
 *
 * @author Ryan Kennedy
 */
public interface VideoSearchResult {
    /**
     * The title of the video file.
     *
     * @return The title of the video file.
     */
    String getTitle();

    /**
     * Summary text associated with the video file.
     *
     * @return Summary text associated with the video file.
     */
    String getSummary();

    /**
     * The URL for the video file or stream.
     *
     * @return The URL for the video file or stream.
     */
    String getUrl();

    /**
     * The URL for linking to the video file. See <a href="http://developer.yahoo.net/faq/index.html#clickurl">URL linking</a> for more information.
     *
     * @return The URL for linking to the video file.
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
     * One of avi, flash, mpeg, msmedia, quicktime, or realmedia.
     * <p/>
     * <b>NOTE:</b> The value returned is not guaranteed to be one of the above options. The XML schema for video
     * search specifies that FileFormat is of type xs:string. It is possible you may receive something other than
     * the previously mentioned formats.
     *
     * @return One of avi, flash, mpeg, msmedia, quicktime, or realmedia.
     */
    String getFileFormat();

    /**
     * The height of the keyframe Yahoo extracted from the video in pixels. The video has this height if rendered
     * at 100%. <b>This field is optional.</b>
     *
     * @return The height of the keyframe Yahoo extracted from the video in pixels. The video has this height if rendered at 100%.
     */
    BigInteger getHeight();

    /**
     * The width of the keyframe Yahoo extracted from the video in pixels. The video has this width if rendered
     * at 100%. <b>This field is optional.</b>
     *
     * @return The width of the keyframe Yahoo extracted from the video in pixels. The video has this width if rendered at 100%.
     */
    BigInteger getWidth();

    /**
     * The duration of the video file in seconds.
     *
     * @return The duration of the video file in seconds.
     */
    BigDecimal getDuration();

    /**
     * Whether the video file is streaming (true) or not (false).
     *
     * @return Whether the video file is streaming (true) or not (false).
     */
    boolean isStreaming();

    /**
     * Either 1 (mono) or 2 (stereo).
     * <p/>
     * <b>NOTE:</b> The value returned is not guaranteed to be one of the above options. The XML schema for video
     * search specifies that Channels is of type xs:string. It is possible you may receive something other than
     * the previously mentioned formats.
     *
     * @return Either 1 (mono) or 2 (stereo).
     */
    String getChannels();

    /**
     * The URL of the thumbnail file and its height and width in pixels. <b>This field is optional.</b>
     *
     * @return The URL of the thumbnail file and its height and width in pixels.
     */
    ImageThumbnail getThumbnail();

    /**
     * The creator of the video file. <b>This field is optional.</b>
     *
     * @return The creator of the video file.
     */
    String getPublisher();

    /**
     * Provides any restrictions for this media object. Restrictions include noframe and noinline. <b>This field is optional.</b>
     * <p/>
     * <ul>
     * <li>Noframe means that you should not display it with a framed page on your site.</li>
     * <li>Noinline means that you should not inline the object in the frame up top (it won't work because the
     * site has some protection based on the "referrer" field).</li>
     * </ul>
     *
     * @return Returns the restrictions placed on the video file.
     */
    String getRestrictions();

    /**
     * The copyright owner. <b>This field is optional.</b>
     *
     * @return The copyright owner.
     */
    String getCopyright();
}