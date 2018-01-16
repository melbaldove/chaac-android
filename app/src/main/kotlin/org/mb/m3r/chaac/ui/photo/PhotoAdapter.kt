package org.mb.m3r.chaac.ui.photo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chauthai.swipereveallayout.ViewBinderHelper
import kotlinx.android.synthetic.main.photo_item.view.*
import org.mb.m3r.chaac.GlideApp
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.data.Photo

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoAdapter(val photos: ArrayList<Pair<Photo, Float>>, val listener: Callback) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    interface Callback {
        fun onDeletePhoto(position: Int)

        fun onEditPhoto(position: Int)
    }

    private val viewBinderHelper = ViewBinderHelper()

    override fun getItemCount(): Int = photos.size

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onBindViewHolder(holder: PhotoHolder?, position: Int) {
        holder?.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PhotoAdapter.PhotoHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.photo_item, parent, false)
        return PhotoHolder(view)
    }

    inner class PhotoHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val swipeRevealLayout = view.swipeRevealDelete!!
        private val imageView: ImageView = view.photoView
        private val caption: TextView = view.imageCaption
        private val remarks: TextView = view.imageRemarks
        private val deleteButton = view.delete_item
        private val editButton = view.edit_item
        private val progressBar = view.progressBarPhoto
        private val statusImg = view.statusImg

        fun bind(position: Int) {
            val photo = photos[position].first
            viewBinderHelper.bind(swipeRevealLayout, photo.checksum)

            editButton.setOnClickListener { _ ->
                listener.onEditPhoto(this.adapterPosition)
            }
            deleteButton.setOnClickListener { _ ->
                listener.onDeletePhoto(this.adapterPosition)
            }

            GlideApp.with(view.context)
                    .load(photo.path)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            caption.text = if (photo.caption.isEmpty()) "No Caption" else photo.caption
            remarks.text =
                    if (photo.remarks.isEmpty()) "This is a cool photo. Rock on MB!"
                    else photo.remarks
            val progressUpdate = photos[position].second.toInt()
            if (progressUpdate > 0) {
                progressBar.progress = photos[position].second.toInt()
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE

            }
            when (photo.status) {
                "UNSYNCED",
                "NEW" -> {
                    statusImg.setImageResource(R.drawable.cloud_sync)
                }
                else -> {
                    statusImg.setImageResource(R.drawable.cloud_check)
                }
            }

        }

    }

    fun addPhoto(photo: Photo): Photo {
        photos.add(Pair(photo, 0f))
        notifyItemInserted(photos.size - 1)
        return photo
    }

    fun removePhoto(photo: Photo): Photo {
        val position = photos.indexOfFirst { pair -> pair.first == photo }
        photos.removeAt(position)
        notifyItemRemoved(position)
        return photo
    }

    fun updatePhoto(newPhoto: Photo) {
        val position = photos.indexOfFirst { pair -> pair.first.checksum == newPhoto.checksum }
        photos[position] = Pair(newPhoto, photos[position].second)
        notifyItemChanged(position, photos[position])
    }

    fun updatePhotoUploadProgress(uploadProgress: Pair<Photo, Float>) {
        val position = photos.indexOfFirst { pair -> pair.first.checksum == uploadProgress.first.checksum }
        if (position != -1) {
            photos[position] = uploadProgress
            notifyItemChanged(position, uploadProgress)
        }
    }

    fun updateStatus(photo: Photo) {
        updatePhoto(photo)
    }

    fun getPhoto(position: Int): Photo = photos.elementAt(position).first

    val size: Int
        get() = photos.size
}