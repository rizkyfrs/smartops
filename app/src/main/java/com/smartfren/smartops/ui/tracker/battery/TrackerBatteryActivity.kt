package com.smartfren.smartops.ui.tracker.battery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.ui.tracker.TrackerViewModel
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrackerBatteryActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableAdapter2: ItemTableAdapter? = null
    private var mItemTableAdapter3: ItemTableAdapter? = null
    private var mItemTableAdapter4: ItemTableAdapter? = null
    private var mItemTableAdapter5: ItemTableAdapter? = null
    private var mItemTableAdapter6: ItemTableAdapter? = null
    private var mItemTableAdapter7: ItemTableAdapter? = null
    private var mItemTableAdapter8: ItemTableAdapter? = null
    private var mItemTableAdapter9: ItemTableAdapter? = null
    private var mItemTableAdapter10: ItemTableAdapter? = null
    private val trackerViewModel: TrackerViewModel by viewModels()
    private val trackerBatteryViewModel: TrackerBatteryViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuList2 = ArrayList<ItemTable>()
    private val menuList3 = ArrayList<ItemTable>()
    private val menuList4 = ArrayList<ItemTable>()
    private val menuList5 = ArrayList<ItemTable>()
    private val menuList6 = ArrayList<ItemTable>()
    private val menuList7 = ArrayList<ItemTable>()
    private val menuList8 = ArrayList<ItemTable>()
    private val menuList9 = ArrayList<ItemTable>()
    private val menuList10 = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
        setupRVTwo()
        setupRVThree()
        setupRVFour()
        setupRVFive()
        setupRVSix()
        setupRVSeven()
        setupRVEight()
        setupRVNine()
        setupRVTen()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.battery)
        clThree.show()
        clFour.show()
        clFive.show()
        clSix.show()
        clSeven.show()
        clEight.show()
        clNine.show()
        clTen.show()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = getString(R.string.battery_improvement_status)
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter
        mItemTableAdapter?.clearList()
        getTrackerBattery()
    }

    private fun getTrackerBattery() {
        trackerViewModel.trackerBatteryDataList()
        trackerViewModel.trackerBatteryData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Regional Network",
                                        "Total",
                                        "Close",
                                        "Cancel",
                                        "Open",
                                        "Super Hub",
                                        "Hub",
                                        "Sub Hub",
                                        "Collector",
                                        "End Site",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.vSiteBatteryTracker!!) {
                                    menuList.add(
                                        ItemTable(
                                            data?.regionNw,
                                            data?.total,
                                            data?.nClose,
                                            data?.nCancel,
                                            data?.nOpen,
                                            data?.superHub,
                                            data?.hub,
                                            data?.subHub,
                                            data?.collector,
                                            data?.endSite,
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter?.updateList(menuList)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = "Battery - Region Status"
        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter2 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter2
        mItemTableAdapter2?.clearList()
        getTrackerBattery2()
    }

    private fun getTrackerBattery2() {
        trackerBatteryViewModel.trackerBatteryPDBRegList()
        trackerBatteryViewModel.trackerBatteryPDBRegData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList2.add(
                                    ItemTable(
                                        "Regional Network",
                                        "Rely on Battery",
                                        "Rely on Genset",
                                        "Rely on Battery (Release Genset)",
                                        "Rely on Fixed Genset (Release Genset)",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsPdbRegion!!) {
                                    menuList2.add(
                                        ItemTable(
                                            data.area,
                                            data.relyBattery.toString(),
                                            data.relyGs.toString(),
                                            data.relyBatteryReleaseGS.toString(),
                                            data.relyFgsReleaseGS.toString(),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter2?.updateList(menuList2)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVThree() {
        tvTitleThree.text = "Battery - MW Topology Status"
        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter3 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter3
        mItemTableAdapter3?.clearList()
        getTrackerBattery3("")
    }

    private fun getTrackerBattery3(region: String?) {
        trackerBatteryViewModel.trackerBatteryPDBMWList(region)
        trackerBatteryViewModel.trackerBatteryPDBMWData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList3.add(
                                    ItemTable(
                                        "Topology",
                                        "Rely on Battery",
                                        "Rely on Genset",
                                        "Rely on Battery (Release Genset)",
                                        "Rely on Fixed Genset (Release Genset)",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsPdbMwTopology!!) {
                                    menuList3.add(
                                        ItemTable(
                                            data.mwTopology,
                                            data.relyBattery.toString(),
                                            data.relyGs.toString(),
                                            data.relyBatteryReleaseGS.toString(),
                                            data.relyFgsReleaseGS.toString(),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter3?.updateList(menuList3)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVFour() {
        tvTitleFour.text = "Battery - Revenue Status"
        val recyclerview = rvFour
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter4 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter4
        mItemTableAdapter4?.clearList()
        getTrackerBattery4("All Region")
    }

    private fun getTrackerBattery4(region: String?) {
        trackerBatteryViewModel.trackerBatteryPDBRevList(region)
        trackerBatteryViewModel.trackerBatteryPDBRevData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList4.add(
                                    ItemTable(
                                        "Revenue",
                                        "Rely on Battery",
                                        "Rely on Genset",
                                        "Rely on Battery (Release Genset)",
                                        "Rely on Fixed Genset (Release Genset)",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsPdbRevenue!!) {
                                    menuList4.add(
                                        ItemTable(
                                            data.revenueCategory,
                                            data.relyBattery.toString(),
                                            data.relyGs.toString(),
                                            data.relyBatteryReleaseGS.toString(),
                                            data.relyFgsReleaseGS.toString(),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter4?.updateList(menuList4)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVFive() {
        tvTitleFive.text = "Backup Time Battery - Region Status"
        val recyclerview = rvFive
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter5 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter5
        mItemTableAdapter5?.clearList()
        getTrackerBattery5()
    }

    private fun getTrackerBattery5() {
        trackerBatteryViewModel.trackerBatteryBBTRegList()
        trackerBatteryViewModel.trackerBatteryBBTRegionData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList5.add(
                                    ItemTable(
                                        "Regional Network",
                                        "Battery Stolen",
                                        "0.5 Hrs",
                                        "1.5 Hrs",
                                        "2.5 Hrs",
                                        "3.5 Hrs",
                                        "4.5 Hrs",
                                        "No Rectifier",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsBbtRegion!!) {
                                    menuList5.add(
                                        ItemTable(
                                            data.area,
                                            data.battStolen.toString(),
                                            data.batt05.toString(),
                                            data.batt15.toString(),
                                            data.batt25.toString(),
                                            data.batt35.toString(),
                                            data.batt45.toString(),
                                            data.battNoRectifier.toString(),
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter5?.updateList(menuList5)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVSix() {
        tvTitleSix.text = "Backup Time Battery - MW Topology Status  "
        val recyclerview = rvSix
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter6 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter6
        mItemTableAdapter6?.clearList()
        getTrackerBattery6()
    }

    private fun getTrackerBattery6() {
        trackerBatteryViewModel.trackerBatteryBBTMWList()
        trackerBatteryViewModel.trackerBatteryBBTMWData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList6.add(
                                    ItemTable(
                                        "Topology",
                                        "Battery Stolen",
                                        "0.5 Hrs",
                                        "1.5 Hrs",
                                        "2.5 Hrs",
                                        "3.5 Hrs",
                                        "4.5 Hrs",
                                        "No Rectifier",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsBbtMwTopology!!) {
                                    menuList6.add(
                                        ItemTable(
                                            data.mwTopology,
                                            data.battStolen.toString(),
                                            data.batt05.toString(),
                                            data.batt15.toString(),
                                            data.batt25.toString(),
                                            data.batt35.toString(),
                                            data.batt45.toString(),
                                            data.battNoRectifier.toString(),
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter6?.updateList(menuList6)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVSeven() {
        tvTitleSeven.text = "Backup Time Battery - Revenue Status"
        val recyclerview = rvSeven
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter7 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter7
        mItemTableAdapter7?.clearList()
        getTrackerBattery7()
    }

    private fun getTrackerBattery7() {
        trackerBatteryViewModel.trackerBatteryBBTRevList()
        trackerBatteryViewModel.trackerBatteryBBTRevData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList7.add(
                                    ItemTable(
                                        "Revenue",
                                        "Battery Stolen",
                                        "0.5 Hrs",
                                        "1.5 Hrs",
                                        "2.5 Hrs",
                                        "3.5 Hrs",
                                        "4.5 Hrs",
                                        "No Rectifier",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsBbtRevenue!!) {
                                    menuList7.add(
                                        ItemTable(
                                            data.revenueCategory,
                                            data.battStolen.toString(),
                                            data.batt05.toString(),
                                            data.batt15.toString(),
                                            data.batt25.toString(),
                                            data.batt35.toString(),
                                            data.batt45.toString(),
                                            data.battNoRectifier.toString(),
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter7?.updateList(menuList7)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }



    private fun setupRVEight() {
        tvTitleEight.text = "Battery - Action To Do"
        val recyclerview = rvEight
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter8 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter8
        mItemTableAdapter8?.clearList()
        getTrackerBattery8()
    }

    private fun getTrackerBattery8() {
        trackerBatteryViewModel.trackerBatteryBATTActionList()
        trackerBatteryViewModel.trackerBatteryBATTActionData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList8.add(
                                    ItemTable(
                                        "Action to do",
                                        "JB",
                                        "WJ",
                                        "CJ",
                                        "EJ",
                                        "NS",
                                        "SS",
                                        "SK",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsBattAction!!) {
                                    menuList8.add(
                                        ItemTable(
                                            data.rectifier1ActionToDo,
                                            data.jb.toString(),
                                            data.wj.toString(),
                                            data.cj.toString(),
                                            data.ej.toString(),
                                            data.ns.toString(),
                                            data.ss.toString(),
                                            data.sk.toString(),
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter8?.updateList(menuList8)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVNine() {
        tvTitleNine.text = "Battery - Status"
        val recyclerview = rvNine
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter9 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter9
        mItemTableAdapter9?.clearList()
        getTrackerBattery9()
    }

    private fun getTrackerBattery9() {
        trackerBatteryViewModel.trackerBatteryBATTStatusList()
        trackerBatteryViewModel.trackerBatteryBATTStatusData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList9.add(
                                    ItemTable(
                                        "Battery Secure",
                                        "JB",
                                        "WJ",
                                        "CJ",
                                        "EJ",
                                        "NS",
                                        "SS",
                                        "SK",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsBattStatus!!) {
                                    menuList9.add(
                                        ItemTable(
                                            data.rectifier1StatusBattery,
                                            data.jb.toString(),
                                            data.wj.toString(),
                                            data.cj.toString(),
                                            data.ej.toString(),
                                            data.ns.toString(),
                                            data.ss.toString(),
                                            data.sk.toString(),
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter9?.updateList(menuList9)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupRVTen() {
        tvTitleTen.text = "Battery - Secure Status"
        val recyclerview = rvTen
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter10 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter10
        mItemTableAdapter10?.clearList()
        getTrackerBattery10()
    }

    private fun getTrackerBattery10() {
        trackerBatteryViewModel.trackerBatteryBATTSecureList()
        trackerBatteryViewModel.trackerBatteryBATTSecureData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { trackerResponse ->
                            if (trackerResponse.success == "true") {
                                menuList10.add(
                                    ItemTable(
                                        "Battery Secure",
                                        "JB",
                                        "WJ",
                                        "CJ",
                                        "EJ",
                                        "NS",
                                        "SS",
                                        "SK",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in trackerResponse.crsBattSecureStatus!!) {
                                    menuList10.add(
                                        ItemTable(
                                            data.rectifier1Bu,
                                            data.jb.toString(),
                                            data.wj.toString(),
                                            data.cj.toString(),
                                            data.ej.toString(),
                                            data.ns.toString(),
                                            data.ss.toString(),
                                            data.sk.toString(),
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                }

                                mItemTableAdapter10?.updateList(menuList10)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(this, it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }
}