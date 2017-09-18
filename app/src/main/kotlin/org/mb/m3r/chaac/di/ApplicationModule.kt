package org.mb.m3r.chaac.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.di.scopes.PerApplication
import org.mb.m3r.chaac.util.schedulers.SchedulerProvider
import org.mb.m3r.chaac.util.schedulers.SchedulerProviderImpl

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
@Module
class ApplicationModule {
    @Provides
    @PerApplication
    fun provideApplicationContext(application: Application): Context = application.applicationContext

    @Provides
    @PerApplication
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()
}