package com.smartfren.smartops.ui.tracker.risk

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.sitemanagementinfo.adapter.SiteManagementInfoDetailAdapter
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_detail_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerRiskDetailActivity : BaseActivity() {
    private var riskID: String? = null
    private var type: String? = ""
    private var actionOpt: ArrayList<OptionModel>? = null
    private val trackerViewModel: TrackerViewModel by viewModels()
    private var mSiteManagementInfoDetailAdapter: SiteManagementInfoDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_activity_detail_new)
        mapView.onCreate(savedInstanceState)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        clearData()

        if (intent.getStringExtra("updateGH") == "yes") {
            getTrackerRiskGHDetail()
        } else if (intent.getStringExtra("trackerManager") == "yes") {
            riskID?.let {
                getTrackerManagerDetail(it)
            }
        } else {
            riskID?.let {
                if (type == "501") {
                    getTrackerRiskSTOMDetail(it)
                } else {
                    getTrackerRiskDetail(it)
                }
            }
        }
    }

    private fun setupUI() {
        if (intent.getStringExtra("trackerManager") == "yes") {
            btnUpdateTask.hide()
            tvTitleAppBar.text = getString(R.string.tracker_manager_detail)
        } else {
            btnUpdateTask.show()
            tvTitleAppBar.text = getString(R.string.trracker_risk_detail)
        }

        riskID = intent.getStringExtra("riskID")

        actionOpt = ArrayList()
        recycleView.show()
        clInfo.hide()
        mapView.hide()
        clActivityText.hide()

        type = MainApp.instance.sharedPreferences?.getString("levelID", "")

        val param = recycleView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, 0, 0)
        recycleView.layoutParams = param

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mSiteManagementInfoDetailAdapter = SiteManagementInfoDetailAdapter(this)
        recyclerview.adapter = mSiteManagementInfoDetailAdapter
        mSiteManagementInfoDetailAdapter?.clearList()
    }

    private fun getTrackerRiskDetail(key: String) {
        trackerViewModel.setTrackerRiskView(key)
        trackerViewModel.trackerRiskViewData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                actionOpt?.add(OptionModel("Risk ID", trackerResponse.cvRegrtrackerOps?.rttNum))
                                actionOpt?.add(OptionModel("Tracker Group Level 1", trackerResponse.cvRegrtrackerOps?.ttGroupLevel1))
                                actionOpt?.add(OptionModel("Tracker Group Level 2", trackerResponse.cvRegrtrackerOps?.ttGroupLevel2))
                                actionOpt?.add(OptionModel("Site ID",trackerResponse.cvRegrtrackerOps?.siteId))
                                actionOpt?.add(OptionModel("Description", trackerResponse.cvRegrtrackerOps?.desc))
                                actionOpt?.add(OptionModel("User Complaint", trackerResponse.cvRegrtrackerOps?.userComplaint))
                                actionOpt?.add(OptionModel("PIC", trackerResponse.cvRegrtrackerOps?.pic))
                                actionOpt?.add(OptionModel("Action Description",trackerResponse.cvRegrtrackerOps?.actionDesc))
                                actionOpt?.add(OptionModel("Report Date", trackerResponse.cvRegrtrackerOps?.reportDate))
                                actionOpt?.add(OptionModel("Target Date", trackerResponse.cvRegrtrackerOps?.targetDate))
                                actionOpt?.add(OptionModel("Resolve Date", trackerResponse.cvRegrtrackerOps?.resolveDate))
                                actionOpt?.add(OptionModel("Close Date", trackerResponse.cvRegrtrackerOps?.closeDate))
                                actionOpt?.add(OptionModel("Action Taken", trackerResponse.cvRegrtrackerOps?.actionTaken))
                                actionOpt?.add(OptionModel("Root Cause Description", trackerResponse.cvRegrtrackerOps?.rootCauseDesc))
                                actionOpt?.add(OptionModel("Create By", trackerResponse.cvRegrtrackerOps?.createdBy))
                                actionOpt?.add(OptionModel("Create On", trackerResponse.cvRegrtrackerOps?.createdOn))
                                actionOpt?.add(OptionModel("Update By", trackerResponse.cvRegrtrackerOps?.updatedBy))
                                actionOpt?.add(OptionModel("Update On", trackerResponse.cvRegrtrackerOps?.updatedOn))
                                actionOpt?.add(OptionModel("Status", trackerResponse.cvRegrtrackerOps?.ttStatus))

                                actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }

                                btnUpdateTask.setOnClickListener {
                                    val intent = Intent(this, TrackerRiskAddActivity::class.java)
                                    intent.putExtra("update", "yes")
                                    intent.putExtra("riskID", trackerResponse.cvRegrtrackerOps?.rttNum)
                                    intent.putExtra("riskGroupLv1", trackerResponse.cvRegrtrackerOps?.ttGroupLevel1)
                                    intent.putExtra("riskGroupLv2", trackerResponse.cvRegrtrackerOps?.ttGroupLevel2)
                                    intent.putExtra("riskSiteID", trackerResponse.cvRegrtrackerOps?.siteId)
                                    intent.putExtra("riskDesc", trackerResponse.cvRegrtrackerOps?.desc)
                                    intent.putExtra("riskUserComp", trackerResponse.cvRegrtrackerOps?.userComplaint)
                                    intent.putExtra("riskPic", trackerResponse.cvRegrtrackerOps?.pic)
                                    intent.putExtra("riskActDesc", trackerResponse.cvRegrtrackerOps?.actionDesc)
                                    intent.putExtra("riskCloseDate", trackerResponse.cvRegrtrackerOps?.closeDate)
                                    intent.putExtra("riskReportDate", trackerResponse.cvRegrtrackerOps?.reportDate)
                                    intent.putExtra("riskTargetDate", trackerResponse.cvRegrtrackerOps?.targetDate)
                                    intent.putExtra("riskResolveDate", trackerResponse.cvRegrtrackerOps?.resolveDate)
                                    intent.putExtra("riskActTaken", trackerResponse.cvRegrtrackerOps?.actionTaken)
                                    intent.putExtra("riskRootCause", trackerResponse.cvRegrtrackerOps?.rootCauseDesc)
                                    intent.putExtra("riskStatus", trackerResponse.cvRegrtrackerOps?.ttStatus)
                                    intent.putExtra("riskCreatedBy", trackerResponse.cvRegrtrackerOps?.createdBy)
                                    intent.putExtra("riskCreatedOn", trackerResponse.cvRegrtrackerOps?.createdOn)
                                    intent.putExtra("riskUpdateBy", trackerResponse.cvRegrtrackerOps?.updatedBy)
                                    intent.putExtra("riskUpdateOn", trackerResponse.cvRegrtrackerOps?.updatedOn)
                                    startActivity(intent)
                                }
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

    private fun getTrackerRiskSTOMDetail(key: String) {
        trackerViewModel.setTrackerRiskSTOMView(key)
        trackerViewModel.trackerRiskSTOMViewData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                actionOpt?.add(OptionModel("Risk ID", trackerResponse.cvRegrtrackerStom?.rttNum))
                                actionOpt?.add(OptionModel("Tracker Group Level 1", trackerResponse.cvRegrtrackerStom?.ttGroupLevel1))
                                actionOpt?.add(OptionModel("Tracker Group Level 2", trackerResponse.cvRegrtrackerStom?.ttGroupLevel2))
                                actionOpt?.add(OptionModel("Site ID", trackerResponse.cvRegrtrackerStom?.siteId))
                                actionOpt?.add(OptionModel("Description", trackerResponse.cvRegrtrackerStom?.desc))
                                actionOpt?.add(OptionModel("User Complaint", trackerResponse.cvRegrtrackerStom?.userComplaint))
                                actionOpt?.add(OptionModel("PIC", trackerResponse.cvRegrtrackerStom?.pic))
                                actionOpt?.add(OptionModel("Action Description", trackerResponse.cvRegrtrackerStom?.actionDesc))
                                actionOpt?.add(OptionModel("Report Date", trackerResponse.cvRegrtrackerStom?.reportDate))
                                actionOpt?.add(OptionModel("Target Date", trackerResponse.cvRegrtrackerStom?.targetDate))
                                actionOpt?.add(OptionModel("Resolve Date", trackerResponse.cvRegrtrackerStom?.resolveDate))
                                actionOpt?.add(OptionModel("Close Date", trackerResponse.cvRegrtrackerStom?.closeDate))
                                actionOpt?.add(OptionModel("Action Taken", trackerResponse.cvRegrtrackerStom?.actionTaken))
                                actionOpt?.add(OptionModel("Root Cause Description", trackerResponse.cvRegrtrackerStom?.rootCauseDesc))
                                actionOpt?.add(OptionModel("Create By", trackerResponse.cvRegrtrackerStom?.createdBy))
                                actionOpt?.add(OptionModel("Create On", trackerResponse.cvRegrtrackerStom?.createdOn))
                                actionOpt?.add(OptionModel("Update By", trackerResponse.cvRegrtrackerStom?.updatedBy))
                                actionOpt?.add(OptionModel("Update On", trackerResponse.cvRegrtrackerStom?.updatedOn))
                                actionOpt?.add(OptionModel("Status", trackerResponse.cvRegrtrackerStom?.ttStatus))

                                actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }

                                btnUpdateTask.setOnClickListener {
                                    val intent = Intent(this, TrackerRiskAddActivity::class.java)
                                    intent.putExtra("update", "yes")
                                    intent.putExtra("riskID", trackerResponse.cvRegrtrackerStom?.rttNum)
                                    intent.putExtra("riskGroupLv1", trackerResponse.cvRegrtrackerStom?.ttGroupLevel1)
                                    intent.putExtra("riskGroupLv2", trackerResponse.cvRegrtrackerStom?.ttGroupLevel2)
                                    intent.putExtra("riskSiteID", trackerResponse.cvRegrtrackerStom?.siteId)
                                    intent.putExtra("riskDesc", trackerResponse.cvRegrtrackerStom?.desc)
                                    intent.putExtra("riskUserComp", trackerResponse.cvRegrtrackerStom?.userComplaint)
                                    intent.putExtra("riskPic", trackerResponse.cvRegrtrackerStom?.pic)
                                    intent.putExtra("riskActDesc", trackerResponse.cvRegrtrackerStom?.actionDesc)
                                    intent.putExtra("riskCloseDate", trackerResponse.cvRegrtrackerStom?.closeDate)
                                    intent.putExtra("riskReportDate", trackerResponse.cvRegrtrackerStom?.reportDate)
                                    intent.putExtra("riskTargetDate", trackerResponse.cvRegrtrackerStom?.targetDate)
                                    intent.putExtra("riskResolveDate", trackerResponse.cvRegrtrackerStom?.resolveDate)
                                    intent.putExtra("riskActTaken", trackerResponse.cvRegrtrackerStom?.actionTaken)
                                    intent.putExtra("riskRootCause", trackerResponse.cvRegrtrackerStom?.rootCauseDesc)
                                    intent.putExtra("riskStatus", trackerResponse.cvRegrtrackerStom?.ttStatus)
                                    intent.putExtra("riskCreatedBy", trackerResponse.cvRegrtrackerStom?.createdBy)
                                    intent.putExtra("riskCreatedOn", trackerResponse.cvRegrtrackerStom?.createdOn)
                                    intent.putExtra("riskUpdateBy", trackerResponse.cvRegrtrackerStom?.updatedBy)
                                    intent.putExtra("riskUpdateOn", trackerResponse.cvRegrtrackerStom?.updatedOn)
                                    startActivity(intent)
                                }
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

    private fun getTrackerManagerDetail(key: String) {
        trackerViewModel.setTrackerManagerView(key)
        trackerViewModel.trackerManagerViewData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                actionOpt?.add(OptionModel("Risk ID", trackerResponse.tbRtracker?.rttNum))
                                actionOpt?.add(OptionModel("Tracker Group Level 1", trackerResponse.tbRtracker?.ttGroupLevel1))
                                actionOpt?.add(OptionModel("Tracker Group Level 2", trackerResponse.tbRtracker?.ttGroupLevel2))
                                actionOpt?.add(OptionModel("Site ID", trackerResponse.tbRtracker?.siteId))
                                actionOpt?.add(OptionModel("Description", trackerResponse.tbRtracker?.desc))
                                actionOpt?.add(OptionModel("User Complaint", trackerResponse.tbRtracker?.userComplaint))
                                actionOpt?.add(OptionModel("PIC", trackerResponse.tbRtracker?.pic))
                                actionOpt?.add(OptionModel("Action Description", trackerResponse.tbRtracker?.actionDesc))
                                actionOpt?.add(OptionModel("Report Date", trackerResponse.tbRtracker?.reportDate))
                                actionOpt?.add(OptionModel("Target Date", trackerResponse.tbRtracker?.targetDate))
                                actionOpt?.add(OptionModel("Resolve Date", trackerResponse.tbRtracker?.resolveDate))
                                actionOpt?.add(OptionModel("Close Date", trackerResponse.tbRtracker?.closeDate))
                                actionOpt?.add(OptionModel("Action Taken", trackerResponse.tbRtracker?.actionTaken))
                                actionOpt?.add(OptionModel("Root Cause Description", trackerResponse.tbRtracker?.rootCauseDesc))
                                actionOpt?.add(OptionModel("Create By", trackerResponse.tbRtracker?.createdBy))
                                actionOpt?.add(OptionModel("Create On", trackerResponse.tbRtracker?.createdOn))
                                actionOpt?.add(OptionModel("Update By", trackerResponse.tbRtracker?.updatedBy))
                                actionOpt?.add(OptionModel("Update On", trackerResponse.tbRtracker?.updatedOn))
                                actionOpt?.add(OptionModel("Status", trackerResponse.tbRtracker?.ttStatus))

                                actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
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

    private fun getTrackerRiskGHDetail() {
        actionOpt?.add(OptionModel("Risk ID", riskID))
        actionOpt?.add(OptionModel("Tracker Group Level 1", intent.getStringExtra("riskGroupLv1")))
        actionOpt?.add(OptionModel("Tracker Group Level 2", intent.getStringExtra("riskGroupLv2")))
        actionOpt?.add(OptionModel("Region", intent.getStringExtra("riskRegion")))
        actionOpt?.add(OptionModel("Site ID", intent.getStringExtra("riskSiteID")))
        actionOpt?.add(OptionModel("User Complaint", intent.getStringExtra("riskUserComp")))
        actionOpt?.add(OptionModel("PIC", intent.getStringExtra("riskPic")))
        actionOpt?.add(OptionModel("Root Cause Description", intent.getStringExtra("riskRootCause")))
        actionOpt?.add(OptionModel("Create By", intent.getStringExtra("riskCreatedBy")))
        actionOpt?.add(OptionModel("Create On", intent.getStringExtra("riskCreatedOn")))
        actionOpt?.add(OptionModel("Update By", intent.getStringExtra("riskUpdateBy")))
        actionOpt?.add(OptionModel("Update On", intent.getStringExtra("riskUpdateOn")))
        actionOpt?.add(OptionModel("Status", intent.getStringExtra("riskStatus")))
        actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }

        btnUpdateTask.setOnClickListener {
            dialogTrackerRisk()
        }
    }

    private fun dialogTrackerRisk() {
        val dialog = Dialog(this)
        var status: String? = null
        val cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        dialog.setContentView(R.layout.pop_up_update_tracker_risk)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val btnUpdateTrackerRisk: Button = dialog.findViewById<View>(R.id.btnUpdateTrackerRisk) as Button
        val etAddDateClose: AppCompatEditText = dialog.findViewById<View>(R.id.etAddDateClose) as AppCompatEditText
        val etAddRiskActTaken: AppCompatEditText = dialog.findViewById<View>(R.id.etAddRiskActTaken) as AppCompatEditText
        val etAddRiskRootCause: AppCompatEditText = dialog.findViewById<View>(R.id.etAddRiskRootCause) as AppCompatEditText
        val spAddTrackerRiskStatus: Spinner = dialog.findViewById<View>(R.id.spAddTrackerRiskStatus) as Spinner
        val btnClose: ImageView = dialog.findViewById<View>(R.id.btnClose) as ImageView
        val btnAddDateClose: ImageView = dialog.findViewById<View>(R.id.btnAddDateClose) as ImageView

        val list_of_items = arrayOf("Close")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list_of_items)

        spAddTrackerRiskStatus.adapter = adapter

        spAddTrackerRiskStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                    status = adapterView.selectedItem.toString()
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }

        val dateSetListenerResolve = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etAddDateClose.setText(sdf.format(cal.time))
        }

        btnAddDateClose.setOnClickListener {
            DatePickerDialog(this, dateSetListenerResolve, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdateTrackerRisk.setOnClickListener {
            postUpdateTrackerRiskGH(
                riskID,
                parseDateToBackend(etAddDateClose.text.toString()),
                etAddRiskActTaken.text.toString(),
                etAddRiskRootCause.text.toString(),
                status
            )
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun postUpdateTrackerRiskGH(key: String?, close_date: String?, action_taken: String?, root_cause_desc: String?, tt_status: String?) {
        trackerViewModel.setPostUpdateTrackerRiskGH(key, close_date, action_taken, root_cause_desc, tt_status)
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

    private fun clearData() {
        actionOpt?.clear()
        mSiteManagementInfoDetailAdapter?.clearList()
    }
}