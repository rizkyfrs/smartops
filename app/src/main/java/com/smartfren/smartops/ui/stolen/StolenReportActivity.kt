package com.smartfren.smartops.ui.stolen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.stolen.adapter.StolenAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StolenReportActivity : BaseActivity() {
    private var counter: Int = 0
    private var token: String? = null
    private var mStolenAdapter: StolenAdapter? = null
    private val mStolenReportViewModel: StolenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mStolenAdapter?.clearList()
        getListStolenReport("$counter", "10")
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        tvTitleAppBar.text = "Stolen Report"

        clInfo.hide()
        layoutIndicatorPriority.hide()
        btnAddTask.show()
        btnAddTask.text = "Add Stolen"

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(applicationContext, StolenReportAddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mStolenAdapter = StolenAdapter(this)
        recyclerview.adapter = mStolenAdapter
    }

    private fun getListStolenReport(start: String?, recperpage: String?) {
        mStolenReportViewModel.setListStolenReport(start, recperpage)
        mStolenReportViewModel.listStolenReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenResponse ->
                            if (stolenResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (stolenResponse.totalRecordCount!! > mStolenAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                stolenResponse.cvStolenReport?.let {
                                    mStolenAdapter?.updateList(
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
        counter += 10
        getListStolenReport("$counter", "10")
    }
}