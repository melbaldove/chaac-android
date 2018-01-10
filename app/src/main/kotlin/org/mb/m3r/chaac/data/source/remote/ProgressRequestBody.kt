package org.mb.m3r.chaac.data.source.remote

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class ProgressRequestBody(private val mFile: File) : RequestBody() {

    var numWriteToCalls = 0
    var lastProgressPercentUpdate = 0f

    private val getProgressSubject: PublishSubject<Float> = PublishSubject.create<Float>()

    fun getProgressSubject(): Observable<Float> {
        return getProgressSubject
    }

    override fun contentType(): MediaType {
        return MediaType.parse("image/*")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        numWriteToCalls++

        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(mFile)
        var uploaded: Long = 0

        inputStream.use { `in` ->
            var read: Int
            read = `in`.read(buffer)
            while (read != -1) {

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = `in`.read(buffer)

                val progress = (uploaded.toFloat() / fileLength.toFloat()) * 100f
                //prevent publishing too many updates, which slows upload, by checking if the upload has progressed by at least 1 percent
                if (progress - lastProgressPercentUpdate > 1 || progress == 100f) {
                    // publish progress
                    // EMULATE SLOW NET REMOVE THIS LATER!!!!!!!!!!!!!!
                    Thread.sleep(100)
                    getProgressSubject.onNext(progress)
                    lastProgressPercentUpdate = progress
                }
            }
        }
    }

    companion object {
        private val DEFAULT_BUFFER_SIZE = 2048
    }
}