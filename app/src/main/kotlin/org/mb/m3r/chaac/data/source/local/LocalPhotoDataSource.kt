package org.mb.m3r.chaac.data.source.local

import io.reactivex.Flowable
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class LocalPhotoDataSource(private val db: Database) : PhotoRepository {
    override fun savePhoto(photo: Photo) {
        db.store().insert(photo).subscribe()
    }

    override fun getPhotos(): Flowable<Photo> = db.store().select(Photo::class).get().flowable()
}