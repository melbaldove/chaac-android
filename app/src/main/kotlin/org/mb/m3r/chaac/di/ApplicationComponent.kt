package org.mb.m3r.chaac.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.data.source.RepositoryModule
import org.mb.m3r.chaac.data.source.remote.UploadStore
import org.mb.m3r.chaac.di.scopes.PerApplication
import org.mb.m3r.chaac.ui.photo.PhotoModule
import org.mb.m3r.chaac.ui.photo.PhotoStore


/**
 * @author Melby Baldove
 */
@PerApplication
@Component(modules = [(RepositoryModule::class), (ApplicationModule::class), (PhotoModule.Store::class), (NetworkModule::class)])
interface ApplicationComponent {
    fun photoStore(): PhotoStore
    fun uploadStore(): UploadStore

    fun inject(app: ChaacApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}
