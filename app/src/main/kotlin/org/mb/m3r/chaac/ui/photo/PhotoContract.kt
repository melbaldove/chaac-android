package org.mb.m3r.chaac.ui.photo

import org.mb.m3r.chaac.ui.base.BasePresenter
import org.mb.m3r.chaac.ui.base.BaseView

/**
 * @author Melby Baldove
 */
interface PhotoContract {
    interface View : BaseView {
        fun addPictures()
    }

    interface Presenter : BasePresenter {
        fun pictureTaken()

        fun savePictureFromTemp(path: String)

        fun loadPictures()
    }
}