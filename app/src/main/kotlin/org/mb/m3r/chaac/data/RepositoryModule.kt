package org.mb.m3r.chaac.data

import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.di.scopes.PerApplication

/**
 * @author Melby Baldove
 */
@Module
class RepositoryModule {

    @PerApplication
    @Provides
    fun providesLocalPhotoRepository(): PhotoRepository = LocalPhotoRepository()

}