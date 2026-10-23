# Uetlib Core

Ubique library for some utilities and helpers 

## Time validation

With Hibernate Validator, `@Positive`, `@PositiveOrZero`, `@Negative`, and
`@NegativeOrZero` also support `java.time.Duration`. Validators are discovered
through Java's service loader; no application configuration is required. Comparisons
preserve nanosecond precision. Null is valid; add `@NotNull` when required.

`@Min` and `@Max` also support `java.time.Year`, comparing its numeric year
against the inclusive `long` bound.

Year bounds accept null; use `@NotNull` when required.

## Changelog

See [CHANGELOG.md](CHANGELOG.md)