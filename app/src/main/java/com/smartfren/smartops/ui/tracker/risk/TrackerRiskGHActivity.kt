package com.smartfren.smartops.ui.tracker.risk

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.ui.tracker.risk.adpater.TrackerRiskAdapter
import com.smartfren.smartops.ui.tracker.risk.adpater.TrackerRiskGHAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.network_inventory.btnBack
import kotlinx.android.synthetic.main.network_inventory.tvTitleAppBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerRiskGHActivity : BaseActivity() {
    private var counter: Int = 0
    private var mTrackerRiskGHAdapter: TrackerRiskGHAdapter? = null
    private val trackerViewModel: TrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mTrackerRiskGHAdapter?.clearList()
        getTrackerRisk("$counter", "10", "")
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.tracker_risk)
        clInfo.hide()
        clTaskCategory.hide()
        layoutIndicatorPriority.hide()
        clSearch.show()

        val param = recycleView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,40,0,0)
        recycleView.layoutParams = param

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mTrackerRiskGHAdapter = TrackerRiskGHAdapter(this)
        recyclerview.adapter = mTrackerRiskGHAdapter
    }

    private fun getTrackerRisk(start: String?, recperpage: String?, status: String?) {
        trackerViewModel.setTrackerRiskGH(start, recperpage, status)
        trackerViewModel.trackerRiskListGHData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (trackerResponse.totalRecordCount!! > mTrackerRiskGHAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                trackerResponse.cvRegrtrackerVal?.let {
                                    mTrackerRiskGHAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                trackerResponse.failureMessage?.let { toast(this, it) }
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

//    private fun getTrackerRiskSearch(site_Zid: String?) {
//        trackerViewModel.setTrackerRiskSearch(site_id)
//        trackerViewModel.trackerRiskListData.observe(this) { event ->
//            event.getContentIfNotHandled()?.let { response ->
//                when (response) {
//                    is Resource.Success -> {
//                        loading(false)
//                        response.data?.let { trackerResponse ->
//                            if (trackerResponse.success == true) {
//                                trackerResponse.cvRegrtrackerOps?.let {
//                                    mTrackerRiskGHAdapter?.updateList(
//                                        it
//                                    )
//                                }
//                            } else {
//                                trackerResponse.failureMessage?.let { toast(this, it) }
//                            }
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        loading(false)
//                        response.message?.let {
//                            toast(this, it)
//                        }
//                    }
//
//                    is Resource.Loading -> {
//                        loading(true)
//                    }
//                }
//            }
//        }
//    }

    private fun loadMore() {
        counter += 10
        getTrackerRisk("$counter", "10", "")
    }
}