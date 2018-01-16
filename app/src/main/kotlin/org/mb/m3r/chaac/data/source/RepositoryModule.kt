package org.mb.m3r.chaac.data.source

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.source.local.Database
import org.mb.m3r.chaac.data.source.local.LocalPhotoDataSource
import org.mb.m3r.chaac.data.source.local.LocalPhotoRepository
import org.mb.m3r.chaac.data.source.local.RequeryDatabase
import org.mb.m3r.chaac.data.source.remote.ChaacAPI
import org.mb.m3r.chaac.data.source.remote.RemotePhotoDataSource
import org.mb.m3r.chaac.data.source.remote.UploadStore
import org.mb.m3r.chaac.data.source.local.TokenSource
import org.mb.m3r.chaac.di.qualifiers.Local
import org.mb.m3r.chaac.di.qualifiers.Remote
import org.mb.m3r.chaac.di.scopes.PerApplication
import javax.inject.Named

/**
 * @author Melby Baldove
 */
@Module
class RepositoryModule {
    private val DB_NAME: String = "chaac.db"
    private val DB_VERSION: Int = 1

    @Provides
    @PerApplication
    fun providesPhotoRepositoryMediator(@Local localSource: LocalPhotoRepository,
                                @Remote remoteSource: PhotoRepository): PhotoRepositoryMediator =
            PhotoRepositoryImpl(localSource, remoteSource)

    @Provides
    @PerApplication
    @Local
    fun providesLocalPhotoDataSource(db: Database): LocalPhotoRepository = LocalPhotoDataSource(db)

    @Provides
    @PerApplication
    @Remote
    fun providesRemotePhotoDataSource(api: ChaacAPI, tokenRepo: TokenRepository): PhotoRepository = RemotePhotoDataSource(api, tokenRepo)

    @Provides
    @PerApplication
    fun provideTokenRepository(@Named("token_sharedPref") sharedPreferences: SharedPreferences): TokenRepository {
        return TokenSource(sharedPreferences)
    }

    @Provides
    @PerApplication
    @Named("token_sharedPref")
    fun providesTokenSharedPref(context: Context) = context.getSharedPreferences("token_sharedPref", 0)

    @Provides
    @PerApplication
    fun providesDatabase(context: Context): Database =
            RequeryDatabase(context, DB_NAME, DB_VERSION)

    @Provides
    @PerApplication
    fun providesUploadStore() : UploadStore = UploadStore()
}