package org.mb.m3r.chaac.ui.photo

import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.di.scopes.PerActivity
import org.mb.m3r.chaac.di.scopes.PerFragment
import org.mb.m3r.chaac.util.schedulers.SchedulerProvider

/**
 * @author Melby Baldove
 */

class PhotoModule {
    @Module
    class Activity {
        @PerActivity
        @Provides
        fun providePhotoView(fragment: PhotoFragment): PhotoContract.View = fragment

        @PerActivity
        @Provides
        fun providePhotoFragment() = PhotoFragment()
    }

    @Module
    class Fragment {
        @Provides
        @PerFragment
        fun providePhotoPresenter(
                view: PhotoContract.View,
                repository: PhotoRepository,
                schedulerProvider: SchedulerProvider): PhotoContract.Presenter =
                PhotoPresenter(view, repository, schedulerProvider)
    }
}
