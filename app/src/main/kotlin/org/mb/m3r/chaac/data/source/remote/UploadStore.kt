package org.mb.m3r.chaac.data.source.remote

import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.AppError
import org.mb.m3r.chaac.flux.Dispatcher
import org.mb.m3r.chaac.flux.Store
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.PHOTO_UPLOADED
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.UPDATE_PHOTO_UPLOAD_PROGRESS

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class UploadStore : Store() {
    override val supportedActions: Array<String>
        get() = arrayOf(UPDATE_PHOTO_UPLOAD_PROGRESS, PHOTO_UPLOADED)

    init {
        Dispatcher.register(this)
    }

    lateinit var uploadProgress: Pair<Any, Float>
        private set
    lateinit var uploadedFile: Any

    override fun receiveAction(action: Action) {
        if(action.type in supportedActions) {
            this.action = action
            when (action.type) {
                UPDATE_PHOTO_UPLOAD_PROGRESS -> updateUploadProgress()
                PHOTO_UPLOADED -> photoUploaded()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateUploadProgress() {
        (action?.payload as Pair<Any, Float>).let {
            uploadProgress = it
            notifyChange()
        }
    }

    private fun photoUploaded() {
        if(action?.payload is Throwable) {
            notifyError(AppError(action?.payload as Throwable))
        } else {
            action?.payload?.let {
                uploadedFile = it
                notifyChange()
            }
        }
    }


}