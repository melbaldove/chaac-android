package org.mb.m3r.chaac.data.source

import io.reactivex.Single
import org.mb.m3r.chaac.ui.signin.Token

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface TokenRepository {
    fun getToken(): Single<Token>
    fun saveToken(token: Token): Single<Token>
}