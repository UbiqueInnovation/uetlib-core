package ch.ubique.backend.core.file;

import static java.util.Objects.requireNonNullElse;

import ch.ubique.backend.core.io.FileProvider;

import org.jspecify.annotations.Nullable;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @deprecated since 2.1.0, use {@link FileProvider} instead
 */
@Deprecated(since = "2.1.0")
public class ByteFile {

    private String fileName;
    private String fileHash;
    private String contentType;
    private byte[] bytes;

    public static ByteFile fromBytes(
            final byte[] bytes,
            @Nullable final String fileName,
            @Nullable final String contentType) {

        final var contentTypeOrOctetStream =
                requireNonNullElse(contentType, "application/octet-stream");

        return new ByteFile()
                .setBytes(bytes)
                .setFileName(fileName)
                .setContentType(contentTypeOrOctetStream);
    }

    public String getFileName() {
        return fileName;
    }

    public ByteFile setFileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileHash() {
        return fileHash;
    }

    public String getContentType() {
        return contentType;
    }

    public ByteFile setContentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public ByteFile setBytes(final byte[] bytes) {
        this.bytes = bytes;
        this.fileHash = DigestUtils.sha3_512Hex(bytes);
        return this;
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("fileName", fileName)
                .append("fileHash", fileHash)
                .toString();
    }
}
