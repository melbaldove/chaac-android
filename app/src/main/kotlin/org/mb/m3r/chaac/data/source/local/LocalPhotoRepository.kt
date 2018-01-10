package org.mb.m3r.chaac.data.source.local

import io.reactivex.Single
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface LocalPhotoRepository : PhotoRepository {
    fun updatePhotoStatus(status: String, photo: Photo): Single<Photo>
}