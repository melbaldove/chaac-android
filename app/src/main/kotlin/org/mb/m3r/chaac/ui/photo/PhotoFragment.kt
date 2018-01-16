package org.mb.m3r.chaac.ui.photo

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.util.Log
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.new_photo.*
import kotlinx.android.synthetic.main.photo_frag.*
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.remote.UploadStore
import org.mb.m3r.chaac.flux.AppError
import org.mb.m3r.chaac.ui.SnappingLinearLayoutManager
import org.mb.m3r.chaac.ui.base.BaseActivity
import org.mb.m3r.chaac.ui.base.BaseFragment
import org.mb.m3r.chaac.util.ActivityUtil
import org.mb.m3r.chaac.util.FileUtil
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil
import java.util.*
import javax.inject.Inject

/**
 * @author Melby Baldove
 */
class PhotoFragment : BaseFragment(), PhotoAdapter.Callback {

    companion object {
        const val TAG = "PHOTO_FRAGMENT"
    }

    override val layoutRes: Int = R.layout.photo_frag

    @Inject lateinit var photoStore: PhotoStore
    @Inject lateinit var uploadStore: UploadStore
    var test = 0f

    val subscriptions = CompositeDisposable()

    lateinit var photoAdapter: PhotoAdapter

    private var imageTempPath: String? = null

    private val REQUEST_IMAGE_CAPTURE = 20

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setActionBarTitle(getString(R.string.chaac))
        fragmentComponent.inject(this)
        // show items in reverse(descending) order
        photo_recycler_view.layoutManager = SnappingLinearLayoutManager(context!!,
                LinearLayoutManager.VERTICAL, true).apply { stackFromEnd = true }
        subscribeToStores()
        PhotoActionCreator.syncToServer()
        PhotoActionCreator.loadPhotos()
    }

    private fun subscribeToStores() {
        photoStore.observable()
                .subscribe {
                    renderForPhotoStore()
                }.let { subscriptions.add(it) }
        uploadStore.observable()
                .subscribe {
                    renderForUploadStore()
                }.let { subscriptions.add(it) }
    }

    private fun renderForPhotoStore() {
        photoStore.action?.let { action ->
            when (action.type) {
                PhotoActionCreator.GET_PHOTOS -> {
                    if (!action.error) {
                        showPhotos(photoStore.photos)
                    } else {
                        // Handle errors
                    }
                }
                PhotoActionCreator.SAVE_PHOTO -> {
                    if (!action.error) {
                        photoStore.photo.let {
                            addToPhotos(it)
                            showEditPhotoDetail(it)
                        }
                    }
                }
                PhotoActionCreator.DELETE_PHOTO -> {
                    if (!action.error) {
                        removeFromPhotos(photoStore.photo)
                    }
                }
                PhotoActionCreator.UPDATE_PHOTO -> {
                    if (!action.error) {
                        updatePhoto(photoStore.photo)
                    }
                }
                PhotoActionCreator.PHOTO_SYNCED -> {
                    if (!action.error) {
                        setPhotoAsSynced(photoStore.photo)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun renderForUploadStore() {
        uploadStore.action?.let { action ->
            when (action.type) {
                PhotoActionCreator.UPDATE_PHOTO_UPLOAD_PROGRESS -> {
                    if (!action.error) {
                        Log.d("upload", (uploadStore.uploadProgress.first as Photo).checksum)
                        Log.d("upload", uploadStore.uploadProgress.second.toString())
                        photoAdapter.updatePhotoUploadProgress(uploadStore.uploadProgress as Pair<Photo, Float>)
                    } else {

                    }
                }
                PhotoActionCreator.PHOTO_UPLOADED -> {
                    if (!action.error) {

                    } else {
                        Log.d("uploaded", (action.payload as AppError).message)
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private fun showPhotos(photos: List<Photo>) {
        val uploadProgressPair = photos.map {
            // 0 for now refactor this later
            Pair(it, 0f)
        }
        photoAdapter = PhotoAdapter(uploadProgressPair as ArrayList<Pair<Photo, Float>>, this)
        photo_recycler_view.adapter = photoAdapter
    }

    private fun addToPhotos(photo: Photo) {
        photoAdapter.addPhoto(photo)
        photo_recycler_view.smoothScrollToPosition(photoAdapter.size - 1)
    }

    @OnClick(R.id.btnCamera)
    fun cameraOnClick() {
        showTakePhoto()
    }

    private fun showTakePhoto() {
        if (!ActivityUtil.hasPermission(context!!, WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    ActivityUtil.PERMISSION_REQUEST_WRITE_EXTERNAL)
            return
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            FileUtil.createTempImageFile().let {
                imageTempPath = it.absolutePath
                putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getUriForFile(context!!, it))
            }
        }
        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ActivityUtil.PERMISSION_REQUEST_WRITE_EXTERNAL,
            ActivityUtil.PERMISSION_REQUEST_CAMERA -> showTakePhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> PhotoActionCreator.savePhoto(imageTempPath!!)
                else -> imageTempPath?.let { FileUtil.deleteFile(it) }
            }
        }
    }

    private fun showEditPhotoDetail(photo: Photo) {
        MaterialDialog.Builder(context!!)
                .title("Describe your image :)")
                .positiveText("Done")
                .negativeText("Cancel")
                .negativeColorRes(R.color.material_color_grey_primary)
                .customView(R.layout.new_photo, true)
                .onPositive({ dialog, _ ->
                    PhotoActionCreator.updatePhoto(photo.apply {
                        caption = dialog.caption_edit.text.toString()
                        remarks = dialog.remarks_edit.text.toString()
                    })
                }).show().apply {
            caption_edit.text = SpannableStringBuilder(photo.caption)
            remarks_edit.text = SpannableStringBuilder(photo.remarks)
        }
    }

    private fun showConfirmDeletePhoto(photo: Photo) {
        MaterialDialog.Builder(context!!)
                .title("Are you sure you want to delete this?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .negativeColorRes(R.color.material_color_grey_primary)
                .positiveColorRes(R.color.material_color_red_700)
                .onPositive({ _, _ -> PhotoActionCreator.deletePhoto(photo) })
                .show()
    }

    private fun removeFromPhotos(photo: Photo) {
        photoAdapter.removePhoto(photo)
    }

    override fun onDeletePhoto(position: Int) {
        val photo = photoAdapter.getPhoto(position)
        showConfirmDeletePhoto(photo)
    }

    private fun updatePhoto(photo: Photo) {
        photoAdapter.updatePhoto(photo)
    }

    override fun onEditPhoto(position: Int) {
        photoAdapter.getPhoto(position).let {
            showEditPhotoDetail(it)
        }
    }

    private fun setPhotoAsSynced(photo: Photo) {
        photoAdapter.updateStatus(photo)
    }
}