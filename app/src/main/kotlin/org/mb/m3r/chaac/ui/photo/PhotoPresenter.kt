package org.mb.m3r.chaac.ui.photo

import io.reactivex.disposables.CompositeDisposable
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.util.ChaacUtil
import org.mb.m3r.chaac.util.schedulers.SchedulerProvider
import javax.inject.Inject

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoPresenter
@Inject
constructor(val view: PhotoContract.View, val repo: PhotoRepository, val schedulerProvider: SchedulerProvider) : PhotoContract.Presenter {

    val subscriptions: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
        loadPhotos()
    }

    override fun unsubscribe() {
        subscriptions.dispose()
    }

    override fun photoTaken() {
        TODO("implement this")
    }

    /**
     * @param {String} path - path where temporary image was stored
     */
    override fun savePhotoFromTemp(path: String) {
        val photoFile = ChaacUtil.storeImage(path)
        ChaacUtil.checkSum(photoFile).observeOn(schedulerProvider.ui()).subscribe({ checksum ->
            // TODO: implement caption handling
            Photo(checksum = checksum, path = photoFile.path, caption = null).let {
                repo.savePhoto(it)
                view.addPhoto(it)
            }
        }) { throwable ->
            // TODO: Handle errors
        }.let { subscriptions.add(it) }
    }

    override fun loadPhotos() {
        repo.getPhotos().subscribeOn(schedulerProvider.io())
                .toList()
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoList ->
                    view.addPhotos(photoList)
                }).let { subscriptions.add(it) }
    }
}