package com.smartfren.smartops.ui.sitevisit

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.sitevisit.adpater.SiteVisitLogAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SiteVisitLogActivity : BaseActivity() {
    private var token: String? = null
    private var region: String? = null
    private var counter: Int = 0
    private var mSiteVisitLogAdapter: SiteVisitLogAdapter? = null
    private val mSiteVisitLogViewModel: SiteVisitViewModel by viewModels()
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private val dataList = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Site Visit Log"
        clInfo.hide()
        clTaskCategory.hide()
        clSearch.hide()

        val param = recycleView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 40, 0, 0)
        recycleView.layoutParams = param

        token = MainApp.instance.sharedPreferences?.getString("token", "")
        region = MainApp.instance.sharedPreferences?.getString("regionName", "")

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSearch.setOnClickListener {
//            getSiteVisitLogSearch(etSearch.text.toString())
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mSiteVisitLogAdapter = SiteVisitLogAdapter(this)
        recyclerview.adapter = mSiteVisitLogAdapter
        mSiteVisitLogAdapter?.clearList()
        counter = 1
        getSiteInfoList("$counter", "10")
    }

    private fun getSiteInfoList(start: String?, recperpage: String?) {
        mSiteVisitLogViewModel.siteVisitLog(start, recperpage)
        mSiteVisitLogViewModel.siteVisitLogData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteInfoResponse ->
                            if (siteInfoResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (siteInfoResponse.totalRecordCount != siteInfoResponse.cvSvlogPersonal?.size) loadMore()
                                        }
                                    }
                                }

                                dataList.clear()

                                if (siteInfoResponse.cvSvlogPersonal != null)
                                    for (data in siteInfoResponse.cvSvlogPersonal!!) {
                                        dataList.add(
                                            ItemTable(
                                                data?.svSite,
                                                data?.svDate,
                                                data?.svReg,
                                                data?.svDeviceCustodian,
                                                data?.svTmin,
                                                data?.svTmax,
                                                data?.svDuration,
                                                "",
                                                "",
                                                "",
                                                "normal",
                                                R.color.color_bg_yellow
                                            )
                                        )
                                    }

                                mSiteVisitLogAdapter?.updateList(dataList)
//                                token?.let { it -> getSite(it, "1") }
                            } else {
                                siteInfoResponse.failureMessage?.let { toast(this, it) }
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

    private fun getSiteVisitLogSearch(key: String) {
        clearData()
        mSiteVisitLogViewModel.siteVisitLogSearch(key)
        mSiteVisitLogViewModel.siteVisitLogSearchData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteManagementResponse ->
                            if (siteManagementResponse.success == true) {
                                siteManagementResponse.cvSvlogPersonal.let {
                                    dataList.add(
                                        ItemTable(
                                            it?.svSite,
                                            it?.svDate,
                                            it?.svReg,
                                            it?.svDeviceCustodian,
                                            it?.svTmin,
                                            it?.svTmax,
                                            it?.svDuration,
                                            "",
                                            "",
                                            "",
                                            "normal",
                                            R.color.color_bg_yellow
                                        )
                                    )
                                    mSiteVisitLogAdapter?.updateList(dataList)
                                }
                            } else {
                                siteManagementResponse.failureMessage?.let { toast(this, it) }
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
                                etSearch.setAdapter(adapter)
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
        dataList.clear()
        mSiteVisitLogAdapter?.clearList()
    }

    private fun loadMore() {
        counter += 10
        getSiteInfoList("$counter", "10")
    }
}