package org.mb.m3r.chaac.data.source

import io.reactivex.Flowable
import org.mb.m3r.chaac.data.Photo

/**
 * @author Melby Baldove
 */
interface PhotoRepository {
    fun savePhoto(photo: Photo)

    fun getPhotos(): Flowable<Photo>

    fun deletePhoto(photo: Photo)
}