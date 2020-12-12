package com.uj.myapplications.utility

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.uj.myapplications.R
import com.uj.myapplications.pojo.MenuPojo.Content
import com.uj.myapplications.pojo.MenuPojo.Extra
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

/**
 * Created by Umesh on 1/24/2018.
 */

class UtilityClass {
    companion object {
        private const val EMAIL_PATTERN = "^[\\p{L}\\p{N}\\._%+-]+@[\\p{L}\\p{N}\\.\\-]+\\.[\\p{L}]{2,}$"
        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[!@_*#\$%^&+=])(?=\\S+\$).{6,}\$"
        private const val maxHeight = 1280.0f
        private const val maxWidth = 1280.0f
        private var toast: Toast? = null
        private val progressDialog: ProgressDialog? = null
        private var snackbar: Snackbar? = null
        val OOPS_STRING: String = "Oops! Something went wrong, Try Again!"
        var dialog: Dialog? = null


        //SDF to generate a unique name for our compress file.
        val SDF = SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault())

        @JvmStatic
        @Throws(IOException::class)
        fun getCompressed(context: Context, path: String): File {
            if (context == null)
                throw  NullPointerException("Context must not be null.")
            //getting device external cache directory, might not be available on some devices,
            // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
            var cacheDir = context.externalCacheDir
            if (cacheDir == null)
            //fall back
                cacheDir = context.cacheDir
            val rootDir = cacheDir.absolutePath + "/Myma"
            val root = File(rootDir)
            //Create ImageCompressor folder if it doesnt already exists.
            if (!root.exists())
                root.mkdirs()

            //decode and resize the original bitmap from @param path.
            val bitmap = decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300)
            //create placeholder for the compressed image file
            val compressed = File(root, SDF.format(Date()) + ".jpg" /*Your desired format*/)
            //convert the decoded bitmap to stream
            val byteArrayOutputStream = ByteArrayOutputStream()

            /*compress bitmap into byteArrayOutputStream
                Bitmap.compress(Format, Quality, OutputStream)
                Where Quality ranges from 1 - 100.
             */
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            /*
                   Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
                   java.io.FileOutputStream can help us do just That!
             */
            val fileOutputStream = FileOutputStream(compressed)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()
            //File written, return to the caller. Done!
            return compressed
        }

