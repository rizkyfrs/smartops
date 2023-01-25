package com.smartfren.smartops.ui.sitemanagementinfo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.sitemanagementinfo.adapter.SiteManagementInfoAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SiteManagementInfoActivity : BaseActivity() {
    private var token: String? = null
    private var counter: Int = 0
    private var mSiteManagementInfoAdapter: SiteManagementInfoAdapter? = null
    private val siteManagementInfoViewModel: SiteManagementInfoViewModel by viewModels()
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private val dataList = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_search)
        setupUI()
        setupRV()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.site_management_info)
        tvCaptionSearch.text = getString(R.string.enter_site_id_as_parameter)
        clSearch.show()

        token = MainApp.instance.sharedPreferences?.getString("token", "")

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSearch.setOnClickListener {
            getSiteManagementInfoDetail(etSearch.text.toString())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_region))
        spSearchRegion.adapter = adapter
        spSearchRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                token?.let { it -> getSite(it, "${i + 1}") }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mSiteManagementInfoAdapter = SiteManagementInfoAdapter(this)
        recyclerview.adapter = mSiteManagementInfoAdapter
        mSiteManagementInfoAdapter?.clearList()
    }

    private fun getSiteInfoList(start: String?, recperpage: String?) {
        siteManagementInfoViewModel.siteManagementInfo(start, recperpage)
        siteManagementInfoViewModel.siteManagementInfoData.observe(this) { event ->
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
                                            if (siteInfoResponse.totalRecordCount!! > mSiteManagementInfoAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                dataList.clear()

                                for (data in siteInfoResponse.cvHubSiteinfo!!) {
                                    dataList.add(
                                        ItemTable(
                                            data.siteId,
                                            data.siteStatus,
                                            data.siteVendor,
                                            data.siteSalesRegion,
                                            data.siteTechnology,
                                            data.idRegion,
                                            data.siteMorphology,
                                            "",
                                            "",
                                            "",
                                            "normal",
                                            R.color.color_bg_yellow
                                        )
                                    )
                                }

                                mSiteManagementInfoAdapter?.updateList(dataList)

                                token?.let { it -> getSite(it, "6") }
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

    private fun getSiteManagementInfoDetail(key: String) {
        clearData()
        siteManagementInfoViewModel.siteManagementInfoDetail(key)
        siteManagementInfoViewModel.siteManagementInfoDetailData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteManagementResponse ->
                            if (siteManagementResponse.success == true) {
                                siteManagementResponse.cvHubSiteinfo?.let {
                                    dataList.add(
                                        ItemTable(
                                            it.siteId,
                                            it.siteStatus,
                                            it.siteVendor,
                                            it.siteSalesRegion,
                                            it.siteTechnology,
                                            it.idRegion,
                                            it.siteMorphology,
                                            "",
                                            "",
                                            "",
                                            "normal",
                                            R.color.color_bg_yellow
                                        )
                                    )
                                    mSiteManagementInfoAdapter?.updateList(dataList)
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
        dataList.clear()
        mSiteManagementInfoAdapter?.clearList()
    }

    private fun loadMore() {
        counter += 11
        getSiteInfoList("$counter", "10")
    }
}