package com.uj.myapplications.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import com.uj.myapplications.R
import com.uj.myapplications.activities.NavigationActivity
import com.uj.myapplications.activities.ServicesActivity
import kotlinx.android.synthetic.main.dialog_image_doc_view.*


@SuppressLint("ValidFragment")
class CustomDialog(var mContext: Context, var layoutId: Int) : DialogFragment() {
    var chooseImageFromListener: ChooseImageFromListener? = null
    private var content: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mContext is NavigationActivity) {
            try {
                chooseImageFromListener = targetFragment as ChooseImageFromListener
            } catch (e: ClassCastException) {
                //chooseImageFromListener = activity as ChooseImageFromListener
                throw ClassCastException(activity.toString() + " must implement ChooseImageFromListener")
            }
        } else if (mContext is ServicesActivity) {
            try {
                chooseImageFromListener = targetFragment as ChooseImageFromListener
            } catch (e: ClassCastException) {
                //chooseImageFromListener = activity as ChooseImageFromListener
                throw ClassCastException(activity.toString() + " must implement ChooseImageFromListener")
            }
        } else {
            try {
                chooseImageFromListener = mContext as ChooseImageFromListener
            } catch (e: ClassCastException) {
                //  chooseImageFromListener = activity as ChooseImageFromListener
                throw ClassCastException(activity.toString() + " must implement ChooseImageFromListener")
            }
        }
        content = arguments?.getString("content")
        // Pick a style based on the num.
        val style = DialogFragment.STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)

    }

//        if (context is NavigationActivity) {
//            try {
//            chooseImageFromListener = parentFragment as ChooseImageFromListener
//        } catch (e: ClassCastException) {
//            throw ClassCastException("Calling fragment must implement Callback interface")
//        }
//        } else if (context is EditProfileActivity) {
//            chooseImageFromListener = (context) as ChooseImageFromListener
//        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Do all the stuff to initialize your custom view
        var view: View? = null
        when (layoutId) {
            R.layout.dialog_image_picker -> {
                view = inflater.inflate(layoutId, container, false)
                // set values for custom dialog components - text, image and button
                val ll_camera = view!!.findViewById(R.id.ll_camera) as LinearLayout
                val ll_gallery = view!!.findViewById(R.id.ll_gallery) as LinearLayout
                val iBtnCloseImagePickerDialog = view!!.findViewById(R.id.iBtnCloseImagePickerDialog) as ImageButton

                ll_camera.setOnClickListener {
                    //UtilityClass.showToast(activity!!, "Camera:Test")
                    chooseImageFromListener?.selectedImageFrom(1)
                    dismiss()
                }
                ll_gallery.setOnClickListener {
                    // UtilityClass.showToast(activity!!, "Gallery:Test")
                    chooseImageFromListener?.selectedImageFrom(2)
                    dismiss()
                }
                iBtnCloseImagePickerDialog.setOnClickListener {
                    dismiss()
                }
            }
            R.layout.dialog_contact_us_success -> {
                view = inflater.inflate(layoutId, container, false)
                val btnCancel = view.findViewById<View>(R.id.btnBacktoHome) as Button
                btnCancel.setOnClickListener {
                    dismiss()
                }
            }
            R.layout.dialog_image_doc_view -> {
                view = inflater.inflate(layoutId, container, false)
                val iBtnCloseImagePickerDialog = view!!.findViewById(R.id.iBtnCloseImagePickerDialog) as ImageButton
                val btnReplaceDOc = view.findViewById<View>(R.id.btnReplaceDoc) as Button
                val imageDoc = view.findViewById<View>(R.id.imageViewDoc) as ImageView
                val txtDoc = view.findViewById<View>(R.id.txt_docName) as TextView
                try {
                    txtDoc.text = URLUtil.guessFileName(content!!, null, null)
                } catch (e: Exception) {
                    txt_docName.text = "image.jpg"
                    e.printStackTrace()
                }
                UtilityClass.setImageFromUrlOrFile(mContext, content!!, imageDoc)
                btnReplaceDOc.setOnClickListener {
                    dismiss()
                }
                iBtnCloseImagePickerDialog.setOnClickListener {
                    dismiss()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        /**
         * Create a new instance of CustomDialogFragment, providing "num" as an
         * argument.
         */
        fun newInstance(context: Context, layoutId: Int, content: String): CustomDialog {
            val f = CustomDialog(context, layoutId)
            // Supply num input as an argument.
            val args = Bundle()
            args.putString("content", content)
            f.arguments = args
            return f
        }
    }


    // Image Attachment Callback

    interface ChooseImageFromListener {
        fun selectedImageFrom(from: Int)
    }


}