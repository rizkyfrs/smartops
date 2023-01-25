package com.smartfren.smartops.ui.regionalusermanager

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.regionalusermanager.adapter.RegionalUserManagerAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegionalUserManagerActivity : BaseActivity() {
    private var counter: Int = 0
    private var token: String? = null
    private var region: String? = null
    private var type: String? = null
    private var levelItem: ArrayList<OptionModel>? = null
    private var itemName: ArrayList<String>? = null
    private var level: String? = null
    private var parentItem: ArrayList<OptionModel>? = null
    private var parentItemNew: ArrayList<OptionModel>? = null
    private var itemNameParent: ArrayList<String>? = null
    private var parent: String? = null
    private var mRegionalUserManagerAdapter: RegionalUserManagerAdapter? = null
    private val mRegionalUserManagerViewModel: RegionalUserManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mRegionalUserManagerAdapter?.clearList()
        getListRegionalUserManager("$counter", "10", "", "")
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.regional_user_manager)
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        region = MainApp.instance.sharedPreferences?.getString("regionCode", "")
        type = MainApp.instance.sharedPreferences?.getString("levelUserID", "")
        levelItem = ArrayList()
        itemName = ArrayList()
        parentItem = ArrayList()
        parentItemNew = ArrayList()
        itemNameParent = ArrayList()
        btnFilter.show()
        clInfo.hide()
        layoutIndicatorPriority.hide()
        if (type != "35") {
            btnAddTask.show()
        }
        btnAddTask.text = "Add User"


        itemNameParent?.add("--Select Parent--")
        val datas = OptionModel(
            "0",
            "--Select Parent--"
        )
        parentItem?.add(datas)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(this, RegionalUserManagerAddActivity::class.java)
            startActivity(intent)
        }

        getRegionalUserLevel()

        btnFilter.setOnClickListener {
            dialogTrackerRisk()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mRegionalUserManagerAdapter = RegionalUserManagerAdapter(this)
        recyclerview.adapter = mRegionalUserManagerAdapter
    }

    private fun getListRegionalUserManager(start: String?, recperpage: String?, u_level: String?, id_parrent: String?) {
        mRegionalUserManagerViewModel.setListRegionalUserManager(start, recperpage, u_level, id_parrent)
        mRegionalUserManagerViewModel.listRegionalUserManager.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { regionalUserManagerResponse ->
                            if (regionalUserManagerResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (regionalUserManagerResponse.totalRecordCount!! > mRegionalUserManagerAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                getListRegionalUserManagerItem("", "${regionalUserManagerResponse.totalRecordCount}", "", "")

                                regionalUserManagerResponse.cvRegusermgm?.let {
                                    mRegionalUserManagerAdapter?.updateList(
                                        it
                                    )
                                }
                            } else {
                                regionalUserManagerResponse.failureMessage?.let { toast(this, it) }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun getListRegionalUserManagerItem(start: String?, recperpage: String?, u_level: String?, id_parrent: String?) {
        mRegionalUserManagerViewModel.setListRegionalUserManagerItem(start, recperpage, u_level, id_parrent)
        mRegionalUserManagerViewModel.listRegionalUserManagerItem.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { regionalUserManagerResponse ->
                            if (regionalUserManagerResponse.success == true) {
                                regionalUserManagerResponse.cvRegusermgm?.let {
                                    parentItemNew?.clear()
                                    if (regionalUserManagerResponse.cvRegusermgm != null)
                                        for (element in regionalUserManagerResponse.cvRegusermgm) {
                                            element.idParrent?.let { itemNameParent?.add(it) }
                                            val datas = OptionModel(
                                                replacePoint(element.idUser),
                                                element.idParrent
                                            )
                                            parentItem?.add(datas)
                                        }

                                    for (i in parentItem!!.distinct()) {
                                        parentItemNew?.add(i)
                                    }
                                }
                            } else {
                                regionalUserManagerResponse.failureMessage?.let { toast(this, it) }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
//                        loading(true)
                    }
                }
            }
        }
    }

    private fun dialogTrackerRisk() {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.pop_up_ttwo_update)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val btnFilter: Button = dialog.findViewById<View>(R.id.btnUpdateTTWO) as Button
        val spLevel: Spinner = dialog.findViewById<View>(R.id.spUpdateRCALevel1) as Spinner
        val spParent: Spinner = dialog.findViewById<View>(R.id.spUpdateRCALevel2) as Spinner
        val tvTitlePopupTTWO: TextView = dialog.findViewById<View>(R.id.tvTitlePopupTTWO) as TextView
        val tvTitleLevel: TextView = dialog.findViewById<View>(R.id.tvTitleUpdateRCALevel1) as TextView
        val tvTitleParent: TextView = dialog.findViewById<View>(R.id.tvTitleUpdateRCA2) as TextView
        val tvTitleUpdateRCA3: TextView = dialog.findViewById<View>(R.id.tvTitleUpdateRCA3) as TextView
        val clUpdateRCALevel3: ConstraintLayout = dialog.findViewById<View>(R.id.clUpdateRCALevel3) as ConstraintLayout
        val btnClose: ImageView = dialog.findViewById<View>(R.id.btnClose) as ImageView

        tvTitleUpdateRCA3.hide()
        clUpdateRCALevel3.hide()

        tvTitlePopupTTWO.text = getString(R.string.filter)
        tvTitleLevel.text = getString(R.string.level)
        tvTitleParent.text = getString(R.string.parent)
        btnFilter.text = getString(R.string.submit)


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName!!)

        spLevel.adapter = adapter

        spLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                if (levelItem != null) {
                    val optionDatas: OptionModel = levelItem!![i]
                    level = optionDatas.id
                    if (adapterView.selectedItem.toString() == "--Select Level--") {
                        level = ""
                    }
                }

                Log.e("level", "" + adapterView.selectedItem.toString())
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        val adapterParent = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemNameParent?.distinct()!!)

        spParent.adapter = adapterParent

        spParent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                if (parentItemNew != null) {
                    val optionDatas: OptionModel = parentItemNew!![i]
                    parent = optionDatas.type
                    if (adapterView.selectedItem.toString() == "--Select Parent--") {
                        parent = ""
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnFilter.setOnClickListener {
            mRegionalUserManagerAdapter?.clearList()
            getListRegionalUserManager("$counter", "10", level, parent)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getRegionalUserLevel() {
        mRegionalUserManagerViewModel.setListRegionalUserManagerLevel()
        mRegionalUserManagerViewModel.listRegionalUserManagerLevel.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { regionalUserManagerResponse ->
                            if (regionalUserManagerResponse.success == true) {
                                itemName?.add("--Select Level--")
                                val datas = OptionModel(
                                    "0",
                                    "--Select Level--"
                                )
                                levelItem?.add(datas)
                                if (regionalUserManagerResponse.cvApirefReguserlevel != null)
                                    for (element in regionalUserManagerResponse.cvApirefReguserlevel) {
                                        element.userlevelname?.let { itemName?.add(it) }
                                        val datas = OptionModel(
                                            element.userlevelid,
                                            element.userlevelname
                                        )
                                        levelItem?.add(datas)
                                    }
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(this, it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun loadMore() {
        counter += 10
        getListRegionalUserManager("$counter", "10", level, parent)
    }
}