package org.mb.m3r.chaac.ui.photo

import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.ui.base.BasePresenter
import org.mb.m3r.chaac.ui.base.BaseView

/**
 * @author Melby Baldove
 */
interface PhotoContract {
    interface View : BaseView {

        fun showPhotos(photos: List<Photo>)

        fun addToPhotos(photo: Photo)

        fun removeFromPhotos(photo: Photo)

        fun updatePhoto(photo: Photo)

        fun showTakePhoto()

        fun showConfirmDeletePhoto(photo: Photo)

        fun showEditPhotoDetail(photo: Photo)

    }

    interface Presenter : BasePresenter {
        fun takePhoto()

        fun savePhoto(path: String)

        fun onDeletePhoto(photo: Photo)

        fun deletePhoto(photo: Photo)

        fun onEditPhoto(photo: Photo)

        fun editPhoto(photo: Photo, caption: String, remarks: String)

        fun loadPhotos()

    }
}