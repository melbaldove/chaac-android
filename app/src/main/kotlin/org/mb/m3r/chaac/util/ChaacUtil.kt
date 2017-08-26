package org.mb.m3r.chaac.util

import android.content.Context
import android.os.Environment.*
import org.mb.m3r.chaac.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author Melby Baldove
 */
object ChaacUtil {

    val DIRECTORY_NAME = "Chaac"

    fun getPictureDirectory(): File {
        val dir = getExternalStoragePublicDirectory(
                DIRECTORY_PICTURES + '/' + DIRECTORY_NAME)

        if(!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun createTempImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "temp"
        val storageDir = getPictureDirectory()
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        return image
    }

}