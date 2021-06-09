package com.sachin.sasthaflickrsearch

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PhotosAdapter(
    private val list: Photos,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photosList : Photos = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return PhotosViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotosViewHolder).bindView(photosList.photoList?.get(position))
    }

    override fun getItemCount(): Int {
        return photosList.photoList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotoList(list: Photos){
        photosList = list
        notifyDataSetChanged()
    }

    fun addPhotoList(list: Photos){
        list.photoList?.let {
            val startPosition = photosList.photoList!!.size - 1
            (photosList.photoList!! as ArrayList).addAll(it)
            notifyItemRangeInserted(startPosition, photosList.perpage)
        }
    }
}

class PhotosViewHolder(
    private val context: Context,
    private val view: View
) :
    RecyclerView.ViewHolder(view) {
    fun bindView(photo: Photo?) {
        photo?.let {
            val image = view.findViewById<ImageView>(R.id.image)
            val link = "https://live.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg"

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(link)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(circularProgressDrawable)
                .into(image)
        }
    }
}