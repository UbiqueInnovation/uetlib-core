package ch.ubique.backend.core.blobstorage;

import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.common.policy.RequestRetryOptions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class BlobStorageService {

    private final Logger logger;

    private final String cacheControl;
    private final BlobServiceClient blobServiceClient;
    private final BlobServiceAsyncClient asyncBlobServiceClient;

    public BlobStorageService(String connectionString, String cacheControl) {
        this.cacheControl = cacheControl;
        this.logger = LoggerFactory.getLogger(getClass());

        this.blobServiceClient =  new BlobServiceClientBuilder().connectionString(connectionString).retryOptions(new RequestRetryOptions()).buildClient();
        this.asyncBlobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildAsyncClient();
    }

    public void upload(byte[] bytes, String container, String path, String contentType) {
        // Compress with Gzip
        byte[] compressed = gZip(bytes);
        InputStream data = new ByteArrayInputStream(compressed);

        // Set important headers
        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentEncoding("gzip");
        headers.setContentType(contentType);
        headers.setCacheControl(cacheControl);

        // Define upload
        var uploadOptions = new BlobParallelUploadOptions(data, compressed.length);
        uploadOptions.setHeaders(headers);
        uploadOptions.setRequestConditions(new BlobRequestConditions());
        uploadOptions.setParallelTransferOptions(new ParallelTransferOptions().setMaxConcurrency(100));

        var timeout = Duration.ofSeconds(30);

        // Upload
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(container).getBlobClient(path);
        blobClient.uploadWithResponse(uploadOptions, timeout, Context.NONE);
    }
    
    public Mono<Response<BlockBlobItem>> uploadAsync(byte[] bytes, String container, String path, String contentType) {
        // Compress with Gzip
        byte[] compressed = gZip(bytes);
        InputStream data = new ByteArrayInputStream(compressed);

        // Set important headers
        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentEncoding("gzip");
        headers.setContentType(contentType);
        headers.setCacheControl(cacheControl);

        // Define upload
        var uploadOptions = new BlobParallelUploadOptions(data, compressed.length);
        uploadOptions.setHeaders(headers);
        uploadOptions.setRequestConditions(new BlobRequestConditions());

        // Upload
        BlobAsyncClient blobClient = asyncBlobServiceClient.getBlobContainerAsyncClient(container).getBlobAsyncClient(path);
        return blobClient.uploadWithResponse(uploadOptions);
    }

    public InputStreamReader getInputStreamReader(String container, String path) throws IOException {
        return new InputStreamReader(getInputStream(container, path));
    }

    public InputStream getInputStream(String container, String path) throws IOException {
        BlobClient client =  blobServiceClient.getBlobContainerClient(container).getBlobClient(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        client.download(outputStream);
        byte[] bytes = outputStream.toByteArray();
        outputStream.close();
        return new ByteArrayInputStream(bytes);
    }

    private byte[] gZip(byte[] input)  {
        try {
            ByteArrayOutputStream compressedOut = new ByteArrayOutputStream(input.length);
            GZIPOutputStream gzipOut = new GZIPOutputStream(compressedOut);
            gzipOut.write(input);
            gzipOut.close();
            byte[] compressed = compressedOut.toByteArray();
            compressedOut.close();
            return compressed;
        } catch (IOException e) {
            logger.warn("Compressing failed", e);
        }
        return new byte[0];
    }
}
