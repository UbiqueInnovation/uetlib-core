package ch.uetlib.core.io;

import java.io.InputStream;

public record DirectInputStreamProvider(InputStream inputStream) implements InputStreamProvider {}
