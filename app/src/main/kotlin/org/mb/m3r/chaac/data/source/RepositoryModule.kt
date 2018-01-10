package org.mb.m3r.chaac.data.source

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.source.local.Database
import org.mb.m3r.chaac.data.source.local.LocalPhotoDataSource
import org.mb.m3r.chaac.data.source.local.LocalPhotoRepository
import org.mb.m3r.chaac.data.source.local.RequeryDatabase
import org.mb.m3r.chaac.data.source.remote.ChaacAPI
import org.mb.m3r.chaac.data.source.remote.RemotePhotoDataSource
import org.mb.m3r.chaac.data.source.remote.UploadStore
import org.mb.m3r.chaac.di.qualifiers.Local
import org.mb.m3r.chaac.di.qualifiers.Remote
import org.mb.m3r.chaac.di.scopes.PerApplication

/**
 * @author Melby Baldove
 */
@Module
class RepositoryModule {
    private val DB_NAME: String = "chaac.db"
    private val DB_VERSION: Int = 1

    @Provides
    @PerApplication
    fun providesPhotoRepository(@Local localSource: LocalPhotoRepository,
                                @Remote remoteSource: PhotoRepository): PhotoRepository =
            PhotoRepositoryImpl(localSource, remoteSource)

    @Provides
    @PerApplication
    @Local
    fun providesLocalPhotoDataSource(db: Database): LocalPhotoRepository = LocalPhotoDataSource(db)

    @Provides
    @PerApplication
    @Remote
    fun providesRemotePhotoDataSource(api: ChaacAPI): PhotoRepository = RemotePhotoDataSource(api)

    @Provides
    @PerApplication
    fun providesDatabase(context: Context): Database =
            RequeryDatabase(context, DB_NAME, DB_VERSION)

    @Provides
    @PerApplication
    fun providesUploadStore() : UploadStore = UploadStore()
}