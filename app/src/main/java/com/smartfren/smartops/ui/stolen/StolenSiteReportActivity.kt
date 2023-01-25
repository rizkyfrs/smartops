package com.smartfren.smartops.ui.stolen

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
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
class StolenSiteReportActivity : BaseActivity(), OnChartValueSelectedListener {
    private var token: String? = null
    private var mItemTableAdapter: ItemTableAdapter? = null
    private var mItemTableAdapterTwo: ItemTableAdapter? = null
    private var mItemTableAdapterThree: ItemTableAdapter? = null
    private val menuList = ArrayList<ItemTable>()
    private val menuListTwo = ArrayList<ItemTable>()
    private val menuListThree = ArrayList<ItemTable>()
    private var scoreList = ArrayList<ItemTable>()
    private val mStolenReportViewModel: StolenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        tvTitleAppBar.text = "Site Stolen Report"
        setupRVOne()
        setupRVTwo()
        setupRVThree()
        rvThree.show()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleChart.text = "Site Stolen Case Cumulative Summary Chart"
        tvTitleOne.text = "Site Stolen Case Cumulative Summary Data"
        cvChart.show()
        llOne.hide()
        llFour.hide()
        cvIndicatorLabel2.setCardBackgroundColor(resources.getColorStateList(R.color.color_two_stackchart))
        cvIndicatorLabel3.setCardBackgroundColor(resources.getColorStateList(R.color.color_six_stackchart))
        tvLabel2ChartOne.text = "Previous Year"
        tvLabel3ChartOne.text = "Current Year"

        val recyclerview = rvOne
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter

        mItemTableAdapter?.clearList()
        getStolenReport(token)
    }

    private fun setupRVTwo() {
        tvTitleTwo.text = "Stolen Case Reported: Current Month Summary"

        val recyclerview = rvTwo
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapterTwo = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapterTwo

        mItemTableAdapterTwo?.clearList()
        getStolenReportCurrentMonth(token)
    }

    private fun setupRVThree() {
        tvTitleThree.text = "Site Stolen Case : KRI Summary"

        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapterThree = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapterThree

        mItemTableAdapterThree?.clearList()
        getStolenKRIReport(token)
    }

    private fun getStolenReport(token: String?) {
        mStolenReportViewModel.setStolenReport(token)
        mStolenReportViewModel.stolenReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenReport ->
                            if (stolenReport.report?.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Month",
                                        "Previous Year",
                                        "Current Year",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in stolenReport.report?.info!!) {
                                    menuList.add(
                                        ItemTable(
                                            data.stolenMon,
                                            data.hyearsum,
                                            data.cyearsum,
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

                                    scoreList.add(
                                        ItemTable(
                                            data.stolenMon,
                                            data.hyearsum,
                                            data.cyearsum,
                                            "",
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
                                mItemTableAdapter?.updateList(menuList)
                                initBarChart(scoreList)
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

    private fun getStolenReportCurrentMonth(token: String?) {
        mStolenReportViewModel.setStolenReportCurrentMonth(token)
        mStolenReportViewModel.stolenReportCurrentMonth.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenReport ->
                            if (stolenReport.report?.success == true) {
                                menuListTwo.add(
                                    ItemTable(
                                        "Region",
                                        "Battery",
                                        "Module",
                                        "Combination",
                                        "Other",
                                        "Total",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in stolenReport.report?.info!!) {
                                    menuListTwo.add(
                                        ItemTable(
                                            data.regionName,
                                            replaceComma(data.battery),
                                            replaceComma(data.module),
                                            replaceComma(data.combination),
                                            replaceComma(data.other),
                                            replaceComma(data.total),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }
                                mItemTableAdapterTwo?.updateList(menuListTwo)
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

    private fun getStolenKRIReport(token: String?) {
        mStolenReportViewModel.setStolenKRIReport(token)
        mStolenReportViewModel.stolenKRIReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenReport ->
                            if (stolenReport.report?.success == true) {
                                menuListThree.add(
                                    ItemTable(
                                        "Region",
                                        "Site Count",
                                        "Total Stolen",
                                        "Battery",
                                        "Modules",
                                        "Combination",
                                        "Other",
                                        "KRI",
                                        "KRI Effectiveness",
                                        "",
                                        "normal",
                                        R.color.color_line_chart
                                    )
                                )

                                for (data in stolenReport.report?.info!!) {
                                    menuListThree.add(
                                        ItemTable(
                                            data.regionName,
                                            data.siteCount,
                                            data.totalStolen,
                                            data.battery,
                                            data.modules,
                                            data.combination,
                                            data.other,
                                            data.kri,
                                            data.kriEffectiveness,
                                            "",
                                            "bold",
                                            R.color.color_line_chart
                                        )
                                    )
                                }
                                mItemTableAdapterThree?.updateList(menuListThree)
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

    private fun initBarChart(scoreList: ArrayList<ItemTable>) {
        val barChart = findViewById<BarChart>(R.id.barChart)

        //now draw bar chart with dynamic data
        val previous: ArrayList<BarEntry> = ArrayList()
        val current: ArrayList<BarEntry> = ArrayList()
        val bulan: ArrayList<String> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            previous.add(
                BarEntry(
                    i.toFloat(),
                    score.typeTwo?.let {
                        floatArrayOf(
                            it.toFloat(),
                        )
                    }
                )
            )

            current.add(
                BarEntry(
                    i.toFloat(),
                    score.typeThree?.let {
                        floatArrayOf(
                            it.toFloat(),
                        )
                    }
                )
            )

            score.typeOne?.let { bulan.add(it) }
        }

        val barDataSet = BarDataSet(previous, "")
        val barDataSet2 = BarDataSet(current, "")

        barDataSet.setColors(resources.getColor(R.color.color_two_stackchart))
        barDataSet2.setColors(resources.getColor(R.color.color_five_stackchart))

        barDataSet.setDrawValues(false)
        barDataSet2.setDrawValues(false)

        val data = BarData(barDataSet, barDataSet2)
        barChart.data = data

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

        barChart.setExtraOffsets(0f, 0f, 10f, 10f)

        // create marker to display box when values are selected
        val markerView = CustomValueMarker(this, R.layout.marker_view)

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
        xAxis.valueFormatter = IndexAxisValueFormatter(bulan)
        xAxis.textSize = 12F
        xAxis.textColor = resources.getColor(R.color.color_grey)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled
        xAxis.setCenterAxisLabels(true);
        xAxis.axisMaximum = bulan.size.toFloat();

        barChart.isDragEnabled = true

        val groupSpace = 0.23f
        val barSpace = 0.00f // x2 dataset
        val barWidth = 0.38f // x2 dataset
        data.barWidth = barWidth

        barChart.xAxis.axisMinimum = 0.0f
        barChart.xAxis.axisMaximum = 0.0f + barChart.barData.getGroupWidth(groupSpace, barSpace) * 12
        barChart.axisLeft.axisMinimum = 0.0f

        // make this BarData object grouped
        data.groupBars(0f, groupSpace, barSpace) // start at x = 0

        barChart.invalidate()
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.e("Entry selected", e.toString())
    }

    override fun onNothingSelected() {}
}