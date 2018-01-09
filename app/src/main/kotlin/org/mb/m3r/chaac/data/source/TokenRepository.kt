package org.mb.m3r.chaac.data.source

import io.reactivex.Single

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface TokenRepository {
    fun getToken(): Single<String>
    fun saveToken(token: String): Single<String>
}