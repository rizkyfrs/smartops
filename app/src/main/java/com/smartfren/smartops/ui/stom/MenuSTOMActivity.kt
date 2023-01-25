package com.smartfren.smartops.ui.stom

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.stom.adapter.MenuSTOMAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.android.synthetic.main.submenu.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuSTOMActivity : BaseActivity() {
    private var mMenuSTOMAdapter: MenuSTOMAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.stom)
        mMenuSTOMAdapter?.clearList()
        val menuList= ArrayList<ContentMenu>()

        menuList.add(ContentMenu(4, "STOM Issue Tracker Manager", R.drawable.tracker))
        menuList.add(ContentMenu(1, "TP Perform", R.drawable.ic_assignment))
        menuList.add(ContentMenu(2, "Renewal Dashboard", R.drawable.ic_assignment))
        menuList.add(ContentMenu(3, "Top Issue", R.drawable.ic_assignment))

        mMenuSTOMAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuSTOMAdapter = MenuSTOMAdapter(this)
        recyclerview.adapter = mMenuSTOMAdapter
    }
}