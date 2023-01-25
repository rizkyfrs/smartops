package com.smartfren.smartops.ui.renewal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.databinding.RenewalLayoutBinding
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RenewalSiteReportActivity : BaseActivity() {
    private var token: String? = null
    private lateinit var binding: RenewalLayoutBinding
    private var mItemTableAdapter: ItemTableAdapter? = null
    private val menuList = ArrayList<ItemTable>()
    private val mRenewalViewModel: RenewalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RenewalLayoutBinding.inflate(layoutInflater)
        binding.renewalViewModel = mRenewalViewModel
        val view = binding.root
        setContentView(view)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        binding.tvTitleAppBar.text = "Site Renewal"
        setupRVOne()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        binding.tvTitleOne.text = "Site Renewal"

        val recyclerview = binding.rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter

        getRenewalReport(token, "2")

        binding.btnFilter.setOnClickListener {
            getRenewalReport(token, mRenewalViewModel.setLeaseDue())
        }
    }

    private fun getRenewalReport(token: String?, leasedue: String?) {
        clearData()
        mRenewalViewModel.setRenewalReport(token, leasedue)
        mRenewalViewModel.renewalReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { renewalReport ->
                            if (renewalReport.report?.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Site ID (COUNT)",
                                        " ",
                                        "Site Renewal Sum",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )
                                menuList.add(
                                    ItemTable(
                                        "Region",
                                        "Process Due",
                                        "1. VALIDATION",
                                        "2. PRICING NEGO",
                                        "3. SITARA",
                                        "4. PKS",
                                        "5. PAYMENT",
                                        "6. RENEWAL",
                                        "7. DROP",
                                        "8. RELOC",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )
                                for (data in renewalReport.report?.info!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.regionName,
                                            data.leaseDue,
                                            data.validation,
                                            data.pricenego,
                                            data.sitara,
                                            data.pks,
                                            data.payment,
                                            data.renewal,
                                            data.drop,
                                            data.reloc,
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

    private fun clearData() {
        mItemTableAdapter?.clearList()
        menuList.clear()
    }
}