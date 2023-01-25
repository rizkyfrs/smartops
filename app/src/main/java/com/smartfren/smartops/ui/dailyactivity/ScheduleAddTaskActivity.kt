package com.smartfren.smartops.ui.dailyactivity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.daily_activity_add_new.btnSaveAddTask
import kotlinx.android.synthetic.main.daily_activity_add_new.etAddActivityRegion
import kotlinx.android.synthetic.main.daily_activity_add_new.tvAddSiteID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleAddTaskActivity : BaseActivity() {
    private var token: String? = null
    private var gpsStatus: Boolean = false
    private val cameraRequest = 1888
    private var regionNames: String? = null
    private var actDomLv1: String? = null
    private var actDomLv2: String? = null
    private var priority: String? = null
    private var region: String? = null
    private var selectImage = ""
    private var filename: String? = null
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
    private var etDateExecs = ""
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
    private var listSiteID: MutableList<String?> = mutableListOf()
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
        region = MainApp.instance.sharedPreferences?.getString("regionName", "")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        btnBack.setOnClickListener {
            onBackPressed()
        }

        tvAddSiteID.setOnClickListener {
            showDialogSiteID(listSiteID)
        }

        when (region) {
            "JB" -> {
                regionNames = "1"
            }
            "NS" -> {
                regionNames = "2"
            }
            "SS" -> {
                regionNames = "3"
            }
            "WJ" -> {
                regionNames = "4"
            }
            "CJ" -> {
                regionNames = "5"
            }
            "EJ" -> {
                regionNames = "6"
            }
            "SK" -> {
                regionNames = "7"
            }
        }

        regionNames?.let { token?.let { it1 -> getSite(it1, it) } }

        token?.let { getActLevel1(it) }

        etAddActivityRegion.isEnabled = false
        etAddActivityRegion.setText(region)

        if (intent.getStringExtra("site") != null) tvAddSiteID.text = intent.getStringExtra("site")

        val adapter = ArrayAdapter(this@ScheduleAddTaskActivity, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_priority))
        spPriority.adapter = adapter
        spPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                priority = adapterView.selectedItem.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        val cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        etDateExec.setText(sdf.format(cal.time))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                etDateExec.setText(sdf.format(cal.time))
            }

        btnDate.setOnClickListener {
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
            ImagePicker.with(this) // User can only capture image from Camera
                .cameraOnly()
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
            selectImage = "before"
            filename = md5(sdf.format(Date()))
        }

        clAddPhotoAfter.setOnClickListener {
            ImagePicker.with(this) // User can only capture image from Camera
                .cameraOnly()
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
            selectImage = "after"
            filename = md5(sdf.format(Date()))
        }

        token?.let { getActLevel1(it) }

        val adapterActType = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_type))

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

        val adapterActStat = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_status))

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

        btnSaveAddTask.setOnClickListener {
            locationEnabled()
            postUpdateTaskManual(
                token,
                tvAddSiteID.text.toString(),
                regionNames,
                actDomLv1,
                actDomLv2,
                tvAddSiteID.text.toString(),
                etAddNeName.text.toString(),
                etAddWOID.text.toString(),
                priority,
                etAddActionNeeded.text.toString(),
                parseDateToBackend(etDateExec.text.toString()),
                filepath,
                filepath2,
                rca1s,
                rca2s,
                rca3s,
                rcaAct,
                actionType,
                etAddActionDetail.text.toString(),
                actStats,
                pending1,
                pending2,
                ""
            )
        }
    }

    private fun getSite(token: String, site: String) {
        listSiteID.clear()
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
                                listSiteID.addAll(itemName)
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

    private fun getActLevel1(token: String) {
        dailyActivityViewModel.listActLevel1(token)
        dailyActivityViewModel.actLevel1Data.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { actLevel1Response ->
                            if (actLevel1Response.getReport()?.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        for (element in actLevel1Response.getReport()?.domain!!) {
                                            element.domainName?.let { add(it) }
                                        }
                                    }
                                }

                                val adapter = ArrayAdapter(
                                    this@ScheduleAddTaskActivity,
                                    android.R.layout.simple_list_item_1,
                                    itemName
                                )
                                spDomainLev1.adapter = adapter

                                spDomainLev1.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            adapterView: AdapterView<*>,
                                            view: View?,
                                            i: Int,
                                            l: Long
                                        ) {
                                            getActLevel2(
                                                MainApp.instance.sharedPreferences?.getString(
                                                    "token",
                                                    ""
                                                )!!,
                                                adapterView.selectedItem.toString()
                                            )
                                            postRCA(
                                                token,
                                                "1",
                                                adapterView.selectedItem.toString(),
                                                ""
                                            )
                                            actDomLv1 = adapterView.selectedItem.toString()
                                        }

                                        override fun onNothingSelected(adapterView: AdapterView<*>) {

                                        }
                                    }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
//                        loading(true)
                    }
                }
            }
        }
    }

    private fun getActLevel2(token: String, domains: String) {
        dailyActivityViewModel.listActLevel2(token, domains)
        dailyActivityViewModel.actLevel2Data.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { actLevel2Response ->
                            if (actLevel2Response.getReport()?.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        for (element in actLevel2Response.getReport()?.activity!!) {
                                            element.activityName?.let { add(it) }
                                        }
                                    }
                                }

                                val adapter = ArrayAdapter(
                                    this@ScheduleAddTaskActivity,
                                    android.R.layout.simple_list_item_1,
                                    itemName
                                )
                                spDomainLev2.adapter = adapter

                                spDomainLev2.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            adapterView: AdapterView<*>,
                                            view: View?,
                                            i: Int,
                                            l: Long
                                        ) {
                                            actDomLv2 = adapterView.selectedItem.toString()
                                        }

                                        override fun onNothingSelected(adapterView: AdapterView<*>) {

                                        }
                                    }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
//                        loading(true)
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
                                            this,
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
                                                    postRCA(token, "2", actDomLv1, listRCA1Id[i]!!)
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
                                            this,
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
                                                    postRCA(token, "3", actDomLv1, listRCA2Id[i]!!)
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
                                            this,
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
                                            this,
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
                                            this,
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
                                            this,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val file: File? = ImagePicker.getFile(data)
                val filePath: String = file?.path!!
                val photo = BitmapFactory.decodeFile(filePath)

                if (photo != null)

                if (selectImage == "after") {
                    Glide.with(this)
                        .asBitmap()
                        .load(drawTextToBitmap(this, photo, "$location"))
                        .into(ivAddPhotoAfter)
                    filepath2 = buildImageBodyPart(
                        "pic_after", "${UUID.randomUUID()}.png",
                        drawTextToBitmap(this, photo, "$location")
                    )
                    llPhotoAfterDefault.hide()
                } else {
                    Glide.with(this)
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

    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLastLocation() {
        fusedLocationClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = "${getCityName(task.result!!.latitude, task.result!!.longitude)}"
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun showDialogSiteID(listSiteID: MutableList<String?>) {
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
            dailyActivityViewModel.setSiteIDSelection("${adapter.getItem(position)}")
            tvAddSiteID.text = dailyActivityViewModel.setSiteID()
            dialog.dismiss()

        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }
}