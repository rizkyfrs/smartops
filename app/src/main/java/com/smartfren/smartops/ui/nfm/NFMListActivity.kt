package com.smartfren.smartops.ui.nfm

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.nfm.adapter.NFMListAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.nfm_base_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NFMListActivity : BaseActivity() {
    private var mNFMListAdapter: NFMListAdapter? = null
    private val mNFMViewModel: NFMViewModel by viewModels()
    private var neCityNFM: String? = ""
    private var neSNMPid: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfm_base_activity)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        neCityNFM = intent.getStringExtra("cityNFMItem")
        tvTitleAppBar.text = "$neCityNFM"
        tvCaptionNFMDetail.invisible()
        btnRefresh.show()

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

        mNFMListAdapter = NFMListAdapter(this)
        recyclerview.adapter = mNFMListAdapter
    }

    private fun refresh() {
        mNFMListAdapter?.clearList()
        getListNFM(neCityNFM)
    }

    private fun getListNFM(iot_id: String?) {
        mNFMViewModel.setListNFM(iot_id)
        mNFMViewModel.listNFM.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { nfmResponse ->
                            val menuList = ArrayList<ItemTable>()
                            if (!nfmResponse.ne.isNullOrEmpty()) {
                                for (data in nfmResponse.ne!!) {
                                    for (dataSnmp in nfmResponse.snmpDev!!) {
                                        if (data?.id == dataSnmp?.id) {
                                            neSNMPid = "1"
                                            break
                                        } else {
                                            neSNMPid = "0"
                                        }
                                    }
                                    menuList.add(
                                        ItemTable(
                                            neSNMPid,
                                            data?.ne!!,
                                            data?.id,
                                            data?.relayValue!!,
                                            neCityNFM,
                                            data.relayName,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            0
                                        )
                                    )
                                }
                                mNFMListAdapter?.updateList(menuList)
                            } else {
                                toast(this, "List is empty.")
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
        stopService(Intent(this, BackgroundSoundService::class.java))
    }

    override fun onPause() {
        super.onPause()
        loading(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        loading(false)
    }
}