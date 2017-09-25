package org.mb.m3r.chaac.ui.photo

import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.ui.base.BasePresenter
import org.mb.m3r.chaac.ui.base.BaseView

/**
 * @author Melby Baldove
 */
interface PhotoContract {
    interface View : BaseView {
        fun addPhotos(photos: List<Photo>)

        fun addPhoto(photo: Photo)
    }

    interface Presenter : BasePresenter {
        fun photoTaken()

        fun savePhotoFromTemp(path: String, caption: String?, remarks: String?)

        fun loadPhotos()
    }
}