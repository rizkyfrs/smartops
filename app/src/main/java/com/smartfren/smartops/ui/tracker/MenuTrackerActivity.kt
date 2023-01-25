package com.smartfren.smartops.ui.tracker

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.tracker.adapter.MenuTrackerAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuTrackerActivity : BaseActivity() {
    private var mMenuTrackerAdapter: MenuTrackerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.tracker)
        mMenuTrackerAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        menuList.add(ContentMenu(1, "FGS", R.drawable.ic_generator))
//        menuList.add(ContentMenu(2, "Battery", R.drawable.ic_battery))
        menuList.add(ContentMenu(3, "Air Conditioner", R.drawable.ic_thermostat))
        menuList.add(ContentMenu(4, "Grounding", R.drawable.ic_electric_meter))
        menuList.add(ContentMenu(5, "Site Landscape", R.drawable.internet))
        menuList.add(ContentMenu(6, "Accessibility", R.drawable.ic_key))
        menuList.add(ContentMenu(7, "Travelling Time", R.drawable.ic_timer))
        menuList.add(ContentMenu(8, "IMB", R.drawable.ic_regops))
        menuList.add(ContentMenu(9, "Visibility Alarm FGS", R.drawable.ic_alarm))
        menuList.add(ContentMenu(10, "Problematic Cluster", R.drawable.ic_report))
        menuList.add(ContentMenu(11, "Wort Cell", R.drawable.ic_regops))
        menuList.add(ContentMenu(12, "N-Weeks", R.drawable.ic_assignment))

        mMenuTrackerAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuTrackerAdapter = MenuTrackerAdapter(this)
        recyclerview.adapter = mMenuTrackerAdapter
    }
}