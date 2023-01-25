package com.smartfren.smartops.ui.workordermanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.towermanagement.adapter.MenuTowerManagementAdapter
import com.smartfren.smartops.ui.workordermanagement.adapter.MenuWorkorderManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuWorkorderManagementActivity : BaseActivity() {
    private var mMenuTowerManagementAdapter: MenuWorkorderManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Work Order Management"
        mMenuTowerManagementAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        menuList.add(ContentMenu(1, getString(R.string.dashboard), R.drawable.ic_assignment))
        menuList.add(ContentMenu(2, "WO Access Permission", R.drawable.ic_assignment))

        mMenuTowerManagementAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuTowerManagementAdapter = MenuWorkorderManagementAdapter(this)
        recyclerview.adapter = mMenuTowerManagementAdapter
    }
}