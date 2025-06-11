# CHANGELOG

## 1.0.1

### Improvements

- Added support for different key serialization (`DEFAULT`/`POSIX`/`UPPERCASE`) methods of `LocalizedValue` objects using jackson annotation, e.g.:
    ```
    record KeySerialization(
        @JsonLocalizedValue(keySerialization = SerializationMethod.POSIX)
            LocalizedValue<String> name) {}
    ```
  This can even be combined with the existing unwrapping functionality:
    ```
    record Combined(
        @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
            @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
            LocalizedValue<String> name) {}  
    ```

### Breaking Changes

- Removed `JsonUnwrappedLocalizedValueSerializer`. The functionality is unchange, but you do not need to set the
  serializer explicitly anymore, see the improvements above and:
    ```diff
    record Unwrapped(
        @JsonUnwrapped(suffix = ":suf")
    -        @JsonSerialize(using = JsonUnwrappedLocalizedValueSerializer.class)
            LocalizedValue<String> name) {}
    ```

## 1.0.0

First release. The code was moved
from [UbiqueInnovation/backend-jvm-lib](https://github.com/UbiqueInnovation/backend-jvm-lib).

