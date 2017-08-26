package org.mb.m3r.chaac

import android.app.Application
import org.mb.m3r.chaac.di.ApplicationComponent

/**
 * @author Melby Baldove
 */
class ChaacApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
    }
}

