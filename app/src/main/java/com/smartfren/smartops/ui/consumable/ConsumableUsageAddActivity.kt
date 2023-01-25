package com.smartfren.smartops.ui.consumable

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.consumable_usage_add.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ConsumableUsageAddActivity : BaseActivity() {
    private var token: String? = null
    private var region: String? = null
    private var selectImage = ""
    private var filename: String? = null
    private var filepath: MultipartBody.Part? = null
    private var filepath2: MultipartBody.Part? = null
    private val cameraRequest = 1888
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var location: String? = null
    private var actionOpt: ArrayList<OptionModel>? = null
    private var actOpt: String? = null
    private val consumableViewModel: ConsumableUsageViewModel by viewModels()
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private val cal = Calendar.getInstance()
    private val myFormat = "dd-MM-yyyy" // mention the format you need
    private val myFormatToBackend = "yyyy-MM-dd HH:mm:ss" // mention the format you need
    private val sdf = SimpleDateFormat(myFormat, Locale.US)
    private val sdfToBackend = SimpleDateFormat(myFormatToBackend, Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consumable_usage_add)
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Add Consumable Usage"

        token = MainApp.instance.sharedPreferences?.getString("token", "")
        region = MainApp.instance.sharedPreferences?.getString("regionCode", "")

        token?.let { it -> region?.let { it1 -> getSite(it, it1) } }
        getChecklistID()
        actionOpt = ArrayList()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        etAddConsSiteVisitDate.setText(sdf.format(cal.time))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                etAddConsSiteVisitDate.setText(sdf.format(cal.time))
            }

        btnAddConsSiteVisitDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )

        clAddPhotoBefore.setOnClickListener {
            imagePicker()
            selectImage = "before"
            filename = md5(sdf.format(Date()))
        }

        clAddPhotoAfter.setOnClickListener {
            imagePicker()
            selectImage = "after"
            filename = md5(sdf.format(Date()))
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSaveAddTask.setOnClickListener {
            if (etAddConsSiteID.text.isEmpty()) {
                toast(this, "Please enter Site ID.")
            } else if (etAdConsNotes.text?.isEmpty()!!) {
                toast(this, "Please enter Notes.")
            } else if (filepath == null) {
                toast(this, "Please add photo before.")
            } else if (filepath2 == null) {
                toast(this, "Please add photo after.")
            } else {
                postAddConsumableUsage(
                    actOpt,
                    etAddConsSiteID.text.toString(),
                    etAddConsSiteVisitDate.text.toString(),
                    etAdConsNotes.text.toString(),
                    getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                    sdf.format(cal.time),
                    filepath,
                    filepath2
                )
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val file: File? = ImagePicker.getFile(data)
                val filePath: String = file?.path!!
                val photo = BitmapFactory.decodeFile(filePath)

                if (selectImage == "after") {
                    Glide.with(this)
                        .asBitmap()
                        .load(drawTextToBitmap(this, photo, "$location"))
                        .into(ivAddPhotoAfter)
                    filepath2 = buildImageBodyPart(
                        "cflm_usage_after", "${UUID.randomUUID()}.png",
                        drawTextToBitmap(this, photo, "$location")
                    )
                    llPhotoAfterDefault.hide()
                } else {
                    Glide.with(this)
                        .asBitmap()
                        .load(drawTextToBitmap(this, photo, "$location"))
                        .into(ivAddPhotoBefore)
                    filepath = buildImageBodyPart(
                        "cflm_usage_before", "${UUID.randomUUID()}.png",
                        drawTextToBitmap(this, photo, "$location")
                    )
                    llPhotoBeforeDefault.hide()
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getChecklistID() {
        consumableViewModel.setConsumableUsageType()
        consumableViewModel.consumableUsageTypeData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { checklistIDResponse ->
                            val itemName = object : java.util.ArrayList<String>() {
                                init {
                                    for (element in checklistIDResponse.cvConsumableType!!) {
                                        element?.consumablesName?.let { add(it) }
                                        val datas = OptionModel(
                                            element?.idConsumables,
                                            element?.consumablesName
                                        )
                                        actionOpt?.add(datas)
                                    }
                                }
                            }
                            val adapter = ArrayAdapter(
                                this,
                                android.R.layout.simple_list_item_1,
                                itemName
                            )

                            spAddAConsType.adapter = adapter

                            spAddAConsType.onItemSelectedListener =
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

    private fun getSite(token: String, site: String) {
        dailyActivityViewModel.listSite(token, site)
        dailyActivityViewModel.siteData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteResponse ->
                            if (siteResponse.getReport()?.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        for (element in siteResponse.getReport()?.sites!!) {
                                            element.siteId?.let { add(it) }
                                        }
                                    }
                                }

                                val adapter = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_list_item_1,
                                    itemName
                                )
                                etAddConsSiteID.setAdapter(adapter)
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

    private fun postAddConsumableUsage(
        consType: String?,
        consSiteId: String?,
        consSiteVisitDate: String?,
        consNotes: String?,
        consUserId: String?,
        consRegisterDate: String?,
        consUsageBefore: MultipartBody.Part?,
        consUsageAfter: MultipartBody.Part?
    ) {
        consumableViewModel.setAddConsumableUsage(
            consType, consSiteId, consSiteVisitDate?.let { parseDateToBackend(it) },
            consNotes, consUserId, sdfToBackend.format(cal.time),
            consUsageBefore, consUsageAfter
        )
        consumableViewModel.addConsumable.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { activityReportResponse ->
                            if (activityReportResponse.success == true) {
                                showDialog("Add Success", "Task has been Add.", true)
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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = "${getCityName(task.result!!.latitude, task.result!!.longitude)}"
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }
}