package org.mb.m3r.chaac.ui.photo

import org.mb.m3r.chaac.ui.base.BasePresenter
import org.mb.m3r.chaac.ui.base.BaseView

/**
 * @author Melby Baldove
 */
interface PhotoContract {
    interface View : BaseView{
        fun takePicture()
    }

    interface Presenter : BasePresenter {
        fun onCameraClick()

        fun savePicture()

        fun loadPictures()
    }
}