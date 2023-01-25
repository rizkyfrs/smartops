package com.smartfren.smartops.ui.dailyactivity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_add_new.*
import kotlinx.android.synthetic.main.daily_activity_add_new.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleUpdateTaskActivity : BaseActivity() {
    private var token: String? = null
    private var gpsStatus: Boolean = false
    private val cameraRequest = 1888
    private var selectImage = ""
    private var filename: String? = null
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private var filepath: MultipartBody.Part? = null
    private var filepath2: MultipartBody.Part? = null
    private var rca1s = ""
    private var rcaE1s: String? = null
    private var rca2s = ""
    private var rcaE2s: String? = null
    private var rca3s = ""
    private var rcaE3s: String? = null
    private var rcaAct = ""
    private var rcaActE: String? = null
    private var actionDetail = ""
    private var actionType = ""
    private var actionEType: String? = null
    private var actStats = ""
    private var actEStats: String? = null
    private var pending1 = ""
    private var pendingE1: String? = null
    private var pending2 = ""
    private var pendingE2: String? = null
    private var siteID = ""
    private var etNEname = ""
    private var etWOid = ""
    private var etActNeed = ""
    private var etDateExec = ""
    private var listRCA1Id: MutableList<String?> = mutableListOf()
    private var listRCA1Name: MutableList<String?> = mutableListOf()
    private var listRCA2Id: MutableList<String?> = mutableListOf()
    private var listRCA2Name: MutableList<String?> = mutableListOf()
    private var listRCA3Id: MutableList<String?> = mutableListOf()
    private var listRCA3Name: MutableList<String?> = mutableListOf()
    private var listRCA4Id: MutableList<String?> = mutableListOf()
    private var listRCA4Name: MutableList<String?> = mutableListOf()
    private var listRCA5Id: MutableList<String?> = mutableListOf()
    private var listRCA5Name: MutableList<String?> = mutableListOf()
    private var listRCA6Id: MutableList<String?> = mutableListOf()
    private var listRCA6Name: MutableList<String?> = mutableListOf()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var location: String? = null
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_activity_add_new)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        siteID = intent.getStringExtra("siteID").toString()
        etNEname = intent.getStringExtra("etNEname").toString()
        etWOid = intent.getStringExtra("etWOid").toString()
        etActNeed = intent.getStringExtra("etActNeed").toString()
        etDateExec = intent.getStringExtra("etDateExec").toString()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

