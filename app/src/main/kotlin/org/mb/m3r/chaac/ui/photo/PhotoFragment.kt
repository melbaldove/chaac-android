package org.mb.m3r.chaac.ui.photo

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import butterknife.OnClick
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.new_photo.*
import kotlinx.android.synthetic.main.photo_frag.*
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.ui.SnappingLinearLayoutManager
import org.mb.m3r.chaac.ui.base.BaseActivity
import org.mb.m3r.chaac.ui.base.BaseFragment
import org.mb.m3r.chaac.ui.photo.PhotoContract.View.Companion.ADD_PHOTO
import org.mb.m3r.chaac.ui.photo.PhotoContract.View.Companion.EDIT_PHOTO
import org.mb.m3r.chaac.util.ActivityUtil
import org.mb.m3r.chaac.util.FileUtil
import javax.inject.Inject


/**
 * @author Melby Baldove
 */
class PhotoFragment : BaseFragment(), PhotoContract.View, PhotoAdapter.Callback {

    override val layoutRes: Int = R.layout.photo_frag

    @Inject
    lateinit var presenter: PhotoContract.Presenter

    lateinit var photoAdapter: PhotoAdapter

    private var imageTempPath: String? = null

    private val REQUEST_IMAGE_CAPTURE = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setActionBarTitle(getString(R.string.chaac))
        fragmentComponent.inject(this)
        // show items in reverse(descending) order
        photo_recycler_view.layoutManager = SnappingLinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, true).apply { stackFromEnd = true }
        presenter.subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun showPhotos(photos: List<Photo>) {
        photoAdapter = PhotoAdapter(photos as ArrayList<Photo>, this)
        photo_recycler_view.adapter = photoAdapter
    }

    override fun addToPhotos(photo: Photo) {
        photoAdapter.addPhoto(photo)
        photo_recycler_view.smoothScrollToPosition(photoAdapter.size - 1)
    }

    @OnClick(R.id.btnCamera)
    fun cameraOnClick() {
        presenter.takePhoto()
    }

    override fun showTakePhoto() {
        if (!ActivityUtil.hasPermission(context, WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    ActivityUtil.PERMISSION_REQUEST_WRITE_EXTERNAL)
            return
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            FileUtil.createTempImageFile().let {
                imageTempPath = it.absolutePath
                putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
        }
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ActivityUtil.PERMISSION_REQUEST_WRITE_EXTERNAL,
            ActivityUtil.PERMISSION_REQUEST_CAMERA -> presenter.takePhoto()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> presenter.photoTaken()
                else -> imageTempPath?.let { FileUtil.deleteFile(it) }
            }
        }
    }

    override fun showAddEditPhotoDetail(action: Int, caption: String?, remarks: String?) {
        MaterialDialog.Builder(context)
                .title("Describe your image :)")
                .positiveText("Done")
                .negativeText("Cancel")
                .negativeColorRes(R.color.material_color_grey_primary)
                .customView(R.layout.new_photo, true)
                .cancelable(false)
                .apply {
                    when (action) {
                        ADD_PHOTO -> onAny(this@PhotoFragment::onAddDialogButtonClick)
                        EDIT_PHOTO -> onAny(this@PhotoFragment::onEditDialogButtonClick)
                    }

                }.show().apply {
            caption_edit.text = SpannableStringBuilder(caption)
            remarks_edit.text = SpannableStringBuilder(remarks)
        }

    }

    override fun showConfirmDeletePhoto(photo: Photo) {
        MaterialDialog.Builder(context)
                .title("Are you sure you want to delete this?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .negativeColorRes(R.color.material_color_grey_primary)
                .positiveColorRes(R.color.material_color_red_700)
                .onPositive({ _, _ -> presenter.deletePhoto(photo) })
                .show()
    }

    override fun removeFromPhotos(photo: Photo) {
        photoAdapter.removePhoto(photo)
    }

    private fun onAddDialogButtonClick(dialog: MaterialDialog, which: DialogAction) {
        when (which) {
            DialogAction.POSITIVE ->
                presenter.savePhoto(imageTempPath!!,
                        dialog.caption_edit.text.toString(), dialog.remarks_edit.text.toString())
        // when user didn't input image details
            else ->
                presenter.savePhoto(imageTempPath!!, null, null)

        }
    }

    private fun onEditDialogButtonClick(dialog: MaterialDialog, which: DialogAction) {
        if (which == DialogAction.POSITIVE)
            TODO("do shit")
    }

    override fun onDeletePhoto(position: Int) {
        val photo = photoAdapter.getPhoto(position)
        presenter.onDeletePhoto(photo)
    }

    override fun onEditPhoto(position: Int) {
        val photo = photoAdapter.photos[position]
        showAddEditPhotoDetail(EDIT_PHOTO, photo.caption, photo.remarks)
    }
}