package org.mb.m3r.chaac.ui.photo

import android.util.Log
import io.reactivex.Single
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepositoryMediator
import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.Dispatcher
import org.mb.m3r.chaac.flux.Store
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.DELETE_PHOTO
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.GET_PHOTOS
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.PHOTO_SYNCED
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.SAVE_PHOTO
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.SYNC_TO_SERVER
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.UPDATE_PHOTO
import org.mb.m3r.chaac.util.FileUtil
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoStore(private val photoRepo: PhotoRepositoryMediator) : Store() {
    override val supportedActions: Array<String>
        get() = arrayOf(GET_PHOTOS, SAVE_PHOTO, UPDATE_PHOTO, DELETE_PHOTO, SYNC_TO_SERVER, PHOTO_SYNCED)

    init {
        Dispatcher.register(this)
    }

    lateinit var photos: List<Photo>
        private set
    lateinit var photo: Photo
        private set

    override fun receiveAction(action: Action) {
        if (action.type in supportedActions) {
            when (action.type) {
                GET_PHOTOS -> loadPhotos(action)
                SAVE_PHOTO -> savePhoto(action)
                UPDATE_PHOTO -> updatePhoto(action)
                DELETE_PHOTO -> deletePhoto(action)
                SYNC_TO_SERVER -> syncToServer()
                PHOTO_SYNCED -> photoSynced(action)
            }
        }
    }

    private fun loadPhotos(action: Action) {
        photoRepo.getPhotos()
                .compose(SchedulerUtil.ioToUi())
                .toSortedList({ x, y -> x.createdDate.compareTo(y.createdDate) })
                .subscribe({ photoList ->
                    photos = photoList
                    notifyChange(action)
                }, { throwable ->
                    notifyError(action, throwable)
                })
    }

    private fun savePhoto(action: Action) {
        composePhoto(action.payload as String)
                .flatMap(photoRepo::createPhoto)
                .compose(SchedulerUtil.ioToUi())
                .subscribe({
                    photo = it
                    notifyChange(action)
                }, { throwable ->
                    Log.e("uploadError", throwable.message, throwable)
                    notifyError(action, throwable)

                })
    }

    private fun updatePhoto(action: Action) {
        (action.payload as Photo).let { changedPhoto ->
            photoRepo.getPhoto(changedPhoto.checksum)
                    .map {
                        it.copy(caption = changedPhoto.caption, remarks = changedPhoto.remarks)
                    }
                    .flatMap(photoRepo::updatePhoto)
                    .compose(SchedulerUtil.ioToUi())
                    .subscribe({
                        photo = it
                        notifyChange(action)
                    }, { throwable ->
                        // TODO: Handle errors
                        notifyError(action, throwable)

                    })
        }
    }

    private fun deletePhoto(action: Action) {
        (action.payload as Photo).let {
            FileUtil.deleteFile(it.path)
            photoRepo.deletePhoto(it).subscribe({}, {
                Log.e("deleteError", it.message, it)
            })
            photo = it
        }
        notifyChange(action)
    }

    /**
     * @param [path] string - where the temporary file is located
     */
    private fun composePhoto(path: String): Single<Photo> {
        return FileUtil.storeImage(path)
                .compose(SchedulerUtil.ioToUi())
                .flatMap({ file ->
                    FileUtil.checkSum(file).map({ checksum ->
                        Photo(checksum, file.path, null)
                    })
                })
    }

    private fun photoSynced(action: Action) {
        (action.payload as Photo).let {
            photo = it
            notifyChange(action)
        }
    }

    private fun syncToServer() {
        photoRepo.syncToServer()
    }
}



