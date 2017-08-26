package org.mb.m3r.chaac

import android.app.Application
import org.mb.m3r.chaac.di.ApplicationComponent
import org.mb.m3r.chaac.di.DaggerApplicationComponent

/**
 * @author Melby Baldove
 */
class ChaacApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        applicationComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()
    }

}

