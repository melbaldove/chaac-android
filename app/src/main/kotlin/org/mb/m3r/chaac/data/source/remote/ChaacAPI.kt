package org.mb.m3r.chaac.data.source.remote

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.ui.signin.Token
import org.mb.m3r.chaac.ui.signin.UserPasswordCredential
import retrofit2.http.*

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
                    @Path("id") id: String,
                    @Body requestBody: MultipartBody): Single<Response<Photo>>

    @POST("sessions")
    fun authenticateCredentials(@Body user: HashMap<String, UserPasswordCredential>): Single<Response<Token>>

    @PUT("users/{id}/photos/{photoId}")
    fun updatePhoto(@Header("Authorization") token: String,
                    @Path("id") id: String,
                    @Path("photoId") photoId: String,
                    @Body photo: HashMap<String, Photo>): Single<Response<Photo>>

    @DELETE("users/{id}/photos/{photoId}")
    fun deletePhoto(@Header("Authorization") token: String,
                    @Path("id") id: String,
                    @Path("photoId") photoId: String) : Completable
}