package com.smartfren.smartops.ui.supportmanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.supportmanagement.adapter.MenuSupportManagementAdapter
import com.smartfren.smartops.ui.supportmanagement.adapter.SubMenuSupportManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuSupportManagementActivity : BaseActivity() {
    private var mMenuSupportManagementAdapter: SubMenuSupportManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Support Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "Site Electricity Payment - Autodebet", 0))
        childItems.add(Child(parent, 2, "Site Stolen Summary", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "Site Management", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent, 3, "Support Site Info", 0))
        childItems1.add(Child(parent, 4, "Support Site Clustering Monitor", 0))
        childItems1.add(Child(parent, 5, "Site Stolen Report", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2,"Site Electricity", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems1.add(Child(parent, 6, "Site Electricity Info", 0))
        childItems1.add(Child(parent, 7, "Site Electricity Payment History", 0))
        childItems1.add(Child(parent, 8, "Site Electricity Payment Anomaly", 0))
        childItems1.add(Child(parent, 9, "Site Electricity KWH Check Report", 0))
        childItems1.add(Child(parent, 10, "Refference", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3,"Tools Management", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        childItems3.add(Child(parent3, 11, "Tools Group", 0))
        childItems3.add(Child(parent3, 12, "Tools Manager", 0))
        childItems3.add(Child(parent3, 13, "Regional Tools Manager", 0))
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val parent4 = Parent(4,"Mobile Device Management", R.drawable.ic_assignment)
        val childItems4 = ArrayList<Child>()
        childItems4.add(Child(parent4, 14, "MDM Devices", 0))
        childItems4.add(Child(parent4, 15, "MDM Users", 0))
        childItems4.add(Child(parent4, 16, "MDM Policies", 0))
        childItems4.add(Child(parent4, 17, "Regional MDM Devices", 0))
        childItems4.add(Child(parent4, 18, "Regional MDM Enrolment Req.", 0))
        parent4.childItems.clear()
        parent4.childItems.addAll(childItems4)


        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)
        itemList.add(parent3)
        itemList.add(parent4)

        mMenuSupportManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuSupportManagementAdapter = SubMenuSupportManagementAdapter(this)
        recyclerview.adapter = mMenuSupportManagementAdapter
    }
}