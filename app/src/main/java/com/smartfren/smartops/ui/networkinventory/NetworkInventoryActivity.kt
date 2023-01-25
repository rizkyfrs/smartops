package com.smartfren.smartops.ui.networkinventory

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NetworkInventoryActivity : BaseActivity(), OnChartValueSelectedListener {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableAdapter2: ItemTableAdapter? = null
    private var mItemTableAdapter3: ItemTableAdapter? = null
    private var mItemTableAdapter4: ItemTableAdapter? = null
    private var mItemTableAdapter5: ItemTableAdapter? = null
    private var mItemTableAdapter6: ItemTableAdapter? = null
    private var mItemTableAdapter7: ItemTableAdapter? = null
    private var mItemTableAdapter8: ItemTableAdapter? = null
    private var mItemTableAdapter8Two: ItemTableAdapter? = null
    private var mItemTableAdapter9: ItemTableAdapter? = null
    private var mItemTableAdapter9Two: ItemTableAdapter? = null
    private var mItemTableAdapter10: ItemTableAdapter? = null
    private var mItemTableAdapter10Two: ItemTableAdapter? = null
    private val networkInventoryViewModel: NetworkInventoryViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuList2 = ArrayList<ItemTable>()
    private val menuList3 = ArrayList<ItemTable>()
    private val menuList4 = ArrayList<ItemTable>()
    private val menuList5 = ArrayList<ItemTable>()
    private val menuList6 = ArrayList<ItemTable>()
    private val menuList7 = ArrayList<ItemTable>()
    private val menuList8 = ArrayList<ItemTable>()
    private val menuList8Two = ArrayList<ItemTable>()
    private val menuList9 = ArrayList<ItemTable>()
    private val menuList9Two = ArrayList<ItemTable>()
    private val menuList10 = ArrayList<ItemTable>()
    private val menuList10Two = ArrayList<ItemTable>()
    private var dataTotalZTE = ArrayList<Int>()
    private var dataTotalNokia = ArrayList<Int>()
    private var dataTotalAirSpan = ArrayList<Int>()
    private var dataTotalFH = ArrayList<Int>()
    private var scoreList = ArrayList<ItemTable>()
    private var scoreList2 = ArrayList<ItemTable>()
    private var scoreList3 = ArrayList<ItemTable>()
    private var scoreList4 = ArrayList<ItemTable>()
    private var scoreList5 = ArrayList<ItemTable>()
    private var scoreList6 = ArrayList<ItemTable>()
    private var scoreList7 = ArrayList<ItemTable>()
    private var scoreList8 = ArrayList<ItemTable>()
    private var scoreList8Two = ArrayList<ItemTable>()
    private var scoreList9 = ArrayList<ItemTable>()
    private var scoreList9Two = ArrayList<ItemTable>()
    private var scoreList10 = ArrayList<ItemTable>()
    private var scoreList10Two = ArrayList<ItemTable>()
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupUI()
        setupRVOne()
//        setupRVTwo()
//        setupRVThree()
        setupRVFour()
        setupRVFive()
//        setupRVSix()
        setupRVSeven()
        setupRVEight()
        setupRVEightTwo()
        setupRVNine()
        setupRVNineTwo()
        setupRVTen()
        setupRVTenTwo()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Network Inventory & Resource Dashboard"
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        clTwo.hide()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleChart.text = "Site Info - Summary By Vendor Chart"
        tvTitleOne.text = "Site Info - Summary By Vendor Data"
        cvChart.show()
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter

        mItemTableAdapter?.clearList()
        getSiteInfoByVendor()
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = "Site Info - Summary By Tag"
        cvChartTwo.show()
        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter2 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter2

        mItemTableAdapter2?.clearList()
        getSiteInfoBySite()
    }

    private fun setupRVThree() {
        clThree.show()
        tvTitleThree.text = "Site Info - Summary By FO NODE"
        cvChartThree.show()
        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter3 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter3

        mItemTableAdapter3?.clearList()
        getSiteInfoByFONode()
    }

    private fun setupRVFour() {
        clFour.show()
        tvTitleChartFour.text = "Site Info - Summary By Model Chart"
        tvTitleFour.text = "Site Info - Summary By Model Data"
        cvChartFour.show()
        val recyclerview = rvFour
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter4 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter4

        mItemTableAdapter4?.clearList()
        getSiteInfoByModel()
    }

    private fun setupRVFive() {
        clFive.show()
        tvTitleChartFive.text = "Site Info - Summary By Topology Chart"
        tvTitleFive.text = "Site Info - Summary By Topology Data"
        cvChartFive.show()
        val recyclerview = rvFive
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter5 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter5

        mItemTableAdapter5?.clearList()
        getSiteInfoByTopology()
    }

    private fun setupRVSix() {
        clSix.show()
        tvTitleSix.text = "Site Info - Summary By FGS"
        cvChartSix.show()
        val recyclerview = rvSix
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter6 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter6

        mItemTableAdapter6?.clearList()
        getSiteInfoByFGS()
    }

    private fun setupRVSeven() {
        clSeven.show()
        tvTitleChartSeven.text = "Clustering Report Chart"
        tvTitleSeven.text = "Clustering Report Data"
        cvChartSeven.show()
        val recyclerview = rvSeven
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter7 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter7

        mItemTableAdapter7?.clearList()
        getClusteringReport(token)
    }

    private fun setupRVEight() {
        clEight.show()
        tvTitleChartEight.text = "PIC Availability - FLM Chart"
        tvTitleEight.text = "PIC Availability - FLM Data"
        cvChartEight.show()
        val recyclerview = rvEight
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter8 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter8

        mItemTableAdapter8?.clearList()
        getTeamAvailability(token, "1")
    }

    private fun setupRVEightTwo() {
        clEightTwo.show()
        tvTitleChartEightTwo.text = "PIC Availability - Technician Chart"
        tvTitleEightTwo.text = "PIC Availability - Technician Chart"
        cvChartEightTwo.show()
        val recyclerview = rvEightTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter8Two = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter8Two

        mItemTableAdapter8Two?.clearList()
        getTeamAvailabilityTech(token, "2")
    }

    private fun setupRVNine() {
        clNine.show()
        tvTitleChartNine.text = "SmartOPS Login - FLM Chart"
        tvTitleNine.text = "SmartOPS Login - FLM Data"
        cvChartNine.show()
        val recyclerview = rvNine
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter9 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter9

        mItemTableAdapter9?.clearList()
        getSmartopsLoginReport(token, "1", "", "")

        llNineOneTwo.setOnClickListener {
            getSmartopsLoginReport(token, "1", "true", "")
        }
    }

    private fun setupRVNineTwo() {
        clNineTwo.show()
        tvTitleChartNineTwo.text = "SmartOPS Login - Technician Chart"
        tvTitleNineTwo.text = "SmartOPS Login - Technician Data"
        cvChartNineTwo.show()
        val recyclerview = rvNineTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter9Two = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter9Two

        mItemTableAdapter9Two?.clearList()
        getSmartopsLoginTechReport(token, "2")
    }

    private fun setupRVTen() {
        clTen.show()
        tvTitleChartTen.text = "MDM Status - FLM Chart"
        tvTitleTen.text = "MDM Status - FLM Data"
        cvChartTen.show()
        val recyclerview = rvTen
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter10 = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter10

        mItemTableAdapter10?.clearList()
        getMDMStatus(token, "1")
    }

    private fun setupRVTenTwo() {
        clTenTwo.show()
        tvTitleChartTenTwo.text = "MDM Status - Technician Chart"
        tvTitleTenTwo.text = "MDM Status - Technician Data"
        cvChartTenTwo.show()
        val recyclerview = rvTenTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter10Two = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter10Two

        mItemTableAdapter10Two?.clearList()
        getMDMStatusTech(token, "2")
    }

    private fun getSiteInfoByVendor() {
        networkInventoryViewModel.siteInfoByVendor()
        networkInventoryViewModel.siteInfoByVendorData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { networkInventorySum ->
                            if (networkInventorySum.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Region",
                                        "ZTE",
                                        "Nokia",
                                        "Air Span",
                                        "FH",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in networkInventorySum.mvrepsitevendorsum!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.zte),
                                            replaceComma(data.nokia),
                                            replaceComma(data.airspan),
                                            replaceComma(data.fiberhome),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    data.zte?.let { dataTotalZTE.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.nokia?.let { dataTotalNokia.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.airspan?.let { dataTotalAirSpan.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.fiberhome?.let { dataTotalFH.add(replacePoint(replaceComma(it))!!.toInt()) }

                                    scoreList.add(
                                        ItemTable(
                                            data.regionName,
                                            replacePoint(replaceComma(data.zte)),
                                            replacePoint(replaceComma(data.nokia)),
                                            replacePoint(replaceComma(data.airspan)),
                                            replacePoint(replaceComma(data.fiberhome)),
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

                                menuList.add(
                                    ItemTable(
                                        "Total",
                                        dataTotalZTE.sum().toString(),
                                        dataTotalNokia.sum().toString(),
                                        dataTotalAirSpan.sum().toString(),
                                        dataTotalFH.sum().toString(),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "bold",
                                        R.color.color_line_chart
                                    )
                                )

                                menuList.add(
                                    ItemTable(
                                        "Grand Total",
                                        "${dataTotalZTE.sum() + dataTotalNokia.sum() + dataTotalAirSpan.sum() + dataTotalFH.sum()}",
                                        " ",
                                        " ",
                                        " ",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "bold",
                                        R.color.color_line_chart
                                    )
                                )

                                mItemTableAdapter?.updateList(menuList)
                                initBarChart("1", scoreList)
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

    private fun getSiteInfoBySite() {
        networkInventoryViewModel.siteInfoBySite()
        networkInventoryViewModel.siteInfoBySiteData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { networkInventorySum ->
                            if (networkInventorySum.success == true) {
                                menuList2.add(
                                    ItemTable(
                                        "Region",
                                        "TAG 5",
                                        "TAG 2",
                                        "TAG 4",
                                        "Ratio",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in networkInventorySum.mvrepsitetagsum!!) {
                                    menuList2.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.tag5),
                                            replaceComma(data.tag2),
                                            replaceComma(data.tag4),
                                            replaceComma(data.ratio),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                    scoreList2.add(
                                        ItemTable(
                                            data?.regionName,
                                            replacePoint(replaceComma(data?.tag5)),
                                            replacePoint(replaceComma(data?.tag2)),
                                            replacePoint(replaceComma(data?.tag4)),
                                            "",
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
                                initBarChart("2", scoreList2)
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

    private fun getSiteInfoByFONode() {
        networkInventoryViewModel.setSiteInfoFONode()
        networkInventoryViewModel.siteInfoByFONodeData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { networkInventorySum ->
                            if (networkInventorySum.success == true) {
                                menuList3.add(
                                    ItemTable(
                                        "Region",
                                        "FO Node - Chain",
                                        "FO Node - Ring",
                                        "FO MCP",
                                        "FO FTIF",
                                        "NA",
                                        "Total Count",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in networkInventorySum.mvrepsitefonode!!) {
                                    menuList3.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.fONodeChain),
                                            replaceComma(data.fONodeRing),
                                            replaceComma(data.foMcp),
                                            replaceComma(data.foFtif),
                                            replaceComma(data.na),
                                            replaceComma(data.totalCount),
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList3.add(
                                        ItemTable(
                                            data?.regionName,
                                            replacePoint(replaceComma(data?.fONodeChain)),
                                            replacePoint(replaceComma(data?.fONodeRing)),
                                            replacePoint(replaceComma(data?.foMcp)),
                                            replacePoint(replaceComma(data?.foFtif)),
                                            replacePoint(replaceComma(data?.na)),
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
                                initBarChart("3", scoreList3)
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

    private fun getSiteInfoByModel() {
        val dataTotalGF = ArrayList<Int>()
        val dataTotalRT = ArrayList<Int>()
        val dataTotalCOW = ArrayList<Int>()
        val dataTotalNA = ArrayList<Int>()
        val dataTotalCount = ArrayList<Int>()

        networkInventoryViewModel.setSiteInfoByModel()
        networkInventoryViewModel.siteInfoByModelData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { networkInventorySum ->
                            if (networkInventorySum.success == true) {
                                menuList4.add(
                                    ItemTable(
                                        "Region",
                                        "Gf",
                                        "RT",
                                        "COW",
                                        "NA",
                                        "Total Count",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in networkInventorySum.mvrepsitemodel!!) {
                                    menuList4.add(
                                        ItemTable(
                                            data.regionName,
                                            replacePoint(replaceComma(data.gf)),
                                            replacePoint(replaceComma(data.rt)),
                                            replacePoint(replaceComma(data.cow)),
                                            replacePoint(replaceComma(data.na)),
                                            replacePoint(replaceComma(data.totalCount)),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList4.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(replacePoint(data.gf)),
                                            replaceComma(replacePoint(data.rt)),
                                            replaceComma(replacePoint(data.cow)),
                                            replaceComma(replacePoint(data.na)),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    data.gf?.let { dataTotalGF.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.rt?.let { dataTotalRT.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.cow?.let { dataTotalCOW.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.na?.let { dataTotalNA.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.totalCount?.let { dataTotalCount.add(replacePoint(replaceComma(it))!!.toInt()) }
                                }

                                menuList4.add(
                                    ItemTable(
                                        "Total",
                                        dataTotalGF.sum().toString(),
                                        dataTotalRT.sum().toString(),
                                        dataTotalCOW.sum().toString(),
                                        dataTotalNA.sum().toString(),
                                        dataTotalCount.sum().toString(),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "bold",
                                        R.color.color_line_chart
                                    )
                                )

                                mItemTableAdapter4?.updateList(menuList4)
                                initBarChart("4", scoreList4)
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

    private fun getSiteInfoByTopology() {
        val dataTotalEndSite = ArrayList<Int>()
        val dataTotalCollector = ArrayList<Int>()
        val dataTotalSubHub = ArrayList<Int>()
        val dataTotalHub = ArrayList<Int>()
        val dataTotalSuperHub = ArrayList<Int>()
        val dataTotalCount = ArrayList<Int>()

        networkInventoryViewModel.siteInfoByTopology()
        networkInventoryViewModel.siteInfoByTopologyData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { networkInventorySum ->
                            if (networkInventorySum.success == true) {
                                menuList5.add(
                                    ItemTable(
                                        "Region",
                                        "End Site",
                                        "Collector",
                                        "Sub Hub",
                                        "Hub",
                                        "Super Hub",
                                        "Total Count",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in networkInventorySum.mvrepsitetxmode!!) {
                                    menuList5.add(
                                        ItemTable(
                                            data.regionName,
                                            replacePoint(replaceComma(data.endSite)),
                                            replacePoint(replaceComma(data.collector)),
                                            replacePoint(replaceComma(data.subHub)),
                                            replacePoint(replaceComma(data.hub)),
                                            replacePoint(replaceComma(data.superHub)),
                                            replacePoint(replaceComma(data.totalCount)),
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList5.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.endSite),
                                            replaceComma(data.collector),
                                            replaceComma(data.subHub),
                                            replaceComma(data.hub),
                                            replaceComma(data.superHub),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    data.endSite?.let { dataTotalEndSite.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.collector?.let { dataTotalCollector.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.subHub?.let { dataTotalSubHub.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.hub?.let { dataTotalHub.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.superHub?.let { dataTotalSuperHub.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.totalCount?.let { dataTotalCount.add(replacePoint(replaceComma(it))!!.toInt()) }
                                }

                                menuList5.add(
                                    ItemTable(
                                        "Total",
                                        dataTotalEndSite.sum().toString(),
                                        dataTotalCollector.sum().toString(),
                                        dataTotalSubHub.sum().toString(),
                                        dataTotalHub.sum().toString(),
                                        dataTotalSuperHub.sum().toString(),
                                        dataTotalCount.sum().toString(),
                                        "",
                                        "",
                                        "",
                                        "bold",
                                        R.color.color_line_chart
                                    )
                                )

                                mItemTableAdapter5?.updateList(menuList5)
                                initBarChart("5", scoreList5)
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

    private fun getSiteInfoByFGS() {
        networkInventoryViewModel.siteInfoByFGS()
        networkInventoryViewModel.siteInfoByFGSData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { networkInventorySum ->
                            if (networkInventorySum.success == true) {
                                menuList6.add(
                                    ItemTable(
                                        "Region",
                                        "NO-FGS",
                                        "FGS-TP",
                                        "FGS-SF",
                                        "Total Count",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in networkInventorySum.mvrepsitefgs!!) {
                                    menuList6.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.noFgs),
                                            replaceComma(data.fgsTp),
                                            replaceComma(data.fgsSf),
                                            replaceComma(data.totalCount),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList6.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.noFgs),
                                            replaceComma(data.fgsTp),
                                            replaceComma(data.fgsSf),
                                            replaceComma(data.totalCount),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter6?.updateList(menuList6)
                                initBarChart("6", scoreList6)
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

    private fun getClusteringReport(token: String?) {
        networkInventoryViewModel.setClusterReport(token)
        networkInventoryViewModel.clusteringReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList7.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "FLM Count",
                                        "FLM Vacant",
                                        "Tech Count",
                                        "Teach Vacant",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList7.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.fLMCount,
                                            data.fLMvacant,
                                            data.techCount,
                                            data.techVacant,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList7.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.fLMCount,
                                            data.fLMvacant,
                                            data.techCount,
                                            data.techVacant,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter7?.updateList(menuList7)
                                initBarChart("7", scoreList7)
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

    private fun getTeamAvailability(token: String?, id: String?) {
        networkInventoryViewModel.setTeamAvailabilityReport(token, id)
        networkInventoryViewModel.teamAvailabilityReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList8.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "PIC Count",
                                        "Vacant",
                                        "Available",
                                        "On Leave",
                                        "Not Update",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList8.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.pICCount,
                                            data.vacant,
                                            data.available,
                                            data.onLeave,
                                            data.notUpdated,
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList8.add(
                                        ItemTable(
                                            data.regionName,
                                            data.available,
                                            data.onLeave,
                                            data.notUpdated,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter8?.updateList(menuList8)
                                initBarChart("8", scoreList8)
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

    private fun getTeamAvailabilityTech(token: String?, id: String?) {
        networkInventoryViewModel.setTeamAvailabilityTechReport(token, id)
        networkInventoryViewModel.teamAvailabilityTechReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList8Two.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "PIC Count",
                                        "Vacant",
                                        "Available",
                                        "On Leave",
                                        "Not Update",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList8Two.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.pICCount,
                                            data.vacant,
                                            data.available,
                                            data.onLeave,
                                            data.notUpdated,
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList8Two.add(
                                        ItemTable(
                                            data.regionName,
                                            data.available,
                                            data.onLeave,
                                            data.notUpdated,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter8Two?.updateList(menuList8Two)
                                initBarChart("8Two", scoreList8Two)
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

    private fun getSmartopsLoginReport(token: String?, id: String?, active: String?, inactive: String?) {
        networkInventoryViewModel.setSmartopsLoginReport(token, id)
        networkInventoryViewModel.smartopsLoginReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList9.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "PIC Count",
                                        "SmartOPS Active",
                                        "SmartOPS Inactive",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList9.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.pICCount,
                                            data.smartOPSActive,
                                            data.smartOPSInactive,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList9.add(
                                        ItemTable(
                                            data.regionName,
                                            data.smartOPSActive,
                                            data.smartOPSInactive,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter9?.updateList(menuList9)
                                initBarChart("9", scoreList9)
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

    private fun getSmartopsLoginTechReport(token: String?, id: String?) {
        networkInventoryViewModel.setSmartopsLoginTechReport(token, id)
        networkInventoryViewModel.smartopsLoginTechReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList9Two.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "PIC Count",
                                        "SmartOPS Active",
                                        "SmartOPS Inactive",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList9Two.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.pICCount,
                                            data.smartOPSActive,
                                            data.smartOPSInactive,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList9Two.add(
                                        ItemTable(
                                            data.regionName,
                                            data.smartOPSActive,
                                            data.smartOPSInactive,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter9Two?.updateList(menuList9Two)
                                initBarChart("9Two", scoreList9Two)
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

    private fun getMDMStatus(token: String?, id: String?) {
        networkInventoryViewModel.setMDMStatusFLMReport(token, id)
        networkInventoryViewModel.mdmStatusFLMReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList10.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "PIC Count",
                                        "Active",
                                        "Inactive",
                                        "No Device",
                                        "Location Service Inactive",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList10.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.pICCount,
                                            data.mDMActive,
                                            data.mDMInactive,
                                            data.mDMNoDevice,
                                            data.mDMLocServiceInactive,
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList10.add(
                                        ItemTable(
                                            data.regionName,
                                            data.mDMActive,
                                            data.mDMInactive,
                                            data.mDMNoDevice,
                                            data.mDMLocServiceInactive,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter10?.updateList(menuList10)
                                initBarChart("10", scoreList10)
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

    private fun getMDMStatusTech(token: String?, id: String?) {
        networkInventoryViewModel.setMDMStatusTechReport(token, id)
        networkInventoryViewModel.mdmStatusTechReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { teamAvailability ->
                            if (teamAvailability.report?.success == true) {
                                menuList10Two.add(
                                    ItemTable(
                                        "Region",
                                        "Cluster Count",
                                        "PIC Count",
                                        "Active",
                                        "Inactive",
                                        "No Device",
                                        "Location Service Inactive",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in teamAvailability.report?.info!!) {
                                    menuList10Two.add(
                                        ItemTable(
                                            data.regionName,
                                            data.clusterCount,
                                            data.pICCount,
                                            data.mDMActive,
                                            data.mDMInactive,
                                            data.mDMNoDevice,
                                            data.mDMLocServiceInactive,
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )

                                    scoreList10Two.add(
                                        ItemTable(
                                            data.regionName,
                                            data.mDMActive,
                                            data.mDMInactive,
                                            data.mDMNoDevice,
                                            data.mDMLocServiceInactive,
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }

                                mItemTableAdapter10Two?.updateList(menuList10Two)
                                initBarChart("10Two", scoreList10Two)
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

    private fun initBarChart(chart: String?, scoreList: ArrayList<ItemTable>) {
        val barChart =
            when (chart) {
                "1" -> {
                    findViewById<BarChart>(R.id.barChart)
                }
                "2" -> {
                    findViewById<BarChart>(R.id.barChartTwo)
                }
                "3" -> {
                    findViewById<BarChart>(R.id.barChartThree)
                }
                "4" -> {
                    findViewById<BarChart>(R.id.barChartFour)
                }
                "5" -> {
                    findViewById<BarChart>(R.id.barChartFive)
                }
                "7" -> {
                    findViewById<BarChart>(R.id.barChartSeven)
                }
                "8" -> {
                    findViewById<BarChart>(R.id.barChartEight)
                }
                "8Two" -> {
                    findViewById<BarChart>(R.id.barChartEightTwo)
                }
                "9" -> {
                    findViewById<BarChart>(R.id.barChartNine)
                }
                "9Two" -> {
                    findViewById<BarChart>(R.id.barChartNineTwo)
                }
                "10" -> {
                    findViewById<BarChart>(R.id.barChartTen)
                }
                "10Two" -> {
                    findViewById<BarChart>(R.id.barChartTenTwo)
                }
                else -> {
                    findViewById<BarChart>(R.id.barChartSix)
                }
            }

        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            if (chart == "3" || chart == "5") {
                entries.add(
                    BarEntry(
                        i.toFloat(),
                        score.typeTwo?.let {
                            score.typeThree?.let { it1 ->
                                score.typeFour?.let { it2 ->
                                    score.typeFive?.let { it3 ->
                                        score.typeSix?.let { it4 ->
                                            floatArrayOf(
                                                it.toFloat(),
                                                it1.toFloat(),
                                                it2.toFloat(),
                                                it3.toFloat(),
                                                it4.toFloat(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                )
            } else if (chart == "2" || chart == "6" || chart == "8" || chart == "8Two") {
                entries.add(
                    BarEntry(
                        i.toFloat(),
                        score.typeTwo?.let {
                            score.typeThree?.let { it1 ->
                                score.typeFour?.let { it2 ->
                                    floatArrayOf(
                                        it.toFloat(),
                                        it1.toFloat(),
                                        it2.toFloat(),
                                    )
                                }
                            }
                        }
                    )
                )
            } else if (chart == "9" || chart == "9Two") {
                entries.add(
                    BarEntry(
                        i.toFloat(),
                        score.typeTwo?.let {
                            score.typeThree?.let { it1 ->
                                floatArrayOf(
                                    it.toFloat(),
                                    it1.toFloat(),
                                )
                            }
                        }
                    )
                )
            } else {
                entries.add(
                    BarEntry(
                        i.toFloat(),
                        score.typeFive?.let {
                            score.typeFour?.let { it1 ->
                                score.typeThree?.let { it2 ->
                                    score.typeTwo?.let { it3 ->
                                        floatArrayOf(
                                            it3.toFloat(),
                                            it2.toFloat(),
                                            it1.toFloat(),
                                            it.toFloat(),
                                        )
                                    }
                                }
                            }
                        }
                    )
                )
            }
        }

        val barDataSet = BarDataSet(entries, "")
        if (chart == "3" || chart == "5") {
            barDataSet.setColors(
                resources.getColor(R.color.color_one_stackchart),
                resources.getColor(R.color.color_two_stackchart),
                resources.getColor(R.color.color_three_stackchart),
                resources.getColor(R.color.color_four_stackchart),
                resources.getColor(R.color.color_five_stackchart)
            )
        } else if (chart == "2" || chart == "6") {
            barDataSet.setColors(
                resources.getColor(R.color.color_one_stackchart),
                resources.getColor(R.color.color_two_stackchart),
                resources.getColor(R.color.color_four_stackchart)
            )
        } else if (chart == "8" || chart == "8Two") {
            barDataSet.setColors(
                resources.getColor(R.color.color_seven_stackchart),
                resources.getColor(R.color.color_ten_stackchart),
                resources.getColor(R.color.color_six_stackchart)
            )
        } else if (chart == "9" || chart == "9Two") {
            barDataSet.setColors(
                resources.getColor(R.color.color_seven_stackchart),
                resources.getColor(R.color.color_six_stackchart)
            )
        } else if (chart == "10" || chart == "10Two") {
            barDataSet.setColors(
                resources.getColor(R.color.color_seven_stackchart),
                resources.getColor(R.color.color_six_stackchart),
                resources.getColor(R.color.color_ten_stackchart),
                resources.getColor(R.color.color_four_stackchart)
            )
        } else {
            barDataSet.setColors(
                resources.getColor(R.color.color_one_stackchart),
                resources.getColor(R.color.color_two_stackchart),
                resources.getColor(R.color.color_three_stackchart),
                resources.getColor(R.color.color_four_stackchart)
            )
        }

        barDataSet.setDrawValues(false)

        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()

        //hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = false

        // set listeners
        barChart.setOnChartValueSelectedListener(this)

        barChart.setExtraOffsets(0f, 0f, 0f, 10f)

        // create marker to display box when values are selected
        val markerView = CustomMarkerBar(this, R.layout.marker_view)

        // Set the marker to the chart
        barChart.marker = markerView

        //remove legend
        barChart.legend.isEnabled = false

        //remove description label
        barChart.description.isEnabled = false

        //add animation
        barChart.animateY(1000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.textSize = 14F
        xAxis.textColor = resources.getColor(R.color.color_grey)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        val entry = e as BarEntry
        if (entry.yVals != null) Log.i("VAL SELECTED", "Value: " + entry.yVals[h.stackIndex]) else Log.i("VAL SELECTED", "Value: " + entry.y)
    }

    override fun onNothingSelected() {}

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d(ContentValues.TAG, "getAxisLabel: index $index")
            return if (index < scoreList.size) {
                scoreList[index].typeOne!!
            } else {
                ""
            }
        }
    }
}