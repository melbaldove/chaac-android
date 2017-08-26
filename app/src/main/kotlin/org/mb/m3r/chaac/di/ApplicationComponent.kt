package org.mb.m3r.chaac.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.data.PhotoRepository
import org.mb.m3r.chaac.data.RepositoryModule
import org.mb.m3r.chaac.di.scopes.PerApplication


/**
 * @author Melby Baldove
 */
@PerApplication
@Component(modules = arrayOf(RepositoryModule::class))
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
