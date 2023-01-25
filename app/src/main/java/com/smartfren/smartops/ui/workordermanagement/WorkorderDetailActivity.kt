package com.smartfren.smartops.ui.workordermanagement

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.sitemanagementinfo.adapter.SiteManagementInfoDetailAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.incident_workorder_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WorkorderDetailActivity : BaseActivity() {
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private var actionOpt: ArrayList<OptionModel>? = null
    private var mAdapter: SiteManagementInfoDetailAdapter? = null
    private var woID: String? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var location: String? = null
    private var token: String? = null
    private var parentUserID: String? = null
    private var levelUserID: String? = null
    private var listPICId: MutableList<String?> = mutableListOf()
    private var listPICName: MutableList<String?> = mutableListOf()
    private val mWOViewModel: WorkorderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.incident_workorder_detail)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        clearData()

        intent.getStringExtra("woID")?.let {
            woID = it
        }

        getTrackerRiskDetail(woID)
        getLastLocation()
    }

    private fun setupUI() {
        actionOpt = ArrayList()
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        levelUserID = MainApp.instance.sharedPreferences?.getString("levelUserID", "")
        parentUserID = MainApp.instance.sharedPreferences?.getString("parentUserID", "")

        if (levelUserID == "34" || levelUserID == "35") {
            btnUpdateWO.show()
        } else {
            btnUpdateWO.hide()
            btnRestoration.hide()
        }

        recycleView.show()

        val param = recycleView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, 0, 0)
        recycleView.layoutParams = param

        btnBack.setOnClickListener {
            onBackPressed()
        }

        cvDepartArrive.hide()
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = SiteManagementInfoDetailAdapter(this)
        recyclerview.adapter = mAdapter
    }

    private fun getTrackerRiskDetail(key: String?) {
        mWOViewModel.setViewWO(key)
        mWOViewModel.viewWO.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { woResponse ->
                            if (woResponse.success == true) {
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Site ID",
                                        if (woResponse.tbIncidentDetailWorkorder?.incSiteId != null) woResponse.tbIncidentDetailWorkorder?.incSiteId else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Site Name",
                                        if (woResponse.tbIncidentDetailWorkorder?.incSiteName != null) woResponse.tbIncidentDetailWorkorder?.incSiteName else "-"
                                    )
                                )
                                actionOpt?.add(OptionModel("Incident SRS TT", woResponse.tbIncidentDetailWorkorder?.incwoSrsTt))
                                actionOpt?.add(OptionModel("Incident WO ID", woResponse.tbIncidentDetailWorkorder?.incwoWoid))
                                actionOpt?.add(OptionModel("Incident Start Time", woResponse.tbIncidentDetailWorkorder?.incwoStarttime))
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident End Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoEndtime != null) woResponse.tbIncidentDetailWorkorder?.incwoEndtime else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Duration",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoDuration != null) woResponse.tbIncidentDetailWorkorder?.incwoDuration else "-"
                                    )
                                )
                                actionOpt?.add(OptionModel("Incident Status",
                                    if (woResponse.tbIncidentDetailWorkorder?.incwoStatus != null) {
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoStatus == "RESOLVED" ||
                                            woResponse.tbIncidentDetailWorkorder?.incwoStatus == "CLOSED") {
                                            woResponse.tbIncidentDetailWorkorder?.incwoStatus + " (Alarm Cleared)"
                                        } else {
                                            woResponse.tbIncidentDetailWorkorder?.incwoStatus + " (Alarm Not Cleared)"
                                        }
                                    } else {
                                        "-"
                                    }))
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Action",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoAction != null) woResponse.tbIncidentDetailWorkorder?.incwoAction else "-"
                                    )
                                )
                                actionOpt?.add(OptionModel("Incident Info", woResponse.tbIncidentDetailWorkorder?.incwoInfo))
                                actionOpt?.add(OptionModel("Incident PIC Team", woResponse.tbIncidentDetailWorkorder?.incwoPicTeam))
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Accept Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoAcceptTime != null) woResponse.tbIncidentDetailWorkorder?.incwoAcceptTime else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Suggestion",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoSuggestion != null) woResponse.tbIncidentDetailWorkorder?.incwoSuggestion else "-"
                                    )
                                )
