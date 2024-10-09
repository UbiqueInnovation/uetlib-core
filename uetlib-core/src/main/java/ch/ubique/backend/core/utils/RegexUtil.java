package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.regex.SerializableMatchResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

public final class RegexUtil {

    private RegexUtil() {}

    public static SerializableMatchResult toSerializableMatchResult(final Matcher matcher) {
        final var matchResult = matcher.toMatchResult();
        final var first = matchResult.start();
        final var last = matchResult.end();
        final var text = matcher.hasMatch() ? matcher.group() : null;
        final var groupIndices =
                IntStream.rangeClosed(0, matchResult.groupCount())
                        .flatMap(g -> IntStream.of(matchResult.start(g), matchResult.end(g)))
                        .toArray();

        final List<String> groups;
        if (text != null) {
            groups =
                    IntStream.rangeClosed(0, matchResult.groupCount())
                            .boxed()
                            .map(
                                    g ->
                                            text.substring(
                                                    groupIndices[g * 2] - first,
                                                    groupIndices[g * 2 + 1] - first))
                            .toList();
        } else {
            groups = List.of();
        }

        return new SerializableMatchResult(
                first,
                last,
                matchResult.groupCount(),
                groups,
                groupIndices,
                text,
                matchResult.namedGroups());
    }
}
