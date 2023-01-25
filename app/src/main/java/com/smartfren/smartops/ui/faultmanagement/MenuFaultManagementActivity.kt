package com.smartfren.smartops.ui.faultmanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.faultmanagement.adapter.MenuFaultManagementAdapter
import com.smartfren.smartops.ui.faultmanagement.adapter.SubMenuFaultManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.android.synthetic.main.submenu.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuFaultManagementActivity : BaseActivity() {
    private var levelUserID: String? = null
    private var mMenuFaultManagementAdapter: SubMenuFaultManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Fault Management"
        levelUserID = MainApp.instance.sharedPreferences?.getString("levelUserID", "")
//        mMenuFaultManagementAdapter?.clearList()
        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "WO & MTTR", 0))
        childItems.add(Child(parent, 2, "Site Down Monitoring", 0))
        childItems.add(Child(parent, 3, "TT Post Mortem", 0))
        childItems.add(Child(parent, 4, "TT Critical Site Down", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "Alarm", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent1, 5, "Site Down Alarm", 0))
        childItems1.add(Child(parent1, 6, "Site Down Alarm UME", 0))
        childItems1.add(Child(parent1, 7, "Active Alarm Site (Sitedown, Celldown, Power)", 0))
        childItems1.add(Child(parent1, 8, "Active Alarm NE (Sitedown, Celldown, Power)", 0))
        childItems1.add(Child(parent1, 9, "Alarm History", 0))
        childItems1.add(Child(parent1, 10, "Alarm Library", 0))
        childItems1.add(Child(parent1, 11, "Alarm Sync Log", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2,"Trouble Ticket", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems2.add(Child(parent1, 12, "All TT", 0))
        childItems2.add(Child(parent1, 13, "TT UME Site Down", 0))
        childItems2.add(Child(parent1, 14, "TT Emergency", 0))
        childItems2.add(Child(parent1, 15, "TT Critical Site Down", 0))
        childItems2.add(Child(parent1, 16, "TT USO Site Down", 0))
        childItems2.add(Child(parent1, 17, "TT VIP Site Down", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3, "Post Mortem", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val parent4 = Parent(4,"Work Order", R.drawable.ic_confirmation)
        val childItems4 = ArrayList<Child>()
        childItems4.add(Child(parent4, 18, "Incident Work Order", 0))
        parent4.childItems.clear()
        parent4.childItems.addAll(childItems4)

        val itemList = ArrayList<Item>()
        if (levelUserID == "35" || levelUserID == "34") {
            itemList.add(parent4)
        } else {
            itemList.add(parent)
            itemList.add(parent1)
            itemList.add(parent2)
            itemList.add(parent3)
            itemList.add(parent4)
        }

        mMenuFaultManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuFaultManagementAdapter = SubMenuFaultManagementAdapter(this)
        recyclerview.adapter = mMenuFaultManagementAdapter
    }
}