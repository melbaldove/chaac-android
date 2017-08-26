package org.mb.m3r.chaac.ui.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.di.ActivityComponent
import org.mb.m3r.chaac.di.DaggerActivityComponent

/**
 * @author Melby Baldove
 */
abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        ButterKnife.bind(this)
    }

    val activityComponent: ActivityComponent
        get() {
            val applicationComponent = (application as ChaacApplication).applicationComponent
            return DaggerActivityComponent.builder()
                    .applicationComponent(applicationComponent)
                    .build()
        }

}