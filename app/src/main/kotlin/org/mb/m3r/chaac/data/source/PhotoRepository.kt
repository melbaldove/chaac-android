package org.mb.m3r.chaac.data.source

import io.reactivex.Observable
import org.mb.m3r.chaac.data.Photo

/**
 * @author Melby Baldove
 */
interface PhotoRepository {
    fun savePhoto(photo: Photo)

    fun getPhotos(): Observable<Photo>
}