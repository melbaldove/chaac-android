package org.mb.m3r.chaac.ui.photo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import butterknife.OnClick
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.ui.base.BaseActivity
import org.mb.m3r.chaac.util.ChaacUtil
import javax.inject.Inject


class PhotoActivity : BaseActivity(), PhotoContract.View {
    override val layoutRes: Int = R.layout.activity_main

    @Inject
    lateinit var presenter: PhotoContract.Presenter

    private var imageTempPath: String? = null

    val REQUEST_IMAGE_CAPTURE = 20
    val REQUEST_SAVE_IMAGE = 21

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
    }

    override fun addPictures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @OnClick(R.id.btnCamera)
    fun cameraOnClick() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            ChaacUtil.createTempImageFile().let {
                imageTempPath = it.absolutePath
                val uri = Uri.fromFile(it)
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
        }

        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            presenter.savePictureFromTemp(imageTempPath!!)
        }
    }
}