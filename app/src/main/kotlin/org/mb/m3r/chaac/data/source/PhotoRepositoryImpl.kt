package org.mb.m3r.chaac.data.source

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.local.LocalPhotoRepository
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator
import org.mb.m3r.chaac.ui.signin.SessionActionCreator
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil
import retrofit2.HttpException

class PhotoRepositoryImpl(val local: LocalPhotoRepository, val remote: PhotoRepository) : PhotoRepositoryMediator {
    override fun getPhoto(key: String): Single<Photo> {
        return local.getPhoto(key)
    }

    override fun getPhotos(): Flowable<Photo> = local.getPhotos().filter({ it.status != "DELETING" })

    override fun createPhoto(photo: Photo): Single<Photo> {
        uploadPhoto(photo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    PhotoActionCreator.photoUploaded(it)
                }, { error ->
                    if (error is HttpException && error.code() == 401) {
                        SessionActionCreator.invalidToken()
                    } else {
                        PhotoActionCreator.photoUploaded(error)
                    }

                })
        return local.createPhoto(photo.copy(status = "NEW"))
    }

    override fun updatePhoto(photo: Photo): Single<Photo> {
        return when (photo.status) {
            "NEW" -> {
                local.updatePhoto(photo)
            }
            else -> {
                remoteUpdatePhoto(photo)
                        .compose(SchedulerUtil.ioToUi())
                        .subscribe({
                            PhotoActionCreator.photoSynced(it)
                        }, { error ->
                            if (error is HttpException && error.code() == 401) {
                                SessionActionCreator.invalidToken()
                            }
                        })
                local.updatePhoto(photo.copy(status = "UNSYNCED"))
            }
        }
    }

    override fun deletePhoto(photo: Photo): Completable {
        return if (photo.status == "NEW") {
            local.deletePhoto(photo)
        } else {
            local.updatePhoto(photo.copy(status = "DELETING"))
                    .flatMapCompletable(this::remoteDeletePhoto)
        }
    }

    override fun syncToServer() {
        local.getPhotos()
                .filter { photo -> photo.status != "SYNCED" }
                .flatMap { photo ->
                    when (photo.status) {
                        "NEW" -> {
                            uploadPhoto(photo).toFlowable()
                        }
                        "DELETING" -> {
                            remoteDeletePhoto(photo).toFlowable()
                        }
                        else -> {
                            remoteUpdatePhoto(photo).toFlowable()
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == "SYNCED") {
                        PhotoActionCreator.photoSynced(it)
                    } else {
                        PhotoActionCreator.photoUploaded(it)
                    }
                }, { error ->
                    if (error is HttpException && error.code() == 401) {
                        SessionActionCreator.invalidToken()
                    } else {
                        PhotoActionCreator.photoUploaded(error)
                    }

                })
    }

    private fun uploadPhoto(photo: Photo): Single<Photo> {
        return remote.createPhoto(photo)
                .subscribeOn(Schedulers.newThread())
                .flatMap { photoResponse ->
                    val updatedPhoto = photo.copy(id = photoResponse.id, status = "UPLOADED")
                    local.updatePhoto(updatedPhoto)
                }
    }

    private fun remoteDeletePhoto(photo: Photo): Completable {
        return remote.deletePhoto(photo)
                .concatWith(local.deletePhoto(photo))
    }

    private fun remoteUpdatePhoto(photo: Photo): Single<Photo> {
        return remote.updatePhoto(photo)
                .subscribeOn(Schedulers.newThread())
                .flatMap {
                    local.updatePhoto(photo.copy(status = "SYNCED"))
                }
    }
}