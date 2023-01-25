package com.smartfren.smartops.ui.powermanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.powermanagement.adapter.MenuPowerManagementAdapter
import com.smartfren.smartops.ui.powermanagement.adapter.SubMenuPowerManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuPowerManagementActivity : BaseActivity() {
    private var mMenuFiberManagementAdapter: SubMenuPowerManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Power Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "PDB", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent1, 1, "Power Management : Site Priority", 0))
        childItems1.add(Child(parent1, 2, "Power Database", 0))
        childItems1.add(Child(parent1, 3, "Power Database Rule", 0))
        childItems1.add(Child(parent1, 4, "Power Management : Battery Relocation", 0))
        childItems1.add(Child(parent1, 5, "Power Management : PGS Cluster", 0))
        childItems1.add(Child(parent1, 6, "Power Management : Mobile Battery Cluster", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2,"NPM", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems2.add(Child(parent1, 7, "NPM Parameter Value", 0))
        childItems2.add(Child(parent1, 8, "NPM Devices", 0))
        childItems2.add(Child(parent1, 9, "Fiber Leased Core Detail", 0))
        childItems2.add(Child(parent1, 10, "NPM History", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3,"Genset", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        childItems3.add(Child(parent3, 11, "Genset Data", 0))
        childItems3.add(Child(parent3, 12, "Genset Refueling Log", 0))
        childItems3.add(Child(parent3, 13, "Genset Running Log", 0))
        childItems3.add(Child(parent3, 14, "Regional Genset Refuel Anomaly", 0))
        childItems3.add(Child(parent3, 15, "Regional Genset Refuel Register", 0))
        childItems3.add(Child(parent3, 16, "Regional Genset Refuel Approval L1", 0))
        childItems3.add(Child(parent3, 17, "Regional Genset Refuel Approval L2", 0))
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val parent4 = Parent(4,"BUGS", R.drawable.ic_assignment)
        val childItems4 = ArrayList<Child>()
        childItems4.add(Child(parent3, 18, "TT BUGS", 0))
        parent4.childItems.clear()
        parent4.childItems.addAll(childItems4)

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)
        itemList.add(parent3)
        itemList.add(parent4)

        mMenuFiberManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuFiberManagementAdapter = SubMenuPowerManagementAdapter(this)
        recyclerview.adapter = mMenuFiberManagementAdapter
    }
}