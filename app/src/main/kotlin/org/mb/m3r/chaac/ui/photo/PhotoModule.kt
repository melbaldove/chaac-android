package org.mb.m3r.chaac.ui.photo

import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.PhotoRepository
import org.mb.m3r.chaac.di.scopes.PerActivity
import org.mb.m3r.chaac.di.scopes.PerFragment

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
        @PerFragment
        @Provides
        fun providePhotoPresenter(repository: PhotoRepository,
                                  view: PhotoContract.View): PhotoContract.Presenter = PhotoPresenter(view, repository)
    }
}
