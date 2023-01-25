package com.smartfren.smartops.ui.corefacility

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.corefacility.adapter.SubMenuCoreFacilityAdapter
import kotlinx.android.synthetic.main.menu_regional_operation.btnBack
import kotlinx.android.synthetic.main.menu_regional_operation.tvTitleAppBar
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuCoreFacilityActivity : BaseActivity() {
    private var mMenuCoreFacilityAdapter: SubMenuCoreFacilityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupRV()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Core & Facility Management"

        val parent = Parent(0, getString(R.string.dashboard), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
        childItems.add(Child(parent, 1, "CCF PM Summary", 0))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "SME", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent1, 2, "Activity Report", 0))
        childItems1.add(Child(parent1, 3, "Problem Log", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2, "CCF", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems2.add(Child(parent1, 4, "Activity Report", 0))
        childItems2.add(Child(parent1, 5, "Problem Log", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3, "NFM", R.drawable.ic_assignment)
        parent3.childItems.clear()

        val itemList = ArrayList<Item>()
        itemList.add(parent)
        itemList.add(parent1)
        itemList.add(parent2)
        itemList.add(parent3)

        mMenuCoreFacilityAdapter?.updateList(itemList)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuCoreFacilityAdapter = SubMenuCoreFacilityAdapter(this)
        recyclerview.adapter = mMenuCoreFacilityAdapter
    }
}