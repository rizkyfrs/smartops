package com.smartfren.smartops.ui.reportactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.reportactivity.adapter.ActivityReportAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ActivityReportActivity : BaseActivity() {
    private var counter: Int = 0
    private var token: String? = null
    private var levelID: String? = null
    private var mActivityReportAdapter: ActivityReportAdapter? = null
    private val mActivityReportViewModel: ActivityReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        mActivityReportAdapter?.clearList()
        counter = 1
        token?.let {
            getListActivityReport(
                it,
                ""
            )
        }

    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        levelID = MainApp.instance.sharedPreferences?.getString("levelID", "")

//        tvRegional.hide()

        clInfo.hide()
        indicatorP0.text = getString(R.string.done)
        indicatorP1.text = getString(R.string.not_done)
        indicatorP2.text = getString(R.string.na)
        indicatorP0.backgroundTintList = resources.getColorStateList(R.color.color_propose_to_closed)
        indicatorP1.backgroundTintList = resources.getColorStateList(R.color.color_red_down)
        indicatorP2.backgroundTintList = resources.getColorStateList(R.color.color_closed)
//        tvP0.textSize = 14F
//        tvP1.textSize = 14F
//        tvP2.textSize = 14F
//        val param = clTaskCategory.layoutParams as ViewGroup.MarginLayoutParams
//        param.setMargins(0, 100, 0, 0)
//        clTaskCategory.layoutParams = param
        tvTitleAppBar.text = getString(R.string.activity_form)

//        if (levelID == "35") {
            btnAddTask.show()
            btnAddTask.text = getString(R.string.add_form)
            btnAddTask.setOnClickListener {
                val intent = Intent(applicationContext, ActivityReportAddActivity::class.java)
                startActivity(intent)
            }
//        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mActivityReportAdapter = ActivityReportAdapter(this)
        recyclerview.adapter = mActivityReportAdapter
    }

    private fun getListActivityReport(token: String?, page: String?) {
        mActivityReportViewModel.listActivityReport(token, page)
        mActivityReportViewModel.activityReportData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { activityReportResponse ->
                            if (activityReportResponse.getReport()?.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (activityReportResponse.getReport()?.totalPage != activityReportResponse.getReport()?.pageNumber) loadMore()
                                        }
                                    }
                                }

                                activityReportResponse.getReport()?.activities?.let {
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
        val token = MainApp.instance.sharedPreferences?.getString("token", "")
        token?.let {
            getListActivityReport(
                it,
                "/page/$counter"
            )
        }
    }
}