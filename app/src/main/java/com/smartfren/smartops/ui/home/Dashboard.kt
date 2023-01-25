package com.smartfren.smartops.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
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
import com.smartfren.smartops.ui.availability.AvailabilityActivity
import com.smartfren.smartops.ui.base.BaseFragment
import com.smartfren.smartops.ui.ccf.CCFActivity
import com.smartfren.smartops.ui.corefacility.MenuCoreFacilityActivity
import com.smartfren.smartops.ui.dashboard.MenuDashboardActivity
import com.smartfren.smartops.ui.datahub.MenuDataHubActivity
import com.smartfren.smartops.ui.gis.CustomMarkerClusteringDemoActivity
import com.smartfren.smartops.ui.login.LoginActivity
import com.smartfren.smartops.ui.material.MenuMaterialActivity
import com.smartfren.smartops.ui.networkinventory.NetworkInventoryActivity
import com.smartfren.smartops.ui.postmortememergency.PMEActivity
import com.smartfren.smartops.ui.regionaloperation.MenuRegionalOperationActivity
import com.smartfren.smartops.ui.regionaloperation.MenuRegionalOperationNewActivity
import com.smartfren.smartops.ui.sitemanagementinfo.SiteManagementInfoActivity
import com.smartfren.smartops.ui.stom.MenuSTOMActivity
import com.smartfren.smartops.ui.tracker.MenuTrackerActivity
import com.smartfren.smartops.ui.webch.DataPeriodModel
import com.smartfren.smartops.ui.webch.WebCHAvailabilityViewModel
import com.smartfren.smartops.ui.womttr.WOMTTRActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.navigation_menu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DecimalFormat

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class Dashboard : BaseFragment(), OnChartValueSelectedListener {
    private var token: String? = null
    private var type: String? = null
    private val defaultFormat = DecimalFormat("#.##")
    private var periods = ArrayList<DataPeriodModel>()
    private val homepageViewModel: HomepageViewModel by viewModels()
    private val webCHAvailabilityViewModel: WebCHAvailabilityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_layout, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
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

        if (type == "666" || type == "501" || type == "20" || type == "32" || type == "3373") {
            btnDashboard.show()
            btnDashboard.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuDashboardActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnDashboard.hide()
        }

        if (type == "666") {
            btnTracker.show()
            btnStom.show()
            tvTitleBtnStom.text = getString(R.string.material)
            btnResourceManagement.show()
            btnSmartGIS.show()
            btnPDB.show()
            btnNOC.show()
            btnDigOperation.show()

            btnTracker.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuTrackerActivity::class.java)
                startActivity(intent)
            }

            btnRegOperation.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuRegionalOperationNewActivity::class.java)
                startActivity(intent)
            }

            btnStom.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuMaterialActivity::class.java)
                startActivity(intent)
            }

            btnResourceManagement.setOnClickListener {
//                val intent = Intent(context?.applicationContext, MenuResourceManagementActivity::class.java)
//                startActivity(intent)
                showDialogFeature("Feature Not Available", "This feature not available for now.")
            }

            btnDigOperation.setOnClickListener {
//                val intent = Intent(context?.applicationContext, MenuOperationAutomationActivity::class.java)
//                startActivity(intent)
                showDialogFeature("Feature Not Available", "This feature not available for now.")
            }

            btnNOC.setOnClickListener {
//                val intent = Intent(context?.applicationContext, MenuNOCActivity::class.java)
//                startActivity(intent)
                showDialogFeature("Feature Not Available", "This feature not available for now.")
            }

            btnPDB.setOnClickListener {
                showDialogFeature("Feature Not Available", "This feature not available for now.")
            }

            btnSmartGIS.setOnClickListener {
                val intent = Intent(context?.applicationContext, CustomMarkerClusteringDemoActivity::class.java)
                startActivity(intent)
            }
        }

        if (type == "501") {
            btnStom.show()
            btnStom.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuSTOMActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnStom.hide()
        }

        if (type == "20" || type == "32" || type == "40" || type == "34" || type == "35" || type == "95" ) {
            btnRegOperation.show()
            btnRegOperation.setOnClickListener {
                val intent =
                    Intent(context?.applicationContext, MenuRegionalOperationActivity::class.java)
                startActivity(intent)
            }
        } else {
//            btnRegOperation.hide()
        }

        if (type == "666" || type == "501" || type == "20" || type == "32" || type == "40" ||
            type == "34"
        ) {
            btnDataHub.show()
            btnDataHub.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuDataHubActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnDataHub.hide()
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(context?.applicationContext, PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        ivProfile.setOnClickListener {
            val intent = Intent(context?.applicationContext, PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        btnSiteInfo.setOnClickListener {
            val intent =
                Intent(context?.applicationContext, SiteManagementInfoActivity::class.java)
            startActivity(intent)
        }

        btnChangePassword.setOnClickListener {
            val intent = Intent(context?.applicationContext, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        btnCoreFacility.setOnClickListener {
            val intent = Intent(context?.applicationContext, MenuCoreFacilityActivity::class.java)
            startActivity(intent)
        }

        btnMenu.setOnClickListener {
            drawer_layout.openDrawer(Gravity.LEFT)
        }

        btnSignOut.setOnClickListener {
            token?.let { it1 -> postLogout(it1) }
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
//            val intent = Intent(context?.applicationContext, TrackerRiskActivity::class.java)
//            startActivity(intent)
            showDialogFeature("Feature Not Available", "This feature not available for now.")
        }
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
                                val sourceString =
                                    "Hello, <b> ${personalInfoResponse.info?.get(0)?.pICName}</b>"
                                tvUsernames.text = Html.fromHtml(sourceString)
                                tvRegionals.text =
                                    "Regional ${personalInfoResponse.info?.get(0)?.regionName}"
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
                                    periods.add(
                                        DataPeriodModel(
                                            data.period,
                                            defaultFormat.format(data.avail!! * 100).toDouble(),
                                            defaultFormat.format(data.populate!! * 100).toDouble(),
                                        )
                                    )
                                }

                                if (webCHAvailResponse.getReport()?.data!!.isNotEmpty()) {
                                    val getItemBottomAvail = webCHAvailResponse.getReport()?.data?.minOf { it -> it.avail!! * 100 }
                                    val getItemTopAvail = webCHAvailResponse.getReport()?.data?.maxOf { it -> it.avail!! * 100 }
                                    val getItemBottomPopulate = webCHAvailResponse.getReport()?.data?.minOf { it -> it.populate!! * 100 }
                                    val getItemTopPopulate = webCHAvailResponse.getReport()?.data?.maxOf { it -> it.populate!! * 100 }
                                    val getItemLastAvail = webCHAvailResponse.getReport()?.data?.last()?.avail!! * 100
                                    val getItemLastPopulate = webCHAvailResponse.getReport()?.data?.last()?.populate!! * 100

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

    private fun showDialogFeature(titles: String?, descs: String?) {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val btnOk = dialog.findViewById(R.id.btnOk) as Button
        val imgView = dialog.findViewById(R.id.imgSuccess) as ImageView
        val title = dialog.findViewById(R.id.tvTitleSuccess) as TextView
        val desc = dialog.findViewById(R.id.tvSuccess) as TextView

        imgView.setImageDrawable(resources.getDrawable(R.drawable.ic_error))
        imgView.imageTintList = resources.getColorStateList(R.color.color_pink)
        if (titles != null && titles != "") title.text = titles
        if (descs != null && descs != "") desc.text = descs

        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        fun newInstance(): Dashboard {
            return Dashboard()
        }
    }
}