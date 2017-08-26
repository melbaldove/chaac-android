package org.mb.m3r.chaac.util

import android.content.Context
import android.os.Environment.*
import android.util.Log
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Melby Baldove
 */
object ChaacUtil {

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

    fun saveImageFromTempFile(path: String): File {
        val source = File(path)
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "pic$timeStamp.jpg"
        val dest = File(getPictureDirectory(), imageFileName)

        try {
            FileUtils.copyFile(source, dest)
            source.delete()
        } catch(e: IOException) {
            Log.e("Error", e.message)
        }

        return dest
    }

}