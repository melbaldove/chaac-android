package org.mb.m3r.chaac.data.source.local

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import org.mb.m3r.chaac.data.exceptions.NoTokenException
import org.mb.m3r.chaac.data.source.TokenRepository
import org.mb.m3r.chaac.ui.signin.Token

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class TokenSource(val sharedPreferences: SharedPreferences) : TokenRepository {
    val gson = Gson()

    override fun getToken(): Single<Token> {
        return Single.create { subscriber ->
            val json = sharedPreferences.getString("token", "NO_TOKEN")
            if(json == "NO_TOKEN") {
                subscriber.onError(NoTokenException())
            }
            else {
                val token = gson.fromJson(json, Token::class.java)
                subscriber.onSuccess(token)
            }
        }
    }

    override fun saveToken(token: Token): Single<Token> {
        return Single.create { subscriber ->
            sharedPreferences.edit().apply {
                val json = gson.toJson(token)
                putString("token", json)
                if (commit()) {
                    subscriber.onSuccess(token)
                } else {
                    subscriber.onError(Throwable("Something went wrong"))
                }
            }
        }
    }

    override fun deleteToken(token: Token): Completable {
        return Completable.create { subscriber ->
            sharedPreferences.edit().remove("token").apply()
            subscriber.onComplete()
        }
    }

}