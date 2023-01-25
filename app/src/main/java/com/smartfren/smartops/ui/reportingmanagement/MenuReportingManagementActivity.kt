package com.smartfren.smartops.ui.reportingmanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.powermanagement.adapter.MenuPowerManagementAdapter
import com.smartfren.smartops.ui.reportingmanagement.adapter.MenuReportingManagementAdapter
import com.smartfren.smartops.ui.reportingmanagement.adapter.SubMenuReportingManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuReportingManagementActivity : BaseActivity() {
    private var mMenuFiberManagementAdapter: SubMenuReportingManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Reporting Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "SQM RAN National", 0))
        childItems.add(Child(parent, 2, "SQM RAN National Weekly", 0))
        childItems.add(Child(parent, 3, "SQM RAN National Daily", 0))
        childItems.add(Child(parent, 4, "KPI Dashboard", 0))
        childItems.add(Child(parent, 5, "Network Utilization Dashboard", 0))
        childItems.add(Child(parent, 6, "CQI & SE Trend", 0))
        childItems.add(Child(parent, 7, "HUC Trend", 0))
        childItems.add(Child(parent, 8, "HUC & Dead Cell Trend", 0))
        childItems.add(Child(parent, 9, "Hospitalized Cells Movement", 0))
        childItems.add(Child(parent, 10, "BBH (Daytime)-Traffic & HUC Trend", 0))
        childItems.add(Child(parent, 11, "Weekly Traffic-Daytime vs Midnight", 0))
        childItems.add(Child(parent, 12, "Daily Traffic Trend (PB)", 0))
        childItems.add(Child(parent, 13, "PRB>70 & DL IP THP< 1.5 (DAYTIME) – Weekly – Regional", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val itemList = ArrayList<Item>()
        itemList.add(parent)

        mMenuFiberManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuFiberManagementAdapter = SubMenuReportingManagementAdapter(this)
        recyclerview.adapter = mMenuFiberManagementAdapter
    }
}