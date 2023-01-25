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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ItemTable
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.adapter.ItemTableAdapter
import com.smartfren.smartops.ui.networkinventory.adapter.NetworkInventoryAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NetworkInventoryActivity2 : BaseActivity() {
    private var mItemTableAdapter: NetworkInventoryAdapter? = null
    private var mItemTableAdapter2: ItemTableAdapter? = null
    private var mItemTableAdapter3: ItemTableAdapter? = null
    private var mItemTableAdapter4: ItemTableAdapter? = null
    private var mItemTableAdapter5: ItemTableAdapter? = null
    private var mItemTableAdapter6: ItemTableAdapter? = null
    private val networkInventoryViewModel: NetworkInventoryViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private val menuList2 = ArrayList<ItemTable>()
    private val menuList3 = ArrayList<ItemTable>()
    private val menuList4 = ArrayList<ItemTable>()
    private val menuList5 = ArrayList<ItemTable>()
    private val menuList6 = ArrayList<ItemTable>()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
//        setupRVTwo()
//        setupRVThree()
//        setupRVFour()
//        setupRVFive()
//        setupRVSix()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Network Inventory"

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleOne.text = "Site Info - Summary By Vendor"
        cvChart.show()
        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = NetworkInventoryAdapter(this)
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
        tvTitleFour.text = "Site Info - Summary By Model"
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
        tvTitleFive.text = "Site Info - Summary By Topology"
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
                                        1
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
                                            1
                                        )
                                    )

                                    data.zte?.let { dataTotalZTE.add(replacePoint(replaceComma(it))!!.toInt()) }
                                    data.nokia?.let {
                                        dataTotalNokia.add(
                                            replacePoint(
                                                replaceComma(
                                                    it
                                                )
                                            )!!.toInt()
                                        )
                                    }
                                    data.airspan?.let {
                                        dataTotalAirSpan.add(
                                            replacePoint(
                                                replaceComma(it)
                                            )!!.toInt()
                                        )
                                    }
                                    data.fiberhome?.let {
                                        dataTotalFH.add(
                                            replacePoint(
                                                replaceComma(
                                                    it
                                                )
                                            )!!.toInt()
                                        )
                                    }

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
                                            0
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
                                        1
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
                                            replaceComma(data.gf),
                                            replaceComma(data.rt),
                                            replaceComma(data.cow),
                                            replaceComma(data.na),
                                            replaceComma(data.totalCount),
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
                                }

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
                                        "Super Hub",
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
                                            replaceComma(data.endSite),
                                            replaceComma(data.collector),
                                            replaceComma(data.subHub),
                                            replaceComma(data.hub),
                                            replaceComma(data.superHub),
                                            replaceComma(data.totalCount),
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
                                }

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
            } else if (chart == "2" || chart == "6") {
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

        barChart.setExtraOffsets(0f, 0f, 0f, 10f)

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