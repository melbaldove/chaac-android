package org.mb.m3r.chaac

import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import org.mb.m3r.chaac.di.ApplicationComponent
import org.mb.m3r.chaac.di.DaggerApplicationComponent

/**
 * @author Melby Baldove
 */
class ChaacApplication : MultiDexApplication() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        init()
    }

    private fun init() {
        applicationComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()

        Stetho.initializeWithDefaults(applicationContext)
    }
}

