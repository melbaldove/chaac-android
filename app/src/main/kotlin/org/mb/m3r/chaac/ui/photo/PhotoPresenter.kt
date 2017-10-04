package org.mb.m3r.chaac.ui.photo

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.util.FileUtil
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil
import javax.inject.Inject

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoPresenter
@Inject
constructor(val view: PhotoContract.View, val repo: PhotoRepository) : PhotoContract.Presenter {

    val subscriptions: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
        loadPhotos()
    }

    override fun unsubscribe() {
        subscriptions.dispose()
    }

    /**
     * @param {String} path - path where temporary image was stored
     */
    override fun savePhoto(path: String) {
        FileUtil.storeImage(path)
                .subscribeOn(Schedulers.io())
                .subscribe({ photoFile ->
                    FileUtil.checkSum(photoFile)
                            .compose(SchedulerUtil.ioToUi())
                            .subscribe({ checksum ->
                                Photo(checksum = checksum, path = photoFile.path, caption = "",
                                        remarks = "", createdDate = System.currentTimeMillis()).let {
                                    repo.savePhoto(it)
                                    view.addToPhotos(it)
                                    view.showEditPhotoDetail(it)
                                }
                            }, { throwable ->
                                // TODO: Handle errors
                            }).let { subscriptions.add(it) }
                }).let { subscriptions.add(it) }
    }

    override fun loadPhotos() {
        repo.getPhotos()
                .compose(SchedulerUtil.ioToUi())
                .toSortedList({ x, y -> x.createdDate.compareTo(y.createdDate) })
                .subscribe({ photoList ->
                    view.showPhotos(photoList)
                }, { throwable ->
                    // TODO: Handle errors
                }).let { subscriptions.add(it) }
    }

    override fun takePhoto() {
        view.showTakePhoto()
    }

    override fun onDeletePhoto(photo: Photo) {
        view.showConfirmDeletePhoto(photo)
    }

    override fun deletePhoto(photo: Photo) {
        photo.let {
            view.removeFromPhotos(it)
            FileUtil.deleteFile(it.path)
            repo.deletePhoto(it)
        }
    }

    override fun onEditPhoto(photo: Photo) {
        view.showEditPhotoDetail(photo)
    }

    override fun editPhoto(photo: Photo, caption: String, remarks: String) {
        photo.let {
            it.caption = caption
            it.remarks = remarks
            repo.savePhoto(it)
            view.updatePhoto(it)
        }
    }
}