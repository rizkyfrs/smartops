package com.smartfren.smartops.ui.availability

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
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
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.webch.DataPeriodModel
import com.smartfren.smartops.ui.webch.WebCHAvailabilityViewModel
import com.smartfren.smartops.utils.CustomMarker
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_linechart.*
import kotlinx.android.synthetic.main.dashboard.tvTtlAvail
import kotlinx.android.synthetic.main.dashboard.tvTtlPopulation
import kotlinx.android.synthetic.main.dashboard.tvValueChartCurrent
import kotlinx.android.synthetic.main.dashboard.tvValueChartDown
import kotlinx.android.synthetic.main.dashboard.tvValueChartTop
import kotlinx.android.synthetic.main.dashboard.tvValueChartTwoCurrent
import kotlinx.android.synthetic.main.dashboard.tvValueChartTwoDown
import kotlinx.android.synthetic.main.dashboard.tvValueChartTwoTop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DecimalFormat

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AvailabilityActivity : BaseActivity(), OnChartValueSelectedListener {
    private var token: String? = null
    private var periods = ArrayList<DataPeriodModel>()
    private val defaultFormat = DecimalFormat("#.##")
    private val webCHAvailabilityViewModel: WebCHAvailabilityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linechart)
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        setupUI()
    }

    private fun setupUI() {
        setupChart("0", "1")

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnFilter.setOnClickListener {
            dialogFilter()
        }
    }

    private fun setupChart(region: String?, period: String?) {
        defaultFormat.maximumFractionDigits = 2

        webCHAvailabilityViewModel.setRegionSelected(region!!.toInt())
        webCHAvailabilityViewModel.setPeriodSelected(period!!.toInt())

        getListWebCHAvailability(
            token,
            region,
            period
        )

        tvTtlAvail.text =
            "Trend of Availability\n${webCHAvailabilityViewModel.setNamePeriod()} " +
                    "(${webCHAvailabilityViewModel.setNameRegion()})"

        tvTtlPopulation.text =
            "Trend of Population\n${webCHAvailabilityViewModel.setNamePeriod()} " +
                    "(${webCHAvailabilityViewModel.setNameRegion()})"
    }

    private fun initLineChart(minim: Float, max: Float, current: Float, chart: String) {
        val line: LineChart? = if (chart == "true") {
            findViewById(R.id.linechart)
        } else {
            findViewById(R.id.linechartPopulation)
        }
        //hide grid lines
        line?.axisLeft?.setDrawGridLines(true)
        val xAxis: XAxis = line!!.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        line.axisRight?.isEnabled = false

        // set listeners
        line.setOnChartValueSelectedListener(this)

        //remove legend
        line.legend?.isEnabled = false

        //remove description label
        line.description?.isEnabled = false
        line.setExtraOffsets(0f, 0f, 0f, 10f)

        // create marker to display box when values are selected
        val markerView = CustomMarker(this, R.layout.marker_view)

        // Set the marker to the chart
        line.marker = markerView

        //add animation
        line.animateX(1000, Easing.EaseInSine)

        //add threshold line
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = line.axisLeft!!

            // disable dual axis (only use LEFT axis)
            line.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(0f, 0f, 0f)

            // axis range
            if (chart == "true") {
                yAxis.axisMaximum = 100.000f
                yAxis.axisMinimum = minim - 00.20f
            } else if (chart == "false") {
                yAxis.axisMaximum = 100.00f
                yAxis.axisMinimum = minim - 2f
            }
        }
        run {
            //Create Limit Lines
            val ll1: LimitLine = if (chart == "true") {
                LimitLine(99.850f, "")
            } else {
                LimitLine(94.00f, "")
            }
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

        if (chart == "true") {
            (defaultFormat.format(minim).toString() + "%").also { tvValueChartDown.text = it }
            (defaultFormat.format(max).toString() + "%").also { tvValueChartTop.text = it }
            (defaultFormat.format(current).toString() + "%").also { tvValueChartCurrent.text = it }
        } else {
            (defaultFormat.format(minim).toString() + "%").also { tvValueChartTwoDown.text = it }
            (defaultFormat.format(max).toString() + "%").also { tvValueChartTwoTop.text = it }
            (defaultFormat.format(current).toString() + "%").also {
                tvValueChartTwoCurrent.text = it
            }
        }
    }

    private fun setDataToLineChart(chart: String?) {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        val line: LineChart? = if (chart == "true") {
            findViewById(R.id.linechart)
        } else {
            findViewById(R.id.linechartPopulation)
        }

        if (chart == "true") {
            for (i in periods.indices) {
                val data = periods[i]
                entries.add(Entry(i.toFloat(), data.number!!.toFloat()))
            }
        } else if (chart == "false") {
            for (i in periods.indices) {
                val data = periods[i]
                entries.add(Entry(i.toFloat(), data.populate!!.toFloat()))
            }
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        line?.data = data
        line?.data?.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${defaultFormat.format(value.toDouble())}%"
            }
        })

        lineDataSet.setDrawValues(false)

        // set the filled area
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider ->
            line?.axisLeft!!.axisMinimum
        }

        val drawable = ContextCompat.getDrawable(this, R.drawable.color_gradient_chart)
        lineDataSet.fillDrawable = drawable

        // black lines and points
        lineDataSet.color = resources.getColor(R.color.color_pink)
        lineDataSet.setCircleColor(resources.getColor(R.color.color_pink))

        // line thickness and point size
        lineDataSet.lineWidth = 1.5f
        lineDataSet.circleRadius = 2f

        // draw points as solid circles
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawCircles(true)

        line?.invalidate()
    }

    private fun getListWebCHAvailability(token: String?, region: String?, period: String?) {
        periods.clear()
        webCHAvailabilityViewModel.listWebAvailability(token, region, period)
        webCHAvailabilityViewModel.webCHAvailabilityData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { webCHAvailResponse ->
                            if (webCHAvailResponse.getReport()?.success == true) {
                                for (data in webCHAvailResponse.getReport()?.data!!) {
                                    try {
                                        periods.add(
                                            DataPeriodModel(
                                                data.period,
                                                defaultFormat.format(data.avail!! * 100).toDouble(),
                                                defaultFormat.format(data.populate!! * 100).toDouble(),
                                            )
                                        )
                                    } catch (nfe: NumberFormatException) {
                                        Log.e("Error","NumberFormat Exception: invalid input string")
                                    }
                                }

                                if (webCHAvailResponse.getReport()?.data!!.isNotEmpty()) {
                                    val getItemBottomAvail =
                                        webCHAvailResponse.getReport()?.data?.minOf { it -> it.avail!! * 100 }
                                    val getItemTopAvail =
                                        webCHAvailResponse.getReport()?.data?.maxOf { it -> it.avail!! * 100 }
                                    val getItemBottomPopulate =
                                        webCHAvailResponse.getReport()?.data?.minOf { it -> it.populate!! * 100 }
                                    val getItemTopPopulate =
                                        webCHAvailResponse.getReport()?.data?.maxOf { it -> it.populate!! * 100 }
                                    val getItemLastAvail =
                                        webCHAvailResponse.getReport()?.data?.last()?.avail!! * 100
                                    val getItemLastPopulate =
                                        webCHAvailResponse.getReport()?.data?.last()?.populate!! * 100

                                    initLineChart(
                                        getItemBottomAvail!!.toFloat(),
                                        getItemTopAvail!!.toFloat(),
                                        getItemLastAvail.toFloat(),
                                        "true"
                                    )

                                    initLineChart(
                                        getItemBottomPopulate!!.toFloat(),
                                        getItemTopPopulate!!.toFloat(),
                                        getItemLastPopulate.toFloat(),
                                        "false"
                                    )
                                }

                                setDataToLineChart("true")
                                setDataToLineChart("false")
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

    private fun dialogFilter() {
        val dialog = Dialog(this)
        var selected: String? = null
        var regions: String? = null

        dialog.setContentView(R.layout.pop_up_filter)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val buttonOk: Button = dialog.findViewById<View>(R.id.btnOk) as Button
        val buttonCancel: Button = dialog.findViewById<View>(R.id.btnCancel) as Button
        val rbDaily: RadioButton = dialog.findViewById<View>(R.id.rbDaily) as RadioButton
        val rbWeekly: RadioButton = dialog.findViewById<View>(R.id.rbWeekly) as RadioButton
        val rbMonthly: RadioButton = dialog.findViewById<View>(R.id.rbMonthly) as RadioButton
        val rbAllRegion: RadioButton = dialog.findViewById<View>(R.id.rbAllRegion) as RadioButton
        val rbJB: RadioButton = dialog.findViewById<View>(R.id.rbJB) as RadioButton
        val rbNS: RadioButton = dialog.findViewById<View>(R.id.rbNS) as RadioButton
        val rbSS: RadioButton = dialog.findViewById<View>(R.id.rbSS) as RadioButton
        val rbWJ: RadioButton = dialog.findViewById<View>(R.id.rbWJ) as RadioButton
        val rbCJ: RadioButton = dialog.findViewById<View>(R.id.rbCJ) as RadioButton
        val rbEJ: RadioButton = dialog.findViewById<View>(R.id.rbEJ) as RadioButton
        val rbSK: RadioButton = dialog.findViewById<View>(R.id.rbSK) as RadioButton
        val rbRegionOne: RadioGroup = dialog.findViewById<View>(R.id.regionOne) as RadioGroup
        val rbRegionTwo: RadioGroup = dialog.findViewById<View>(R.id.regionTwo) as RadioGroup

        rbRegionOne.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i != -1) rbRegionTwo.check(
                -1
            )
        })


        rbRegionTwo.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i != -1) rbRegionOne.check(
                -1
            )
        })

        buttonOk.setOnClickListener {
            when {
                rbAllRegion.isChecked -> {
                    regions = "0"
                }
                rbJB.isChecked -> {
                    regions = "1"
                }
                rbNS.isChecked -> {
                    regions = "2"
                }
                rbSS.isChecked -> {
                    regions = "3"
                }
                rbWJ.isChecked -> {
                    regions = "4"
                }
                rbCJ.isChecked -> {
                    regions = "5"
                }
                rbEJ.isChecked -> {
                    regions = "6"
                }
                rbSK.isChecked -> {
                    regions = "7"
                }
            }

            when {
                rbDaily.isChecked -> {
                    selected = "1"
                }
                rbWeekly.isChecked -> {
                    selected = "2"
                }
                rbMonthly.isChecked -> {
                    selected = "3"
                }
            }

            when {
                regions == null -> {
                    toast(this, getString(R.string.please_select_region))
                }
                selected == null -> {
                    toast(this, getString(R.string.please_select_period))
                }
                else -> {
                    setupChart(regions, selected)
                    dialog.dismiss()
                }
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}