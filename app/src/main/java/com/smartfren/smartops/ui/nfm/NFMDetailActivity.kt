package com.smartfren.smartops.ui.nfm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.nfm.adapter.NFMDetailAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.nfm_base_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NFMDetailActivity : BaseActivity() {
    private var mNFMDetailAdapter: NFMDetailAdapter? = null
    private val mNFMViewModel: NFMViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfm_base_activity)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "${intent.getStringExtra("cityNFMItem")}"
        tvCaptionNFMDetail.text = intent.getStringExtra("titleNFMItem")

        refresh()

        btnRefresh.setOnClickListener {
            refresh()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mNFMDetailAdapter = NFMDetailAdapter(this)
        recyclerview.adapter = mNFMDetailAdapter
    }

    private fun refresh() {
        mNFMDetailAdapter?.clearList()
        getDetailNFM(intent.getStringExtra("cityNFMItem"), intent.getStringExtra("idNFMItem"))
    }

    private fun getDetailNFM(iot_id: String?, ne_id: String?) {
        mNFMViewModel.setDetailNFM(iot_id, ne_id)
        mNFMViewModel.detailNFM.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { nfmResponse ->
                            val menuList = ArrayList<ItemTable>()
                            if (!nfmResponse.snmpVal.isNullOrEmpty()) {
                                for (data in nfmResponse.snmpVal!!) {
                                    menuList.add(ItemTable(data?.parameter, data?.time, data?.value, "", "", "", "", "", "", "", "", 0))
                                }
                                mNFMDetailAdapter?.updateList(menuList)
                            } else {
//                                nfmResponse.failureMessage?.let { toast(this, it) }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        if (response.data != null) {
                            response.message?.let {
                                toast(this, it)
                            }
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }
}