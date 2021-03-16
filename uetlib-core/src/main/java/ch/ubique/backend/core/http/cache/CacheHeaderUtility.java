package ch.ubique.backend.core.http.cache;

public class CacheHeaderUtility {
    public static final String CACHE = "x-ms-metacache";
    public static final String NEXT_REFRESH = "x-ms-meta-nextrefresh";
    public static final String BEST_BEFORE = "x-ms-meta-bestbefore";
    public static final String REFRESH_BACKOFF = "x-ms-meta-backoff";
    public static final String MINIMUM_API_VERSION = "x-ms-meta-minimumapiversion";
    public static final String CACHE_SHORT = "cache";
    public static final String NEXT_REFRESH_SHORT = "nextrefresh";
    public static final String BEST_BEFORE_SHORT = "bestbefore";
    public static final String REFRESH_BACKOFF_SHORT = "backoff";
    public static final String MINIMUM_API_VERSION_SHORT = "minimumapiversion";
    public static final String MAX_AGE = "max-age";
    public static final String DEFAULT_API_VERSION = "1";

    private CacheHeaderUtility() {}
}
