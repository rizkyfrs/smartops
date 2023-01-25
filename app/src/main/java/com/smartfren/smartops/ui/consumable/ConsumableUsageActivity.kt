package com.smartfren.smartops.ui.consumable

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.consumable.adapter.ConsumableUsageAdapter
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ConsumableUsageActivity : BaseActivity() {
    private var token: String? = null
    private var region: String? = null
    private var counter: Int = 0
    private var mConsumableUsageAdapter: ConsumableUsageAdapter? = null
    private val consumableViewModel: ConsumableUsageViewModel by viewModels()
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mConsumableUsageAdapter?.clearList()
        getConsumableUsageReport("$counter", "10")
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Consumable Usage Report"
        clInfo.hide()
        layoutIndicatorPriority.hide()
        btnAddTask.show()
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(this, ConsumableUsageAddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mConsumableUsageAdapter = ConsumableUsageAdapter(this)
        recyclerview.adapter = mConsumableUsageAdapter
    }

    private fun getConsumableUsageReport(start: String?, recperpage: String?) {
        consumableViewModel.setConsumableUsage(start, recperpage)
        consumableViewModel.consumableUsageData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { consumableResponse ->
                            if (consumableResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (consumableResponse.totalRecordCount!! > mConsumableUsageAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                consumableResponse.cvConsflmreport?.let {
                                    mConsumableUsageAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                consumableResponse.failureMessage?.let { toast(this, it) }
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
        counter += 10
        getConsumableUsageReport("$counter", "10")
    }
}