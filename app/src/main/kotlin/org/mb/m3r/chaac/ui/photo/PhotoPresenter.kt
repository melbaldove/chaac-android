package org.mb.m3r.chaac.ui.photo

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PhotoPresenter
@Inject constructor(val view : PhotoContract.View) : PhotoContract.Presenter {

    lateinit var subscriptions : CompositeDisposable

    override fun subscribe() {
        loadPictures()
    }

    override fun unsubscribe() {
        subscriptions.dispose()
    }

    override fun onCameraClick() {
        view.takePicture()
    }

    override fun savePicture() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadPictures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}