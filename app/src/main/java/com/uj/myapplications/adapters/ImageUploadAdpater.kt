package com.uj.myapplications.adapters


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.listitem_imageview.view.*
import android.os.Build
import android.graphics.drawable.Drawable.ConstantState
import android.annotation.SuppressLint
import android.support.annotation.RequiresApi
import android.widget.ImageView
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.annotation.NonNull
import android.util.Log
import com.bumptech.glide.request.RequestOptions
import com.uj.myapplications.fragments.AddMenuFragment
import com.uj.myapplications.utility.GlideApp

//0 for add and 1 for Edit
class ImageUploadAdpater(val items: Array<String>, val context: Context, val flagAddOrUpdate: Int) :
    RecyclerView.Adapter<ImageUploadAdpater.ImageViewHolder>() {
    private var mClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_imageview, p0, false))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        GlideApp.with(context)
            .load(items[position])
            .into(holder.imageUploadToSet)
        Handler().postDelayed({
            if (flagAddOrUpdate == 0) {
                val flag = hasImage(holder.imageUploadToSet)
                if (flag) {
                    holder.imageDelete.visibility = View.VISIBLE
                } else {
                    holder.imageDelete.visibility = View.GONE
                }
            } else {
                if (!(items[position].isNullOrEmpty()) && UtilityClass.checkURL(items[position])) {
                    holder.imageDelete.visibility = View.VISIBLE
                } else {
                    holder.imageDelete.visibility = View.GONE
                    val flag = hasImage(holder.imageUploadToSet)
                    if (flag) {
                        holder.imageDelete.visibility = View.VISIBLE
                    } else {
                        holder.imageDelete.visibility = View.GONE
                    }

                }
            }
        }, 1000)

        // holder.imageUploadToSet.setIm(context.getDrawable(R.drawable.transparent_drawable))
        // UtilityClass.setImageFromUrlOrFile(context, items[position], holder.imageUploadToSet)
        /* if (UtilityClass.checkImageResource(context, holder.imageUploadToSet, R.drawable.transparent_drawable)) {
             //Set Delete Button OnUti
             holder.imageDelete.visibility = View.GONE
         } else {
             holder.imageDelete.visibility = View.VISIBLE
            // UtilityClass.setImageFromUrlOrFile(context, items[position], holder.imageUploadToSet)
         }*/

        //holder.imageUpload.setImageResource(items[position])
        holder.imageUpload.setOnClickListener {
            if (mClickListener != null) {
                mClickListener!!.onItemClick(position)
            }
        }

        holder.imageDelete.setOnClickListener {
            //Set Default image
            if (flagAddOrUpdate == 0) {
                holder.imageUpload.setImageResource(R.drawable.ic_upload)
                holder.imageUploadToSet.setImageResource(R.drawable.transparent_drawable)
                items[position] = ""
                holder.imageDelete.visibility = View.GONE
                notifyItemChanged(position)
                //Remove Element that element from Array List
                AddMenuFragment.photos.removeAt(position)
                val size = AddMenuFragment.photos.size
                Log.e("AddMenuFragment.photos", "Size is : $size")
                // notifyDataSetChanged()
            } else {
                //Case Edit
                holder.imageUpload.setImageResource(R.drawable.ic_upload)
                holder.imageUploadToSet.setImageResource(R.drawable.transparent_drawable)
                items[position] = ""
                holder.imageDelete.visibility = View.GONE
                notifyItemChanged(position)
                //Remove Element that element from Array List

                if (AddMenuFragment.menuPhotosFromServer.size >= position + 1 && UtilityClass.checkURL(
                        AddMenuFragment.menuPhotosFromServer.get(position)
                    )
                ) {
                    if (!AddMenuFragment.deletedPhotos.contains(AddMenuFragment.menuPhotosFromServer.get(position)))
                        AddMenuFragment.deletedPhotos.add(AddMenuFragment.menuPhotosFromServer.get(position))

                } else {
                    Log.e("Del", "This image is file so Only remove")
                    AddMenuFragment.photos.removeAt(position - 1)
                    val size = AddMenuFragment.photos.size
                    Log.e("AddMenuFragment.photos", "Size is : $size")
                }
            }
        }
        /*   if (holder.imageUploadToSet.drawable != null) {
               holder.imageDelete.visibility = View.VISIBLE
           } else {
               holder.imageDelete.visibility = View.GONE
           }*/
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return 4
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val imageUpload = view.imageView
        val imageUploadToSet = view.imageViewToSet
        val imageDelete = view.iBtn_ic_delete
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    private fun hasImage(view: ImageView): Boolean {
        val drawable = view.drawable
        var hasImage = drawable != null

        if (hasImage && drawable is BitmapDrawable) {
            hasImage = drawable.bitmap != null
        }

        return hasImage
    }

    fun setImageFromUrlOrFile(context: Context, path: String, image: ImageView) {
        val req = RequestOptions()

        /*  RequestOptions requestOptions = new RequestOptions();
requestOptions.placeholder(R.drawable.ic_placeholder);
requestOptions.error(R.drawable.ic_error);*/

    }


}


