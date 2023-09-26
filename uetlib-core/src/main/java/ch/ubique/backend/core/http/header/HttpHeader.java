package ch.ubique.backend.core.http.header;

/** dashes are stripped for Azure headers for custom headers, as they do not support them. */
public enum HttpHeader {
    MAX_AGE("max-age"),
    S_MAX_AGE("s-maxage"),
    NEXT_REFRESH("next-refresh"),
    BEST_BEFORE("best-before"),
    BACKOFF("backoff"),
    CONTENT_LENGTH_UNCOMPRESSED("content-length-uncompressed"),
    AMZ_ACL("x-amz-acl");

    private final String value;
    private final String noDashesAzure;
    private final String withMicrosoftPrefix;
    private final String withAmazonPrefix;

    HttpHeader(final String value) {
        this.value = value;
        this.noDashesAzure = value.replace("-", "");
        this.withMicrosoftPrefix = "x-ms-meta-" + noDashesAzure;
        this.withAmazonPrefix = "x-amz-meta-" + value;
    }

    public String value() {
        return value;
    }

    public String noDashesAzure() {
        return noDashesAzure;
    }

    public String withMicrosoftPrefix() {
        return withMicrosoftPrefix;
    }

    public String withAmazonPrefix() {
        return withAmazonPrefix;
    }


}
