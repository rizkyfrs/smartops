package com.smartfren.smartops.ui.workordermanagement

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.ttwo.TTWOViewModel
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.incident_workorder_update.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WorkorderUpdateActivity : BaseActivity() {
    private var rcaLv1: String? = ""
    private var rcaLv2: String? = ""
    private var rcaLv3: String? = ""
    private var woID: String? = null
    private var deptTimeWO: String? = null
    private var arriveTimeWO: String? = null
    private var tsTimeWO: String? = null
    private var tsRemarkWO: String? = null
    private var rcaLv1Item: ArrayList<OptionModel>? = null
    private var rcaLv2Item: ArrayList<OptionModel>? = null
    private var rcaLv3Item: ArrayList<OptionModel>? = null
    private val mWOViewModel: WorkorderViewModel by viewModels()
    private val mTTWOViewModel: TTWOViewModel by viewModels()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var location: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.incident_workorder_update)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupUI() {
        rcaLv1Item = ArrayList()
        rcaLv2Item = ArrayList()
        rcaLv3Item = ArrayList()

        getLastLocation()

        intent.getStringExtra("woID")?.let {
            woID = it
        }

        intent.getStringExtra("deptTimeWO")?.let {
            deptTimeWO = parseDateToBackendTimeStamp(it)
        }

        intent.getStringExtra("arriveTimeWO")?.let {
            arriveTimeWO = parseDateToBackendTimeStamp(it)
        }

        if (intent.getStringExtra("tsTimeWO").isNullOrEmpty()) {
            tsTimeWO = setCurrentTime()
        } else {
            tsTimeWO = intent.getStringExtra("tsTimeWO")
        }

        if (intent.getStringExtra("tsRemarkWO").isNullOrEmpty()) {
            tsRemarkWO = "-"
        } else {
            tsRemarkWO = intent.getStringExtra("tsRemarkWO")
        }

        getTrackerRiskGroupLv1("cv_apireff_rcalev1", "600", "", "")

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSaveRestorationWO.setOnClickListener {
            postRestoration(
                woID,
                "RESTORED",
                deptTimeWO,
                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                arriveTimeWO,
                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                location,
                tsTimeWO,
                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                tsRemarkWO,
                rcaLv1,
                rcaLv2,
                rcaLv3,
                etAddActionDetailWO.text.toString(),
                getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())
            )
        }
    }

    private fun getTrackerRiskGroupLv1(key: String?, recperpage: String?, rca1: String?, rca2: String?) {
        mTTWOViewModel.setListTTWORCALv1(key, recperpage, rca1, rca2)
        mTTWOViewModel.listTTWORCALv1.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ttwoResponse ->
                            if (ttwoResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (ttwoResponse.cvApireffRca != null)
                                            for (element in ttwoResponse.cvApireffRca) {
                                                element.rcaLev1?.let { add(it) }
                                                val datas = OptionModel(
                                                    element.rcaLev1ID,
                                                    element.rcaLev1
                                                )
                                                rcaLv1Item?.add(datas)
                                            }
                                    }
                                }
//                                for (element in ttwoResponse.cvApireffRca!!) {
//                                    listRCALv1.add(element.rcaLev1)
//                                }
//
//                                val dist = listRCALv1.toSet().toList()

                                val adapterGroupLv1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                spRCALev1WO.adapter = adapterGroupLv1

//                                intent.getStringExtra("ttwoRCA1").let {
//                                    val spinnerPosition = adapterGroupLv1.getPosition(it)
//                                    spRCALev1WO.setSelection(spinnerPosition)
//                                }

                                spRCALev1WO.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
//                                            getTrackerRiskGroupLv2(listGroupLv1Id[i]!!)
//                                            groupLv1 = listGroupLv1Name[i]!!
//                                        listRCALv2.clear()
//                                        listRCALv3.clear()
                                        if (rcaLv1Item != null) {
                                            val optionDatas: OptionModel = rcaLv1Item!![i]
                                            rcaLv1 = optionDatas.id
                                            getTrackerRiskGroupLv2("cv_apireff_rcalev2", "600", optionDatas.id, "")
                                        }
