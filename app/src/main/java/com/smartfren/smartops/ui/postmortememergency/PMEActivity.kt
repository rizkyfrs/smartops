package com.smartfren.smartops.ui.postmortememergency

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PMEActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private val ccfViewModel: ccfViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.post_mortem_emergency)
        clTwo.hide()

        getPME()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = getString(R.string.post_mortem_emergency)
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter

        mItemTableAdapter?.clearList()
    }

    private fun getPME() {
        ccfViewModel.pmeSum()
        ccfViewModel.pmeData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Total Case",
                                        "Total Action",
                                        "Close Action",
                                        "Open",
                                        "Average Action",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                menuList.add(
                                    ItemTable(
                                        pmeSum.cvPostmortemSummary!![0]?.value,
                                        pmeSum.cvPostmortemSummary!![1]?.value,
                                        pmeSum.cvPostmortemSummary!![3]?.value,
                                        pmeSum.cvPostmortemSummary!![2]?.value,
                                        pmeSum.cvPostmortemSummary!![4]?.value,
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "bold",
                                        R.color.color_grey
                                    )
                                )

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