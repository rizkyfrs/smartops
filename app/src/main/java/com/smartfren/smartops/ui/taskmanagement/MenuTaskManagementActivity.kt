package com.smartfren.smartops.ui.taskmanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.taskmanagement.adapter.MenuTaskManagementAdapter
import com.smartfren.smartops.ui.taskmanagement.adapter.SubMenuTaskManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuTaskManagementActivity : BaseActivity() {
    private var mMenuTaskManagementAdapter: SubMenuTaskManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Task Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "Dashboard : Operation Summary", 0))
        childItems.add(Child(parent, 2, "Daily Activity Management", 0))
        childItems.add(Child(parent, 3, "Issue Tracker", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "Daily Activity Management", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent, 4, "Regional Task Monitor - Site Based", 0))
        childItems1.add(Child(parent1, 5, "Regional Task Monitor - PIC Based", 0))
        childItems1.add(Child(parent1, 6, "Regional Task Upload", 0))
        childItems1.add(Child(parent1, 7, "Regional Task List", 0))
        childItems1.add(Child(parent1, 8, "Regional Task Schedule", 0))
        childItems1.add(Child(parent1, 9, "Regional Submitted Activity Form", 0))
        childItems1.add(Child(parent1, 10, "Regional Submitted Rectification", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2, "Issue Tracker", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems2.add(Child(parent2, 11, "Issue Tracker - RNOM", 0))
        childItems2.add(Child(parent2, 12, "Issue Tracker - STOM", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3, "Shift Tracker", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)
        itemList.add(parent3)

        mMenuTaskManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuTaskManagementAdapter = SubMenuTaskManagementAdapter(this)
        recyclerview.adapter = mMenuTaskManagementAdapter
    }
}