package org.mb.m3r.chaac.data.source

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.mb.m3r.chaac.data.Photo

/**
 * @author Melby Baldove
 */
interface PhotoRepository {
    fun createPhoto(photo: Photo): Single<Photo>

    fun updatePhoto(photo: Photo): Single<Photo>

    fun getPhotos(): Flowable<Photo>

    fun deletePhoto(photo: Photo): Completable

    fun getPhoto(key: String): Single<Photo>
}