//                                        spinnerLv3()
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

    private fun getTrackerRiskGroupLv2(key: String?, recperpage: String?, rca1: String?, rca2: String?) {
        rcaLv2Item?.clear()
        mTTWOViewModel.setListTTWORCALv2(key, recperpage, rca1, rca2)
        mTTWOViewModel.listTTWORCALv2.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ttwoResponse ->
                            if (ttwoResponse.success == true) {
//                                listRCALv2.add("--Select RCA Lv 2--")
                                val itemName = object : ArrayList<String>() {
                                    init {
//                                        val datas = OptionModel("0", "--Select RCA Lv 2--")
                                        if (ttwoResponse.cvApireffRcalev2 != null)
                                            for (element in ttwoResponse.cvApireffRcalev2) {
                                                element.rcaLev2?.let { add(it) }
                                                val datas = OptionModel(
                                                    element.rcaLev2ID,
                                                    element.rcaLev2
                                                )
                                                rcaLv2Item?.add(datas)
                                            }
//                                        rcaLv2Item?.add(datas)
                                    }
                                }

                                val adapterGroupLv2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                spRCALev2WO.adapter = adapterGroupLv2

//                                intent.getStringExtra("ttwoRCA2").let {
//                                    val spinnerPosition = adapterGroupLv2.getPosition(it)
//                                    spRCALev2WO.setSelection(spinnerPosition)
//                                }

                                spRCALev2WO.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        val optionDatas: OptionModel = rcaLv2Item!![i]
                                        rcaLv2 = optionDatas.id
                                        getTrackerRiskGroupLv3("cv_apireff_rcalev3", "600", "", optionDatas.id)
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

    private fun getTrackerRiskGroupLv3(key: String?, recperpage: String?, rca1: String?, rca2: String?) {
        rcaLv3Item?.clear()
        mTTWOViewModel.setListTTWORCALv3(key, recperpage, rca1, rca2)
        mTTWOViewModel.listTTWORCALv3.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ttwoResponse ->
                            if (ttwoResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (ttwoResponse.cvApireffRcalev3 != null)
                                            for (element in ttwoResponse.cvApireffRcalev3) {
                                                element.rcaLev3?.let { add(it) }
                                                val datas = OptionModel(
                                                    element.rcaLev3ID,
                                                    element.rcaLev3
                                                )
                                                rcaLv3Item?.add(datas)
                                            }
                                    }
                                }

                                val adapterGroupLv3 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                spRCALev3WO.adapter = adapterGroupLv3

//                                intent.getStringExtra("ttwoRCA3").let {
//                                    val spinnerPosition = adapterGroupLv3.getPosition(it)
//                                    spRCALev3WO.setSelection(spinnerPosition)
//                                }

                                spRCALev3WO.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        if (rcaLv3Item != null) {
                                            val optionDatas: OptionModel = rcaLv3Item!![i]
                                            rcaLv3 = optionDatas.id
                                        }
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

    private fun postRestoration(
        key: String?, incwo_status: String?, incwo_departure_time: String?, incwo_departure_by: String?, incwo_arrive_time: String?,
        incwo_arrive_by: String?, incwo_onsite_coordinate: String?, incwo_troubleshoot_time: String?, incwo_troubleshoot_by: String?,
        incwo_troubleshoot_action: String?, incwo_rca1: String?, incwo_rca2: String?, incwo_rca3: String?, incwo_action: String?, incwo_pic_team: String?
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
//                                getTrackerRiskDetail(woID)
                                showDialog("", "Incident Work Order has been update.", true)
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
        fusedLocationClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = "${task.result!!.latitude}, ${task.result!!.longitude}"
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }
}