package org.mb.m3r.chaac.data.source.remote

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.MultipartBody
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.data.source.TokenRepository
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil
import java.io.File

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class RemotePhotoDataSource(private val api: ChaacAPI, private val tokenRepository: TokenRepository) : PhotoRepository {
    override fun getPhoto(key: String): Single<Photo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createPhoto(photo: Photo): Single<Photo> {
        val file = File(photo.path)
        val filePart = ProgressRequestBody(file).apply {
            getProgressSubject()
                    .compose(SchedulerUtil.ioToUi())
                    .subscribe({ progressPercent ->
                        PhotoActionCreator.updateUploadProgress(Pair(photo, progressPercent))
                    }, {
                        // TODO: HANDLE ERRORS
                    })
        }
        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", file.name, filePart)
                .build()

        return tokenRepository.getToken()
                .flatMap { token ->
                    api.uploadPhoto(token.token, token.userId, requestBody)
                }
                .map { response ->
                    response.data
                }
    }

    override fun updatePhoto(photo: Photo): Single<Photo> {
        val map = HashMap<String, Photo>()
        map.put("photo", photo)
        return tokenRepository.getToken()
                .flatMap { token ->
                    api.updatePhoto(token.token, token.userId, photo.id.toString(), map)
                }
                .map { response ->
                    response.data
                }
    }

    override fun getPhotos(): Flowable<Photo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePhoto(photo: Photo): Completable {
            return tokenRepository.getToken()
                    .flatMapCompletable { token ->
                        api.deletePhoto(token.token, token.userId, photo.id.toString())
                    }
    }
}