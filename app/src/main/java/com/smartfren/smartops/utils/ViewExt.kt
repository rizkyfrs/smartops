package com.smartfren.smartops.utils

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.smartfren.smartops.R
import kotlinx.android.synthetic.main.activity_form_add_task.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Context.alertDialogLocation() {
    val mAlertDialog = AlertDialog.Builder(this)
    mAlertDialog.setIcon(R.mipmap.ic_launcher_smartops) //set alertdialog icon
    mAlertDialog.setTitle(getString(R.string.string_title_location_setting)) //set alertdialog title
    mAlertDialog.setMessage(getString(R.string.string_detail_location_setting)) //set alertdialog message
    mAlertDialog.setPositiveButton(getString(R.string.yes)) { dialog, id ->
        //perform some tasks here
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
    mAlertDialog.setNegativeButton(getString(R.string.no)) { dialog, id ->
        //perform som tasks here
    }
    mAlertDialog.show()
}

fun removeBackSlash(urls: String): String {
    var url = urls
    val re = Regex("\b")
    url = url.let { it1 -> re.replace(it1, "") }
    return url
}

fun parseDateFromBackend(dates: String): String {
    var date: String? = null
    val dateFormatBackend = SimpleDateFormat("yyyy-MM-dd")
    val displayDate = SimpleDateFormat("dd-MM-yyyy")

    date = displayDate.format(dateFormatBackend.parse(dates))

    return date
}

fun parseDateToBackend(dates: String?): String? {
    var date: String? = null
    val dateFormatBackend = SimpleDateFormat("yyyy-MM-dd")
    val displayDate = SimpleDateFormat("dd-MM-yyyy")

    date = if (dates!!.isNotEmpty()) dateFormatBackend.format(displayDate.parse(dates)) else ""

    return date
}

fun parseDateToBackendTimeStamp(dates: String?): String? {
    var date: String? = null
    val dateFormatBackend = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val displayDate = SimpleDateFormat("dd-MM-yyyy HH:mm")

    date = if (dates!!.isNotEmpty()) dateFormatBackend.format(displayDate.parse(dates)) else ""

    return date
}

fun parseDateFull(dates: String?, formatDate: String?): String? {
    val dateFormatBackend = SimpleDateFormat(formatDate)
    val displayDate = SimpleDateFormat("EEE, dd MMM yyyy")

    return displayDate.format(dateFormatBackend.parse(dates))
}

fun parseDateTime(dates: String?, formatDate: String?): String? {
    val dateFormatBackend = SimpleDateFormat(formatDate)
    val displayDate = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss")

    return displayDate.format(dateFormatBackend.parse(dates))
}

fun getTimeNow(formatDate: String?): String? {
    val cal = Calendar.getInstance()
    val myFormat = formatDate // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    return sdf.format(cal.time)
}

fun replaceText(text: String?): String? {
    val oldValue = ","
    val newValue = "."

    return text?.replace(oldValue, newValue)
}

fun replacePointToComma(text: String?): String? {
    val oldValue = "."
    val newValue = ","

    return text?.replace(oldValue, newValue)
}

fun replaceComma(text: String?): String? {
    val oldValue = ",00"
    val newValue = ""

    return text?.replace(oldValue, newValue)
}

fun replacePoint(text: String?): String? {
    val oldValue = "."
    val newValue = ""

    return text?.replace(oldValue, newValue)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getGreetingMessage(): String {
    val c = Calendar.getInstance()
    val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

    return when (timeOfDay) {
        in 0..11 -> getString(R.string.selamat_pagi)
        in 12..15 -> getString(R.string.selamat_siang)
        in 16..20 -> getString(R.string.selamat_petang)
        in 21..23 -> getString(R.string.selamat_malam)
        else -> ""
    }
}

fun convertStringToRequestBody(data: String?): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), data)
}

fun Context.buildImageBodyPart(pic: String, fileName: String, bitmap: Bitmap?): MultipartBody.Part {
    val leftImageFile = convertBitmapToFile(fileName, resizeBitmap(bitmap))
    val reqFile = RequestBody.create(MediaType.parse("image/*"), leftImageFile)
    return MultipartBody.Part.createFormData(pic, leftImageFile.name, reqFile)
}

fun buildImageBodyPart2(pic: String, fileName: String, file: File?): MultipartBody.Part {
    val f = File(file?.path!!)
    val reqFile = RequestBody.create(MediaType.parse("image/*"), f)
    return MultipartBody.Part.createFormData(pic, fileName, reqFile)
}

