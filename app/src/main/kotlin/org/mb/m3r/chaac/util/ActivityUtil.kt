package org.mb.m3r.chaac.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat

/**
 * @author Melby Baldove
 */
object ActivityUtil {
    val PERMISSION_REQUEST_WRITE_EXTERNAL = 1337
    val PERMISSION_REQUEST_CAMERA = 1338

    fun addFragmentToActivity(fragmentManager: FragmentManager, containerLayoutId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction()
                .add(containerLayoutId, fragment)
                .commit()
    }

    fun hasPermission(context: Context, permission: String): Boolean =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

}