package com.smartfren.smartops.ui.datahub

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Child
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.data.models.Item
import com.smartfren.smartops.data.models.Parent
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.datahub.adapter.MenuDataHubAdapter
import kotlinx.android.synthetic.main.submenu.*
import kotlinx.android.synthetic.main.submenu.btnBack
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuDataHubActivity : BaseActivity() {
//    private var mMenuDataHubAdapter: MenuDataHubAdapter? = null
    private var mMenuDataHubAdapter: MenuDataHubAdapter? = null
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submenu)
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.data_hub)
        type = MainApp.instance.sharedPreferences?.getString("levelUserID", "")

        val menuList= ArrayList<ContentMenu>()

        menuList.add(ContentMenu(1, getString(R.string.site_management_info), R.drawable.ic_wifi))
        menuList.add(ContentMenu(2, "Core & Facility Management", R.drawable.ic_wifi))
        menuList.add(ContentMenu(3, "Daily Activity Management", R.drawable.ic_wifi))
        menuList.add(ContentMenu(4, "Managed Network Element", R.drawable.ic_wifi))
        menuList.add(ContentMenu(5, "Material Management", R.drawable.ic_wifi))
        menuList.add(ContentMenu(6, "Risk Tracker", R.drawable.ic_wifi))
        menuList.add(ContentMenu(7, "Resource Management", R.drawable.ic_wifi))
        menuList.add(ContentMenu(8, "Site Data", R.drawable.ic_wifi))
        menuList.add(ContentMenu(9, "Workorder Management", R.drawable.ic_wifi))

        //some predefined values:
        val parent = Parent(0, getString(R.string.site_management_info), R.drawable.ic_assignment)
        val childItems = ArrayList<Child>()
//        childItems.add(Child(parent1, 0, "CCF PM Report", R.drawable.ic_assignment))
        parent.childItems.clear()
        parent.childItems.addAll(childItems)

        val parent1 = Parent(1, "Core & Facility Management", R.drawable.ic_assignment)
        val childItems1 = ArrayList<Child>()
        childItems1.add(Child(parent1, 1, "CCF PM Report", 0))
        parent1.childItems.clear()
        parent1.childItems.addAll(childItems1)

        val parent2 = Parent(2, "Daily Activity Management", R.drawable.ic_assignment)
        val childItems2 = ArrayList<Child>()
        childItems2.add(Child(parent2, 2, "FLM Daily Activity Task", 0))
        childItems2.add(Child(parent2, 3, "FLM Site Visit History", 0))
        childItems2.add(Child(parent2, 4, "FLM Corrective Detection", 0))
        childItems2.add(Child(parent2, 5, "PM Rectification Data", 0))
        parent2.childItems.clear()
        parent2.childItems.addAll(childItems2)

        val parent3 = Parent(3, "Managed Network Element", R.drawable.ic_assignment)
        val childItems3 = ArrayList<Child>()
        childItems3.add(Child(parent3, 6, "Microwave", R.drawable.ic_assignment))
        parent3.childItems.clear()
        parent3.childItems.addAll(childItems3)

        val parent4 = Parent(4, "Material Management", R.drawable.ic_assignment)
        val childItems4 = ArrayList<Child>()
        childItems4.add(Child(parent4, 7, "Consumable Part Management", 0))
        childItems4.add(Child(parent4, 8, "Spare Part Management", 0))
        parent4.childItems.clear()
        parent4.childItems.addAll(childItems4)

        val parent5 = Parent(5, "Risk Tracker", R.drawable.ic_assignment)
        val childItems5 = ArrayList<Child>()
        parent5.childItems.clear()
        parent5.childItems.addAll(childItems5)

        val parent6 = Parent(6, "Resource Management", R.drawable.ic_assignment)
        val childItems6 = ArrayList<Child>()
        childItems6.add(Child(parent6, 9, "Regional Summary", 0))
        childItems6.add(Child(parent6, 10, "Regional Area Coord. Summary", 0))
        childItems6.add(Child(parent6, 11, "Regional PIC Summary", 0))
        childItems6.add(Child(parent6, 12, "Regional PIC - PM Summary", 0))
        parent6.childItems.clear()
        parent6.childItems.addAll(childItems6)

        val parent7 = Parent(7, "Site Data", R.drawable.ic_assignment)
        val childItems7 = ArrayList<Child>()
        childItems7.add(Child(parent7, 13, "52 Kab", 0))
        childItems7.add(Child(parent7, 14, "High Revenue Site", 0))
        childItems7.add(Child(parent7, 15, "Site USO", 0))
        parent7.childItems.clear()
        parent7.childItems.addAll(childItems7)

        val parent8 = Parent(8, "Workorder Management", R.drawable.ic_assignment)
        val childItems8 = ArrayList<Child>()
        childItems8.add(Child(parent8, 16, "WO Cross Reference", 0))
        childItems8.add(Child(parent8, 17, "WO Active", 0))
        childItems8.add(Child(parent8, 18, "WO History", 0))
        parent8.childItems.clear()
        parent8.childItems.addAll(childItems8)

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

//        mMenuDataHubAdapter?.updateList(menuList)

        val recyclerview = rvSubmenu
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMenuDataHubAdapter = MenuDataHubAdapter(this, itemList)
        recyclerview.adapter = mMenuDataHubAdapter

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}