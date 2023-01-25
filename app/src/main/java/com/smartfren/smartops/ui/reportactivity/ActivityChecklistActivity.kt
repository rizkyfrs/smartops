package com.smartfren.smartops.ui.reportactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.reportactivity.adapter.ActivityChecklistAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ActivityChecklistActivity : BaseActivity() {
    private var token: String? = null
    private var actID: String? = null
    private var actNumID: String? = null
    private var mActivityChecklistAdapter: ActivityChecklistAdapter? = null
    private val mActivityReportViewModel: ActivityReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        token?.let {
            getListActivityReport(
                it,
                intent.getStringExtra("formNum")
            )
        }
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        tvTitleAppBar.text = getString(R.string.activity_checklist)

        clInfo.invisible()
        clActivityForm.show()
        btnAddTask.show()
        btnAddTask.text = getString(R.string.additional_item)

        actID = intent.getStringExtra("actID")
        actNumID = intent.getStringExtra("actNumID")

        intent.getStringExtra("nameActivity").let {
            tvActivityChecklistType.text = it
        }

        intent.getStringExtra("siteID").let {
            tvActivityChecklistSiteID.text = it
        }

        intent.getStringExtra("dateActivity").let {
            tvActivityChecklistDate.text = it
        }

//        tlActReport.show()
//        tvRegional.hide()
        indicatorP0.text = getString(R.string.pass)
        indicatorP1.text = getString(R.string.fail)
        indicatorP2.text = getString(R.string.na)
        indicatorP0.backgroundTintList = resources.getColorStateList(R.color.color_propose_to_closed)
        indicatorP1.backgroundTintList = resources.getColorStateList(R.color.color_red_down)
        indicatorP2.backgroundTintList = resources.getColorStateList(R.color.color_closed)
//        tvP0.textSize = 14F
//        tvP1.textSize = 14F
//        tvP2.textSize = 14F
//        indicatorP0.setBackgroundResource(R.drawable.bg_done)
//        indicatorP2.setBackgroundResource(R.drawable.bg_p_uncategory)
//        val param = rvSchedule.layoutParams as ViewGroup.MarginLayoutParams
//        param.setMargins(0, 50, 0, 0)
//        rvSchedule.layoutParams = param

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(applicationContext, ActivityChecklistUpdateActivity::class.java)
            intent.putExtra("addAdditionalNew", "true")
            intent.putExtra("actID", actID)
            intent.putExtra("actFormNum", actNumID)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mActivityChecklistAdapter = ActivityChecklistAdapter(this)
        recyclerview.adapter = mActivityChecklistAdapter
    }

    private fun getListActivityReport(token: String?, formNum: String?) {
        mActivityReportViewModel.listActivityChecklist(token, formNum)
        mActivityReportViewModel.activityChecklistData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { activityChecklistResponse ->
                            if (activityChecklistResponse.getReport()?.success == true) {
                                activityChecklistResponse.getReport()?.checklist?.let {
                                    mActivityChecklistAdapter?.updateList(
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
}