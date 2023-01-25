package com.smartfren.smartops.ui.tracker.risk

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_form_add_task.*
import kotlinx.android.synthetic.main.tracker_risk_add.*
import kotlinx.android.synthetic.main.tracker_risk_add.btnBack
import kotlinx.android.synthetic.main.tracker_risk_add.btnSaveAddTask
import kotlinx.android.synthetic.main.tracker_risk_add.tvTitleAppBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerRiskAddActivity : BaseActivity() {
    private var token: String? = null
    private var region: String? = null
    private var type: String? = null
    private var regionName: String? = null
    private var userID: String? = null
    private var status: String? = null
    private var statuse: String? = null
    private var groupLv1: String? = null
    private var groupLv1e: String? = null
    private var groupLv2: String? = null
    private var groupLv2e: String? = null
    private val cal = Calendar.getInstance()
    private val myFormat = "dd-MM-yyyy" // mention the format you need
    private val myFormatToBackend = "yyyy-MM-dd HH:mm:ss" // mention the format you need
    private val sdf = SimpleDateFormat(myFormat, Locale.US)
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val sdfToBackend = SimpleDateFormat(myFormatToBackend, Locale.US)
    private var listGroupLv1Id: MutableList<String?> = mutableListOf()
    private var listGroupLv1Name: MutableList<String?> = mutableListOf()
    private var listGroupLv2Id: MutableList<String?> = mutableListOf()
    private var listGroupLv2Name: MutableList<String?> = mutableListOf()
    private var listSiteID: MutableList<String?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tracker_risk_add)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        type = MainApp.instance.sharedPreferences?.getString("levelID", "")
        region = MainApp.instance.sharedPreferences?.getString("regionCode", "")
        regionName = MainApp.instance.sharedPreferences?.getString("regionName", "")
        userID = getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_risk_status))
        spAddRiskStatus.adapter = adapter

        when {
            intent.getStringExtra("update") == "yes" -> {
                tvTitleAppBar.text = getString(R.string.edit_tracker_risk)
                btnSaveAddTask.text = getString(R.string.update_task)

                token?.let { it -> region?.let { it1 -> getSite(it, it1) } }

                if (type == "501") {
                    clRiskGroupLv1.hide()
                    clRiskGroupLv2.hide()
                    clRiskRegion.hide()
                    clUserComp.hide()
                    tvTitleAddRiskDateReport.hide()
                    clAddRiskDateReport.hide()
                } else {
                    clUserComp.show()
                    clDateResolved.show()
                    clRiskTaken.show()
                    clRootCauseDesc.show()
                    getTrackerRiskGroupLv1("0")
                    spAddRiskLevel1.isEnabled = false
                    spAddRiskLevel2.isEnabled = false
                }

                groupLv1e = intent.getStringExtra("riskGroupLv1")
                groupLv2e = intent.getStringExtra("riskGroupLv2")

                val textAsli: String? = intent.getStringExtra("riskSiteID")
                val textEdit = textAsli?.substring(0, textAsli.indexOf(","))
                tvAddRiskSiteID.text = textEdit

                etAddRiskProbDesc.setText(intent.getStringExtra("riskDesc"))
                etAddRiskUserComp.setText(intent.getStringExtra("riskUserComp"))
                etAddRiskPIC.setText(intent.getStringExtra("riskPic"))
                etAddRiskActDesc.setText(intent.getStringExtra("riskActDesc"))
                etAddRiskPIC.setText(intent.getStringExtra("riskPic"))
                etAddDateReport.setText(intent.getStringExtra("riskReportDate"))
                etAddDateReport.isEnabled = false
                etAddDateTarget.setText(intent.getStringExtra("riskTargetDate"))
                etAddDateTarget.isEnabled = false
                etAddDateResolved.setText(intent.getStringExtra("riskResolveDate"))
                etAddRiskActTaken.setText(intent.getStringExtra("riskActTaken"))
                etAddRiskRootCause.setText(intent.getStringExtra("riskRootCause"))
                statuse = intent.getStringExtra("riskStatus")

                btnSaveAddTask.setOnClickListener {
                    when {
//                        etAddRiskSiteID.text.isEmpty() -> {
//                            toast(this, "Please enter Site ID.")
//                        }
                        etAddRiskProbDesc.text?.isEmpty()!! -> {
                            toast(this, "Please enter Problem Description.")
                        }
                        etAddRiskActDesc.text?.isEmpty()!! -> {
                            toast(this, "Please enter Action Description.")
                        }
                        etAddDateReport.text?.isEmpty()!! -> {
                            toast(this, "Please enter Date Reported.")
                        }
                        etAddDateTarget.text?.isEmpty()!! -> {
                            toast(this, "Please enter Date Target.")
                        }
                        status == "Resolve" && etAddDateResolved.text?.isEmpty()!! -> {
                            toast(this, "Please enter Date Resolved.")
                        }
                        else -> {
                            postEditTrackerRisk(
                                type,
                                intent.getStringExtra("riskID"),
                                groupLv1,
                                groupLv2,
                                region,
                                tvAddRiskSiteID.text.toString(),
                                etAddRiskProbDesc.text.toString(),
                                etAddRiskUserComp.text.toString(),
                                etAddRiskPIC.text.toString(),
                                etAddRiskActDesc.text.toString(),
                                parseDateToBackend(etAddDateReport.text.toString()),
                                parseDateToBackend(etAddDateTarget.text.toString()),
                                parseDateToBackend(etAddDateResolved.text.toString()),
                                etAddRiskActTaken.text.toString(),
                                etAddRiskRootCause.text.toString(),
                                status, userID, sdfToBackend.format(cal.time)
                            )
                        }
                    }
                }
            }
//            intent.getStringExtra("updateGH") == "yes" -> {
//                tvTitleAppBar.text = "Update Tracker Risk"
//                tvTitleAddRiskDateResolved.text = "Date - Closed"
//                btnSaveAddTask.text = getString(R.string.update_task)
//                clAddTrackerRisk.hide()
//                clDateResolved.show()
//                clRiskTaken.show()
//                clRootCauseDesc.show()
//
//                val list_of_items = arrayOf("Close")
//
//                val adapter =
//                    ArrayAdapter(
//                        this,
//                        android.R.layout.simple_list_item_1, list_of_items
//                    )
//
//                spAddRiskStatus.adapter = adapter
//
//                btnSaveAddTask.setOnClickListener {
//                    postUpdateTrackerRiskGH(
//                        intent.getStringExtra("riskIDGH"),
//                        parseDateToBackend(etAddDateResolved.text.toString()),
//                        etAddRiskActTaken.text.toString(),
//                        etAddRiskRootCause.text.toString(),
//                        status
//                    )
//                }
//            }
            else -> {
                tvTitleAppBar.text = getString(R.string.add_tracker_risk)
                clUserComp.hide()
                clDateResolved.hide()
                clRiskTaken.hide()
                clRootCauseDesc.hide()

                token?.let { it -> region?.let { it1 -> getSite(it, it1) } }
                getTrackerRiskGroupLv1("0")

                val list_of_items = arrayOf("Open")

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list_of_items)

                spAddRiskStatus.adapter = adapter

                btnSaveAddTask.setOnClickListener {
                    when {
//                        etAddRiskSiteID.text.isEmpty() -> {
//                            toast(this, "Please enter Site ID.")
//                        }
                        etAddRiskProbDesc.text?.isEmpty()!! -> {
                            toast(this, "Please enter Problem Description.")
                        }
                        etAddRiskActDesc.text?.isEmpty()!! -> {
                            toast(this, "Please enter Action Description.")
                        }
                        etAddDateReport.text?.isEmpty()!! -> {
                            toast(this, "Please enter Date Reported.")
                        }
                        etAddDateTarget.text?.isEmpty()!! -> {
                            toast(this, "Please enter Date Target.")
                        }
                        else -> {
                            postAddTrackerRisk(
                                type,
                                groupLv1,
                                groupLv2,
                                region,
                                tvAddRiskSiteID.text.toString(),
                                etAddRiskProbDesc.text.toString(),
                                etAddRiskPIC.text.toString(),
                                etAddRiskActDesc.text.toString(),
                                parseDateToBackend(etAddDateReport.text.toString()),
                                parseDateToBackend(etAddDateTarget.text.toString()),
                                status, userID, sdfToBackend.format(cal.time)
                            )
                        }
                    }
                }
            }
        }
        tvAddRiskSiteID.setOnClickListener {
            showDialogSiteID(listSiteID)
            if (MainApp.instance.sharedPreferences?.getString("addSiteID","") != "") {
                tvAddRiskSiteID.text = MainApp.instance.sharedPreferences?.getString("addSiteID","")
            }
        }

        etAddRiskRegion.isEnabled = false
        etAddRiskRegion.setText(regionName)
        etAddDateReport.setText(sdf.format(cal.time))

        val dateSetListenerReport = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etAddDateReport.setText(sdf.format(cal.time))
        }

        val dateSetListenerTarget = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etAddDateTarget.setText(sdf.format(cal.time))
        }

        val dateSetListenerResolve = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etAddDateResolved.setText(sdf.format(cal.time))
        }

        btnAddDateReport.setOnClickListener {
            DatePickerDialog(this, dateSetListenerReport, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnAddDateTarget.setOnClickListener {
            DatePickerDialog(this, dateSetListenerTarget, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnAddDateResolved.setOnClickListener {
            DatePickerDialog(this, dateSetListenerResolve, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val spinnerPosition = adapter.getPosition(statuse)
        spAddRiskStatus.setSelection(spinnerPosition)

        spAddRiskStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                status = adapterView.selectedItem.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
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

    private fun getTrackerRiskGroupLv1(key: String?) {
        trackerViewModel.setTrackerRiskGroupLv1(key)
        trackerViewModel.trackerRiskGroupLv1Data.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                for (element in trackerResponse.cvApireffTrackerGroup1!!) {
                                    listGroupLv1Id.add(element.paramId)
                                    listGroupLv1Name.add(element.paramValue)
                                }

                                val adapterGroupLv1 = ArrayAdapter(this@TrackerRiskAddActivity, android.R.layout.simple_list_item_1, listGroupLv1Name)

                                spAddRiskLevel1.adapter = adapterGroupLv1

                                val spinnerPosition = adapterGroupLv1.getPosition(groupLv1e)
                                spAddRiskLevel1.setSelection(spinnerPosition)

                                spAddRiskLevel1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        getTrackerRiskGroupLv2(listGroupLv1Id[i]!!)
                                        groupLv1 = listGroupLv1Name[i]!!
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>) {
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
                        loading(true)
                    }
                }
            }
        }
    }

    private fun getTrackerRiskGroupLv2(key: String?) {
        listGroupLv2Id.clear()
        listGroupLv2Name.clear()
        trackerViewModel.setTrackerRiskGroupLv2(key)
        trackerViewModel.trackerRiskGroupLv2Data.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                if (trackerResponse.cvApireffTrackerGroup2 != null)
                                    for (element in trackerResponse.cvApireffTrackerGroup2) {
                                        listGroupLv2Id.add(element.paramId)
                                        listGroupLv2Name.add(element.paramValue)
                                    }

                                val adapterGroupLv2 = ArrayAdapter(
                                    this@TrackerRiskAddActivity, android.R.layout.simple_list_item_1,
                                    listGroupLv2Name
                                )

                                spAddRiskLevel2.adapter = adapterGroupLv2

                                val spinnerPosition = adapterGroupLv2.getPosition(groupLv2e)
                                spAddRiskLevel2.setSelection(spinnerPosition)

                                spAddRiskLevel2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {
                                        groupLv2 = listGroupLv2Name[i]!!
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>) {
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
                        loading(true)
                    }
                }
            }
        }
    }

    private fun postAddTrackerRisk(
        type: String?,
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?,
    ) {
        if (type == "501") {
            trackerViewModel.setPostAddTrackerRiskSTOM(
                tt_group_level_1, tt_group_level_2, region, site_id, desc,
                pic, action_desc, report_date, target_date, tt_status, created_by,
                created_on
            )
            trackerViewModel.addRiskTrackerSTOMData.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            loading(false)
                            response.data?.let { updateTaskManualResponse ->
                                if (updateTaskManualResponse.success == true) {
                                    showDialog("Add Success", "Tracker Risk has been add.", true)
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
        } else {
            trackerViewModel.setPostAddTrackerRisk(
                tt_group_level_1, tt_group_level_2, region, site_id, desc,
                pic, action_desc, report_date, target_date, tt_status, created_by,
                created_on
            )
            trackerViewModel.addRiskTrackerData.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            loading(false)
                            response.data?.let { updateTaskManualResponse ->
                                if (updateTaskManualResponse.success == true) {
                                    showDialog("Add Success", "Tracker Risk has been add.", true)
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
    }

    private fun postEditTrackerRisk(
        type: String?,
        key: String?,
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        user_complaint: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?
    ) {
        if (type == "501") {
            trackerViewModel.setPostEditTrackerRiskSTOM(
                key, site_id, desc, pic, action_desc, target_date, resolve_date, action_taken,
                root_cause_desc, tt_status, userID, sdfToBackend.format(cal.time)
            )
            trackerViewModel.editRiskTrackerSTOMData.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            loading(false)
                            response.data?.let { updateTrackerRiskResponse ->
                                if (updateTrackerRiskResponse.success == true) {
                                    showDialog(
                                        "Update Success.",
                                        "Tracker Risk has been update.",
                                        true
                                    )
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
        } else {
            trackerViewModel.setPostEditTrackerRisk(
                key,
                tt_group_level_1,
                tt_group_level_2,
                region,
                site_id,
                desc,
                user_complaint,
                pic,
                action_desc,
                report_date,
                target_date,
                resolve_date,
                action_taken,
                root_cause_desc,
                tt_status,
                created_by,
                created_on
            )
            trackerViewModel.editRiskTrackerData.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            loading(false)
                            response.data?.let { updateTrackerRiskResponse ->
                                if (updateTrackerRiskResponse.success == true) {
                                    showDialog(
                                        "Update Success.",
                                        "Tracker Risk has been update.",
                                        true
                                    )
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
    }

    private fun postUpdateTrackerRiskGH(
        key: String?, close_date: String?, action_taken: String?, root_cause_desc: String?,
        tt_status: String?
    ) {
        trackerViewModel.setPostUpdateTrackerRiskGH(
            key, close_date, action_taken, root_cause_desc, tt_status
        )
        trackerViewModel.updateRiskTrackerGHData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { updateTrackerRiskResponse ->
                            if (updateTrackerRiskResponse.success == true) {
                                showDialog("Update Success.", "Tracker Risk has been update.", true)
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

//    private fun showDialogSiteID(listSiteID: MutableList<String?>) {
//        val dialog = Dialog(this)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.pop_up_spinner_search)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//
//        val editText = dialog.findViewById(R.id.etSiteID) as EditText
//        val listView = dialog.findViewById(R.id.lvSiteID) as ListView
//        val btnClose = dialog.findViewById(R.id.btnClose) as ImageView
//
//        dialog.show()
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listSiteID)
//
//        listView.adapter = adapter
//        editText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                adapter.filter.filter(s)
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            tvAddRiskSiteID.text = adapter.getItem(position)
//            dialog.dismiss()
//        }
//
//        btnClose.setOnClickListener {
//            dialog.dismiss()
//        }
//    }
}