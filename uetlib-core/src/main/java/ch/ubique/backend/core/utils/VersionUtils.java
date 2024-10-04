package ch.ubique.backend.core.utils;

import com.github.zafarkhaja.semver.Version;
import jakarta.validation.constraints.NotNull;

public final class VersionUtils {

    private VersionUtils() {}

    public static boolean isLowerThanIgnorePrerelease(
            @NotNull final Version thisVersion,@NotNull final Version otherVersion) {
        if (thisVersion.isSamePatchVersionAs(otherVersion)) {
            return false;
        }

        if (thisVersion.isLowerThan(otherVersion)) {
            return true;
        }

        return false;
    }
}