fun resizeBitmap(oldBitmap: Bitmap?): Bitmap {
    return Bitmap.createScaledBitmap(oldBitmap!!, 600, 600 * oldBitmap.height / oldBitmap.width, true)
}

fun Context.convertBitmapToFile(fileName: String, bitmap: Bitmap?): File {
    //create a file to write bitmap data
    val file = File(cacheDir, fileName)
    file.createNewFile()

    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitMapData = bos.toByteArray()

    //write the bytes in file
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}

fun md5(s: String): String? {
    val MD5 = "MD5"
    try {
        // Create MD5 Hash
        val digest: MessageDigest = MessageDigest
            .getInstance(MD5)
        digest.update(s.toByteArray())
        val messageDigest: ByteArray = digest.digest()

        // Create Hex String
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2) h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

@RequiresApi(Build.VERSION_CODES.M)
fun isNetworkConnected(context: Context): Boolean {

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

}

@RequiresApi(Build.VERSION_CODES.M)
fun <T : Application> AndroidViewModel.hasInternetConnection(): Boolean {
    val connectivityManager = getApplication<T>().getSystemService(
        Context.CONNECTIVITY_SERVICE,
    ) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun Activity.showDialog(titles: String?, descs: String?, finish: Boolean?) {
    val dialog = Dialog(this)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_success)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    val btnOk = dialog.findViewById(R.id.btnOk) as Button
    val title = dialog.findViewById(R.id.tvTitleSuccess) as TextView
    val desc = dialog.findViewById(R.id.tvSuccess) as TextView

    if (titles != null && titles != "") title.text = titles
    if (descs != null && descs != "") desc.text = descs

    btnOk.setOnClickListener {
        dialog.dismiss()
        if (finish == true) {
            finish()
        }
    }
    dialog.show()
}

fun Activity.showDialogFeature(titles: String?, descs: String?) {
    val dialog = Dialog(this)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_success)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    val btnOk = dialog.findViewById(R.id.btnOk) as Button
    val imgView = dialog.findViewById(R.id.imgSuccess) as ImageView
    val title = dialog.findViewById(R.id.tvTitleSuccess) as TextView
    val desc = dialog.findViewById(R.id.tvSuccess) as TextView

    imgView.setImageDrawable(resources.getDrawable(R.drawable.ic_error))
    imgView.imageTintList = resources.getColorStateList(R.color.color_pink)
    if (titles != null && titles != "") title.text = titles
    if (descs != null && descs != "") desc.text = descs

    btnOk.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

fun drawTextToBitmap(
    gContext: Context,
    bitmaps: Bitmap,
    gText: String,
): Bitmap? {
    var bitmap = bitmaps
    val resources: Resources = gContext.resources
    val scale: Float = resources.displayMetrics.density
    var bitmapConfig = bitmap.config
    // set default bitmap config if none
    if (bitmapConfig == null) {
        bitmapConfig = Bitmap.Config.ARGB_8888
    }
    // resource bitmaps are imutable,
    // so we need to convert it to mutable one
    bitmap = bitmap.copy(bitmapConfig, true)
    val canvas = Canvas(bitmap)
    // new antialised Paint
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // text color - #3D3D3D
    paint.color = Color.WHITE
    // text size in pixels
    paint.textSize = (10 * scale).toInt().toFloat()
    // text shadow
//    paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

    // draw text to the Canvas center
    val bounds = Rect()
    var noOfLines = 0
    for (line in gText.split("\n").toTypedArray()) {
        noOfLines++
    }
    paint.getTextBounds(gText, 0, gText.length, bounds)
    val x = 5
    var y: Int = bitmap.height - bounds.height() * 5
    val mPaint = Paint()
    mPaint.color = gContext.resources.getColor(R.color.color_transparent)
    val left = 0F
    val top = 0F
    val right = bitmap.width
    val bottom = bitmap.height
    canvas.drawRect(left, top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
    for (line in gText.split("\n").toTypedArray()) {
        canvas.drawText(line, x.toFloat(), y.toFloat(), paint)
        y += paint.descent().toInt() - paint.ascent().toInt()
    }
    return bitmap
}

fun Context.getCityName(lat: Double, long: Double): String {
    val geoCoder = Geocoder(this, Locale.getDefault())
//    val address = geoCoder.getFromLocation(lat, long, 3)
    val sb = StringBuilder()
    val cal = Calendar.getInstance()
    val myFormat = "EEE, d MMM yyyy HH:mm:ss" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    var addresses: List<Address?>? = null

    try {
        addresses = geoCoder.getFromLocation(lat, long, 3)
    } catch (ioException: IOException) {
        Log.e("Address", "Service Not Available", ioException)
    } catch (illegalArgumentException: IllegalArgumentException) {
        Log.e("Address", "Invalid Latitude or Longitude Used" + ". " +
                    "Latitude = " + lat + ", Longitude = " +
                long, illegalArgumentException
        )
    }

    sb.append(sdf.format(cal.time)).append("\n")
    sb.append(addresses?.get(0)?.featureName).append(", ")
    sb.append(addresses?.get(0)?.thoroughfare).append("\n")
    sb.append(addresses?.get(0)?.subLocality).append("\n")
    sb.append(addresses?.get(0)?.locality).append("\n")
    sb.append(addresses?.get(0)?.adminArea).append("\n")
    return sb.toString()
}

fun defaultFormat(double: Double?): String? {
    val defaultFormat = DecimalFormat("#.##")
    defaultFormat.maximumFractionDigits = 1

    return defaultFormat.format(double)
}

fun decodeToken(jwt: String): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
    val parts = jwt.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        "$header"
        "$payload"
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}

fun getUserIDJWT(tokenJWT: String?): String? {
    val mDecode = tokenJWT?.let { decodeToken(it) }
    val test = mDecode?.let { JSONObject(it).getString("security").toString() }
    val mDecodeTokenOk = test?.let { JSONObject(it).getString("userid").toString() }
    return mDecodeTokenOk
}

fun getUserLevelIDJWT(tokeJWT: String?): String? {
    val mDecode = tokeJWT?.let { decodeToken(it) }
    val test = mDecode?.let { JSONObject(it).getString("security").toString() }
    val mDecodeTokenOk = test?.let { JSONObject(it).getString("userlevelid").toString() }
    return mDecodeTokenOk
}

fun getUserParentIDJWT(userParentIDJWT: String?): String? {
    val mDecode = userParentIDJWT?.let { decodeToken(it) }
    val test = mDecode?.let { JSONObject(it).getString("security").toString() }
    val mDecodeTokenOk = test?.let { JSONObject(it).getString("parentuserid").toString() }
    return mDecodeTokenOk
}

fun Activity.dialogShowImage(images: String) {
    val dialog = Dialog(this)

    dialog.setContentView(R.layout.pop_up_image)
    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    val image: ImageView = dialog.findViewById<View>(R.id.ivPopup) as ImageView
    val btnClose: ImageView = dialog.findViewById<View>(R.id.btnClose) as ImageView

    Glide.with(this)
        .load(removeBackSlash(images))
        .into(image)

    btnClose.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun Activity.imagePicker() {
    ImagePicker.with(this) // User can only capture image from Camera
        .cameraOnly()
        .compress(1024)  //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)
        .start()
}

fun setDelimiterComma(text: String?): String? {
    val string = text
    val domain: String? = string?.substringBeforeLast(",")
    return domain
}

fun setCurrentTime(): String? {
    val cal = Calendar.getInstance()
    val myFormatToBackend = "yyyy-MM-dd HH:mm:ss"
    val sdfToBackend = SimpleDateFormat(myFormatToBackend, Locale.US)
    return sdfToBackend.format(cal.time)
}

fun Activity.getFileType(uri: Uri): String? {
    val r = contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(r.getType(uri))
}

fun Activity.showDialogSiteID(listSiteID: MutableList<String?>) {
    val dialog = Dialog(this)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.pop_up_spinner_search)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    val editText = dialog.findViewById(R.id.etSiteID) as EditText
    val listView = dialog.findViewById(R.id.lvSiteID) as ListView
    val btnClose = dialog.findViewById(R.id.btnClose) as ImageView

    dialog.show()

    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listSiteID)

    listView.adapter = adapter
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            adapter.filter.filter(s)
        }

        override fun afterTextChanged(s: Editable) {}
    })
    listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        tvAddSiteID.text = adapter.getItem(position)
        dialog.dismiss()
    }

    btnClose.setOnClickListener {
        dialog.dismiss()
    }
}