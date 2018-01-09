package org.mb.m3r.chaac.data.source.remote

import io.reactivex.Single
import okhttp3.MultipartBody
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.ui.signin.Token
import org.mb.m3r.chaac.ui.signin.UserPasswordCredential
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface ChaacAPI {
    companion object {
        val URL = "http://192.168.0.100:4000/api/"
    }

    @POST("users/{id}/photos")
    fun uploadPhoto(@Header("Authorization") token: String,
                    @Path("id") id: Int,
                    @Body requestBody: MultipartBody): Single<Photo>

    @POST("sessions")
    fun authenticateCredentials(@Body user: HashMap<String, UserPasswordCredential>): Single<Response<Token>>
}