package com.smartfren.smartops.ui.dashboard

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dashboard.adapter.MenuDashboardAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.android.synthetic.main.submenu.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuDashboardActivity : BaseActivity() {
    private var mMenuDashboardAdapter: MenuDashboardAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.dashboard)
        mMenuDashboardAdapter?.clearList()
        val menuList= ArrayList<ContentMenu>()

////        menuList.add(ContentMenu(1, getString(R.string.network_inventory), R.drawable.ic_wifi))
//        menuList.add(ContentMenu(2, getString(R.string.availability), R.drawable.ic_assignment))
//        menuList.add(ContentMenu(3, getString(R.string.wo_mttr), R.drawable.ic_assignment))
//        menuList.add(ContentMenu(4, getString(R.string.post_mortem_emergency), R.drawable.ic_assignment))
////        menuList.add(ContentMenu(5, getString(R.string.ccf_activity_report), R.drawable.ic_assignment))
////        menuList.add(ContentMenu(6, getString(R.string.vendor), R.drawable.ic_assignment))
//        menuList.add(ContentMenu(7, getString(R.string.tracker), R.drawable.tracker))
//        menuList.add(ContentMenu(8, "SRS", R.drawable.ic_assignment))

        menuList.add(ContentMenu(1, getString(R.string.network_inventory), R.drawable.ic_wifi))
        menuList.add(ContentMenu(2, getString(R.string.availability), R.drawable.ic_assignment))
        menuList.add(ContentMenu(3, getString(R.string.wo_mttr), R.drawable.ic_assignment))
        menuList.add(ContentMenu(4, getString(R.string.post_mortem_emergency), R.drawable.ic_assignment))
        menuList.add(ContentMenu(5, getString(R.string.ccf_activity_report), R.drawable.ic_assignment))
        menuList.add(ContentMenu(6, "Issue Tracker Manager", R.drawable.ic_assignment))
        menuList.add(ContentMenu(7, "Site Renewal", R.drawable.ic_assignment))
        menuList.add(ContentMenu(8, "Site Stolen", R.drawable.ic_assignment))

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