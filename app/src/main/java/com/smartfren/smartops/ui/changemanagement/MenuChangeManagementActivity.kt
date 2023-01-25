package com.smartfren.smartops.ui.changemanagement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.changemanagement.adapter.SubMenuChangeManagementAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.android.synthetic.main.submenu.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuChangeManagementActivity : BaseActivity() {
    private val itemList = ArrayList<Item>()
    private var mMenuChangeManagementAdapter: SubMenuChangeManagementAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Change Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "NW. BIS & Change Management", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent1, 1, "BIS Site Data Update", 0))
        childItems1.add(Child(parent1, 2, "BIS Site Summary", 0))
        childItems1.add(Child(parent1, 3, "BIS Site List", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)

        mMenuChangeManagementAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuChangeManagementAdapter = SubMenuChangeManagementAdapter(this)
        recyclerview.adapter = mMenuChangeManagementAdapter
    }
}