package org.mb.m3r.chaac.ui.photo

import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.PhotoRepository
import org.mb.m3r.chaac.di.scopes.PerActivity

/**
 * @author Melby Baldove
 */
@Module
class PhotoModule {
    @PerActivity
    @Provides
    fun providePhotoActivity(): PhotoContract.View = PhotoActivity()

    @PerActivity
    @Provides
    fun providePhotoPresenter(repository: PhotoRepository,
                              view: PhotoContract.View): PhotoContract.Presenter = PhotoPresenter(view, repository)

}
