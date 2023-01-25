package com.smartfren.smartops.ui.corefacility.ccfreport

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.corefacility.ccfreport.adapter.CCFReportActivityAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CCFReportActivity : BaseActivity() {
    private var mActivityReportAdapter: CCFReportActivityAdapter? = null
    private var counter: Int = 0
    private var token: String? = null
    private val ccfReportViewModel: CCFReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mActivityReportAdapter?.clearList()
        token?.let {
            getListCCFReport(
                it,
                "/page/1"
            )
        }
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")

        tvTitleAppBar.text = getString(R.string.ccf_activity_report)
        clInfo.hide()
        indicatorP2.hide()
        indicatorP0.text = getString(R.string.validate)
        indicatorP1.text = getString(R.string.not_validated)
        indicatorP0.backgroundTintList =
            resources.getColorStateList(R.color.color_propose_to_closed)
        indicatorP1.backgroundTintList = resources.getColorStateList(R.color.color_closed)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mActivityReportAdapter = CCFReportActivityAdapter(this)
        recyclerview.adapter = mActivityReportAdapter
    }

    private fun getListCCFReport(token: String?, page: String?) {
        ccfReportViewModel.listCCFReport(token, page)
        ccfReportViewModel.ccfReportData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ccfReportResponse ->
                            if (ccfReportResponse.getReport()?.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (ccfReportResponse.getReport()?.totalPage != ccfReportResponse.getReport()?.pageNumber) loadMore()
                                        }
                                    }
                                }

                                ccfReportResponse.getReport()?.activities?.let {
                                    mActivityReportAdapter?.updateList(
                                        it
                                    )
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

    private fun loadMore() {
        counter += 1
        token?.let {
            getListCCFReport(
                it,
                "/page/$counter"
            )
        }
    }
}