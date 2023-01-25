package com.smartfren.smartops.ui.materialmanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.materialmanagement.adapter.MenuMaterialManagementAdapter
import com.smartfren.smartops.ui.powermanagement.adapter.MenuPowerManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuMaterialManagementActivity : BaseActivity() {
    private var mMenuMaterialManagementAdapter: MenuMaterialManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Material Management"
        mMenuMaterialManagementAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        menuList.add(ContentMenu(1, "Spare Part Management", R.drawable.ic_assignment))
        menuList.add(ContentMenu(2, "Consumable Part Management", R.drawable.ic_assignment))

        mMenuMaterialManagementAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuMaterialManagementAdapter = MenuMaterialManagementAdapter(this)
        recyclerview.adapter = mMenuMaterialManagementAdapter
    }
}