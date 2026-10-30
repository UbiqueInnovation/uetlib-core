package ch.ubique.uetlib.image.webp;

import java.time.Duration;

public record WebpEncodingConfig(
        Duration duration,
        WebpQuality quality,
        CompressionMethod method,
        int compressionFactor,
        Duration processTimeout) {

    public WebpEncodingConfig withDuration(final Duration duration) {
        return new WebpEncodingConfig(duration, quality, method, compressionFactor, processTimeout);
    }

    public static WebpEncodingConfigBuilder builder() {
        return new WebpEncodingConfigBuilder(
                Duration.ofSeconds(1),
                LosslessWebp.INSTANCE,
                CompressionMethod.M4,
                75,
                Duration.ofSeconds(30));
    }

    public static final class WebpEncodingConfigBuilder {

        private Duration duration;
        private WebpQuality quality;

        private CompressionMethod method;

        private int compressionFactor;

        private Duration processTimeout;

        private WebpEncodingConfigBuilder(
                final Duration initialDuration,
                final WebpQuality initialQuality,
                final CompressionMethod method,
                final int compressionFactor,
                final Duration processTimeout) {
            this.duration = initialDuration;
            this.quality = initialQuality;
            this.method = method;
            this.compressionFactor = compressionFactor;
            this.processTimeout = processTimeout;
        }

        public WebpEncodingConfigBuilder duration(final Duration duration) {
            this.duration = duration;
            return this;
        }

        public WebpEncodingConfigBuilder quality(final WebpQuality quality) {
            this.quality = quality;
            return this;
        }

        public WebpEncodingConfigBuilder method(final CompressionMethod method) {
            this.method = method;
            return this;
        }

        public WebpEncodingConfigBuilder compressionFactor(final int compressionFactor) {
            this.compressionFactor = compressionFactor;
            return this;
        }

        public WebpEncodingConfigBuilder processTimeout(final Duration processTimeout) {
            this.processTimeout = processTimeout;
            return this;
        }

        public WebpEncodingConfig build() {
            return new WebpEncodingConfig(
                    duration, quality, method, compressionFactor, processTimeout);
        }
    }
}