//                                actionOpt?.add(
//                                    OptionModel(
//                                        "Incident Accept By",
//                                        if (woResponse.tbIncidentDetailWorkorder?.incwoAcceptBy != null) woResponse.tbIncidentDetailWorkorder?.incwoAcceptBy else "-"
//                                    )
//                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Departure Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime != null) woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime else "-"
                                    )
                                )
//                                actionOpt?.add(
//                                    OptionModel(
//                                        "Incident Departure By",
//                                        if (woResponse.tbIncidentDetailWorkorder?.incwoDepartureBy != null) woResponse.tbIncidentDetailWorkorder?.incwoDepartureBy else "-"
//                                    )
//                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Arrive Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoArriveTime != null) woResponse.tbIncidentDetailWorkorder?.incwoArriveTime else "-"
                                    )
                                )
//                                actionOpt?.add(
//                                    OptionModel(
//                                        "Incident Arrive By",
//                                        if (woResponse.tbIncidentDetailWorkorder?.incwoArriveBy != null) woResponse.tbIncidentDetailWorkorder?.incwoArriveBy else "-"
//                                    )
//                                )
//                                actionOpt?.add(
//                                    OptionModel(
//                                        "Incident Coordinate",
//                                        if (woResponse.tbIncidentDetailWorkorder?.incwoOnsiteCoordinate != null) woResponse.tbIncidentDetailWorkorder?.incwoOnsiteCoordinate else "-"
//                                    )
//                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Troubleshoot Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootTime != null) woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootTime else "-"
                                    )
                                )
//                                actionOpt?.add(
//                                    OptionModel(
//                                        "Incident Troubleshoot By",
//                                        if (woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootBy != null) woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootBy else "-"
//                                    )
//                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Troubleshoot Action",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootAction != null) woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootAction else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Resolved Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoResolvedTime != null) woResponse.tbIncidentDetailWorkorder?.incwoResolvedTime else "-"
                                    )
                                )
