package ch.ubique.backend.core.http.file

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

object FileUtility {
    /**
     * Compress a byte array using gzip.
     *
     * @param input
     * @return the compressed data
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun gZip(input: ByteArray): ByteArray {
        val compressedOut = ByteArrayOutputStream()
        val gzipOut = GZIPOutputStream(compressedOut)
        gzipOut.write(input, 0, input.size)
        gzipOut.finish()
        gzipOut.close()
        return compressedOut.toByteArray()
    }
}