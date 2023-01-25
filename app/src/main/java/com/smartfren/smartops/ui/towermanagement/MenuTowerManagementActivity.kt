package com.smartfren.smartops.ui.towermanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.towermanagement.adapter.MenuTowerManagementAdapter
import com.smartfren.smartops.ui.towermanagement.adapter.SubMenuTowerManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuTowerManagementActivity : BaseActivity() {
    private var mMenuTowerManagementAdapter: SubMenuTowerManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Tower Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "TP Service Category", 0))
        childItems.add(Child(parent, 2, "Dashboard : TT/WO Weekly Active Log Summary", 0))
        childItems.add(Child(parent, 3, "TT/WO Weekly History Sum.", 0))
        childItems.add(Child(parent, 4, "TT/WO Daily Open/Close Sum.", 0))
        childItems.add(Child(parent, 5, "Active TT/WO : Region Sum.", 0))
        childItems.add(Child(parent, 6, "Active TT/WO : Aging Category Sum.", 0))
        childItems.add(Child(parent, 7, "Active TT/WO : Alarm Category Sum.", 0))
        childItems.add(Child(parent, 8, "Active TT/WO : Alarm Aging Sum.", 0))
        childItems.add(Child(parent, 9, "Dashboard : Site Lease Renewal Data", 0))
        childItems.add(Child(parent, 10, "Dashboard : Site Renewal Progress Summary", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(0, "TP Site Data", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(0, "TP Maintenance Report", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(0, "Site Renewal", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)

        mMenuTowerManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuTowerManagementAdapter = SubMenuTowerManagementAdapter(this)
        recyclerview.adapter = mMenuTowerManagementAdapter
    }
}