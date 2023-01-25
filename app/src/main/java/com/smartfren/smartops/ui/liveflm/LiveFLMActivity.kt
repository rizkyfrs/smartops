package com.smartfren.smartops.ui.liveflm

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.mapboxsdk.Mapbox
import com.smartfren.smartops.BuildConfig
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.dailyactivity.adapter.LiveFLMActivityAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LiveFLMActivity : BaseActivity() {
    private var counter: Int = 0
    private var mScheduleTaskListDetailAdapter: LiveFLMActivityAdapter? = null
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        loop()
        counter = 1
        mScheduleTaskListDetailAdapter?.clearList()
        getTaskListDetailSchedule("$counter", "10")
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Live FLM Activity"
        clInfo.hide()

        tvInfo.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_wifi), null, null, null)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mScheduleTaskListDetailAdapter = LiveFLMActivityAdapter(this)
        recyclerview.adapter = mScheduleTaskListDetailAdapter
    }

    private fun getTaskListDetailSchedule(start: String?, recperpage: String?) {
        dailyActivityViewModel.setListFLMActivity(start, recperpage)
        dailyActivityViewModel.getLiveFLMActivity.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (scheduleResponse.totalRecordCount!! > mScheduleTaskListDetailAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                scheduleResponse.mvDactlivemon?.let {
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

    private fun loadMore() {
        counter += 10
        getTaskListDetailSchedule("$counter", "10")
    }

    private fun loop() {
        CoroutineScope(IO).launch {
            delay(60000)
            CoroutineScope(Main).launch {
                mScheduleTaskListDetailAdapter?.clearList()
                getTaskListDetailSchedule("$counter", "10")
                Log.e("tess run", "yaaaaaaa")
                loop()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}