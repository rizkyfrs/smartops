package com.smartfren.smartops.ui.nfm

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.nfm.adapter.NFMAlarmAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.nfm_homebase_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NFMAlarmActivity : BaseActivity() {
    private var mNFMAlarmAdapter: NFMAlarmAdapter? = null
    private val mNFMViewModel: NFMViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfm_homebase_activity)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "NFM Alarm"
        clBtnAlarm.hide()
        btnRefresh.show()

        loadData()

        btnRefresh.setOnClickListener {
            loadData()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        val param = recyclerview.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = 40
        recyclerview.layoutParams = param
        recyclerview.layoutManager = LinearLayoutManager(this)

        mNFMAlarmAdapter = NFMAlarmAdapter(this)
        recyclerview.adapter = mNFMAlarmAdapter
    }

    private fun getListNFM() {
        mNFMViewModel.setAlarmNFM()
        mNFMViewModel.alarmNFM.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { nfmResponse ->
                            if (!nfmResponse.getAlarm().isNullOrEmpty()) {
                                val menuList = ArrayList<ItemTable>()
                                for (data in nfmResponse.getAlarm()!!) {
                                    menuList.add(
                                        ItemTable(
                                            data?.alarm, data?.raisedTime, "", "", "", "",
                                            "", "", "", "", "", 0
                                        )
                                    )
                                }
                                mNFMAlarmAdapter?.updateList(menuList)
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
    }

    private fun loadData() {
        mNFMAlarmAdapter?.clearList()
        getListNFM()
    }
}