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

        fun showTakePhoto()

        fun showAddEditPhotoDetail()
    }

    interface Presenter : BasePresenter {
        fun takePhoto()

        fun savePhoto(path: String, caption: String?, remarks: String?)

        fun loadPhotos()

        fun photoTaken()
    }
}