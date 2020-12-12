package com.uj.myapplications.adapters

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import com.uj.myapplications.utility.GlideApp
import kotlinx.android.synthetic.main.listitem_imageview.view.*


class ImageAdapter(val items: List<String>, val context: Context) :

    RecyclerView.Adapter<ImageAdapter.ImageViewHolder1>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder1 {
        return ImageViewHolder1(LayoutInflater.from(context).inflate(R.layout.listitem_imageview, p0, false))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ImageViewHolder1, position: Int) {
        GlideApp.with(context)
            .load(items[position])
            .into(holder.imageUploadToSet)
        /*  Handler().postDelayed({
              val flag = hasImage(holder.imageUploadToSet)
              if (flag) {
                  holder.imageDelete.visibility = View.VISIBLE
              } else {
                  holder.imageDelete.visibility = View.GONE
              }
          }, 1000)*/

    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    inner class ImageViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val imageUpload = view.imageView
        val imageUploadToSet = view.imageViewToSet
        val imageDelete = view.iBtn_ic_delete
    }
}