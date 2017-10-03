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
class PhotoAdapter(val photos: ArrayList<Photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun getItemCount(): Int = photos.size

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onBindViewHolder(holder: PhotoHolder?, position: Int) {
        photos.get(index = position).let {
            viewBinderHelper.bind(holder?.swipeRevealLayout, it.checksum)
            holder?.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PhotoAdapter.PhotoHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.photo_item, parent, false)
        return PhotoHolder(view)
    }

    class PhotoHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val swipeRevealLayout = view.swipeRevealDelete!!
        private val imageView: ImageView = view.photoView
        private val caption: TextView = view.imageCaption
        private val remarks: TextView = view.imageRemarks

        fun bind(photo: Photo) {
            GlideApp.with(view.context)
                    .load(photo.path)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            caption.text = if (photo.caption.isNullOrEmpty()) "No Caption" else photo.caption
            remarks.text =
                    if (photo.remarks.isNullOrEmpty()) "This is a cool photo. Rock on MB!"
                    else photo.remarks
        }
    }

    fun addPhoto(photo: Photo) {
        photos.add(photo)
        this.notifyItemInserted(photos.size)
    }

    val size: Int
        get() = photos.size

}