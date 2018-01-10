package org.mb.m3r.chaac.data.source.remote

import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.MultipartBody
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil
import java.io.File

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class RemotePhotoDataSource(private val api: ChaacAPI): PhotoRepository {
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

        return api.uploadPhoto("a327b492b4531450099ac16196161d54d0407a221edd76d7ab7d8e99a7705c6f", 3, requestBody)
    }

    override fun updatePhoto(photo: Photo): Single<Photo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhotos(): Flowable<Photo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePhoto(photo: Photo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}