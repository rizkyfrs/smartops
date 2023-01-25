package com.smartfren.smartops.ui.tracker.accessibility

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerAccessibilityActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableAdapterTwo: ItemTableAdapter? = null
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuListOpen = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
        setupRVTwo()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.accessibility)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = getString(R.string.accessibility_issue_status)
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter
        getAccessibility()
        mItemTableAdapter?.clearList()
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = getString(R.string.accessibility_issue_open)
        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapterTwo = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapterTwo
        getAccessibilityOpen()
        mItemTableAdapterTwo?.clearList()
    }

    private fun getAccessibility() {
        trackerViewModel.trackerAccessibilityDataList()
        trackerViewModel.trackerAccessibilityData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Regional Network",
                                        "Total",
                                        "Close",
                                        "Cancel",
                                        "Open",
                                        "Super Hub",
                                        "Hub",
                                        "Sub Hub",
                                        "Collector",
                                        "End Site",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.vSiteAccessibilityTracker!!) {
                                    menuList.add(
                                        ItemTable(
                                            data?.regionNw,
                                            data?.total,
                                            data?.nClose,
                                            data?.nCancel,
                                            data?.nOpen,
                                            data?.superHub,
                                            data?.hub,
                                            data?.subHub,
                                            data?.collector,
                                            data?.endSite,
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

    private fun getAccessibilityOpen() {
        trackerViewModel.trackerAccessibilityOpenDataList()
        trackerViewModel.trackerAccessibilityOpenData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                menuListOpen.add(
                                    ItemTable(
                                        "Regional Network",
                                        "Access\nBlock",
                                        "Danger At\nNight",
                                        "Not 7x24\nHours",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.vSiteAccessibilityTrackerOpen!!) {
                                    menuListOpen.add(
                                        ItemTable(
                                            data?.regionNw,
                                            data?.accessBlock,
                                            data?.dangerNight,
                                            data?.not247Hours,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapterTwo?.updateList(menuListOpen)
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