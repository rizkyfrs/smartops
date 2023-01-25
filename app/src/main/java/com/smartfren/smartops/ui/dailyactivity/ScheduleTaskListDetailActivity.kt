package com.smartfren.smartops.ui.dailyactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.smartfren.smartops.BuildConfig
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.adapter.ScheduleTaskListDetailAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.parseDateFull
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleTaskListDetailActivity : BaseActivity() {
    private var mScheduleTaskListDetailAdapter: ScheduleTaskListDetailAdapter? = null
    private var token: String? = null
    private var region: String? = null
    private var siteID: String? = null
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        mScheduleTaskListDetailAdapter?.clearList()
        token?.let {
            intent.getStringExtra("site")?.let { it1 ->
                tvInfo.text = it1
                siteID = it1
                getTaskListDetailSchedule(
                    it,
                    it1
                )
            }
        }
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        region = MainApp.instance.sharedPreferences?.getString("regionName", "")
        btnAddTask.text = getString(R.string.report_task)

//        tvRegional.text = "Regional $region"
        tvInfo.setCompoundDrawablesWithIntrinsicBounds(
            resources.getDrawable(R.drawable.ic_wifi),
            null,
            null,
            null
        )

        if (MainApp.instance.sharedPreferences?.getString("levelID", "") == "35") {
            btnAddTask.show()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(this, ScheduleAddTaskActivity::class.java)
            intent.putExtra("site", siteID)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mScheduleTaskListDetailAdapter = ScheduleTaskListDetailAdapter(this)
        recyclerview.adapter = mScheduleTaskListDetailAdapter
    }

    private fun getTaskListDetailSchedule(token: String, site: String) {
        dailyActivityViewModel.getListTaskDetailSchedule(token, site)
        dailyActivityViewModel.listTaskDetailSchedule.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.getReport()?.success == true) {
                                scheduleResponse.getReport()?.sites?.let {
                                    if (it[0].dactPlannedDate != null) tvSelectDate.text =
                                        parseDateFull(it[0].dactPlannedDate, "yyyy-MM-dd")
                                    mScheduleTaskListDetailAdapter?.updateList(
                                        it
                                    )
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
}