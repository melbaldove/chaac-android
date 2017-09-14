package org.mb.m3r.chaac.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.data.source.RepositoryModule
import org.mb.m3r.chaac.di.scopes.PerApplication


/**
 * @author Melby Baldove
 */
@PerApplication
@Component(modules = arrayOf(RepositoryModule::class, ApplicationModule::class))
interface ApplicationComponent {
    fun inject(app: ChaacApplication)

    fun photoRepository(): PhotoRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}
