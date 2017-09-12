package org.mb.m3r.chaac.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * @author Melby Baldove
 */
object ActivityUtil {
    fun addFragmentToActivity(fragmentManager: FragmentManager, containerLayoutId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction()
                .add(containerLayoutId, fragment)
                .commit()
    }
}