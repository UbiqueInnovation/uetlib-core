package ch.ubique.backend.image.blurhash;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;

import io.trbl.blurhash.BlurHash;

import org.imgscalr.Scalr;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;

/** A client to retrieve blur hashes and image dimensions for images. */
public class BlurHashClient {

    public static final String FALLBACK_GREY_BLUR_HASH = "L0HoLG%MM{%M_3fQfQfQIUfQfQfQ";
    public static final double FALLBACK_ASPECT_RATIO = 1.0;
    public static final String DEFAULT_USER_AGENT = "ubique-backend";
    private static final Logger logger = LoggerFactory.getLogger(BlurHashClient.class);
    private final Cache<String, BlurHashRepresentation> urlToBlurHashCache;
    private final String userAgent;

    public BlurHashClient(final int maximumSize, final String userAgent) {
        this.urlToBlurHashCache = Caffeine.newBuilder().maximumSize(maximumSize).build();
        this.userAgent = userAgent;
    }

    public BlurHashClient(
            final int maximumWeight,
            final Weigher<String, BlurHashRepresentation> weigher,
            final String userAgent) {
        this.urlToBlurHashCache =
                Caffeine.newBuilder().maximumWeight(maximumWeight).weigher(weigher).build();
        this.userAgent = userAgent;
    }

    /**
     * Return an url connection inputstream with custom user agent. Should be used when syncing data
     * from websites, so that the other side knows who's crawling the data.
     */
    private InputStream getInputStreamWithCustomUserAgent(final String url) throws IOException {
        final var hc = URI.create(url).toURL().openConnection();
        hc.setRequestProperty("User-Agent", userAgent);
        hc.setRequestProperty("Referer", url);
        return hc.getInputStream();
    }

    public String getBlurHash(final String imageUrl, final String fallbackBlurHash) {
        return encodeToBlurHashRepresentation(imageUrl, fallbackBlurHash).blurHash();
    }

    public BlurHashRepresentation encodeToBlurHashRepresentation(
            final String imageUrl, final String fallbackBlurHash) {
        logger.debug("getting blur hash for image with url {}", imageUrl);
        return urlToBlurHashCache.get(
                imageUrl,
                url -> {
                    try {
                        return encodeToBlurHashRepresentation(
                                ImageIO.read(getInputStreamWithCustomUserAgent(imageUrl)),
                                fallbackBlurHash);
                    } catch (final IOException e) {
                        return new BlurHashRepresentation(fallbackBlurHash, FALLBACK_ASPECT_RATIO);
                    }
                });
    }

    public String getBlurHash(final byte[] imageBytes, final String fallbackBlurHash) {
        return encodeToBlurHashRepresentation(imageBytes, fallbackBlurHash).blurHash();
    }

    public BlurHashRepresentation encodeToBlurHashRepresentation(
            final byte[] imageBytes, final String fallbackBlurHash) {
        try (final var inputStream = new ByteArrayInputStream(imageBytes)) {
            return encodeToBlurHashRepresentation(inputStream, fallbackBlurHash);
        } catch (final IOException e) {
            logger.info("failed to create blur hash for imageBytes: {}", imageBytes, e);
            return new BlurHashRepresentation(fallbackBlurHash, FALLBACK_ASPECT_RATIO);
        }
    }

    public String getBlurHash(final InputStream inputStream, final String fallbackBlurHash) {
        return encodeToBlurHashRepresentation(inputStream, fallbackBlurHash).blurHash();
    }

    private BlurHashRepresentation encodeToBlurHashRepresentation(
            final InputStream inputStream, final String fallbackBlurHash) {
        try {
            return encodeToBlurHashRepresentation(ImageIO.read(inputStream), fallbackBlurHash);
        } catch (final IOException e) {
            logger.info("failed to create blur hash for inputStream: {}", inputStream, e);
            return new BlurHashRepresentation(fallbackBlurHash, FALLBACK_ASPECT_RATIO);
        }
    }

    public String getBlurHash(final BufferedImage bufferedImage, final String fallbackBlurHash) {
        return encodeToBlurHashRepresentation(bufferedImage, fallbackBlurHash).blurHash();
    }

    public BlurHashRepresentation encodeToBlurHashRepresentation(
            final BufferedImage bufferedImage, final String fallbackBlurHash) {
        try {
            if (bufferedImage.getHeight() <= 150 && bufferedImage.getWidth() <= 150) {
                return encodeToBlurHashRepresentationNoResize(bufferedImage);
            }

            final var thumbnail = Scalr.resize(bufferedImage, 150);
            final var blurHashRepresentation = encodeToBlurHashRepresentationNoResize(thumbnail);
            // Flush clears native references so the image can be GC-ed. See tip of Scalr::resize
            thumbnail.flush();
            return blurHashRepresentation;
        } catch (final IllegalArgumentException | ImagingOpException e) {
            logger.info("failed to create blur hash for image: {}", bufferedImage, e);
            return new BlurHashRepresentation(fallbackBlurHash, FALLBACK_ASPECT_RATIO);
        } finally {
            // Flush clears native references so the image can be GC-ed. See tip of Scalr::resize
            bufferedImage.flush();
        }
    }

    public BlurHashRepresentation encodeToBlurHashRepresentationNoResize(
            final BufferedImage bufferedImage) {
        final var aspectRatio = (double) bufferedImage.getWidth() / bufferedImage.getHeight();
        return new BlurHashRepresentation(BlurHash.encode(bufferedImage), aspectRatio);
    }
}
