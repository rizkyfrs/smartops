package com.smartfren.smartops.ui.workordermanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.ui.workordermanagement.adapter.WOAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_base.btnBack
import kotlinx.android.synthetic.main.activity_base.tvTitleAppBar
import kotlinx.android.synthetic.main.incident_workorder_update.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WorkorderActivity : BaseActivity() {
    private var counter: Int = 0
    private var status = ""
    private var woid = ""
    private var mWOAdapter: WOAdapter? = null
    private val mWOViewModel: WorkorderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        setupSpinner()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, HomepageActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//    }

    private fun setupUI() {
        tvTitleAppBar.text = "Incident Work Order"
        clSpinnerFilter.show()
        btnFilter.show()
        tvFilter.hide()
        vSpace.hide()
        btnAddTask.hide()
        clInfo.hide()
        layoutIndicatorPriority.hide()

        woid = intent.getStringExtra("incwoID").toString()

        if (!intent.getStringExtra("woID").isNullOrEmpty()) {
            Log.e("tagwo", "$woid---")
            val intent = Intent(this, WorkorderDetailActivity::class.java)
            intent.putExtra("woID", woid)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mWOAdapter = WOAdapter(this)
        recyclerview.adapter = mWOAdapter
    }

    private fun setupSpinner() {
        val statuts = arrayOf("ALL","NEW", "DEPART", "ARRIVE", "TROUBLESHOOT", "RESTORED", "RESOLVED", "CLOSED")
        val adapterStatusWO = ArrayAdapter(this, android.R.layout.simple_list_item_1, statuts)

        spFilter.adapter = adapterStatusWO

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                mWOAdapter?.clearList()
                counter = 1
                status = adapterView.selectedItem.toString()
                if (status == "ALL") status = ""
                getListWO(if (status == "ALL") "" else status ,"$counter", "10")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
    }

    private fun getListWO(status: String?, start: String?, recperpage: String?) {
        mWOViewModel.setListWO(status, start, recperpage)
        mWOViewModel.listWO.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { woResponse ->
                            if (woResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (woResponse.totalRecordCount!! > mWOAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                woResponse.tbIncidentWorkorder?.let {
                                    mWOAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                woResponse.failureMessage?.let { toast(this, it) }
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

    private fun loadMore() {
        counter += 10
        getListWO(status, "$counter", "10")
    }
}