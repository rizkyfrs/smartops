package com.smartfren.smartops.ui.tracker.risk

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.ui.tracker.risk.adpater.TrackerManagerAdapter
import com.smartfren.smartops.ui.tracker.risk.adpater.TrackerRiskAdapter
import com.smartfren.smartops.ui.tracker.risk.adpater.TrackerRiskGHAdapter
import com.smartfren.smartops.ui.tracker.risk.adpater.TrackerRiskSTOMAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.network_inventory.btnBack
import kotlinx.android.synthetic.main.network_inventory.tvTitleAppBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerRiskActivity : BaseActivity() {
    private var counter: Int = 0
    private var status: String? = ""
    private var type: String? = ""
    private var typeTracker: String? = ""
    private var mTrackerRiskAdapter: TrackerRiskAdapter? = null
    private var mTrackerRiskGHAdapter: TrackerRiskGHAdapter? = null
    private var mTrackerRiskSTOMAdapter: TrackerRiskSTOMAdapter? = null
    private var mTrackerManager: TrackerManagerAdapter? = null
    private val trackerViewModel: TrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1

        if (typeTracker == "yes") {
            mTrackerManager?.clearList()
            getTrackerManager("$counter", "10", status)
        } else {
            when (type) {
                "501" -> {
                    mTrackerRiskSTOMAdapter?.clearList()
                    getTrackerRiskSTOM("$counter", "10", status)
                }
                "20" -> {
                    mTrackerRiskGHAdapter?.clearList()
                    getTrackerRiskGH("$counter", "10", status)
                }
                else -> {
                    mTrackerRiskAdapter?.clearList()
                    getTrackerRisk("$counter", "10", status)
                }
            }
        }
    }

    private fun setupUI() {
        type = MainApp.instance.sharedPreferences?.getString("levelID", "")
        typeTracker = intent.getStringExtra("trackerManager")
        if (typeTracker == "yes") {
            clInfo.hide()
            layoutIndicatorPriority.hide()
            btnAddTask.hide()
            vSpace.hide()
            btnFilter.show()
            tvTitleAppBar.text = getString(R.string.tracker_manager)
        } else {
            clInfo.hide()
            layoutIndicatorPriority.hide()
            btnAddTask.show()
            btnFilter.show()
            btnAddTask.text = getString(R.string.add_tracker_risk)
            tvTitleAppBar.text = getString(R.string.tracker_risk)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(this, TrackerRiskAddActivity::class.java)
            startActivity(intent)
        }

        btnFilter.setOnClickListener {
            dialogFilter()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        if (typeTracker == "yes") {
            mTrackerManager = TrackerManagerAdapter(this)
            recyclerview.adapter = mTrackerManager
        } else {
            when (type) {
                "501" -> {
                    mTrackerRiskSTOMAdapter = TrackerRiskSTOMAdapter(this)
                    recyclerview.adapter = mTrackerRiskSTOMAdapter
                }
                "20" -> {
                    mTrackerRiskGHAdapter = TrackerRiskGHAdapter(this)
                    recyclerview.adapter = mTrackerRiskGHAdapter
                }
                else -> {
                    mTrackerRiskAdapter = TrackerRiskAdapter(this)
                    recyclerview.adapter = mTrackerRiskAdapter
                }
            }
        }
    }

    private fun getTrackerRisk(start: String?, recperpage: String?, status: String?) {
        trackerViewModel.setTrackerRisk(start, recperpage, status)
        trackerViewModel.trackerRiskListData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (trackerResponse.totalRecordCount!! > mTrackerRiskAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                trackerResponse.cvRegrtrackerOps?.let {
                                    mTrackerRiskAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                trackerResponse.failureMessage?.let { toast(this, it) }
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

    private fun getTrackerRiskGH(start: String?, recperpage: String?, status: String?) {
        trackerViewModel.setTrackerRiskGH(start, recperpage, status)
        trackerViewModel.trackerRiskListGHData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (trackerResponse.totalRecordCount!! > mTrackerRiskGHAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                trackerResponse.cvRegrtrackerVal?.let {
                                    mTrackerRiskGHAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                trackerResponse.failureMessage?.let { toast(this, it) }
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

    private fun getTrackerRiskSTOM(start: String?, recperpage: String?, status: String?) {
        trackerViewModel.setTrackerRiskSTOM(start, recperpage, status)
        trackerViewModel.trackerRiskListSTOMData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (trackerResponse.totalRecordCount!! > mTrackerRiskSTOMAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                trackerResponse.cvRegrtrackerStom?.let {
                                    mTrackerRiskSTOMAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                trackerResponse.failureMessage?.let { toast(this, it) }
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

    private fun getTrackerManager(start: String?, recperpage: String?, status: String?) {
        trackerViewModel.setTrackerManager(start, recperpage, status)
        trackerViewModel.trackerManagerData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (trackerResponse.totalRecordCount!! > mTrackerManager?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                trackerResponse.tbRtracker?.let {
                                    mTrackerManager?.updateList(
                                        it
                                    )
                                }
                            } else {
                                trackerResponse.failureMessage?.let { toast(this, it) }
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

    private fun getTrackerRiskSearch(site_id: String?) {
        trackerViewModel.setTrackerRiskSearch(site_id)
        trackerViewModel.trackerRiskListData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                trackerResponse.cvRegrtrackerOps?.let {
                                    mTrackerRiskAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                trackerResponse.failureMessage?.let { toast(this, it) }
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

    private fun dialogFilter() {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.pop_up_filter)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val buttonOk: Button = dialog.findViewById<View>(R.id.btnOk) as Button
        val buttonCancel: Button = dialog.findViewById<View>(R.id.btnCancel) as Button
        val tvTitleHide: TextView = dialog.findViewById<View>(R.id.tvTitlePeriod) as TextView
        val tvTitle: TextView = dialog.findViewById<View>(R.id.tvTitleRegion) as TextView
        val rbOpen: RadioButton = dialog.findViewById<View>(R.id.rbAllRegion) as RadioButton
        val rbResolve: RadioButton = dialog.findViewById<View>(R.id.rbJB) as RadioButton
        val rbCancel: RadioButton = dialog.findViewById<View>(R.id.rbNS) as RadioButton
        val rbClose: RadioButton = dialog.findViewById<View>(R.id.rbSS) as RadioButton
        val rbWJ: RadioButton = dialog.findViewById<View>(R.id.rbWJ) as RadioButton
        val rbCJ: RadioButton = dialog.findViewById<View>(R.id.rbCJ) as RadioButton
        val rbEJ: RadioButton = dialog.findViewById<View>(R.id.rbEJ) as RadioButton
        val rbSK: RadioButton = dialog.findViewById<View>(R.id.rbSK) as RadioButton
        val rgRegion: RadioGroup = dialog.findViewById<View>(R.id.rgPeriod) as RadioGroup

        tvTitleHide.hide()
        rgRegion.hide()
        rbWJ.hide()
        rbCJ.hide()
        rbEJ.hide()
        rbSK.hide()
        tvTitle.text = getString(R.string.status)
        rbOpen.text = getString(R.string.open)
        rbResolve.text = getString(R.string.resolve)
        rbCancel.text = getString(R.string.cancel)
        rbClose.text = getString(R.string.closed)

        buttonOk.setOnClickListener {
            counter = 1
            when {
                rbOpen.isChecked -> {
                    status = "Open"
                }
                rbResolve.isChecked -> {
                    status = "Resolve"
                }
                rbCancel.isChecked -> {
                    status = "Cancel"
                }
                rbClose.isChecked -> {
                    status = "Close"
                }
            }
            if (typeTracker == "yes") {
                mTrackerManager?.clearList()
                getTrackerManager("$counter", "10", status)
            } else {
                when (type) {
                    "501" -> {
                        mTrackerRiskSTOMAdapter?.clearList()
                        getTrackerRiskSTOM("$counter", "10", status)
                    }
                    "20" -> {
                        mTrackerRiskGHAdapter?.clearList()
                        getTrackerRiskGH("$counter", "10", status)
                    }
                    else -> {
                        mTrackerRiskAdapter?.clearList()
                        getTrackerRisk("$counter", "10", status)
                    }
                }
            }
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadMore() {
        counter += 10
        if (typeTracker == "yes") {
            getTrackerManager("$counter", "10", status)
        } else {
            when (type) {
                "501" -> {
                    getTrackerRiskSTOM("$counter", "10", status)
                }
                "20" -> {
                    getTrackerRiskGH("$counter", "10", status)
                }
                else -> {
                    getTrackerRisk("$counter", "10", status)
                }
            }
        }
    }
}