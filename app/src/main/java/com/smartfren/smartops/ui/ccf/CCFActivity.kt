package com.smartfren.smartops.ui.ccf

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CCFActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableAdapter2: ItemTableAdapter? = null
    private var mItemTableAdapter3: ItemTableAdapter? = null
    private var mItemTableAdapter4: ItemTableAdapter? = null
    private var mItemTableAdapter5: ItemTableAdapter? = null
    private val ccfViewModel: CCFViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuList2 = ArrayList<ItemTable>()
    private val menuList3 = ArrayList<ItemTable>()
    private val menuList4 = ArrayList<ItemTable>()
    private val menuList5 = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
        setupRVTwo()
        setupRVThree()
        setupRVFour()
        setupRVFive()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.ccf)
        clThree.show()
        clFour.show()
        clFive.show()

        getCCFD()
        getCCFW()
        getCCFM()
        getCCFNM()
        getCCFY()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = getString(R.string.ccf_daily)
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter

        mItemTableAdapter?.clearList()
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = getString(R.string.ccf_weekly)
        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter2 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter2

        mItemTableAdapter2?.clearList()
    }

    private fun setupRVThree() {
        tvTitleThree.text = getString(R.string.ccf_monthly)
        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter3 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter3

        mItemTableAdapter3?.clearList()
    }

    private fun setupRVFour() {
        tvTitleFour.text = getString(R.string.ccf_qs)
        val recyclerview = rvFour
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter4 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter4

        mItemTableAdapter4?.clearList()
    }

    private fun setupRVFive() {
        tvTitleFive.text = getString(R.string.ccf_yearly)
        val recyclerview = rvFive
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter5 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter5

        mItemTableAdapter5?.clearList()
    }

    private fun getCCFD() {
        ccfViewModel.setCCFD()
        ccfViewModel.ccfdData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Region",
                                        "Site ID",
                                        "A01",
                                        "A28",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in pmeSum.cvRepccfDaily!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.idRegion?.takeLast(2),
                                            data.siteId,
                                            data.a01,
                                            data.a28,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
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

    private fun getCCFW() {
        ccfViewModel.setCCFW()
        ccfViewModel.ccfwData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList2.add(
                                    ItemTable(
                                        "Region",
                                        "Site ID",
                                        "A02",
                                        "A03",
                                        "A05",
                                        "A29",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in pmeSum.cvRepccfWeekly!!) {
                                    menuList2.add(
                                        ItemTable(
                                            data.idRegion?.takeLast(2),
                                            data.siteId,
                                            data.a02,
                                            data.a03,
                                            data.a05,
                                            data.a29,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter2?.updateList(menuList2)
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

    private fun getCCFM() {
        ccfViewModel.setCCFM()
        ccfViewModel.ccfmData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList3.add(
                                    ItemTable(
                                        "Region",
                                        "Site ID",
                                        "A30",
                                        "A35",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in pmeSum.cvRepccfMonthly!!) {
                                    menuList3.add(
                                        ItemTable(
                                            data.idRegion?.takeLast(2),
                                            data.siteId,
                                            data.a30,
                                            data.a35,
                                           "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter3?.updateList(menuList3)
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

    private fun getCCFNM() {
        ccfViewModel.setCCFNM()
        ccfViewModel.ccfnmData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList4.add(
                                    ItemTable(
                                        "Region",
                                        "Site ID",
                                        "A20",
                                        "A23",
                                        "A24",
                                        "A31",
                                        "A32",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in pmeSum.cvRepccfNmonthly!!) {
                                    menuList4.add(
                                        ItemTable(
                                            data.idRegion?.takeLast(2),
                                            data.siteId,
                                            data.a20,
                                            data.a23,
                                            data.a24,
                                            data.a31,
                                            data.a32,
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter4?.updateList(menuList4)
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

    private fun getCCFY() {
        ccfViewModel.setCCFY()
        ccfViewModel.ccfyData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList5.add(
                                    ItemTable(
                                        "Region",
                                        "Site ID",
                                        "A15",
                                        "A17",
                                        "A18",
                                        "A21",
                                        "A25",
                                        "A26",
                                        "A33",
                                        "A34",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in pmeSum.cvRepccfYearly!!) {
                                    menuList5.add(
                                        ItemTable(
                                            data.idRegion?.takeLast(2),
                                            data.siteId,
                                            data.a15,
                                            data.a17,
                                            data.a18,
                                            data.a21,
                                            data.a25,
                                            data.a26,
                                            data.a33,
                                            data.a34,
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter5?.updateList(menuList5)
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