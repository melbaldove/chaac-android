package org.mb.m3r.chaac.ui.photo

import io.reactivex.Single
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.Dispatcher
import org.mb.m3r.chaac.flux.Store
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.DELETE_PHOTO
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.GET_PHOTOS
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.SAVE_PHOTO
import org.mb.m3r.chaac.ui.photo.PhotoActionCreator.UPDATE_PHOTO
import org.mb.m3r.chaac.util.FileUtil
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoStore(private val photoRepo: PhotoRepository) : Store() {
    init {
        Dispatcher.register(this)
    }

    lateinit var photos: List<Photo>
        private set
    lateinit var photo: Photo
        private set

    override fun receiveAction(action: Action) {
        this.action = action
        when (action.type) {
            GET_PHOTOS -> loadPhotos()
            SAVE_PHOTO -> savePhoto()
            UPDATE_PHOTO -> updatePhoto()
            DELETE_PHOTO -> deletePhoto()
        }
    }

    private fun loadPhotos() {
        photoRepo.getPhotos()
                .compose(SchedulerUtil.ioToUi())
                .toSortedList({ x, y -> x.createdDate.compareTo(y.createdDate) })
                .subscribe({ photoList ->
                    photos = photoList
                    notifyChange()
                }, { throwable ->
                    // TODO: Handle errors
                })
    }

    private fun savePhoto() {
        composePhoto(action.payload as String)
                .subscribe({ photo ->
                    photoRepo.savePhoto(photo)
                    this@PhotoStore.photo = photo
                    notifyChange()
                }, { throwable ->
                    // TODO: Handle errors
                })
    }

    private fun updatePhoto() {
        (action.payload as Photo).let {
            photoRepo.savePhoto(it)
            photo = it
        }
        notifyChange()
    }

    private fun deletePhoto() {
        (action.payload as Photo).let {
            FileUtil.deleteFile(it.path)
            photoRepo.deletePhoto(it)
            photo = it
        }
        notifyChange()
    }

    /**
     * @param [path] string - where the temporary file is located
     */
    private fun composePhoto(path: String): Single<Photo> {
        return FileUtil.storeImage(path)
                .compose(SchedulerUtil.ioToUi())
                .flatMap({ file ->
                    FileUtil.checkSum(file).map({ checksum ->
                        Photo(checksum, file.path)
                    })
                })
    }
}



