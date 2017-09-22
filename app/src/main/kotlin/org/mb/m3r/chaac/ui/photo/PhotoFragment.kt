package org.mb.m3r.chaac.ui.photo

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.photo_frag.*
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.ui.base.BaseActivity
import org.mb.m3r.chaac.ui.base.BaseFragment
import org.mb.m3r.chaac.util.ActivityUtil
import org.mb.m3r.chaac.util.ChaacUtil
import javax.inject.Inject

/**
 * @author Melby Baldove
 */
class PhotoFragment : BaseFragment(), PhotoContract.View {

    override val layoutRes: Int = R.layout.photo_frag

    @Inject
    lateinit var presenter: PhotoContract.Presenter

    lateinit var photoAdapter: PhotoAdapter

    private var imageTempPath: String? = null

    val REQUEST_IMAGE_CAPTURE = 20
    val REQUEST_SAVE_IMAGE = 21

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setActionBarTitle(getString(R.string.chaac))
        fragmentComponent.inject(this)
        photo_recycler_view.layoutManager = LinearLayoutManager(context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        presenter.subscribe()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            super.onCreateView(inflater, container, savedInstanceState)

    override fun addPhotos(photos: List<Photo>) {
        photoAdapter = PhotoAdapter(photos as ArrayList<Photo>)
        photo_recycler_view.adapter = photoAdapter

    }

    override fun addPhoto(photo: Photo) {
        photoAdapter.addPhoto(photo)
        // scrolls to first item since items are in descending order
        photo_recycler_view.scrollToPosition(photoAdapter.size - 1)
    }

    @OnClick(R.id.btnCamera)
    fun cameraOnClick() {
        takePicture()
    }

    private fun takePicture() {
        if (!ActivityUtil.hasPermission(context, WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), ActivityUtil.PERMISSION_REQUEST_WRITE_EXTERNAL)
            return
        }
        if (!ActivityUtil.hasPermission(context, CAMERA)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), ActivityUtil.PERMISSION_REQUEST_CAMERA)
            return
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            ChaacUtil.createTempImageFile().let {
                imageTempPath = it.absolutePath
                putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
        }
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ActivityUtil.PERMISSION_REQUEST_WRITE_EXTERNAL,
            ActivityUtil.PERMISSION_REQUEST_CAMERA -> takePicture()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            presenter.savePhotoFromTemp(imageTempPath!!)
        }
    }
}