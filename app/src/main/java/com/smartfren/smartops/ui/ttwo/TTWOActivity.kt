package com.smartfren.smartops.ui.ttwo

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.ttwo.adapter.TTWOAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TTWOActivity : BaseActivity() {
    private var counter: Int = 0
    private var token: String? = null
    private var region: String? = null
    private var mTTWOAdapter: TTWOAdapter? = null
    private val mTTWOViewModel: TTWOViewModel by viewModels()
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mTTWOAdapter?.clearList()
        getListTTWO("$counter", "10")
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.ttworca)
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        region = MainApp.instance.sharedPreferences?.getString("regionCode", "")

        token?.let { it -> region?.let { it1 -> getSite(it, it1) } }

        clInfo.hide()
        clTaskCategory.hide()
        clSearch.show()

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSearch.setOnClickListener {
//            getListTTWO("$counter", "10")
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mTTWOAdapter = TTWOAdapter(this)
        recyclerview.adapter = mTTWOAdapter
    }

    private fun getListTTWO(start: String?, recperpage: String?) {
        mTTWOViewModel.setListTTWO(start, recperpage)
        mTTWOViewModel.listTTWO.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ttwoResponse ->
                            if (ttwoResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (ttwoResponse.totalRecordCount!! > mTTWOAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                ttwoResponse.cvFsttwohistRCA?.let {
                                    mTTWOAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                ttwoResponse.failureMessage?.let { toast(this, it) }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun getSite(token: String, site: String) {
        dailyActivityViewModel.listSite(token, site)
        dailyActivityViewModel.siteData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteResponse ->
                            if (siteResponse.getReport()?.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        for (element in siteResponse.getReport()?.sites!!) {
                                            element.siteId?.let { add(it) }
                                        }
                                    }
                                }

                                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)
                                etSearch.setAdapter(adapter)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun loadMore() {
        counter += 10
        getListTTWO("$counter", "10")
    }
}