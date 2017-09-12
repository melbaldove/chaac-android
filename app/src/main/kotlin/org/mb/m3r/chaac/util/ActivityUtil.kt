package org.mb.m3r.chaac.util

import android.app.Fragment
import android.app.FragmentManager

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