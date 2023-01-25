package com.smartfren.smartops.ui.womttr

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
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.network_inventory.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WOMTTRActivity : BaseActivity() {
    private var mItemTableAdapter: ItemTableAdapter? = null
    private val womttrViewModel: WOMTTRViewModel by viewModels()
    private val menuList = ArrayList<ItemTable>()
    private var dataAVGa = ArrayList<Int>()
    private var dataAVGTotal = ArrayList<Int>()
    private var dataAVGb = ArrayList<Int>()
    private var dataAVGc = ArrayList<Int>()
    private var dataAVGd = ArrayList<Int>()
    private var dataAVGe = ArrayList<Int>()
    private var scoreList = ArrayList<ItemTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_inventory)
        setupRVOne()
        setupUI()
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.womttr)
        clOne.hide()
        clTwo.hide()
        clThree.show()
        cvChartThree.show()
        tvLabelCharOne.text = "< 10 mnt"
        tvLabelCharTwo.text = "10-60 min"
        tvLabelCharThree.text = "1-4 hrs"
        tvLabelCharFour.text = "4-24 hrs"
        tvLabelCharFive.text = "Above 24 hrs"

        getWOMTTR()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRVOne() {
        tvTitleThree.text = getString(R.string.wo_mttr)
        val recyclerview = rvThree
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTableAdapter = ItemTableAdapter(this)
        recyclerview.adapter = mItemTableAdapter

        mItemTableAdapter?.clearList()
    }

    private fun getWOMTTR() {
        womttrViewModel.setWOMTTR()
        womttrViewModel.womttrData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { pmeSum ->
                            if (pmeSum.success == true) {
                                menuList.add(
                                    ItemTable(
                                        "Region",
                                        "< 10 min",
                                        "10-60 min",
                                        "1-4 hrs",
                                        "4-24 hrs",
                                        "Above 24 hrs",
                                        "Average",
                                        "",
                                        "",
                                        "",
                                        "normal",
                                        R.color.color_grey
                                    )
                                )

                                for (data in pmeSum.cvWomttrRep!!) {
                                    data?.a?.let { dataAVGTotal.add(replacePoint(replaceComma(data.a))!!.toInt()) }
                                    data?.b?.let { dataAVGTotal.add(replacePoint(replaceComma(data.b))!!.toInt()) }
                                    data?.c?.let { dataAVGTotal.add(replacePoint(replaceComma(data.c))!!.toInt()) }
                                    data?.d?.let { dataAVGTotal.add(replacePoint(replaceComma(data.d))!!.toInt()) }
                                    data?.e?.let { dataAVGTotal.add(replacePoint(replaceComma(data.e))!!.toInt()) }

                                    menuList.add(
                                        ItemTable(
                                            data?.regionName,
                                            replaceComma(data?.a),
                                            replaceComma(data?.b),
                                            replaceComma(data?.c),
                                            replaceComma(data?.d),
                                            replaceComma(data?.e),
                                            defaultFormat(dataAVGTotal.average()),
                                            "",
                                            "",
                                            "",
                                            "bold",
                                            R.color.color_grey
                                        )
                                    )
                                    data?.a?.let { dataAVGa.add(replacePoint(replaceComma(data.a))!!.toInt()) }
                                    data?.b?.let { dataAVGb.add(replacePoint(replaceComma(data.b))!!.toInt()) }
                                    data?.c?.let { dataAVGc.add(replacePoint(replaceComma(data.c))!!.toInt()) }
                                    data?.d?.let { dataAVGd.add(replacePoint(replaceComma(data.d))!!.toInt()) }
                                    data?.e?.let { dataAVGe.add(replacePoint(replaceComma(data.e))!!.toInt()) }

                                    scoreList.add(
                                        ItemTable(
                                            data?.regionName,
                                            replacePoint(replaceComma(data?.a)),
                                            replacePoint(replaceComma(data?.b)),
                                            replacePoint(replaceComma(data?.c)),
                                            replacePoint(replaceComma(data?.d)),
                                            replacePoint(replaceComma(data?.e)),
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
                                        "Average",
                                        defaultFormat(dataAVGa.average()),
                                        defaultFormat(dataAVGb.average()),
                                        defaultFormat(dataAVGc.average()),
                                        defaultFormat(dataAVGd.average()),
                                        defaultFormat(dataAVGe.average()),
                                        " ",
                                        "",
                                        "",
                                        "",
                                        "bold",
                                        R.color.color_grey
                                    )
                                )

                                mItemTableAdapter?.updateList(menuList)
                                initBarChart()
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

    private fun initBarChart() {
        val barChart = findViewById<BarChart>(R.id.barChartThree)

        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            entries.add(
                BarEntry(
                    i.toFloat(),
                    floatArrayOf(
                        score.typeTwo!!.toFloat(),
                        score.typeThree!!.toFloat(),
                        score.typeFour!!.toFloat(),
                        score.typeFive!!.toFloat(),
                        score.typeSix!!.toFloat(),
                    )
                )
            )
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(
            resources.getColor(R.color.color_one_stackchart),
            resources.getColor(R.color.color_two_stackchart),
            resources.getColor(R.color.color_three_stackchart),
            resources.getColor(R.color.color_four_stackchart),
            resources.getColor(R.color.color_five_stackchart)
        )
//        barDataSet.stackLabels = arrayOf("<10 min", "10-60 min", "1-4 hrs", "4-24 hrs","Above 24 hrs")

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