        @JvmStatic
        fun decodeImageFromFiles(path: String, width: Int, height: Int): Bitmap {
            val scaleOptions = BitmapFactory.Options()
            scaleOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, scaleOptions);
            var scale = 1;
            while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height
            ) {
                scale *= 2
            }
            // decode with the sample size
            var outOptions = BitmapFactory.Options()
            outOptions.inSampleSize = scale
            return BitmapFactory.decodeFile(path, outOptions)
        }

        fun validateString(text: String): String {
            return if (text != "null" && text.trim().isNotEmpty())
                text
            else ""
        }

        fun hideDialog() {
            if (dialog != null) {
                dialog!!.dismiss()
            }

        }

        fun getCurrentTimeInFormat(): String {
            val c = Calendar.getInstance()
            //  2015/01/02 23:14:05
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val strDate = dateFormat.format(c.time)
            return strDate
        }

        fun isValidPassword(target: String): Boolean {
            return (target.matches(Regex(PASSWORD_REGEX)))
        }

        fun showDialog(context: Context) {
            if (dialog != null && dialog!!.isShowing()) {
                dialog!!.dismiss()
                dialog = null
            }
            dialog = Dialog(context, R.style.styleDialogTransparent)
            //dialog = new Dialog(context);
            dialog!!.setContentView(R.layout.progressview_dialog)
            // indicatorView.show();
            dialog!!.setCancelable(false)
            dialog!!.show()
        }

        @JvmStatic
        fun showToast(context: Context, message: String) {
            showToast(context, message, false)// false mean DURATION_SHORT
        }

        fun showToast(context: Context, message: String, durationShort: Boolean) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, message, if (durationShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
            toast!!.show()
        }

        fun isInternetAvailable1(ctx: Context): Boolean {
            val cm = ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return (!(cm.activeNetworkInfo == null || !cm.activeNetworkInfo.isAvailable || !cm.activeNetworkInfo.isConnected))
        }

        fun isInternetAvailable(
            context: Context
        ): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        fun showSnackBar(view: View, message: String, duaraton: Int) {
            showSnackBar(view, message, null, null, duaraton)
        }

        fun showSnackBar(
            view: View,
            message: String,
            actionName1: String?,
            listener1: View.OnClickListener?,
            duration: Int
        ) {
            showSnackBar(view, message, actionName1, listener1, null, null, duration)
        }

        fun showSnackBar(
            view: View,
            message: String,
            actionName1: String?,
            listener1: View.OnClickListener?,
            actionName2: String?,
            listener2: View.OnClickListener?,
            duration: Int
        ) {
            if (snackbar != null) {
                snackbar!!.dismiss()
            }
            snackbar = Snackbar.make(view, message, duration)
            val snackbarView = snackbar!!.getView()
            snackbarView.setBackgroundColor(Color.parseColor("#ec3338"))
            //  snackbar.(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
            if (actionName1 != null && listener1 != null) {
                snackbar!!.setAction(actionName1, listener1)
            }
            if (actionName2 != null && listener2 != null) {
                snackbar!!.setAction(actionName2, listener2)
            }
            snackbar!!.show()
        }

        fun dismissSnackbar() {
            if (snackbar != null) {
                snackbar!!.dismiss()
            }
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getPathFromURI(context: Context, uri: Uri): String? {

            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isTrimmedEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)!!
                    )

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isTrimmedEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }

        fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            } finally {
                if (cursor != null)
                    cursor.close()
            }
            return null
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun createRandomFileName(): String {
            val stringBuilder = StringBuilder("aastha_")
            stringBuilder.append(System.currentTimeMillis())
            stringBuilder.append(".jpg")
            return stringBuilder.toString()
        }

        fun getImageUri(inImage: Bitmap, context: Context): Uri {
            // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
            return Uri.parse(path)
        }

        // Returns true if external storage for photos is available
        private fun isExternalStorageAvailable(): Boolean {
            val state = Environment.getExternalStorageState()
            return state == Environment.MEDIA_MOUNTED
        }

        fun compressImage(context: Context, imagePath: String): String {
            var scaledBitmap: Bitmap? = null

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var bmp: Bitmap? = BitmapFactory.decodeFile(imagePath, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
            val maxRatio = maxWidth / maxHeight

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()

            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(scaledBitmap!!)
            canvas.matrix = scaleMatrix
            canvas.drawBitmap(
                bmp!!, middleX - bmp.width / 2, middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG)
            )
            if (bmp != null) {
                bmp.recycle()
            }

            val exif: ExifInterface
            try {
                exif = ExifInterface(imagePath)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                }
                scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height,
                    matrix, true
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            var out: FileOutputStream? = null
            val filepath = getFilename(context)
            try {
                out = FileOutputStream(filepath)

                // write the compressed bitmap at the destination specified by
                // filename.
                scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return filepath
        }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }

        fun getFilename(context: Context): String {
            val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString() + "/AasthaCenter")
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs()
            }
            val mImageName = "IMG_" + System.currentTimeMillis().toString() + ".jpg"
            return mediaStorageDir.absolutePath + "/" + mImageName
        }


        fun commaSeparatedStringToArrayList(commaSeparated: String?): ArrayList<String>? {
            var list: ArrayList<String>? = null
            if (commaSeparated != null && commaSeparated.length > 0) {
                list =
                        ArrayList(Arrays.asList(*commaSeparated.split(",".toRegex()).dropLastWhile { it.isTrimmedEmpty() }.toTypedArray()))
            }
            return list
        }

        fun arrayListToCommaSeparatedString(list: ArrayList<String>): String? {
            var s: String? = null
            if (list.size > 0) {
                s = TextUtils.join(",", list.toTypedArray())
            }
            return s
        }


        fun getDateYYYYMMDD(calendar: Calendar): String {
            val formattt = SimpleDateFormat("yyyy-MM-dd")
            return formattt.format(calendar.time)
        }

        fun isIntentResolutionAvailable(ctx: Context, intent: Intent): Boolean {
            val mgr = ctx.packageManager
            val list = mgr.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            return list.size > 0
        }

        fun longDateToReadableDate(timestamp: String?): String? {
            var dateString: String? = null
            if (timestamp != null && !timestamp.isTrimmedEmpty()) {
                val timeStamp = java.lang.Long.parseLong(timestamp)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timeStamp
                dateString = "" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        calendar.getDisplayName(
                            Calendar.MONTH,
                            Calendar.SHORT,
                            Locale.getDefault()
                        ) + " " + calendar.get(Calendar.YEAR)
            }
            return dateString
        }

        fun isPastTime(timeForAlarm: Long): Boolean {
            var isPAstTime = false
            try {
                val calendarSecond = Calendar.getInstance()
                calendarSecond.timeInMillis = timeForAlarm
                val calendarNowTime = Calendar.getInstance()
                isPAstTime = calendarSecond.timeInMillis - calendarNowTime.timeInMillis <= 0
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return isPAstTime
        }

        @JvmStatic
        fun getTimeINMills(): Long {
            var timeINMills: Long = 0
            try {
                val calendar = Calendar.getInstance()
                timeINMills = calendar.timeInMillis
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return timeINMills
        }

        fun parseDateToddMMyyyy(time: String): String? {
            val inputPattern = "dd/MM/yyyy HH:mm:ss"
            val outputPattern = "dd-MMM-yyyy h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()

        }

        fun calculateTax(amount: Double?, taxRate: Double?): Double? {
            var temp_var: Double? = 0.0
            temp_var = amount!! * (taxRate!! / 100)
            return temp_var
        }

        @JvmStatic
        fun hideKeypad(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        @JvmStatic
        fun shakeView(view: View, context: Context) {
            val animShake = AnimationUtils.loadAnimation(context, R.anim.animation)
            view.startAnimation(animShake)
        }

        fun setImageFromUrlOrFile(context: Context, path: String, image: ImageView) {
            val req = RequestOptions()

            /*  RequestOptions requestOptions = new RequestOptions();
  requestOptions.placeholder(R.drawable.ic_placeholder);
  requestOptions.error(R.drawable.ic_error);*/
            GlideApp.with(context)
                .load(path).placeholder(R.drawable.transparent_drawable).error(R.drawable.transparent_drawable)
                .into(image)
        }

        fun setImageFromUrlOrFileProfile(context: Context, path: String, image: ImageView) {
            val req = RequestOptions()

            /*  RequestOptions requestOptions = new RequestOptions();
  requestOptions.placeholder(R.drawable.ic_placeholder);
  requestOptions.error(R.drawable.ic_error);*/
            GlideApp.with(context)
                .load(path).placeholder(R.drawable.ic_user).error(R.drawable.ic_user)
                .apply(RequestOptions.circleCropTransform())
                .into(image)
        }

        @JvmStatic
        fun shakeItemView(view: View, context: Context) {
            val animShake = AnimationUtils.loadAnimation(context, R.anim.shake)
            val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 10))
            } else {
                vibrator.vibrate(100)
            }

            view.startAnimation(animShake)
        }

        fun isValidEmail(target: CharSequence): Boolean {
            return (!TextUtils.isEmpty(target) && Regex(EMAIL_PATTERN).matches(target))
        }

        fun isValidWord(word: String): Boolean {
            return word.matches("[^.]*".toRegex())
        }

        /*  fun isValidIDN(target: String): Boolean {
            return (!DomainValidator.getInstance().isValid(target))
        }

        fun isValidWhoIsDomain(target: CharSequence): Boolean {
            return (!DomainValidator.getInstance().isValid(target.toString()))
        }


        fun isValidISDCode(target: CharSequence): Boolean {
            return (!TextUtils.isEmpty(target) && target.matches(Regex("^+[1-9]{1,3}\$")))
        }

        fun isValidPassword(target: String): Boolean {
            return (target.matches(Regex(PASSWORD_REGEX)))
        }

        fun convertToDoubleZero(target: Double): Double {
            val df = DecimalFormat("#.##")
            val dx = df.format(target)
            return java.lang.Double.valueOf(dx)!!

        }

        fun isCartContain(list: ArrayList<DomainPriceListPojo>): Boolean {
            var arrayListToSendOnCart: java.util.ArrayList<ArrayList<DomainPriceListPojo>>? = ArrayList()
            arrayListToSendOnCart = Config.getCartList()
            var flag: Boolean = false
            if (arrayListToSendOnCart != null) {
                if (arrayListToSendOnCart!!.size > 0) {
                    for (listInside: ArrayList<DomainPriceListPojo> in arrayListToSendOnCart) {
                        if (listInside[0].getName().equals(list[0].getName())) {
                            println("List Already Contains")
                            //   arrayListToSendOnCart.remove(listInside)
                            flag = true
                            //  UtilityClass.showToast(context!!, "List Already Contains this item")
                            break
                        }
                    }
                }
            }
            return flag
        }

        fun askForInput(context: Context, title: String, message: String, positiveButtonText: String?, negativeButtonText: String?, showOnlyOneButton: Boolean, callback: AlertDialogCallback<String>) {
            val alert = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            alert.setTitle(title)
            alert.setIcon(R.mipmap.ic_launcher)
                    ?: "Ok") { dialog, whichButton -> callback.alertDialogCallback("1") }
            if (!showOnlyOneButton) {
                alert.setNegativeButton(negativeButtonText ?: "Cancel") { dialog, whichButton ->
                    // Canceled.
                    callback.alertDialogCallback("0")
                }
            }
            alert.setCancelable(false)

            alert.show()

        }

    }*/
        // Load Fragment
        fun switchToFragment(
            fragment: Fragment?,
            frag_tag: String,
            fragmentId: Int,
            framentManager: FragmentManager,
            addToBackStack: Boolean
        ) {
            if (fragment != null) {
                val ft = framentManager.beginTransaction()
                ft.replace(fragmentId, fragment, frag_tag)
                if (addToBackStack) {
                    ft.addToBackStack(frag_tag)
                } else {
                    ft.addToBackStack(null)
                }
                ft.commit()
            }
        }

        // Load Fragment
        fun switchToFragmentAdd(
            fragment: Fragment?,
            frag_tag: String,
            fragmentId: Int,
            framentManager: FragmentManager,
            addToBackStack: Boolean
        ) {
            if (fragment != null) {
                val ft = framentManager.beginTransaction()
                ft.add(fragmentId, fragment, frag_tag)
                if (addToBackStack) {
                    ft.addToBackStack(frag_tag)
                } else {
                    ft.addToBackStack(null)
                }
                ft.commit()
            }
        }


        // Goto App Settings
        fun goToAppSetting(context: Context) {
            val builder = AlertDialog.Builder(context)
                .setMessage(
                    "For proper functioning of app you need to provide all permisson to the app." +
                            "this won't harm your phone or your data." +
                            "For that go to Enable Now -> Permissions"
                )
                .setPositiveButton("Enable Now") { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }
            builder.create().show()
        }

        fun changeDateToFormat(currentdateFormat: String, requireFormat: String, dateStr: String): String {
            var result: String? = null
            if (dateStr.isNullOrEmpty()) {
                return result!!
            }
            val formatterOld = SimpleDateFormat(currentdateFormat, Locale.getDefault())
            val formatterNew = SimpleDateFormat(requireFormat, Locale.getDefault())
            var date: Date? = null
            try {
                date = formatterOld.parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            if (date != null) {
                result = getFormattedDate(date)
            }

            return result!!

        }

        fun getFormattedDate(date: Date): String {
            val cal = Calendar.getInstance()
            cal.time = date
            //2nd of march 2015
            val day = cal.get(Calendar.DATE)

            if (!(day > 10 && day < 19))
                when (day % 10) {
                    1 -> return SimpleDateFormat("EEEE ',' d'st' 'of' MMMM yyyy").format(date)
                    2 -> return SimpleDateFormat("EEEE ',' d'nd' 'of' MMMM yyyy").format(date)
                    3 -> return SimpleDateFormat("EEEE ',' d'rd' 'of' MMMM yyyy").format(date)
                    else -> return SimpleDateFormat("EEEE ',' d'th' 'of' MMMM yyyy").format(date)
                }
            return SimpleDateFormat("d'th' 'of' MMMM yyyy").format(date)
        }


        fun makeJsonArrayofHashMapMenuItems(map: HashMap<String, Int>): JSONArray {
            var jsonArray = JSONArray()
            map.forEach { (key, value) ->
                /* var key = "$key"
                 var value = "$value"*/
                var jsonObject = JSONObject()
                jsonObject.put("name", "$key")
                jsonObject.put("qty", "$value".toString())
                jsonArray.put(jsonObject)

            }

            return jsonArray

        }

        fun makeJsonArrayofListMapMenuItems(map: List<Content>): JSONArray {
            var jsonArray = JSONArray()
            map.forEach { it ->
                /* var key = "$key"
                 var value = "$value"*/
                var jsonObject = JSONObject()
                jsonObject.put("name", it.name)
                jsonObject.put("qty", it.qty.toString())
                jsonArray.put(jsonObject)

            }

            return jsonArray

        }

        fun makeJsonArrayofHashMapExtraMenuItems(map: HashMap<String, Int>): JSONArray {
            var jsonArray = JSONArray()
            map.forEach { (key, value) ->
                /* var key = "$key"
                 var value = "$value"*/
                var jsonObject = JSONObject()
                jsonObject.put("name", "$key")
                jsonObject.put("price", value)
                jsonArray.put(jsonObject)

            }

            return jsonArray

        }

        fun makeJsonArrayofListExtraMenuItems(list: List<Extra>): JSONArray {
            var jsonArray = JSONArray()
            list.forEach { it ->
                /* var key = "$key"
                 var value = "$value"*/
                var jsonObject = JSONObject()
                jsonObject.put("name", it?.name)
                jsonObject.put("price", it?.price)
                jsonArray.put(jsonObject)

            }

            return jsonArray

        }

        @JvmStatic
        @SuppressLint("NewApi")
        fun checkImageResource(
            ctx: Context?, imageView: ImageView?,
            imageResource: Int
        ): Boolean {
            var result = false

            if (ctx != null && imageView != null && imageView!!.drawable != null) {
                val constantState: Drawable.ConstantState?

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    constantState = ctx.resources
                        .getDrawable(imageResource, ctx.theme)
                        .constantState
                } else {
                    constantState = ctx.resources.getDrawable(imageResource)
                        .constantState
                }

                if (imageView!!.getDrawable().getConstantState() === constantState) {
                    result = true
                }
            }

            return result
        }

        @JvmStatic
        fun checkURL(input: CharSequence): Boolean {
            if (TextUtils.isEmpty(input)) {
                return false
            }
            var URL_PATTERN: Pattern = Patterns.WEB_URL
            var isURL = URL_PATTERN.matcher(input).matches()
            if (!isURL) {
                var urlString = input.toString()
                if (URLUtil.isNetworkUrl(urlString)) {
                    try {
                        URL(urlString)
                        isURL = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return isURL
        }

        fun getCurrentTime(): String {
            val cal = Calendar.getInstance()
            val date = cal.time
            val dateFormat = SimpleDateFormat("HH:mm:ss")
            val formattedDate = dateFormat.format(date)
            return formattedDate
        }


    }


}