package ch.uetlib.core.utils;

import static ch.uetlib.core.utils.VersionUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import com.github.zafarkhaja.semver.Version;

import org.junit.jupiter.api.Test;

class VersionUtilsTest {

    @Test
    void testIsLowerThanIgnorePrerelease() {
        // lower patch version -> always lower
        assertTrue(isLowerThanIgnorePrerelease(Version.parse("2.1.8"), Version.parse("2.2.0")));

        // same patch version -> never lower
        assertFalse(
                isLowerThanIgnorePrerelease(
                        Version.parse("2.2.0-alpha.2"), Version.parse("2.2.0-alpha.1")));
        assertFalse(
                isLowerThanIgnorePrerelease(
                        Version.parse("2.2.0-alpha.2"), Version.parse("2.2.0-alpha.3")));

        // same patch version -> never lower
        assertFalse(
                isLowerThanIgnorePrerelease(
                        Version.parse("2.2.0-alpha.2"), Version.parse("2.2.0")));
        assertFalse(
                isLowerThanIgnorePrerelease(
                        Version.parse("2.2.0-alpha.2"), Version.parse("2.2.0")));

        // same patch version -> never lower
        assertFalse(
                isLowerThanIgnorePrerelease(
                        Version.parse("2.2.0"), Version.parse("2.2.0-alpha.2")));
    }
}
