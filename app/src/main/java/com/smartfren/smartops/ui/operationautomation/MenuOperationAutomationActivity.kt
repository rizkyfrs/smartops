package com.smartfren.smartops.ui.operationautomation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dashboard.adapter.MenuDashboardAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuOperationAutomationActivity : BaseActivity() {
    private var mMenuDashboardAdapter: MenuDashboardAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Operation Automation"
        mMenuDashboardAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        menuList.add(ContentMenu(21, "Digital Opertaion", R.drawable.ic_assignment))
        menuList.add(ContentMenu(22, "Foresight", R.drawable.ic_assignment))

        mMenuDashboardAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuDashboardAdapter = MenuDashboardAdapter(this)
        recyclerview.adapter = mMenuDashboardAdapter
    }
}