//        clEleven.hide()
//        clTwelve.hide()
//        clThirteen.hide()

        clAdd.hide()

        if (intent.getStringExtra("type") == "update") {
            tvTitleAppBar.text = getString(R.string.update_task)
            btnSaveAddTask.text = getString(R.string.update_task)
            if (intent.getStringExtra("picBefore") == null) {
                llPhotoBeforeDefault.show()
            } else {
                intent.getStringExtra("picBefore")?.let {
                    Glide.with(this@ScheduleUpdateTaskActivity)
                        .asBitmap()
                        .load(removeBackSlash(it))
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                                ivAddPhotoBefore.setImageBitmap(resource)
                                filepath = buildImageBodyPart("pic_before", "${UUID.randomUUID()}.png",
                                    resource
                                )
                                Log.e("tes file", "" + filepath)
                            }

                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        })
                    llPhotoBeforeDefault.hide()
                }
            }

            if (intent.getStringExtra("picAfter") == null) {
                llPhotoAfterDefault.show()
            } else {
                intent.getStringExtra("picAfter")?.let {
                    Glide.with(this@ScheduleUpdateTaskActivity)
                        .asBitmap()
                        .load(removeBackSlash(it))
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                                ivAddPhotoAfter.setImageBitmap(resource)
                                filepath2 = buildImageBodyPart("pic_after", "${UUID.randomUUID()}.png",
                                    resource
                                )
                                Log.e("tes file", "" + filepath2)
                            }

                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        })
                    llPhotoAfterDefault.hide()
                }
            }

            etAddActionDetail.setText(intent.getStringExtra("actDetail"))
            rcaE1s = intent.getStringExtra("rca1")
            rcaE2s = intent.getStringExtra("rca2")
            rcaE3s = intent.getStringExtra("rca3")
            rcaActE = intent.getStringExtra("rca4")
            actionEType = intent.getStringExtra("actType")
            actEStats = intent.getStringExtra("actStat")
            pendingE1 = intent.getStringExtra("pending1")
            pendingE2 = intent.getStringExtra("pending2")
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

        ivAddPhotoBefore.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()               //User can only capture image from Camera
                .compress(512)	   //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
            selectImage = "before"
            filename = md5(sdf.format(Date()))
        }

        ivAddPhotoAfter.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()               //User can only capture image from Camera
                .compress(512)		//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
            selectImage = "after"
            filename = md5(sdf.format(Date()))
        }

        postRCA(token, "1", intent.getStringExtra("domainLev1").toString(), "")

        val adapterActType = ArrayAdapter(
            this@ScheduleUpdateTaskActivity,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.item_type)
        )

        spActionType.adapter = adapterActType

        val spinnerPosition = adapterActType.getPosition(actionEType)
        spActionType.setSelection(spinnerPosition)

        spActionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                when (i) {
                    0 -> {
                        actionType = "1"
                    }
                    1 -> {
                        actionType = "2"
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        val adapterActStat = ArrayAdapter(
            this@ScheduleUpdateTaskActivity,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.item_status)
        )

        spActionStatus.adapter = adapterActStat

        if (intent.getStringExtra("type") == "new") {
            spActionStatus.setSelection(0)
            spActionStatus.isEnabled = false
        } else {
            val spinnerPosActStat = adapterActStat.getPosition(actEStats)
            spActionStatus.setSelection(spinnerPosActStat)
        }

        spActionStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                when (i) {
                    0 -> {
                        clPendingLv1.hide()
                        clPendingLv2.hide()
                        actStats = "2"
                        pending1 = ""
                        pending2 = ""
                    }
                    1 -> {
                        clPendingLv1.show()
                        clPendingLv2.show()
                        actStats = "11"
                    }
                    2 -> {
                        clPendingLv1.hide()
                        clPendingLv2.hide()
                        actStats = "12"
                        pending1 = ""
                        pending2 = ""
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSaveAddTask.setOnClickListener {
            locationEnabled()
            actionDetail = etAddActionDetail.text.toString()

            if (gpsStatus) {
                if (intent.getStringExtra("type") != "new") {
                    postUpdateTask(
                        token,
                        intent.getStringExtra("siteID").toString(),
                        intent.getStringExtra("activityNum").toString(),
                        filepath,
                        filepath2,
                        rca1s,
                        rca2s,
                        rca3s,
                        rcaAct,
                        actionType,
                        actionDetail,
                        actStats,
                        pending1,
                        pending2
                    )
                } else {
                    postUpdateTaskManual(
                        token,
                        intent.getStringExtra("siteID"),
                        intent.getStringExtra("regionName"),
                        intent.getStringExtra("domainLev1"),
                        intent.getStringExtra("domainLev2"),
                        intent.getStringExtra("siteID"),
                        intent.getStringExtra("etNEname"),
                        intent.getStringExtra("etWOid"),
                        intent.getStringExtra("priority"),
                        intent.getStringExtra("etActNeed"),
                        intent.getStringExtra("etDateExec"),
                        filepath,
                        filepath2,
                        rca1s,
                        rca2s,
                        rca3s,
                        rcaAct,
                        actionType,
                        actionDetail,
                        actStats,
                        pending1,
                        pending2,
                        ""
                    )
                }
            } else {
                alertDialogLocation()
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
                    getLastLocation()
                    Glide.with(this@ScheduleUpdateTaskActivity)
                        .asBitmap()
                        .load(drawTextToBitmap(this, photo, "$location"))
                        .into(ivAddPhotoAfter)
                    filepath2 = buildImageBodyPart(
                        "pic_after", "${UUID.randomUUID()}.png",
                        drawTextToBitmap(this, photo, "$location")
                    )
                    llPhotoAfterDefault.hide()
                } else {
                    getLastLocation()
                    Glide.with(this@ScheduleUpdateTaskActivity)
                        .asBitmap()
                        .load(drawTextToBitmap(this, photo, "$location"))
                        .into(ivAddPhotoBefore)
                    filepath = buildImageBodyPart(
                        "pic_before", "${UUID.randomUUID()}.png",
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

    private fun postUpdateTask(
        token: String?, site: String?, activityNumber: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
    ) {
        dailyActivityViewModel.postUpdateTask(
            token,
            site,
            activityNumber,
            pic_before,
            pic_after,
            rca_lev_1,
            rca_lev_2,
            rca_lev_3,
            rca_action,
            action_type,
            action_detail,
            activity_status,
            pending_remarks_1,
            pending_remarks_2
        )
        dailyActivityViewModel.postUpdateTaskData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { updateTaskResponse ->
                            if (updateTaskResponse.getReport()?.success == true) {
                                showDialog("", "", true)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun postUpdateTaskManual(
        token: String?, site: String?,
        region_code: String?,
        domain_lev_1: String?,
        domain_lev_2: String?,
        site_id: String?,
        ne_name: String?,
        wo_id: String?,
        priority: String?,
        action_needed: String?,
        actual_date: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
        action_remark: String?,
    ) {
        dailyActivityViewModel.postUpdateTaskManualTask(
            token, site,
            region_code,
            domain_lev_1,
            domain_lev_2,
            site_id,
            ne_name,
            wo_id,
            priority,
            action_needed,
            actual_date,
            pic_before, pic_after,
            rca_lev_1,
            rca_lev_2,
            rca_lev_3,
            rca_action,
            action_type,
            action_detail,
            activity_status,
            pending_remarks_1,
            pending_remarks_2,
            action_remark
        )
        dailyActivityViewModel.postUpdateTaskManualData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { updateTaskManualResponse ->
                            if (updateTaskManualResponse.getReport()?.success == true) {
                                showDialog("", "", true)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun postRCA(
        token: String?,
        rca_level: String?,
        rca_group: String?,
        rca_parent: String?
    ) {
        dailyActivityViewModel.postRCATask(token, rca_level, rca_group, rca_parent)
        dailyActivityViewModel.postRCAData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { rcaResponse ->
                            if (rcaResponse.getReport()?.success == true) {
                                when (dailyActivityViewModel.rcaLevel()) {
                                    "1" -> {
                                        listRCA1Id.clear()
                                        listRCA1Name.clear()

                                        for (element in rcaResponse.getReport()?.rca!!) {
                                            listRCA1Id.add(element.rcaNum)
                                            listRCA1Name.add(element.rcaName)
                                        }

                                        val adapterRca1 = ArrayAdapter(
                                            this@ScheduleUpdateTaskActivity,
                                            android.R.layout.simple_list_item_1,
                                            listRCA1Name
                                        )

                                        spRCALev1.adapter = adapterRca1

                                        val spinnerPosition = adapterRca1.getPosition(rcaE1s)
                                        spRCALev1.setSelection(spinnerPosition)

                                        spRCALev1.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>,
                                                    view: View?,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    postRCA(token, "2", rca_group, listRCA1Id[i]!!)
                                                    rca1s = listRCA1Id[i]!!
                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>) {
                                                }
                                            }
                                    }
                                    "2" -> {
                                        listRCA2Id.clear()
                                        listRCA2Name.clear()

                                        for (element in rcaResponse.getReport()?.rca!!) {
                                            listRCA2Id.add(element.rcaNum)
                                            listRCA2Name.add(element.rcaName)
                                        }

                                        val adapterRca2 = ArrayAdapter(
                                            this@ScheduleUpdateTaskActivity,
                                            android.R.layout.simple_list_item_1,
                                            listRCA2Name
                                        )

                                        spRCALev2.adapter = adapterRca2

                                        val spinnerPosition = adapterRca2.getPosition(rcaE2s)
                                        spRCALev2.setSelection(spinnerPosition)

                                        spRCALev2.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>,
                                                    view: View?,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    postRCA(token, "3", rca_group, listRCA2Id[i]!!)
                                                    rca2s = listRCA2Id[i]!!

                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>) {

                                                }
                                            }
                                    }
                                    "3" -> {
                                        listRCA3Id.clear()
                                        listRCA3Name.clear()

                                        for (element in rcaResponse.getReport()?.rca!!) {
                                            listRCA3Id.add(element.rcaNum)
                                            listRCA3Name.add(element.rcaName)
                                        }

                                        val adapterRca3 = ArrayAdapter(
                                            this@ScheduleUpdateTaskActivity,
                                            android.R.layout.simple_list_item_1,
                                            listRCA3Name
                                        )
                                        spRCALev3.adapter = adapterRca3

                                        val spinnerPosition = adapterRca3.getPosition(rcaE3s)
                                        spRCALev3.setSelection(spinnerPosition)

                                        spRCALev3.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>,
                                                    view: View?,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    postRCA(token, "4", "", listRCA3Id[i]!!)
                                                    rca3s = listRCA3Id[i]!!
                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>) {

                                                }
                                            }
                                    }
                                    "4" -> {
                                        listRCA4Id.clear()
                                        listRCA4Name.clear()

                                        for (element in rcaResponse.getReport()?.rca!!) {
                                            listRCA4Id.add(element.rcaNum)
                                            listRCA4Name.add(element.rcaAction)
                                        }

                                        val adapterRca4 = ArrayAdapter(
                                            this@ScheduleUpdateTaskActivity,
                                            android.R.layout.simple_list_item_1,
                                            listRCA4Name
                                        )
                                        spRCAAction.adapter = adapterRca4

                                        val spinnerPosition = adapterRca4.getPosition(rcaActE)
                                        spRCAAction.setSelection(spinnerPosition)

                                        spRCAAction.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>,
                                                    view: View?,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    postRCA(token, "5", "", "")
                                                    rcaAct = listRCA4Name[i]!!
                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>) {

                                                }
                                            }
                                    }
                                    "5" -> {
                                        listRCA5Id.clear()
                                        listRCA5Name.clear()

                                        for (element in rcaResponse.getReport()?.rca!!) {
                                            listRCA5Id.add(element.rcaNum)
                                            listRCA5Name.add(element.rcaName)
                                        }

                                        val adapter = ArrayAdapter(
                                            this@ScheduleUpdateTaskActivity,
                                            android.R.layout.simple_list_item_1,
                                            listRCA5Name
                                        )

                                        spPendingLv1.adapter = adapter

                                        val spinnerPosition = adapter.getPosition(pendingE1)
                                        spPendingLv1.setSelection(spinnerPosition)

                                        spPendingLv1.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>,
                                                    view: View?,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    postRCA(token, "6", listRCA5Name[i]!!, "")
                                                    pending1 = listRCA5Name[i]!!
                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>) {

                                                }
                                            }
                                    }
                                    "6" -> {
                                        listRCA6Id.clear()
                                        listRCA6Name.clear()

                                        for (element in rcaResponse.getReport()?.rca!!) {
                                            listRCA6Id.add(element.rcaNum)
                                            listRCA6Name.add(element.rcaName)
                                        }

                                        val adapter = ArrayAdapter(
                                            this@ScheduleUpdateTaskActivity,
                                            android.R.layout.simple_list_item_1,
                                            listRCA6Name
                                        )
                                        spPendingLv2.adapter = adapter

                                        val spinnerPosition = adapter.getPosition(pendingE2)
                                        spPendingLv2.setSelection(spinnerPosition)

                                        spPendingLv2.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>,
                                                    view: View?,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    pending2 = listRCA6Name[i]!!
                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>) {

                                                }
                                            }
                                    }
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
//                        loading(true)
                    }
                }
            }
        }
    }

    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLastLocation() {
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = getCityName(task.result.latitude, task.result.longitude)
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }
}