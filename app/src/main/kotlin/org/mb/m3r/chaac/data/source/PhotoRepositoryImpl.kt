package org.mb.m3r.chaac.data.source

import io.reactivex.Flowable
import org.mb.m3r.chaac.data.Photo

class PhotoRepositoryImpl(val local: PhotoRepository) : PhotoRepository {
    override fun getPhotos(): Flowable<Photo> = local.getPhotos()

    override fun savePhoto(photo: Photo) {
        local.savePhoto(photo)
    }
}