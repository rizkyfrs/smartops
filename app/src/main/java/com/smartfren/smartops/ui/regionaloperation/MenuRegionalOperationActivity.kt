package com.smartfren.smartops.ui.regionaloperation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.regionaloperation.adapter.MenuRegionalOperationAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuRegionalOperationActivity : BaseActivity() {
    private var type: String? = null
    private var mMenuRegionalOperationAdapter: MenuRegionalOperationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.regional_operation)
        type = MainApp.instance.sharedPreferences?.getString("levelUserID", "")

        mMenuRegionalOperationAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        if (type == "20") {
            menuList.add(ContentMenu(8, getString(R.string.issue_tracker_validation), R.drawable.tracker))
            menuList.add(ContentMenu(10, getString(R.string.live_flm_activity), R.drawable.ic_wifi))
        } else if (type == "40" || type == "34" || type == "95") {
            menuList.add(ContentMenu(1, getString(R.string.daily_activity), R.drawable.ic_assignment))
//            menuList.add(ContentMenu(7, getString(R.string.ttwo_rca_validation), R.drawable.ic_assignment))
            menuList.add(ContentMenu(8, "Issue Tracker Manager", R.drawable.tracker))
            menuList.add(ContentMenu(9, "Team Management", R.drawable.img_account))
            menuList.add(ContentMenu(10, getString(R.string.live_flm_activity), R.drawable.ic_wifi))
//            menuList.add(ContentMenu(12, "Stolen Report", R.drawable.ic_assignment))
//            menuList.add(ContentMenu(13, "MDR", R.drawable.ic_assignment))
        } else if (type == "32") {
            menuList.add(ContentMenu(7, getString(R.string.ttwo_rca_validation), R.drawable.ic_assignment))
            menuList.add(ContentMenu(8, getString(R.string.isse_tracker_register), R.drawable.tracker))
            menuList.add(ContentMenu(9, "Team Management", R.drawable.img_account))
            menuList.add(ContentMenu(10, getString(R.string.live_flm_activity), R.drawable.ic_wifi))
        } else if (type == "35" || type == "21") {
            menuList.add(ContentMenu(1, getString(R.string.daily_activity), R.drawable.ic_assignment))
            menuList.add(ContentMenu(2, getString(R.string.activity_form), R.drawable.ic_assignment))
            menuList.add(ContentMenu(3, getString(R.string.activity_log), R.drawable.ic_assignment))
//            menuList.add(ContentMenu(4, getString(R.string.material_delivery_request), R.drawable.ic_wifi))
            menuList.add(ContentMenu(5, getString(R.string.site_visit), R.drawable.ic_wifi))
//            menuList.add(ContentMenu(6, getString(R.string.consumable_usage_report), R.drawable.ic_assignment))
//            menuList.add(ContentMenu(7, getString(R.string.ttwo_rca_validation), R.drawable.ic_assignment))
//            menuList.add(ContentMenu(8, getString(R.string.isse_tracker_register), R.drawable.ic_wifi))
//            menuList.add(ContentMenu(12, "Stolen Report", R.drawable.ic_assignment))
        } else if (type == "501") {
            menuList.add(ContentMenu(11, getString(R.string.stom), R.drawable.ic_wifi))
        } else if (type == "666") {
            menuList.add(ContentMenu(1, getString(R.string.daily_activity), R.drawable.ic_assignment))
            menuList.add(ContentMenu(2, getString(R.string.activity_form), R.drawable.ic_assignment))
            menuList.add(ContentMenu(3, getString(R.string.activity_log), R.drawable.ic_assignment))
            menuList.add(ContentMenu(5, getString(R.string.site_visit), R.drawable.ic_wifi))
            menuList.add(ContentMenu(7, getString(R.string.ttwo_rca_validation), R.drawable.ic_assignment))
            menuList.add(ContentMenu(8, "Issue Tracker Manager", R.drawable.tracker))
            menuList.add(ContentMenu(9, "Team Management", R.drawable.img_account))
            menuList.add(ContentMenu(10, getString(R.string.live_flm_activity), R.drawable.ic_wifi))
        }

        mMenuRegionalOperationAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuRegionalOperationAdapter = MenuRegionalOperationAdapter(this)
        recyclerview.adapter = mMenuRegionalOperationAdapter
    }
}