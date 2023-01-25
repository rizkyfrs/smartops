package com.smartfren.smartops.ui.ttwo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.showDialog
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.ttwo_add.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TTWOAddActivity : BaseActivity() {
    private var listRCALv1: MutableList<String?> = mutableListOf()
    private var listRCALv2: MutableList<String?> = mutableListOf()
    private var listRCALv3: MutableList<String?> = mutableListOf()
    private var rcaLv1: String? = ""
    private var rcaLv2: String? = ""
    private var rcaLv3: String? = ""
    private var ttwoRCAID: String? = ""
    private var ttwoTimeDuration: String? = ""
    private var ttwoSitePICID: String? = ""
    private var ttwoSiteID: String? = ""
    private var rcaLv1Item: ArrayList<OptionModel>? = null
    private var rcaLv2Item: ArrayList<OptionModel>? = null
    private var rcaLv3Item: ArrayList<OptionModel>? = null
    private val mTTWOViewModel: TTWOViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ttwo_add)
        setupUI()
    }

    private fun setupUI() {
        ttwoRCAID = intent.getStringExtra("ttwoRCAID")
        ttwoTimeDuration = intent.getStringExtra("ttwoTimeDuration")
        ttwoSitePICID = intent.getStringExtra("ttwoSitePICID")
        ttwoSiteID = intent.getStringExtra("ttwoSiteID")
        rcaLv1Item = ArrayList()
        rcaLv2Item = ArrayList()
        rcaLv3Item = ArrayList()

        getTrackerRiskGroupLv1("cv_apireff_rcalev1", "600", "", "")

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSaveAddTask.text = getString(R.string.update_rca)
        btnSaveAddTask.setOnClickListener {
            postUpdateTTWORCA(
                ttwoRCAID, rcaLv1, rcaLv2, rcaLv3, ttwoTimeDuration,
                ttwoSitePICID, ttwoSiteID
            )

            Log.e("Rca 1", "$rcaLv1")
            Log.e("Rca 2", "$rcaLv2")
            Log.e("Rca 3", "$rcaLv3")
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

                                spAddRCALevel1.adapter = adapterGroupLv1

                                intent.getStringExtra("ttwoRCA1").let {
                                    val spinnerPosition = adapterGroupLv1.getPosition(it)
                                    spAddRCALevel1.setSelection(spinnerPosition)
                                }

                                spAddRCALevel1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
//                                            getTrackerRiskGroupLv2(listGroupLv1Id[i]!!)
//                                            groupLv1 = listGroupLv1Name[i]!!
//                                        listRCALv2.clear()
//                                        listRCALv3.clear()
                                        if (rcaLv1Item != null) {
                                            val optionDatas: OptionModel = rcaLv1Item!![i]
                                            rcaLv1 = optionDatas.type
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
//                                if (ttwoResponse.cvApireffRca != null)
//                                    for (element in ttwoResponse.cvApireffRcalev2!!) {
//                                        listRCALv2.add(element.rcaLev2)
//                                    }
//
//                                val dist = listRCALv2.distinct().toList()

                                val adapterGroupLv2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                spAddRCALevel2.adapter = adapterGroupLv2

                                intent.getStringExtra("ttwoRCA2").let {
                                    val spinnerPosition = adapterGroupLv2.getPosition(it)
                                    spAddRCALevel2.setSelection(spinnerPosition)
                                }

                                spAddRCALevel2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
//                                            getTrackerRiskGroupLv2(listGroupLv1Id[i]!!)
//                                            groupLv1 = listGroupLv1Name[i]!!
//                                            Log.e("RCALv2", "" + dist[i])
                                        val optionDatas: OptionModel = rcaLv2Item!![i]
                                        rcaLv2 = optionDatas.type
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
//                                listRCALv3.add("--Select RCA Lv 3--")
//
//                                if (ttwoResponse.cvApireffRca != null)
//                                    for (element in ttwoResponse.cvApireffRca) {
//                                        listRCALv3.add(element.rcaLev3)
//                                    }

                                val itemName = object : ArrayList<String>() {
                                    init {
//                                        val data = OptionModel("0", "--Select RCA Lv 3--")
                                        if (ttwoResponse.cvApireffRcalev3 != null)
                                            for (element in ttwoResponse.cvApireffRcalev3) {
                                                element.rcaLev3?.let { add(it) }
                                                val datas = OptionModel(
                                                    element.rcaLev3ID,
                                                    element.rcaLev3
                                                )
                                                rcaLv3Item?.add(datas)
                                            }
//                                        rcaLv3Item?.add(data)
                                    }
                                }

                                val adapterGroupLv3 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                spAddRCALevel3.adapter = adapterGroupLv3

                                intent.getStringExtra("ttwoRCA3").let {
                                    val spinnerPosition = adapterGroupLv3.getPosition(it)
                                    spAddRCALevel3.setSelection(spinnerPosition)
                                }

                                spAddRCALevel3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
//                                            getTrackerRiskGroupLv2(listGroupLv1Id[i]!!)
//                                            groupLv1 = listGroupLv1Name[i]!!
//                                            Log.e("RCALv2", "" + dist[i])
                                        if (rcaLv3Item != null) {
                                            val optionDatas: OptionModel = rcaLv3Item!![i]
                                            rcaLv3 = optionDatas.type
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

    private fun postUpdateTTWORCA(
        key: String?, u_rca_1: String?, u_rca_2: String?, u_rca_3: String?, time_duration: String?, site_pic_id: String?,
        site_id: String?
    ) {
        mTTWOViewModel.setPostTTWORCA(
            key, u_rca_1, u_rca_2, u_rca_3, time_duration, site_pic_id, site_id
        )
        mTTWOViewModel.updateTTWORCA.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { updateTTWORCAResponse ->
                            if (updateTTWORCAResponse.success == true) {
                                showDialog("Update Success.", "TT/WO RCA has been update.", true)
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