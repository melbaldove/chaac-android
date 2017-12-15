package org.mb.m3r.chaac.util

import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.support.v4.content.FileProvider
import android.util.Log
import io.reactivex.Single
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.mb.m3r.chaac.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Melby Baldove
 */
object FileUtil {

    val DIRECTORY_NAME = "Chaac"

    fun getPictureDirectory(): File {
        val dir = getExternalStoragePublicDirectory(
                DIRECTORY_PICTURES + '/' + DIRECTORY_NAME)

        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun createTempImageFile(): File {
        val imageFileName = "temp"
        val storageDir = getPictureDirectory()
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        return image
    }

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "pic$timeStamp.jpg"

        return File(getPictureDirectory(), imageFileName)
    }

    /*
     * Stores image from temp directory to picture directory
     */
    fun storeImage(path: String): Single<File> = Single.create<File> { subscriber ->
        val tempSource = File(path)
        // Create an image file name
        val dest = createImageFile()
        moveFile(tempSource, dest)

        subscriber.onSuccess(dest)
    }

    fun moveFile(source: File, dest: File) {
        try {
            FileUtils.copyFile(source, dest)
            source.delete()
        } catch (e: IOException) {
            Log.e("Error", e.message)
        }
    }

    fun checkSum(file: File): Single<String> = Single.create<String> { subscriber ->
        val fileInputStream = FileInputStream(file)
        try {
            /*
             * This is a workaround to a bug described in this link
             * https://stackoverflow.com/questions/9126567/method-not-found-using-digestutils-in-android
             */
            val checksum = String(Hex.encodeHex(DigestUtils.md5(IOUtils.toByteArray(fileInputStream))))
            subscriber.onSuccess(checksum)
        } catch (e: IOException) {
            e.printStackTrace()
            subscriber.onError(e)
        } finally {
            IOUtils.closeQuietly(fileInputStream)
        }
    }

    fun deleteFile(path: String) {
        try {
            File(path).delete()
        } catch (e: IOException) {
            Log.e("Error", e.message)
        }
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    }
}