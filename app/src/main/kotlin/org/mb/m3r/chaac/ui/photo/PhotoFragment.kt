package org.mb.m3r.chaac.ui.photo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.ui.base.BaseFragment
import org.mb.m3r.chaac.util.ChaacUtil
import javax.inject.Inject

/**
 * @author Melby Baldove
 */
class PhotoFragment : BaseFragment(), PhotoContract.View {
    override val layoutRes: Int = R.layout.photo_frag

    @Inject
    lateinit var presenter: PhotoContract.Presenter

    private var imageTempPath: String? = null

    val REQUEST_IMAGE_CAPTURE = 20
    val REQUEST_SAVE_IMAGE = 21

    override fun addPictures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @OnClick(R.id.btnCamera)
    fun cameraOnClick() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            ChaacUtil.createTempImageFile().let {
                imageTempPath = it.absolutePath
                putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
        }

        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            presenter.savePictureFromTemp(imageTempPath!!)
        }
    }
}