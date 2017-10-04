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

        fun showTakePhoto()

        fun showConfirmDeletePhoto(photo: Photo)

        fun showAddEditPhotoDetail(action: Int, caption: String?, remarks: String?)

        companion object {
            val EDIT_PHOTO = 1
            val ADD_PHOTO = 0
        }
    }

    interface Presenter : BasePresenter {
        fun takePhoto()

        fun savePhoto(path: String, caption: String?, remarks: String?)

        fun onDeletePhoto(photo: Photo)

        fun deletePhoto(photo: Photo)

        fun loadPhotos()

        fun photoTaken()
    }
}