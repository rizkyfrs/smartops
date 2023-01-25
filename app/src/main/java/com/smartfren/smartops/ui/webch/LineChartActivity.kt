package com.smartfren.smartops.ui.webch

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.CustomMarker
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_linechart.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DecimalFormat

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LineChartActivity : BaseActivity(), OnChartValueSelectedListener {
    private val defaultFormat = DecimalFormat("#.##")
    private var periods = ArrayList<DataPeriodModel>()
    private val webCHAvailabilityViewModel: WebCHAvailabilityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linechart)
        setupUI()
    }

    private fun setupUI() {
        defaultFormat.maximumFractionDigits = 2
        tvTitleAppBar.text =
            "Availability Report : ${intent.getStringExtra("nameregion")}/${intent.getStringExtra("nameperiod")} "
        tvTtlAvail.text = "Trend of Availability - ${intent.getStringExtra("nameperiod")} " +
                "(${intent.getStringExtra("nameregion")})"

        tvTtlPopulation.text = "Trend of Population - ${intent.getStringExtra("nameperiod")} " +
                "(${intent.getStringExtra("nameregion")})"

        getListWebCHAvailability(
            intent.getStringExtra("token"),
            intent.getStringExtra("region"),
            intent.getStringExtra("period")
        )

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initLineChart() {
        //hide grid lines
        linechart.axisLeft.setDrawGridLines(true)
        val xAxis: XAxis = linechart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        linechart.axisRight.isEnabled = false

        // set listeners
        linechart.setOnChartValueSelectedListener(this)

        //remove legend
        linechart.legend.isEnabled = false

        //remove description label
        linechart.description.isEnabled = false
        linechart.setExtraOffsets(0f, 0f, 0f, 10f)

        // create marker to display box when values are selected
        val markerView = CustomMarker(this, R.layout.marker_view)

        // Set the marker to the chart
        linechart.marker = markerView

        //add animation
        linechart.animateX(1000, Easing.EaseInSine)

        //add threshold line
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = linechart.axisLeft

            // disable dual axis (only use LEFT axis)
            linechart.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(0f, 0f, 0f)

            // axis range
            yAxis.axisMaximum = 100.000f
//            yAxis.axisMinimum = 98.500f
        }
        run {
            // // Create Limit Lines // //
            val ll1 = LimitLine(99.850f, "")
            ll1.lineWidth = 1.5f
            ll1.enableDashedLine(10f, 5f, 0f)
            ll1.textSize = 14f

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(false)
            xAxis.setDrawLimitLinesBehindData(false)

            // add limit lines
            yAxis.addLimitLine(ll1)
        }

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -90f
//        xAxis.spaceMax = 2f
//        xAxis.spaceMin = 2f
    }

    private fun initLineChartPeriod(minim: Float) {
        //hide grid lines
        linechartPopulation.axisLeft.setDrawGridLines(true)
        val xAxis: XAxis = linechartPopulation.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        linechartPopulation.axisRight.isEnabled = false

        //remove legend
        linechartPopulation.legend.isEnabled = false
        linechartPopulation.setExtraOffsets(0f, 0f, 0f, 10f)

        // create marker to display box when values are selected
        val markerView = CustomMarker(this, R.layout.marker_view)

        // Set the marker to the chart
        linechartPopulation.marker = markerView

        //remove description label
        linechartPopulation.description.isEnabled = false

        //add animation
        linechartPopulation.animateX(1000, Easing.EaseInSine)

        //add threshold line
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = linechartPopulation.axisLeft

            // disable dual axis (only use LEFT axis)
            linechartPopulation.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(0f, 0f, 0f)

            // axis range
            yAxis.axisMaximum = 100.00f
            yAxis.axisMinimum = minim
        }
        run {
            // // Create Limit Lines // //
            val ll1 = LimitLine(93.00f, "")
            ll1.lineWidth = 1.5f
            ll1.enableDashedLine(10f, 5f, 0f)
            ll1.textSize = 14f

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(false)
            xAxis.setDrawLimitLinesBehindData(false)

            // add limit lines
            yAxis.addLimitLine(ll1)
        }

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -90f
//        xAxis.spaceMax = 2f
//        xAxis.spaceMin = 2f
    }

    private fun setDataToLineChart() {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in periods.indices) {
            val data = periods[i]
            entries.add(Entry(i.toFloat(), data.number!!.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        linechart.data = data
        linechart.data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${defaultFormat.format(value.toDouble())}%"
            }
        })

        lineDataSet.setDrawValues(true)

        // set the filled area
        lineDataSet.setDrawFilled(false)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider ->
            linechart.axisLeft.axisMinimum
        }

        val drawable = ContextCompat.getDrawable(this, R.drawable.color_gradient_chart)
        lineDataSet.fillDrawable = drawable

        // black lines and points
        lineDataSet.color = resources.getColor(R.color.black)
        lineDataSet.setCircleColor(resources.getColor(R.color.black))

        // line thickness and point size
        lineDataSet.lineWidth = 1.5f
        lineDataSet.circleRadius = 2f

        // draw points as solid circles
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawCircles(true)

        linechart.invalidate()
    }

    private fun setDataToLineChartPopulate() {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in periods.indices) {
            val data = periods[i]
            entries.add(Entry(i.toFloat(), data.populate!!.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        linechartPopulation.data = data
        linechartPopulation.data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${defaultFormat.format(value.toDouble())}%"
            }
        })

        lineDataSet.setDrawValues(true)

        // set the filled area
        lineDataSet.setDrawFilled(false)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider ->
            linechartPopulation.axisLeft.axisMinimum
        }

        val drawable = ContextCompat.getDrawable(this, R.drawable.color_gradient_chart)
        lineDataSet.fillDrawable = drawable

        // black lines and points
        lineDataSet.color = resources.getColor(R.color.black)
        lineDataSet.setCircleColor(resources.getColor(R.color.black))

        // line thickness and point size
        lineDataSet.lineWidth = 1.5f
        lineDataSet.circleRadius = 2f

        // draw points as solid circles
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawCircles(true)

        linechartPopulation.invalidate()
    }

    private fun getListWebCHAvailability(token: String?, region: String?, period: String?) {
        webCHAvailabilityViewModel.listWebAvailability(token, region, period)
        webCHAvailabilityViewModel.webCHAvailabilityData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { webCHAvailResponse ->
                            if (webCHAvailResponse.getReport()?.success == true) {
                                for (data in webCHAvailResponse.getReport()?.data!!) {
                                    periods.add(
                                        DataPeriodModel(
                                            data.period,
                                            defaultFormat.format(data.avail!! * 100).toDouble(),
                                            defaultFormat.format(data.populate!! * 100).toDouble(),
                                        )
                                    )
                                }

                                if (webCHAvailResponse.getReport()?.data!!.isNotEmpty()) {
                                    initLineChart()
                                    val getItemPopulate =
                                        webCHAvailResponse.getReport()?.data?.minOf { it -> it.populate!! * 100 }
                                    initLineChartPeriod(getItemPopulate!!.toFloat() - 2f)
                                }

                                setDataToLineChart()
                                setDataToLineChartPopulate()
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

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.e("Entry selected", e.toString())
    }

    override fun onNothingSelected() {

        Log.e("Nothing selected", "Nothing selected.")
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
            val index = value.toInt()
            return if (index < periods.size) {
                periods[index].bulan
            } else {
                ""
            }
        }
    }

}