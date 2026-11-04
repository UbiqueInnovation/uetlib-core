package ch.uetlib.core.utils;

import com.github.zafarkhaja.semver.Version;

public final class VersionUtils {

    private VersionUtils() {}

    public static boolean isLowerThanIgnorePrerelease(
            final Version thisVersion, final Version otherVersion) {
        if (thisVersion.isSamePatchVersionAs(otherVersion)) {
            return false;
        }

        return thisVersion.isLowerThan(otherVersion);
    }
}
