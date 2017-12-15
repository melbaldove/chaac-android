package org.mb.m3r.chaac.ui.photo

import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.di.scopes.PerApplication

/**
 * @author Melby Baldove
 */

class PhotoModule {
    @Module
    class Store {
        @Provides
        @PerApplication
        fun providePhotoStore(repository: PhotoRepository) = PhotoStore(repository)
    }
}
