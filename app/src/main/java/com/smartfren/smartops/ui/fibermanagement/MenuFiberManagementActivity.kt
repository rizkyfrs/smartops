package com.smartfren.smartops.ui.fibermanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.fibermanagement.adapter.MenuFiberManagementAdapter
import com.smartfren.smartops.ui.fibermanagement.adapter.SubMenuFiberManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuFiberManagementActivity : BaseActivity() {
    private var mMenuFiberManagementAdapter: SubMenuFiberManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Fiber Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "Reconciliation", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent1, 1, "Fiber Reconciliation", 0))
        childItems1.add(Child(parent1, 2, "Fiber Recon. MTTR Monthly", 0))
        childItems1.add(Child(parent1, 3, "Fiber Recon. MTTR Weekly", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2,"Fiber Database", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems2.add(Child(parent1, 4, "Fiber Library", 0))
        childItems2.add(Child(parent1, 5, "Fiber Leased Core Summary", 0))
        childItems2.add(Child(parent1, 6, "Fiber Leased Core Detail", 0))
        childItems2.add(Child(parent1, 7, "Fiber Duplicates", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)

        mMenuFiberManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuFiberManagementAdapter = SubMenuFiberManagementAdapter(this)
        recyclerview.adapter = mMenuFiberManagementAdapter
    }
}