package com.smartfren.smartops.ui.noc

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.regionaloperation.adapter.MenuRegionalOperationAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.android.synthetic.main.submenu.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuNOCActivity : BaseActivity() {
    private var mMenuDashboardAdapter: MenuRegionalOperationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "NOC"
        mMenuDashboardAdapter?.clearList()
        val menuList= ArrayList<ContentMenu>()

        menuList.add(ContentMenu(8, "NOC FM", R.drawable.ic_assignment))
        menuList.add(ContentMenu(8, "NOC NCM", R.drawable.ic_assignment))
        menuList.add(ContentMenu(8, "NOC NICM", R.drawable.ic_assignment))
        menuList.add(ContentMenu(8, "NOC RPM", R.drawable.ic_assignment))

        mMenuDashboardAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuDashboardAdapter = MenuRegionalOperationAdapter(this)
        recyclerview.adapter = mMenuDashboardAdapter
    }
}