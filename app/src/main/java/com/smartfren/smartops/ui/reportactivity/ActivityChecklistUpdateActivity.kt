package com.smartfren.smartops.ui.reportactivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_form_add_checklist.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ActivityChecklistUpdateActivity : BaseActivity() {
    private val cameraRequest = 1888
    private var filename: String? = null
    private var token: String? = null
    private var actFormID: String? = null
    private var actID: String? = null
    private var actCLID: String? = null
    private var image: ImageView? = null
    private var checklistID: String? = null
    private var actResultNum: String? = null
    private var status: String? = null
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private var actionOpt: ArrayList<OptionModel>? = null
    private var actionAdditional: ArrayList<OptionModel>? = null
    private var actOpt: String? = null
    private var actOptEdit: String? = null
    private var resultOptionMandatory: String? = null
    private var resultTextMandatory: String? = null
    private var resultAttachmentMandatory: String? = null
    private var filepath: MultipartBody.Part? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var location: String? = null
    private val mActivityReportViewModel: ActivityReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_add_checklist)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        checklistID = intent.getStringExtra("checklistID")
        actResultNum = intent.getStringExtra("activityResultNum")
        resultOptionMandatory = intent.getStringExtra("resultOptionMandatory")
        resultTextMandatory = intent.getStringExtra("resultTextMandatory")
        resultAttachmentMandatory = intent.getStringExtra("resultAttachmentMandatory")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        getChecklistID(token, checklistID)
        actionOpt = ArrayList()
        actionAdditional = ArrayList()

        btnBack.setOnClickListener {
            onBackPressed()
        }

        tvTitleAddActivityType.text = intent.getStringExtra("checklistItem").toString()

        intent.getStringExtra("actResultText").let {
            etAddARemark.setText(it)
        }

        intent.getStringExtra("actResultAtt").let {
            if (it != null) {
                Glide.with(this)
                    .asBitmap()
                    .load(it)
                    .error(R.drawable.image_default)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                            ivAddPhotoAttachment.setImageBitmap(resource)
                            filepath = buildImageBodyPart("ar_cl_att", "${UUID.randomUUID()}.png",
                                resource
                            )
                            Log.e("tes file", "" + filepath)
                        }

                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                    })
                llAttachment.hide()
            } else {
                llAttachment.show()
            }
        }

        intent.getStringExtra("actItemStatus").let {
            when (it) {
                "0" -> {
                    actOptEdit = "FAIL"
                }
                "1" -> {
                    actOptEdit = "PASS"
                }
                "2" -> {
                    actOptEdit = "NA"
                }
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_status_checklist))
        spStatus.adapter = adapter
        val spinnerPosition = adapter.getPosition(actOptEdit)
        spStatus.setSelection(spinnerPosition)
        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                status = i.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )

        ivAddPhotoAttachment.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()               //User can only capture image from Camera
                .compress(1024)        //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
            filename = md5(sdf.format(Date()))
        }

        if (MainApp.instance.sharedPreferences?.getString("levelID", "") != "35") {
            btnSaveAddTask.hide()
        }

        if (intent.getStringExtra("addAdditionalNew") == "true") {
            tvTitleAppBar.text = getString(R.string.additional_item)
            clChecklistAdditional.show()
            tvTitleAddActivityType.hide()
            getChecklistAdditional(token, intent.getStringExtra("actFormNum"))
            actFormID = intent.getStringExtra("actFormNum")
            actID = intent.getStringExtra("actID")
            btnSaveAddTask.setOnClickListener {
                if (resultTextMandatory == "1" && etAddARemark.text!!.isEmpty()) {
                    toast(this, getString(R.string.remark_mandatory))
                } else if (resultAttachmentMandatory == "1" && filepath == null) {
                    toast(this, getString(R.string.attachment_mandatory))
                } else {
                    postActivityChecklistFormNew(
                        token, actFormID, actID, actCLID, actOpt,
                        etAddARemark.text.toString(), filepath, status
                    )
                }
            }
        } else {
            btnSaveAddTask.setOnClickListener {
                if (resultOptionMandatory == "1" && actOpt == null) {
                    toast(this, getString(R.string.option_mandatory))
                } else if (resultTextMandatory == "1" && etAddARemark.text?.isEmpty()!!) {
                    toast(this, getString(R.string.remark_mandatory))
                } else if (resultAttachmentMandatory == "1" && filepath == null) {
                    toast(this, getString(R.string.attachment_mandatory))
                } else {
                    postActivityChecklistForm(
                        token,
                        actResultNum,
                        actOpt,
                        etAddARemark.text.toString(),
                        filepath,
                        status
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val file: File? = ImagePicker.getFile(data)
                val filePath: String = file?.path.toString()
                val photo = BitmapFactory.decodeFile(filePath)

                if (photo != null)

                Glide.with(this)
                    .asBitmap()
                    .load(drawTextToBitmap(this, photo, "$location"))
                    .into(ivAddPhotoAttachment)
                filepath = buildImageBodyPart("ar_cl_att", "${UUID.randomUUID()}.png",
                    drawTextToBitmap(this, photo, "$location")
                )
                llAttachment.hide()
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getChecklistID(token: String?, checklistID: String?) {
        mActivityReportViewModel.listCheckListID(token, checklistID)
        mActivityReportViewModel.checklistIDData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { checklistIDResponse ->
                            val itemName = object : ArrayList<String>() {
                                init {
                                    for (element in checklistIDResponse.getReport()?.options!!) {
                                        element.optionDesc?.let { add(it) }
                                        val datas = OptionModel(
                                            element.optionID,
                                            element.optionDesc
                                        )
                                        actionOpt?.add(datas)
                                    }
                                }
                            }
                            val adapter = ArrayAdapter(
                                this@ActivityChecklistUpdateActivity,
                                android.R.layout.simple_list_item_1,
                                itemName
                            )

                            spMultipleChoice.adapter = adapter

                            intent.getStringExtra("actResultOpt").let {
                                val spinnerPosition = adapter.getPosition(it)
                                spMultipleChoice.setSelection(spinnerPosition)
                            }

                            spMultipleChoice.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {
                                        if (actionOpt != null) {
                                            val optionDatas: OptionModel = actionOpt!![i]
                                            actOpt = optionDatas.id
                                        }
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>) {

                                    }
                                }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun postActivityChecklistForm(
        token: String?,
        ar_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_status: String?,
    ) {
        mActivityReportViewModel.postChecklistForm(
            token, ar_id, ar_cl_opt,
            ar_cl_text, ar_cl_att, ar_status
        )
        mActivityReportViewModel.activityChecklistForm.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { activityReportResponse ->
                            if (activityReportResponse.getReport()?.success == true) {
                                showDialog("", activityReportResponse.getReport()?.message, true)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun getChecklistAdditional(token: String?, formNum: String?) {
        mActivityReportViewModel.listActivityChecklistAdditional(token, formNum)
        mActivityReportViewModel.listChecklistAdditional.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.report.let { checklistResponse ->
                            val itemName = object : ArrayList<String>() {
                                init {
                                    if (checklistResponse?.checklist != null)
                                        for (element in checklistResponse.checklist!!) {
                                            element.clDescription?.let { add(it) }
                                            val datas = OptionModel(
                                                element.clId,
                                                element.clDescription
                                            )
                                            actionAdditional?.add(datas)
                                        }

                                    resultAttachmentMandatory = checklistResponse?.checklist?.get(0)?.clAttachmentMandatory
                                    resultTextMandatory = checklistResponse?.checklist?.get(0)?.clNotesMandatory
                                }
                            }

                            val adapter = ArrayAdapter(this@ActivityChecklistUpdateActivity, android.R.layout.simple_list_item_1, itemName)

                            spAdditionalChoice.adapter = adapter

                            spAdditionalChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                    if (actionAdditional != null) {
                                        val optionDatas: OptionModel = actionAdditional!![i]
                                        actCLID = optionDatas.id
                                        getChecklistID(token, optionDatas.id)
                                    }
                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>) {

                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun postActivityChecklistFormNew(
        token: String?,
        ar_act_id: String?,
        ar_act_cat: String?,
        ar_cl_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_cl_status: String?,
    ) {
        mActivityReportViewModel.setPostActivityReportFormNew(
            token, ar_act_id, ar_act_cat,
            ar_cl_id, ar_cl_opt, ar_cl_text, ar_cl_att, ar_cl_status
        )
        mActivityReportViewModel.postActivityReportFormNew.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { activityReportResponse ->
                            if (activityReportResponse.getReport()?.success == true) {
                                showDialog("", activityReportResponse.getReport()?.message, true)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = "${getCityName(task.result!!.latitude, task.result!!.longitude)}"
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }
}