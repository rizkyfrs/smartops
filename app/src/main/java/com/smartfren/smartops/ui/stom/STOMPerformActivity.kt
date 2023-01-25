package com.smartfren.smartops.ui.stom

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class STOMPerformActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableTwoAdapter: ItemTableAdapter? = null
    private var mItemTableThreeAdapter: ItemTableAdapter? = null
    private var mItemTableFourAdapter: ItemTableAdapter? = null
    private val stomViewModel: STOMViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuListTwo = ArrayList<ItemTable>()
    private val menuListThree = ArrayList<ItemTable>()
    private val menuListFour = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupUI()
        setupRVOne()
        setupRVTwo()
        setupRVThree()
        setupRVFour()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "STOM"
        clThree.show()
        clFour.show()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = "Lease Status - All TP"
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter
        mItemTableAdapter?.clearList()
        getSTOMLeaseAllTP()
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = "Lease Status - DL"
        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableTwoAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableTwoAdapter
        mItemTableTwoAdapter?.clearList()
        getSTOMLeaseAllDL()
    }

    private fun setupRVThree() {
        tvTitleThree.text = "Lease Status - In Building"
        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableThreeAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableThreeAdapter
        mItemTableThreeAdapter?.clearList()
        getSTOMLeaseAllIB()
    }

    private fun setupRVFour() {
        tvTitleFour.text = "Lease Status - Other"
        val recyclerview = rvFour
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableFourAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableFourAdapter
        mItemTableFourAdapter?.clearList()
        getSTOMLeaseAllOther()
    }

    private fun getSTOMLeaseAllTP() {
        stomViewModel.stomLeaseAllTPList()
        stomViewModel.stomLeaseAllTPData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuList.add(
                                    ItemTable(
                                        "Lease Status",
                                        "No. of Site(s)",
                                        "",
                                        "",
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

                                for (data in stomResponse.crsStomLease!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.leaseStatus,
                                            data.jmlSite?.toString(),
                                            "",
                                            "",
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

    private fun getSTOMLeaseAllDL() {
        stomViewModel.stomLeaseAllDLList()
        stomViewModel.stomLeaseAllDLData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuListTwo.add(
                                    ItemTable(
                                        "Lease Status",
                                        "No. of Site(s)",
                                        "",
                                        "",
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

                                for (data in stomResponse.crsStomLease!!) {
                                    menuListTwo.add(
                                        ItemTable(
                                            data.leaseStatus,
                                            data.jmlSite?.toString(),
                                            "",
                                            "",
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

                                mItemTableTwoAdapter?.updateList(menuListTwo)
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

    private fun getSTOMLeaseAllIB() {
        stomViewModel.stomLeaseAllIBList()
        stomViewModel.stomLeaseAllIBData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuListThree.add(
                                    ItemTable(
                                        "Lease Status",
                                        "No. of Site(s)",
                                        "",
                                        "",
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

                                for (data in stomResponse.crsStomLease!!) {
                                    menuListThree.add(
                                        ItemTable(
                                            data.leaseStatus,
                                            data.jmlSite?.toString(),
                                            "",
                                            "",
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

                                mItemTableThreeAdapter?.updateList(menuListThree)
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

    private fun getSTOMLeaseAllOther() {
        stomViewModel.stomLeaseAllOtherList()
        stomViewModel.stomLeaseAllOtherData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuListFour.add(
                                    ItemTable(
                                        "Lease Status",
                                        "No. of Site(s)",
                                        "",
                                        "",
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

                                for (data in stomResponse.crsStomLease!!) {
                                    menuListFour.add(
                                        ItemTable(
                                            data.leaseStatus,
                                            data.jmlSite?.toString(),
                                            "",
                                            "",
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

                                mItemTableFourAdapter?.updateList(menuListFour)
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