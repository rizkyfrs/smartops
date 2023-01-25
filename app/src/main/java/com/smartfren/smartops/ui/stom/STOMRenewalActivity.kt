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
class STOMRenewalActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableTwoAdapter: ItemTableAdapter? = null
    private var mItemTableThreeAdapter: ItemTableAdapter? = null
    private val stomViewModel: STOMViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuListTwo = ArrayList<ItemTable>()
    private val menuListThree = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupUI()
        setupRVOne()
        setupRVTwo()
        setupRVThree()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.stom)
        clThree.show()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = "Lease Milestone Status - TP"
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter
        mItemTableAdapter?.clearList()
        getSTOMLeaseAllTP()
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = "Lease Milestone Status - Direct Lease"
        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableTwoAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableTwoAdapter
        mItemTableTwoAdapter?.clearList()
        getSTOMLeaseAllDL()
    }

    private fun setupRVThree() {
        tvTitleThree.text = "Lease Milestone Status - In Building"
        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableThreeAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableThreeAdapter
        mItemTableThreeAdapter?.clearList()
        getSTOMLeaseAllIB()
    }

    private fun getSTOMLeaseAllTP() {
        stomViewModel.stomMilestoneTPList()
        stomViewModel.stomMilestoneTPData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuList.add(
                                    ItemTable(
                                        "LED Group",
                                        "No. of Site(s)",
                                        "Renewal Done",
                                        "Terminate / Drop",
                                        "Leased Expired under Renewal Process in Procurement",
                                        "Leased Valid under Renewal Process in Procurement",
                                        "Leased Expired on Process Review",
                                        "Leased Valid on Process Review",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in stomResponse.crsStomMilestoneTp!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.ledGroup,
                                            data.jmlSite.toString(),
                                            data.renewalDone.toString(),
                                            data.terminateDrop.toString(),
                                            data.leaseExpiredRenewalOnprocess.toString(),
                                            data.leaseValidOnprocessReview.toString(),
                                            data.leaseExpiredOnprocessReview.toString(),
                                            data.leaseValidOnprocessReview.toString(),
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
        stomViewModel.stomMilestoneDLList()
        stomViewModel.stomMilestoneDLData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuListTwo.add(
                                    ItemTable(
                                        "LED Group",
                                        "No. of Site(s)",
                                        "Dismantle / Drop",
                                        "Renewal Done",
                                        "Payment Process",
                                        "PKS Process",
                                        "SITARA Process",
                                        "Pricing Nego",
                                        "Review Process",
                                        "Not Yet Process",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in stomResponse.crsStomMilestoneDl!!) {
                                    menuListTwo.add(
                                        ItemTable(
                                            data.ledGroup,
                                            data.jmlSite?.toString(),
                                            data.dismantleDrop.toString(),
                                            data.renewalDone.toString(),
                                            data.paymentProcess.toString(),
                                            data.pksProcess.toString(),
                                            data.sitaraProcess.toString(),
                                            data.pricingNego.toString(),
                                            data.reviewProcess.toString(),
                                            data.notYetSubmit.toString(),
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
        stomViewModel.stomMilestoneIBList()
        stomViewModel.stomMilestoneIBData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stomResponse ->
                            if (stomResponse.success == "true") {
                                menuListThree.add(
                                    ItemTable(
                                        "LED Group",
                                        "No. of Site(s)",
                                        "Renewal Done",
                                        "Terminate / Drop",
                                        "Leased Expired under Renewal Process in Procurement",
                                        "Leased Valid under Renewal Process in Procurement",
                                        "Leased Expired on Process Review",
                                        "Leased Valid on Process Review",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in stomResponse.crsStomMilestoneTp!!) {
                                    menuListThree.add(
                                        ItemTable(
                                            data.ledGroup,
                                            data.jmlSite.toString(),
                                            data.renewalDone.toString(),
                                            data.terminateDrop.toString(),
                                            data.leaseExpiredOnprocessReview.toString(),
                                            data.leaseValidRenewalOnprocess.toString(),
                                            data.leaseExpiredOnprocessReview.toString(),
                                            data.leaseValidOnprocessReview.toString(),
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
}