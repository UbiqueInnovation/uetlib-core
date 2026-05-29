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

    @Nullable private String fileName;
    @Nullable private String fileHash;
    @Nullable private String contentType;
    private byte @Nullable [] bytes;

    public static ByteFile fromBytes(
            final byte[] bytes,
             final @Nullable  String fileName,
             final @Nullable  String contentType) {

        final var contentTypeOrOctetStream =
                requireNonNullElse(contentType, "application/octet-stream");

        return new ByteFile()
                .setBytes(bytes)
                .setFileName(fileName)
                .setContentType(contentTypeOrOctetStream);
    }

    @Nullable
    public String getFileName() {
        return fileName;
    }

    public ByteFile setFileName( final @Nullable  String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Nullable
    public String getFileHash() {
        return fileHash;
    }

    @Nullable
    public String getContentType() {
        return contentType;
    }

    public ByteFile setContentType( final @Nullable  String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte @Nullable [] getBytes() {
        return bytes;
    }

    public ByteFile setBytes(final byte @Nullable [] bytes) {
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
