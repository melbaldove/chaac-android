package org.mb.m3r.chaac.ui.photo

import io.reactivex.disposables.CompositeDisposable
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.util.ChaacUtil
import javax.inject.Inject

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoPresenter
@Inject
constructor(val view: PhotoContract.View, val repo: PhotoRepository) : PhotoContract.Presenter {

    lateinit var subscriptions: CompositeDisposable

    override fun subscribe() {
        loadPictures()
    }

    override fun unsubscribe() {
        subscriptions.dispose()
    }

    override fun pictureTaken() {
        TODO("implement this")
    }

    override fun savePictureFromTemp(path: String) {
        val image = ChaacUtil.saveImageFromTempFile(path)
    }

    override fun loadPictures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}