package org.mb.m3r.chaac.ui.photo

import android.os.Bundle
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.ui.base.BaseActivity
import org.mb.m3r.chaac.util.ActivityUtil
import javax.inject.Inject
import javax.inject.Provider


class PhotoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)

        if (savedInstanceState == null) {
            ActivityUtil.addFragmentToActivity(supportFragmentManager, R.id.frag_container, PhotoFragment())
        }
    }
}