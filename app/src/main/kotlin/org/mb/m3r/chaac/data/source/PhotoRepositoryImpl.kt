package org.mb.m3r.chaac.data.source

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.local.LocalPhotoRepository
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil

class PhotoRepositoryImpl(val local: LocalPhotoRepository, val remote: PhotoRepository) : PhotoRepositoryMediator {
    override fun getPhoto(key: String): Single<Photo> {
        return local.getPhoto(key)
    }

    override fun getPhotos(): Flowable<Photo> = local.getPhotos()

    override fun createPhoto(photo: Photo): Single<Photo> {
        remote.createPhoto(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { photoResponse ->
                    val updatedPhoto = photo.copy(id = photoResponse.id, status = "UPLOADED")
                    local.updatePhoto(updatedPhoto)
                }
                .subscribe({
                    PhotoActionCreator.photoUploaded(it)
                }, { error ->
                    Log.e("threadingError", error.message, error)
                    PhotoActionCreator.photoUploaded(error)
                })
        return local.createPhoto(photo.copy(status = "NEW"))
    }

    override fun updatePhoto(photo: Photo): Single<Photo> {
        return when (photo.status) {
            "NEW" -> {
                local.updatePhoto(photo)
            }
            else -> {
                remote.updatePhoto(photo)
                        .compose(SchedulerUtil.ioToUi())
                        .flatMap {
                            local.updatePhoto(photo.copy(status = "SYNCED"))
                        }
                        .subscribe({
                            PhotoActionCreator.photoSynced(it)
                        }, {
                            Log.d("error", it.message)
                        })
                local.updatePhoto(photo.copy(status = "UNSYNCED"))
            }
        }
    }

    override fun deletePhoto(photo: Photo) {
        local.deletePhoto(photo)
    }

    override fun syncToServer() {
        val photos = local.getPhotos()
                .filter { photo -> photo.status != "SYNCED" }
                .flatMap { photo ->
                    if (photo.status == "NEW") {
                        remote.createPhoto(photo)
                                .subscribeOn(Schedulers.newThread())
                                .flatMap { photoResponse ->
                                    val updatedPhoto = photo.copy(id = photoResponse.id, status = "UPLOADED")
                                    updatePhoto(updatedPhoto)
                                }.toFlowable()
                    } else {
                        remote.updatePhoto(photo)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .flatMap {
                                    local.updatePhoto(photo.copy(status = "SYNCED"))
                                }.toFlowable()
                    }
                }
                .subscribe({
                    if (it.status == "SYNCED") {
                        PhotoActionCreator.photoSynced(it)
                    } else {
                        PhotoActionCreator.photoUploaded(it)
                    }
                }, {

                })
    }

}