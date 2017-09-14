package org.mb.m3r.chaac.data.source

import io.reactivex.Observable
import org.mb.m3r.chaac.data.Photo

class PhotoRepositoryImpl(val local: PhotoRepository) : PhotoRepository {
    override fun getPhotos(): Observable<Photo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun savePhoto(photo: Photo) {
        local.savePhoto(photo)
    }
}