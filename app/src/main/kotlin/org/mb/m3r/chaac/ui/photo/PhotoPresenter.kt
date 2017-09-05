package org.mb.m3r.chaac.ui.photo

import io.reactivex.disposables.CompositeDisposable
import org.mb.m3r.chaac.data.PhotoRepository
import org.mb.m3r.chaac.util.ChaacUtil
import javax.inject.Inject

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
        TODO("save imagePath to sqlite and info whether it is uploaded or not")

    }

    override fun loadPictures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}