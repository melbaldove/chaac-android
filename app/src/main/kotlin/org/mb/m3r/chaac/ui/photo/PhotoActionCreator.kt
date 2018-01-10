package org.mb.m3r.chaac.ui.photo

import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.Dispatcher

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
object PhotoActionCreator {
    const val DELETE_PHOTO = "DELETE PHOTO"
    const val SAVE_PHOTO = "SAVE_PHOTO"
    const val UPDATE_PHOTO = "UPDATE_PHOTO"
    const val GET_PHOTOS = "GET_PHOTOS"
    const val UPDATE_PHOTO_UPLOAD_PROGRESS = "UPDATE_PHOTO_UPLOAD_PROGRESS"
    const val PHOTO_UPLOADED = "PHOTO_UPLOADED"

    fun deletePhoto(photo: Photo) {
        Dispatcher.dispatch(Action.create(DELETE_PHOTO, photo))
    }

    fun loadPhotos() {
        Dispatcher.dispatch(Action.create(GET_PHOTOS, null))
    }

    fun savePhoto(path: String) {
        Dispatcher.dispatch(Action.create(SAVE_PHOTO, path))
    }

    fun updatePhoto(photo: Photo) {
        Dispatcher.dispatch(Action.create(UPDATE_PHOTO, photo))
    }

    fun updateUploadProgress(uploadProgress: Pair<Photo, Float>) {
        Dispatcher.dispatch(Action.create(UPDATE_PHOTO_UPLOAD_PROGRESS, uploadProgress))
    }

    fun photoUploaded(photo: Photo) {

    }

    fun photoUploaded(throwable: Throwable) {
        Dispatcher.dispatch(Action.create(PHOTO_UPLOADED, throwable))
    }

}