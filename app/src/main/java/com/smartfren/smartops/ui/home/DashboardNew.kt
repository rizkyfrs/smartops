package com.smartfren.smartops.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
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
import com.smartfren.smartops.BuildConfig
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.availability.AvailabilityActivity
import com.smartfren.smartops.ui.base.BaseFragment
import com.smartfren.smartops.ui.ccf.CCFActivity
import com.smartfren.smartops.ui.home.adapter.MenuNavAdapter
import com.smartfren.smartops.ui.login.LoginActivity
import com.smartfren.smartops.ui.networkinventory.NetworkInventoryActivity
import com.smartfren.smartops.ui.postmortememergency.PMEActivity
import com.smartfren.smartops.ui.webch.DataPeriodModel
import com.smartfren.smartops.ui.webch.WebCHAvailabilityViewModel
import com.smartfren.smartops.ui.womttr.WOMTTRActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.navigation_menu_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DecimalFormat

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardNew : BaseFragment(), OnChartValueSelectedListener {
    private var token: String? = null
    private var type: String? = null
    private val defaultFormat = DecimalFormat("#.##")
    private var periods = ArrayList<DataPeriodModel>()
    private var mMenuNavAdapter: MenuNavAdapter? = null
    private val homepageViewModel: HomepageViewModel by viewModels()
    private val webCHAvailabilityViewModel: WebCHAvailabilityViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.navigation_layout, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRV()
        setupMenuNav()
    }

    override fun onResume() {
        super.onResume()
        token?.let { it1 -> getPersonalInfo(it1) }
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        type = MainApp.instance.sharedPreferences?.getString("levelUserID", "")
        tvVersion.text = "v${BuildConfig.VERSION_NAME}"
        tvRegionals.hide()

        setupChart("0", "1")

        btnMenu.setOnClickListener {
            drawer_layout.openDrawer(Gravity.LEFT)
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(context?.applicationContext, PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        ivProfile.setOnClickListener {
            val intent = Intent(context?.applicationContext, PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        clSubmenuOne.setOnClickListener {
            val intent = Intent(context?.applicationContext, NetworkInventoryActivity::class.java)
            startActivity(intent)

        }

        clSubmenuTwo.setOnClickListener {
            val intent = Intent(context?.applicationContext, AvailabilityActivity::class.java)
            startActivity(intent)
        }

        clSubmenuThree.setOnClickListener {
            val intent = Intent(context?.applicationContext, WOMTTRActivity::class.java)
            startActivity(intent)
        }

        clSubmenuFour.setOnClickListener {
            val intent = Intent(context?.applicationContext, PMEActivity::class.java)
            startActivity(intent)
        }

        clSubmenuFive.setOnClickListener {
            val intent = Intent(context?.applicationContext, CCFActivity::class.java)
            startActivity(intent)
        }

        clSubmenuSix.setOnClickListener {
            requireActivity().showDialogFeature("Feature Not Available", "This feature not available for now.")
//            val intent = Intent(context?.applicationContext, MainActivity::class.java)
//            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            token?.let { it1 -> postLogout(it1) }
        }
    }

    private fun setupRV() {
        val recyclerview = rvNavBar
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        mMenuNavAdapter = MenuNavAdapter(requireContext())
        recyclerview.adapter = mMenuNavAdapter
    }

    private fun setupMenuNav() {
        mMenuNavAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        menuList.add(ContentMenu(1, "SmartGIS", R.drawable.internet))
        menuList.add(ContentMenu(17, "Test Signal", R.drawable.ic_speed))
        menuList.add(ContentMenu(2, getString(R.string.dashboard), R.drawable.ic_dashboard))
        menuList.add(ContentMenu(3, getString(R.string.data_hub), R.drawable.ic_assignment))
        menuList.add(ContentMenu(4, "Change Management", R.drawable.ic_check_circle))
        menuList.add(ContentMenu(5, "Core & Facility Management", R.drawable.ic_server))
        menuList.add(ContentMenu(6, "Fault Management", R.drawable.ic_report))
        menuList.add(ContentMenu(7, "Fiber Management", R.drawable.ic_account_tree))
        menuList.add(ContentMenu(8, "Material Management", R.drawable.ic_assignment))
        menuList.add(ContentMenu(9, "Reporting Management", R.drawable.ic_folder))
        menuList.add(ContentMenu(10, "Power Management", R.drawable.ic_bolt))
        menuList.add(ContentMenu(11, "Resource Management", R.drawable.ic_lan))
        menuList.add(ContentMenu(12, "Support Management", R.drawable.ic_assignment))
        menuList.add(ContentMenu(13, "Tower Management", R.drawable.ic_cell_tower))
        menuList.add(ContentMenu(14, "Task Management", R.drawable.ic_layers))
        menuList.add(ContentMenu(15, "Work Order Management", R.drawable.ic_confirmation))
        menuList.add(ContentMenu(16, "Change Password", R.drawable.ic_lock))

        mMenuNavAdapter?.updateList(menuList)
    }

    private fun getPersonalInfo(token: String?) {
        homepageViewModel.personalInfo(token)
        homepageViewModel.personalInfoData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { personalInfoResponse ->
                            if (personalInfoResponse?.success == true) {
                                Glide.with(this)
                                    .load(personalInfoResponse.info?.get(0)?.pICPhoto?.let {
                                        removeBackSlash(
                                            it
                                        )
                                    })
                                    .placeholder(R.drawable.img_account)
                                    .priority(Priority.HIGH)
                                    .error(R.drawable.img_account)
                                    .into(ivProfile)

                                Glide.with(this)
                                    .load(personalInfoResponse.info?.get(0)?.pICPhoto?.let {
                                        removeBackSlash(
                                            it
                                        )
                                    })
                                    .placeholder(R.drawable.img_account)
                                    .priority(Priority.HIGH)
                                    .error(R.drawable.img_account)
                                    .into(ivProfileUser)

                                tvUsername.text = personalInfoResponse.info?.get(0)?.pICName
                                val sourceString = "Hello, <b> ${personalInfoResponse.info?.get(0)?.pICName}</b>"
                                tvUsernames.text = Html.fromHtml(sourceString)
                                tvRegionals.text = "Regional ${personalInfoResponse.info?.get(0)?.regionName}"
                                tvEmail.text = personalInfoResponse.info?.get(0)?.pICMail
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let {
                            toast(requireContext(), it)
                        }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun setupChart(region: String?, period: String?) {
        defaultFormat.maximumFractionDigits = 2

        webCHAvailabilityViewModel.setRegionSelected(region!!.toInt())
        webCHAvailabilityViewModel.setPeriodSelected(period!!.toInt())

        getListWebCHAvailability(token, region, period)

        tvTtlAvail.text = "Trend of Availability\n${webCHAvailabilityViewModel.setNamePeriod()} " + "(${webCHAvailabilityViewModel.setNameRegion()})"
        tvTtlPopulation.text =
            "Trend of Population\n${webCHAvailabilityViewModel.setNamePeriod()} " + "(${webCHAvailabilityViewModel.setNameRegion()})"
    }

    private fun initLineChart(minim: Float, max: Float, current: Float, chart: String) {
        val line: LineChart? = if (chart == "true") {
            view?.findViewById(R.id.linechart)
        } else {
            view?.findViewById(R.id.linechartPopulation)
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
        val markerView = CustomMarker(requireContext(), R.layout.marker_view)

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
            (defaultFormat.format(current).toString() + "%").also { tvValueChartTwoCurrent.text = it }
        }
    }

    private fun setDataToLineChart(chart: String?) {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        val line: LineChart? = if (chart == "true") {
            view?.findViewById(R.id.linechart)
        } else {
            view?.findViewById(R.id.linechartPopulation)
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

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.color_gradient_chart)
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
        webCHAvailabilityViewModel.webCHAvailabilityData.observe(viewLifecycleOwner) { event ->
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
                                                if (data.avail != null) defaultFormat.format((data.avail!! * 100)).toDouble() else 0.0,
                                                if (data.populate != null) defaultFormat.format((data.populate!! * 100)).toDouble() else 0.0,
                                            )
                                        )
                                    } catch (nfe: NumberFormatException) {
                                        Log.e("Error","NumberFormat Exception: invalid input string")
                                    }
                                }

                                if (webCHAvailResponse.getReport()?.data!!.isNotEmpty()) {
                                    val getItemBottomAvail =
                                        webCHAvailResponse.getReport()?.data?.minOf { it -> if (it.avail != null) it.avail!! * 100 else 0.0 }
                                    val getItemTopAvail =
                                        webCHAvailResponse.getReport()?.data?.maxOf { it -> if (it.avail != null) it.avail!! * 100 else 0.0 }
                                    val getItemBottomPopulate =
                                        webCHAvailResponse.getReport()?.data?.minOf { it -> if (it.populate != null) it.populate!! * 100 else 0.0 }
                                    val getItemTopPopulate =
                                        webCHAvailResponse.getReport()?.data?.maxOf { it -> if (it.populate != null) it.populate!! * 100 else 0.0 }
                                    val getItemLastAvail =
                                        if (webCHAvailResponse.getReport()?.data?.last()?.avail != null) webCHAvailResponse.getReport()?.data?.last()?.avail!! * 100 else 0.0
                                    val getItemLastPopulate =
                                        if (webCHAvailResponse.getReport()?.data?.last()?.populate != null) webCHAvailResponse.getReport()?.data?.last()?.populate!! * 100 else 0.0

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
                            toast(requireContext(), it)
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

    private fun postLogout(token: String?) {
        homepageViewModel.logout(token)
        homepageViewModel.logout.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { responses ->
                            if (responses.getReport()?.success == true) {
                                MainApp.instance.sharedPreferences?.edit()
                                    ?.putString("token", "")
                                    ?.putString("username", MainApp.instance.sharedPreferences?.getString("username", ""))
                                    ?.putString("password", MainApp.instance.sharedPreferences?.getString("password", ""))
                                    ?.apply()
                                val intent = Intent(context?.applicationContext, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                                val intentService = Intent(context, BackgroundSoundService::class.java)
                                context?.stopService(intentService)
                            }
                        }
                    }

                    is Resource.Error -> {
                        loading(false)
                        response.message?.let { toast(requireContext(), it) }
                    }

                    is Resource.Loading -> {
                        loading(true)
                    }
                }
            }
        }
    }

    private fun togglePlayPause() {
        Stopwatch.toggle(true)
    }

    private fun resetStopwatch() {
        Stopwatch.reset()
    }

    private fun updateDisplayedText(totalTime: Long, useLongerMSFormat: Boolean, schedule: Boolean) {
        if (schedule) {

        }

        if (totalTime != 0L) {

        } else {

        }
    }

    private val updateListener = object : Stopwatch.UpdateListener {
        override fun onUpdate(
            totalTime: Long,
            useLongerMSFormat: Boolean,
            schedule: Boolean
        ) {
            updateDisplayedText(totalTime, useLongerMSFormat, schedule)
        }

        override fun onStateChanged(state: Stopwatch.State) {
//            updateIcons(state)
//            view.stopwatch_lap.beVisibleIf(state == Stopwatch.State.RUNNING)
//            view.stopwatch_reset.beVisibleIf(state != Stopwatch.State.STOPPED)
        }
    }

    companion object {
        fun newInstance(): DashboardNew {
            return DashboardNew()
        }
    }
}