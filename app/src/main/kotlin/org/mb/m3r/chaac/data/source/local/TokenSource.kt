package org.mb.m3r.chaac.data.source.local

import android.content.SharedPreferences
import io.reactivex.Single
import org.mb.m3r.chaac.data.source.TokenRepository

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class TokenSource(val sharedPreferences: SharedPreferences) : TokenRepository {
    override fun getToken(): Single<String> {
        return Single.create { subscriber ->
            val token = sharedPreferences.getString("token", "NO_TOKEN")
            if(token == "NO_TOKEN") {
                subscriber.onError(Throwable("No Token"))
            }
            else {
                subscriber.onSuccess(token)
            }
        }
    }

    override fun saveToken(token: String): Single<String> {
        return Single.create { subscriber ->
            sharedPreferences.edit().apply {
                putString("token", token)
                if (commit()) {
                    subscriber.onSuccess(token)
                } else {
                    subscriber.onError(Throwable("Something went wrong"))
                }
            }
        }
    }
}