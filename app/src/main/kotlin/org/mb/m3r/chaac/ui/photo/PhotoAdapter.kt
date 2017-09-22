package org.mb.m3r.chaac.ui.photo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.photo_item.view.*
import org.mb.m3r.chaac.GlideApp
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.data.Photo

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoAdapter(val photos: ArrayList<Photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {
    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotoHolder?, position: Int) {
        photos.get(index = position).let {
            holder?.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PhotoAdapter.PhotoHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.photo_item, parent, false)
        return PhotoHolder(view)
    }

    class PhotoHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.photoView

        fun bind(photo: Photo) {
            GlideApp.with(view.context)
                    .load(photo.path)
                    .centerCrop()
                    .into(imageView)
        }

    }

    fun addPhoto(photo: Photo) {
        photos.add(photo)
        this.notifyItemInserted(photos.size)
    }

    val size: Int
        get() = photos.size

}