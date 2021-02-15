package ch.ubique.backend.core.http.cache

object CacheHeaderUtility {
    const val CACHE = "x-ms-metacache"
    const val NEXT_REFRESH = "x-ms-meta-nextrefresh"
    const val BEST_BEFORE = "x-ms-meta-bestbefore"
    const val REFRESH_BACKOFF = "x-ms-meta-backoff"
    const val MINIMUM_API_VERSION = "x-ms-meta-minimumapiversion"
    const val CACHE_SHORT = "cache"
    const val NEXT_REFRESH_SHORT = "nextrefresh"
    const val BEST_BEFORE_SHORT = "bestbefore"
    const val REFRESH_BACKOFF_SHORT = "backoff"
    const val MINIMUM_API_VERSION_SHORT = "minimumapiversion"
    const val MAX_AGE = "max-age"
    const val DEFAULT_API_VERSION = "1"
}