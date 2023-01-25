package com.smartfren.smartops.ui.resourcemanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.resourcemanagement.adapter.MenuResourceManagementAdapter
import com.smartfren.smartops.ui.resourcemanagement.adapter.SubMenuResourceManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuResourceManagementActivity : BaseActivity() {
    private var mMenuResourceManagementAdapter: SubMenuResourceManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Resource Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "Site Management Info", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "Resource Status", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2,"Resource Aggregates", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3,"Regional Site Manager", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val parent4 = Parent(4,"Regional Site Info", R.drawable.ic_assignment)
        val childItems4 = ArrayList<Child>()
        parent4.childItems.clear()
        parent4.childItems.addAll(childItems4)

        val parent5 = Parent(5,"Regional Site PIC Monitor", R.drawable.ic_assignment)
        val childItems5 = ArrayList<Child>()
        parent5.childItems.clear()
        parent5.childItems.addAll(childItems5)

        val parent6 = Parent(6,"Regional Site PIC Update", R.drawable.ic_assignment)
        val childItems6 = ArrayList<Child>()
        parent6.childItems.clear()
        parent6.childItems.addAll(childItems6)

        val parent7 = Parent(7,"Regional User Monitor", R.drawable.ic_assignment)
        val childItems7= ArrayList<Child>()
        parent7.childItems.clear()
        parent7.childItems.addAll(childItems7)

        val parent8 = Parent(8,"Regional User Manager", R.drawable.ic_assignment)
        val childItems8= ArrayList<Child>()
        parent8.childItems.clear()
        parent8.childItems.addAll(childItems8)

        val parent9 = Parent(9,"Regional Cluster Manager", R.drawable.ic_assignment)
        val childItems9= ArrayList<Child>()
        parent9.childItems.clear()
        parent9.childItems.addAll(childItems9)

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)
        itemList.add(parent3)
        itemList.add(parent4)
        itemList.add(parent5)
        itemList.add(parent6)
        itemList.add(parent7)
        itemList.add(parent8)
        itemList.add(parent9)

        mMenuResourceManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuResourceManagementAdapter = SubMenuResourceManagementAdapter(this)
        recyclerview.adapter = mMenuResourceManagementAdapter
    }
}