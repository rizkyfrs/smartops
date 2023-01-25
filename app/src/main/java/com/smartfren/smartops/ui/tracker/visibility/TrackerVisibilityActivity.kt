package com.smartfren.smartops.ui.tracker.visibility

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerVisibilityActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.visibility_alarm_fgs)
        clTwo.hide()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = getString(R.string.visibility_alarm_fgs_low)
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter
        mItemTableAdapter?.clearList()
        getTrackerVisibility()
    }

    private fun getTrackerVisibility() {
        trackerViewModel.trackerLowFuelDataList()
        trackerViewModel.trackerLowFuelData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Regional Network",
                                        "Direct Lease - DONE",
                                        "Direct Lease - OPEN",
                                        "Direct Lease - Progress (%)",
                                        "In-Building - DONE",
                                        "In-Building - OPEN",
                                        "In-Building - Progress (%)",
                                        "Other - DONE",
                                        "Other - OPEN",
                                        "Other - Progress (%)",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.vSiteActivityLowFuelAlarmSensor!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.regionNw,
                                            data.dlClose,
                                            data.dlOpen,
                                            data.dlProgress,
                                            data.ibsClose,
                                            data.ibsOpen,
                                            data.ibsProgress,
                                            data.otherClose,
                                            data.otherOpen,
                                            data.otherProgress,
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter?.updateList(menuList)
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