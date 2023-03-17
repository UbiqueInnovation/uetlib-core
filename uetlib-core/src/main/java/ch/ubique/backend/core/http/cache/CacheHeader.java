package ch.ubique.backend.core.http.cache;

/** dashes are stripped for Azure headers for custom headers, as they do not support them. */
public enum CacheHeader {
    MAX_AGE("max-age"),
    S_MAX_AGE("s-maxage"),
    NEXT_REFRESH("next-refresh"),
    BEST_BEFORE("best-before"),
    BACKOFF("backoff"),
    CONTENT_LENGTH_UNCOMPRESSED("content-length-uncompressed");

    private final String value;

    CacheHeader(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String noDashesAzure() {
        return value.replace("-", "");
    }

    public String withMicrosoftPrefix() {
        return "x-ms-meta-" + noDashesAzure();
    }

    public String withAmazonPrefix() {
        return "x-amz-meta-" + value;
    }
}
