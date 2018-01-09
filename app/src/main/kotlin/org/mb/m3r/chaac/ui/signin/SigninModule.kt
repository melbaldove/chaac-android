package org.mb.m3r.chaac.ui.signin

import dagger.Module
import dagger.Provides
import org.mb.m3r.chaac.data.source.TokenRepository
import org.mb.m3r.chaac.data.source.remote.ChaacAPI
import org.mb.m3r.chaac.di.scopes.PerApplication

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
@Module
class SigninModule {
    @PerApplication
    @Provides
    fun providesSigninStore(tokenRepository: TokenRepository, chaacAPI: ChaacAPI): SigninStore {
        return SigninStore(tokenRepository, chaacAPI)
    }
}