//                                actionOpt?.add(
//                                    OptionModel(
//                                        "Incident Resolved By",
//                                        if (woResponse.tbIncidentDetailWorkorder?.incwoResolvedBy != null) woResponse.tbIncidentDetailWorkorder?.incwoResolvedBy else "-"
//                                    )
//                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident RCA 1",
                                        if (!woResponse.tbIncidentDetailWorkorder?.incwoRca1.isNullOrEmpty()) woResponse.tbIncidentDetailWorkorder?.incwoRca1 else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident RCA 2",
                                        if (!woResponse.tbIncidentDetailWorkorder?.incwoRca2.isNullOrEmpty()) woResponse.tbIncidentDetailWorkorder?.incwoRca2 else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident RCA 3",
                                        if (!woResponse.tbIncidentDetailWorkorder?.incwoRca3.isNullOrEmpty()) woResponse.tbIncidentDetailWorkorder?.incwoRca3 else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Update Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoUpdatedTime != null) woResponse.tbIncidentDetailWorkorder?.incwoUpdatedTime else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Category",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoCategory != null) woResponse.tbIncidentDetailWorkorder?.incwoCategory else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Sub Team",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoSubTeam != null) woResponse.tbIncidentDetailWorkorder?.incwoSubTeam else "-"
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Region",
                                        if (woResponse.tbIncidentDetailWorkorder?.incwoRegion != null) woResponse.tbIncidentDetailWorkorder?.incwoRegion else "-"
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Incident Occurance Time",
                                        if (woResponse.tbIncidentDetailWorkorder?.incOccurenceTime != null) woResponse.tbIncidentDetailWorkorder?.incOccurenceTime else "-"
                                    )
                                )

                                actionOpt?.let { mAdapter?.updateList(it) }

                                when (woResponse.tbIncidentDetailWorkorder?.incwoStatus) {
                                    "NEW" -> {
                                        btnUpdateWO.text = "Departure"
                                        btnUpdateWO.setOnClickListener {
                                            postUpdateIncidentWO(
                                                woID,
                                                "DEPART",
                                                setCurrentTime(),
                                                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())
                                            )
                                        }

                                        if (levelUserID == "34") {
                                            btnRestoration.show()
                                            btnRestoration.text = "Reassign"
                                            btnUpdateWO.hide()
                                            getListTeam(token)
                                        }
                                    }
                                    "DEPART" -> {
                                        btnUpdateWO.text = "Arrive"
                                        btnUpdateWO.setOnClickListener {
                                            getLastLocation()
                                            postUpdateIncidentWO(
                                                woID,
                                                "ARRIVE",
                                                woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime,
                                                woResponse.tbIncidentDetailWorkorder?.incwoDepartureBy,
                                                setCurrentTime(),
                                                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                                                location,
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())
                                            )
                                        }
                                    }
                                    "ARRIVE" -> {
                                        btnUpdateWO.text = "Troubleshoot"

                                        if (levelUserID == "35") {
                                            btnUpdateWO.show()
                                            btnRestoration.show()
                                        }

                                        btnUpdateWO.setOnClickListener {
                                            clDialogTroubleshootWO.show()
                                        }

                                        btnSaveTroubleshootWO.setOnClickListener {
                                            if (etAddRemarkTroubleshotWO.text?.isEmpty()!!) {
                                                toast(this, "Please enter remark!")
                                            } else {
                                                postUpdateIncidentWO(
                                                    woID,
                                                    "TROUBLESHOOT",
                                                    woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime,
                                                    woResponse.tbIncidentDetailWorkorder?.incwoDepartureBy,
                                                    woResponse.tbIncidentDetailWorkorder?.incwoArriveTime,
                                                    woResponse.tbIncidentDetailWorkorder?.incwoArriveBy,
                                                    location,
                                                    setCurrentTime(),
                                                    getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                                                    etAddRemarkTroubleshotWO.text.toString(),
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())
                                                )
                                            }
                                        }

                                        btnRestoration.setOnClickListener {
                                            val intent = Intent(this, WorkorderUpdateActivity::class.java)
                                            intent.putExtra("woID", woID)
                                            intent.putExtra("deptTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime)
                                            intent.putExtra("arriveTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoArriveTime)
                                            intent.putExtra("tsTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootTime)
                                            intent.putExtra("tsRemarkWO", woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootAction)
                                            startActivity(intent)
                                        }

                                        btnCloseTroubleshootWO.setOnClickListener {
                                            clDialogTroubleshootWO.hide()
                                        }

                                    }
                                    "TROUBLESHOOT" -> {
                                        if (levelUserID == "35") {
                                            btnUpdateWO.hide()
                                            btnRestoration.show()
                                        }

                                        btnRestoration.setOnClickListener {
                                            val intent = Intent(this, WorkorderUpdateActivity::class.java)
                                            intent.putExtra("woID", woID)
                                            intent.putExtra("deptTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime)
                                            intent.putExtra("arriveTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoArriveTime)
                                            intent.putExtra("tsTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootTime)
                                            intent.putExtra("tsRemarkWO", woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootAction)
                                            startActivity(intent)
                                        }
                                    }
                                    "RESOLVED" -> {
                                        if (levelUserID == "35") {
                                            btnUpdateWO.hide()
                                            btnRestoration.show()
                                        }

                                        btnRestoration.setOnClickListener {
                                            val intent = Intent(this, WorkorderUpdateActivity::class.java)
                                            intent.putExtra("woID", woID)
                                            intent.putExtra("deptTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoDepartureTime)
                                            intent.putExtra("arriveTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoArriveTime)
                                            intent.putExtra("tsTimeWO", woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootTime)
                                            intent.putExtra("tsRemarkWO", woResponse.tbIncidentDetailWorkorder?.incwoTroubleshootAction)
                                            startActivity(intent)
                                        }
                                    }
                                    "RESTORED" -> {
//                                        if (levelUserID == "35") {
                                            btnUpdateWO.hide()
                                            btnRestoration.hide()
//                                        }
                                    }
                                    "CLOSED" -> {
//                                        if (levelUserID == "35") {
                                            btnUpdateWO.hide()
                                            btnRestoration.hide()
//                                        }
                                    }
                                }
//                                getListTeamSchedule(token)
//                                stopService(Intent(this, BackgroundSoundService::class.java))
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

    private fun postUpdateIncidentWO(
        key: String?,
        incwo_status: String?,
        incwo_departure_time: String?,
        incwo_departure_by: String?,
        incwo_arrive_time: String?,
        incwo_arrive_by: String?,
        incwo_onsite_coordinate: String?,
        incwo_troubleshoot_time: String?,
        incwo_troubleshoot_by: String?,
        incwo_troubleshoot_action: String?,
        incwo_rca1: String?,
        incwo_rca2: String?,
        incwo_rca3: String?,
        incwo_action: String?,
        incwo_pic_team: String?
    ) {
        mWOViewModel.setEditWO(
            key,
            incwo_status,
            incwo_departure_time,
            incwo_departure_by,
            incwo_arrive_time,
            incwo_arrive_by,
            incwo_onsite_coordinate,
            incwo_troubleshoot_time,
            incwo_troubleshoot_by,
            incwo_troubleshoot_action,
            incwo_rca1,
            incwo_rca2,
            incwo_rca3,
            incwo_action,
            incwo_pic_team
        )
        mWOViewModel.editWO.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { checkinResponse ->
                            if (checkinResponse.success == true) {
                                clearData()
                                if (incwo_status == "TROUBLESHOOT") {
                                    clDialogTroubleshootWO.hide()
                                    showDialog("", "Incident Work Order has been update.", false)
                                } else {
                                    showDialog("", "Incident Work Order has been update.", false)
                                }
                                getTrackerRiskDetail(woID)
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

    private fun clearData() {
        actionOpt?.clear()
        mAdapter?.clearList()
    }

    private fun getLastLocation() {
        fusedLocationClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = "${task.result!!.latitude}, ${task.result!!.longitude}"
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun getListTeam(token: String?) {
        dailyActivityViewModel.getListTeamSchedule(token)
        dailyActivityViewModel.listTeamSchedule.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.getReport()?.success == true) {
                                listPICId.clear()
                                listPICName.clear()

                                listPICId.add("0")
                                listPICName.add(getString(R.string.select_FLM_PIC))

                                for (data in scheduleResponse.getReport()?.team!!) {
                                    listPICId.add(data.userID)
                                    listPICName.add(data.userFullName)
                                }

                                btnRestoration.show()
                                btnRestoration.setOnClickListener {
                                    dialogTrackerRisk()
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

    private fun dialogTrackerRisk() {
        val dialog = Dialog(this)
        var userFlmID: String? = null

        dialog.setContentView(R.layout.pop_up_ttwo_update)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val btnReassign: Button = dialog.findViewById<View>(R.id.btnUpdateTTWO) as Button
        val spLevel: Spinner = dialog.findViewById<View>(R.id.spUpdateRCALevel1) as Spinner
        val spParent: Spinner = dialog.findViewById<View>(R.id.spUpdateRCALevel2) as Spinner
        val tvTitlePopupTTWO: TextView = dialog.findViewById<View>(R.id.tvTitlePopupTTWO) as TextView
        val tvTitleLevel: TextView = dialog.findViewById<View>(R.id.tvTitleUpdateRCALevel1) as TextView
        val tvTitleParent: TextView = dialog.findViewById<View>(R.id.tvTitleUpdateRCA2) as TextView
        val tvTitleUpdateRCA3: TextView = dialog.findViewById<View>(R.id.tvTitleUpdateRCA3) as TextView
        val clUpdateRCALevel3: ConstraintLayout = dialog.findViewById<View>(R.id.clUpdateRCALevel3) as ConstraintLayout
        val btnClose: ImageView = dialog.findViewById<View>(R.id.btnClose) as ImageView

        tvTitleUpdateRCA3.hide()
        clUpdateRCALevel3.hide()
        tvTitleUpdateRCA3.hide()
        tvTitleParent.hide()
        spParent.hide()

        tvTitleLevel.text = "FLM PIC"
        tvTitlePopupTTWO.text = "Reassign FLM PIC"
        btnReassign.text = "Reassign"


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listPICName!!)

        spLevel.adapter = adapter

        spLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
//                if (i != 0) {
                    userFlmID = listPICId[i]
//                }

                Log.e("level", "" + listPICId[i])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnReassign.setOnClickListener {
            if (userFlmID != "0") {
                postUpdateIncidentWO(
                    woID,
                    "NEW",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    userFlmID
                )
                dialog.dismiss()
            } else {
                toast(applicationContext, "Please select FLM PIC!")
            }
        }

        dialog.show()
    }

//    override fun onPause() {
//        super.onPause()
//        loading(false)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        loading(false)
//    }

}