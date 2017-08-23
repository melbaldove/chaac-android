package org.mb.m3r.chaac.ui.photo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract

import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.ui.base.BasePresenter
import org.mb.m3r.chaac.ui.base.BaseView
import javax.inject.Inject

class PhotoActivity : AppCompatActivity(), PhotoContract.View {

    @Inject
    lateinit var presenter : PhotoContract.Presenter

    override fun takePicture() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
