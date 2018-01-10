package org.mb.m3r.chaac.data.source.local

import io.reactivex.Flowable
import io.reactivex.Single
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class LocalPhotoDataSource(private val db: Database) : LocalPhotoRepository {
    override fun updatePhotoStatus(status: String, photo: Photo): Single<Photo> {
        val updatedPhoto = photo.copy(status = status)
        return updatePhoto(updatedPhoto)
    }

    override fun createPhoto(photo: Photo): Single<Photo> {
        return db.store().upsert(photo)
    }

    override fun updatePhoto(photo: Photo): Single<Photo> {
        return createPhoto(photo)
    }

    override fun getPhotos(): Flowable<Photo> = db.store().select(Photo::class).get().flowable()

    override fun deletePhoto(photo: Photo) {
        db.store().delete(photo).subscribe()
    }

}
