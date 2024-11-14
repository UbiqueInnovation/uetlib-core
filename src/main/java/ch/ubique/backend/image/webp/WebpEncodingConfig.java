package ch.ubique.backend.image.webp;

import java.time.Duration;

public record WebpEncodingConfig(
        Duration duration, WebpQuality quality, CompressionMethod method, int compressionFactor) {
    public static WebpEncodingConfigBuilder builder() {
        return new WebpEncodingConfigBuilder(
                Duration.ofSeconds(1), LosslessWebp.INSTANCE, CompressionMethod.M4, 75);
    }

    public static class WebpEncodingConfigBuilder {
        private Duration duration;
        private WebpQuality quality;

        private CompressionMethod method;

        private int compressionFactor;

        private WebpEncodingConfigBuilder(
                final Duration initialDuration,
                final WebpQuality initialQuality,
                CompressionMethod method,
                int compressionFactor) {
            this.duration = initialDuration;
            this.quality = initialQuality;
            this.method = method;
            this.compressionFactor = compressionFactor;
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

        public WebpEncodingConfig build() {
            return new WebpEncodingConfig(duration, quality, method, compressionFactor);
        }
    }
}
