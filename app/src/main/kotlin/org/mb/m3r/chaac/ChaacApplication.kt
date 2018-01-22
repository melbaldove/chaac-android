package org.mb.m3r.chaac

import android.support.multidex.MultiDexApplication
import android.util.Log
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import org.mb.m3r.chaac.di.ApplicationComponent
import org.mb.m3r.chaac.di.DaggerApplicationComponent
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig



/**
 * @author Melby Baldove
 */
class ChaacApplication : MultiDexApplication() {

    lateinit var applicationComponent: ApplicationComponent

    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not initDagger your app in this process.
            return
        }
        LeakCanary.install(this)
        initFirebaseRemoteConfig()
        fetchRemoteConfig()
        initDagger()
        Stetho.initializeWithDefaults(applicationContext)
    }

    private fun fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        mFirebaseRemoteConfig.activateFetched()
                        Log.d("remoteConfig", mFirebaseRemoteConfig.getString("server_url"))

                    }
                    else {
                        Log.d("remoteConfig", mFirebaseRemoteConfig.getString("server_url"))

                        Log.d("remoteConfig", "not successful")
                    }
                }
    }

    private fun initDagger() {
        applicationComponent = DaggerApplicationComponent.builder()
                .application(this)
                .backendURL(mFirebaseRemoteConfig.getString("server_url"))
                .build()
    }

    private fun initFirebaseRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .build()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults)
    }


}

