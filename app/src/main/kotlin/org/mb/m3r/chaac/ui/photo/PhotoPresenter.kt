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
        loadPictures()
    }

    override fun unsubscribe() {
        subscriptions.dispose()
    }

    override fun pictureTaken() {
        TODO("implement this")
    }

    /**
     * @param {String} path - path where temporary image was stored
     */
    override fun savePhotoFromTemp(path: String) {
        val photoFile = ChaacUtil.storeImage(path)
        ChaacUtil.checkSum(photoFile).subscribe({ checksum ->
            // TODO: implement caption handling
            val photo = Photo(checksum = checksum, path = path, caption = null)
            repo.savePhoto(photo)
        }) { throwable ->
            // TODO: Handle errors
        }.let { subscriptions.add(it) }
    }

    override fun loadPictures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}