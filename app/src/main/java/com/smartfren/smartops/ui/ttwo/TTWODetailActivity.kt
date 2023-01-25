package com.smartfren.smartops.ui.ttwo

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.sitemanagementinfo.adapter.SiteManagementInfoDetailAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_detail_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TTWODetailActivity : BaseActivity() {
    private var actionOpt: ArrayList<OptionModel>? = null
    private val mTTWOViewModel: TTWOViewModel by viewModels()
    private var mAdapter: SiteManagementInfoDetailAdapter? = null

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

        intent.getStringExtra("ttwoID")?.let {
            getTrackerRiskDetail(it)
        }
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.ttworca_detail)
        actionOpt = ArrayList()
        recycleView.show()
        clInfo.hide()
        mapView.hide()
        clActivityText.hide()

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
        mAdapter = SiteManagementInfoDetailAdapter(this)
        recyclerview.adapter = mAdapter
        mAdapter?.clearList()
    }

    private fun getTrackerRiskDetail(key: String) {
        mTTWOViewModel.setViewTTWO(key)
        mTTWOViewModel.viewTTWO.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ttwoResponse ->
                            if (ttwoResponse.success == true) {
                                actionOpt?.add(OptionModel("TTWO ID", ttwoResponse.cvFsttwohistRCA?.histNum))
                                actionOpt?.add(OptionModel("Region", ttwoResponse.cvFsttwohistRCA?.region))
                                actionOpt?.add(OptionModel("TT ID", ttwoResponse.cvFsttwohistRCA?.ttId))
                                actionOpt?.add(OptionModel("WO ID", ttwoResponse.cvFsttwohistRCA?.woId))
                                actionOpt?.add(OptionModel("Time Start", ttwoResponse.cvFsttwohistRCA?.timeStart))
                                actionOpt?.add(OptionModel("Time End", ttwoResponse.cvFsttwohistRCA?.timeEnd))
                                actionOpt?.add(OptionModel("Time Duration", ttwoResponse.cvFsttwohistRCA?.timeDuration))
                                actionOpt?.add(OptionModel("Assignment", ttwoResponse.cvFsttwohistRCA?.assignment))
                                actionOpt?.add(OptionModel("Site PIC ID", ttwoResponse.cvFsttwohistRCA?.sitePicId))
                                actionOpt?.add(OptionModel("NE Name", ttwoResponse.cvFsttwohistRCA?.neName))
                                actionOpt?.add(OptionModel("Site ID", ttwoResponse.cvFsttwohistRCA?.siteId))
                                actionOpt?.add(OptionModel("Domain", ttwoResponse.cvFsttwohistRCA?.domain))
                                actionOpt?.add(OptionModel("Alarm Name", ttwoResponse.cvFsttwohistRCA?.alarmName))
                                actionOpt?.add(OptionModel("Category", ttwoResponse.cvFsttwohistRCA?.category))
                                actionOpt?.add(OptionModel("Service Affecting", ttwoResponse.cvFsttwohistRCA?.serviceAffecting))
                                actionOpt?.add(OptionModel("WO Description", ttwoResponse.cvFsttwohistRCA?.woDescription))
                                actionOpt?.add(OptionModel("RCA 1", ttwoResponse.cvFsttwohistRCA?.uRca1))
                                actionOpt?.add(OptionModel("RCA 2", ttwoResponse.cvFsttwohistRCA?.uRca2))
                                actionOpt?.add(OptionModel("RCA 3", ttwoResponse.cvFsttwohistRCA?.uRca3))
                                actionOpt?.add(OptionModel("RCA Flag", ttwoResponse.cvFsttwohistRCA?.uRcaFlag))
                                actionOpt?.add(OptionModel("RCA", ttwoResponse.cvFsttwohistRCA?.rca))
                                actionOpt?.add(OptionModel("RCA Lev 1", ttwoResponse.cvFsttwohistRCA?.rcaLev1))
                                actionOpt?.add(OptionModel("RCA Lev 2", ttwoResponse.cvFsttwohistRCA?.rcaLev2))
                                actionOpt?.add(OptionModel("Remark", ttwoResponse.cvFsttwohistRCA?.remarks))

                                actionOpt?.let { mAdapter?.updateList(it) }

                                btnUpdateTask.text = getString(R.string.update_rca)
                                btnUpdateTask.setOnClickListener {
                                    val intent = Intent(applicationContext, TTWOAddActivity::class.java)
                                    intent.putExtra("ttwoRCAID", ttwoResponse.cvFsttwohistRCA?.histNum)
                                    intent.putExtra("ttwoRCA1", ttwoResponse.cvFsttwohistRCA?.uRca1)
                                    intent.putExtra("ttwoRCA2", ttwoResponse.cvFsttwohistRCA?.uRca2)
                                    intent.putExtra("ttwoRCA3", ttwoResponse.cvFsttwohistRCA?.uRca3)
                                    intent.putExtra("ttwoTimeDuration", ttwoResponse.cvFsttwohistRCA?.timeDuration)
                                    intent.putExtra("ttwoSitePICID", ttwoResponse.cvFsttwohistRCA?.sitePicId)
                                    intent.putExtra("ttwoSiteID", ttwoResponse.cvFsttwohistRCA?.neName)
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

    private fun clearData() {
        actionOpt?.clear()
        mAdapter?.clearList()
    }
}