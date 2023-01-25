package com.smartfren.smartops.ui.tracker.nweeks

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
class TrackerNWeeksActivity : BaseActivity() {
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
        tvTitleAppBar.text = getString(R.string.nweeks)
        clTwo.hide()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = getString(R.string.nweeks_worst_cell)
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter
        mItemTableAdapter?.clearList()
        getTrackerNWeeks()
    }

    private fun getTrackerNWeeks() {
        trackerViewModel.trackerNWeeksDataList()
        trackerViewModel.trackerNWeeksData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster/Site",
                                        "FOPS MGR",
                                        "FOPS AC",
                                        "Avail(%)",
                                        "GOOD(#)",
                                        "AVG(#)",
                                        "WORST(#)",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsNworstSite!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.region,
                                            data.vendorId,
                                            data.fopsMgr,
                                            data.fopsAc,
                                            data.avail.toString(),
                                            data.good.toString(),
                                            data.average.toString(),
                                            data.worst.toString(),
                                            "",
                                            "",
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