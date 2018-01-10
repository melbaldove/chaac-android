package org.mb.m3r.chaac.data.source

import io.reactivex.Flowable
import io.reactivex.Single
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.local.LocalPhotoRepository
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil

class PhotoRepositoryImpl(val local: LocalPhotoRepository, val remote: PhotoRepository) : PhotoRepository {
    override fun getPhotos(): Flowable<Photo> = local.getPhotos()

    override fun createPhoto(photo: Photo): Single<Photo> {
        remote.createPhoto(photo)
                .compose(SchedulerUtil.ioToUi())
                .subscribe({
                    local.updatePhotoStatus("UPLOADED", photo).subscribe()
                    PhotoActionCreator.photoUploaded(it)
                }, {
                    PhotoActionCreator.photoUploaded(it)
                })
        return local.createPhoto(photo)
    }

    override fun updatePhoto(photo: Photo): Single<Photo> {
        return local.updatePhoto(photo)
    }

    override fun deletePhoto(photo: Photo) {
        local.deletePhoto(photo)
    }
}