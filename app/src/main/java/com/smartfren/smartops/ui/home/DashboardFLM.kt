package com.smartfren.smartops.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.smartfren.smartops.BuildConfig
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseFragment
import com.smartfren.smartops.ui.corefacility.MenuCoreFacilityActivity
import com.smartfren.smartops.ui.dailyactivity.*
import com.smartfren.smartops.ui.dailyactivity.adapter.DailyActivityAdapter
import com.smartfren.smartops.ui.dashboard.MenuDashboardActivity
import com.smartfren.smartops.ui.datahub.MenuDataHubActivity
import com.smartfren.smartops.ui.faultmanagement.MenuFaultManagementActivity
import com.smartfren.smartops.ui.login.LoginActivity
import com.smartfren.smartops.ui.regionaloperation.MenuRegionalOperationActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard_flm.*
import kotlinx.android.synthetic.main.navigation_layout_flm.*
import kotlinx.android.synthetic.main.navigation_menu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardFLM : BaseFragment(), OnMapReadyCallback {
    private var mTaskList: DailyActivityAdapter? = null
    private var token: String? = null
    private var dataList = ArrayList<PojoOfJsonArray>()
    private val consolidatedList = mutableListOf<ListItem>()
    private val homepageViewModel: HomepageViewModel by viewModels()
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private var checkInId: String? = null
    private var region: String? = null
    private var lat: String? = null
    private var long: String? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var siteID: String? = ""
    private var type: String? = null
    private var map: MapboxMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.navigation_layout_flm, null, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.viewLifecycleOwner.lifecycleScope.launch {
            Mapbox.getInstance(requireContext(), BuildConfig.MAPBOX_ACCESS_TOKEN)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@DashboardFLM)
            setupUI()
        }
    }

    override fun onResume() {
        super.onResume()
        token?.let { it1 ->
            getPersonalInfo(it1)
            if (type == "35" || type == "21") {
                getFLMStatistic(it1, "personal")
            } else if (type == "34") {
                clTaskFLM.invisible()
                rvTaskFLM.hide()
                getFLMStatistic(it1, "team")
            } else if (type == "40" || type == "32") {
                clTaskFLM.invisible()
                rvTaskFLM.hide()
                vDisableSiteVisit.show()
                vDisableTagging.show()
                getFLMStatistic(it1, "region")
                btnCheckIn.setOnClickListener { }
                btnCheckOut.setOnClickListener { }
                btnAvail.setOnClickListener { }
                btnOnLeave.setOnClickListener { }
            }
        }
        Stopwatch.addUpdateListener(updateListener)
    }

    override fun onPause() {
        super.onPause()
        Stopwatch.removeUpdateListener(updateListener)
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        type = MainApp.instance.sharedPreferences?.getString("levelUserID", "")
        tvVersion.text = "v${BuildConfig.VERSION_NAME}"

        btnStom.hide()
        btnSiteInfo.hide()
        btnDigOperation.show()
        tvDigOpertion.text = "Fault Management"
        icDigOperation.setImageDrawable(resources.getDrawable(R.drawable.ic_report))
        icDigOperation.imageTintList = resources.getColorStateList(R.color.white)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

        if (type == "32") {
            btnDashboard.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuDashboardActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnDashboard.setOnClickListener {
                drawer_layout.closeDrawer(Gravity.LEFT)
            }
        }

        if (type == "32" || type == "40" || type == "34") {
            btnDataHub.show()
            btnDataHub.setOnClickListener {
                val intent = Intent(context?.applicationContext, MenuDataHubActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnDataHub.hide()
        }

        clDeviceStatusTop.setOnClickListener {
            clDialogDeviceStatus.show()
        }

        btnOke.setOnClickListener {
            clDialogDeviceStatus.hide()
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(context?.applicationContext, PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        ivProfile.setOnClickListener {
            val intent = Intent(context?.applicationContext, PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        btnRegOperation.setOnClickListener {
            val intent =
                Intent(context?.applicationContext, MenuRegionalOperationActivity::class.java)
            startActivity(intent)
        }

        btnDigOperation.setOnClickListener {
            val intent = Intent(context?.applicationContext, MenuFaultManagementActivity::class.java)
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

        btnAvail.setOnClickListener {
            postUpdateTagging(token, "1")
        }

        btnOnLeave.setOnClickListener {
            postUpdateTagging(token, "2")
        }

        btnPermission.setOnClickListener {
            postUpdateTagging(token, "3")
        }

        btnSignOut.setOnClickListener {
            token?.let { it1 -> postLogout(it1) }
        }

        btnCheckIn.setOnClickListener {
            clUpdateTaggingPopup.show()
            token?.let { it -> region?.let { it1 -> getSite(it, it1) } }
        }

        btnClosePopup.setOnClickListener {
            clUpdateTaggingPopup.hide()
        }

        btnSubmitPopup.setOnClickListener {
            val cal = Calendar.getInstance()
            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            when {
                etSiteIdPopup.text.toString().isEmpty() -> {
                    toast(requireContext(), "Please enter Site ID!")
                }
                else -> {
                    postCheckIn(
                        sdf.format(cal.time),
                        region,
                        etSiteIdPopup.text.toString(),
                        "0",
                        getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString()),
                        replacePointToComma(lat),
                        replacePointToComma(long)
                    )
                }
            }
        }

        btnCheckOut.setOnClickListener {
            postCheckOut(
                MainApp.instance.sharedPreferences?.getString("checkinid", ""),
                replacePointToComma(lat),
                replacePointToComma(long)
            )
        }
    }

    private fun setupRV() {
        val recyclerview = rvTaskFLM
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        mTaskList = DailyActivityAdapter(requireContext(), consolidatedList)
        recyclerview.adapter = mTaskList
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
                                if (personalInfoResponse.info?.get(0)?.deviceLastLat != null && personalInfoResponse.info?.get(0)?.deviceLastLong != null) {
                                    location(personalInfoResponse.info?.get(0)?.deviceLastLat, personalInfoResponse.info?.get(0)?.deviceLastLong)
                                }

                                for (profile in personalInfoResponse.info!!) {
                                    if (profile.deviceStatus != null && profile.deviceStatus != "") {
                                        when (profile.deviceStatus) {
                                            "AVAILABLE" -> {
                                                ivDeviceStatusTop.setImageDrawable(resources.getDrawable(R.drawable.device_avail))
                                                ivDeviceStatusTop.imageTintList = resources.getColorStateList(R.color.color_green_done)
                                            }
                                            "ACTIVE" -> {
                                                ivDeviceStatusTop.setImageDrawable(resources.getDrawable(R.drawable.device_avail))
                                                ivDeviceStatusTop.imageTintList = resources.getColorStateList(R.color.color_green_done)
                                            }
                                            "NOT AVAILABLE" -> {
                                                ivDeviceStatusTop.setImageDrawable(resources.getDrawable(R.drawable.device_not_avail))
                                                ivDeviceStatusTop.imageTintList = resources.getColorStateList(R.color.color_red_down)
                                            }
                                            "NO DEVICE" -> {
                                                ivDeviceStatusTop.setImageDrawable(resources.getDrawable(R.drawable.device_not_avail))
                                                ivDeviceStatusTop.imageTintList = resources.getColorStateList(R.color.color_grey)
                                            }
                                            else -> {
                                                ivDeviceStatusTop.setImageDrawable(resources.getDrawable(R.drawable.device_not_avail))
                                                ivDeviceStatusTop.imageTintList = resources.getColorStateList(R.color.color_grey)
                                            }
                                        }
                                        tvDeviceStat.text = profile.deviceStatus
                                        tvDeviceDetStatus.text = profile.deviceStatus
                                    }

                                    if (profile.deviceLocationService != null && profile.deviceLocationService != "") {
                                        when (profile.deviceLocationService) {
                                            "VISIBLE" -> {
                                                ivLocationStatusTops.setImageDrawable(resources.getDrawable(R.drawable.ic_location_visible))
                                                ivLocationStatusTops.imageTintList = resources.getColorStateList(R.color.color_green_done)
                                            }
                                            "NOT VISIBLE" -> {
                                                ivLocationStatusTops.setImageDrawable(resources.getDrawable(R.drawable.ic_location_not_visible))
                                                ivLocationStatusTops.imageTintList = resources.getColorStateList(R.color.color_red_down)
                                            }
                                            else -> {
                                                ivLocationStatusTops.setImageDrawable(resources.getDrawable(R.drawable.ic_location_not_visible))
                                                ivLocationStatusTops.imageTintList = resources.getColorStateList(R.color.color_grey)
                                            }
                                        }
                                        tvDeviceLocation.text = profile.deviceLocationService
                                        tvDeviceLoc.text = profile.deviceLocationService
                                    }

                                    if (profile.deviceLastReporDate != null) tvDeviceLast.text = profile.deviceLastReporDate
                                    if (profile.deviceLastLocationReported != null) tvDeviceLastLoc.text = profile.deviceLastLocationReported
                                    if (profile.deviceLastLocationReportedTime != null) tvDeviceLastLocTime.text =
                                        profile.deviceLastLocationReportedTime
                                    if (profile.deviceLastLat != null) tvDeviceLastLat.text = profile.deviceLastLat
                                    if (profile.deviceLastLong != null) tvDeviceLastLong.text = profile.deviceLastLong
                                }

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

                                when (personalInfoResponse.info?.get(0)?.pICTagging) {
                                    "1" -> {
                                        tvTagging.text = resources.getText(R.string.available)
                                        clCurrentTagging.backgroundTintList = resources.getColorStateList(R.color.color_green_done)
                                        btnAvail.isEnabled = false
                                        btnAvail.backgroundTintList = resources.getColorStateList(R.color.color_close)
                                        btnOnLeave.isEnabled = false
                                        btnOnLeave.backgroundTintList = resources.getColorStateList(R.color.color_close)
                                    }
                                    "2" -> {
                                        tvTagging.text = resources.getText(R.string.on_leave)
                                        clCurrentTagging.backgroundTintList = resources.getColorStateList(R.color.color_red_down)
                                        btnAvail.isEnabled = false
                                        btnAvail.backgroundTintList = resources.getColorStateList(R.color.color_close)
                                        btnOnLeave.isEnabled = false
                                        btnOnLeave.backgroundTintList = resources.getColorStateList(R.color.color_close)
                                    }
                                    "3" -> {
                                        tvTagging.text = resources.getText(R.string.permission)
                                        clCurrentTagging.backgroundTintList = resources.getColorStateList(R.color.color_closed)
                                    }
                                    "0" -> {
                                        if (type == "35" || type == "34" || type == "21") {
                                            dialogTagging()
                                        }
                                    }
                                }

                                if (personalInfoResponse.info?.get(0)?.pICTaggingDate != null) {
                                    tvTaggingDate.text = "Updated on " + parseDateTime(
                                        personalInfoResponse.info?.get(0)?.pICTaggingDate,
                                        "yyyy-MM-dd HH:mm:ss"
                                    )
                                }

                                tvUsername.text = personalInfoResponse.info?.get(0)?.pICName
                                val sourceString = "Hello, <b> ${personalInfoResponse.info?.get(0)?.pICName}</b>"
                                tvUsernames.text = Html.fromHtml(sourceString)
                                tvRegionals.text = "Regional ${personalInfoResponse.info?.get(0)?.regionName}"
                                tvEmail.text = personalInfoResponse.info?.get(0)?.pICMail

                                if (personalInfoResponse.info?.get(0)?.levelID == "35" ||
                                    personalInfoResponse.info?.get(0)?.levelID == "21") {
                                    ivProfile.imageTintList = resources.getColorStateList(R.color.color_grey)
                                    setupRV()
                                    token?.let { it1 -> getListSchedule(it1, "") }

                                    btnAddTask.setOnClickListener {
                                        val intent = Intent(context?.applicationContext, ScheduleAddTaskActivity::class.java)
                                        startActivity(intent)
                                    }
                                }

                                region = personalInfoResponse.info?.get(0)?.regionCode
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

    private fun getFLMStatistic(token: String?, type: String?) {
        homepageViewModel.flmStatistic(token, type)
        homepageViewModel.flmStatisticData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { flmStatisticResponse ->
                            if (flmStatisticResponse?.success == true) {
                                tvSchedule.text = if (!flmStatisticResponse.stats?.get(0)?.totalScheduled.isNullOrEmpty())
                                    flmStatisticResponse.stats?.get(0)?.totalScheduled else "0"
                                tvOnComplete.text = if (!flmStatisticResponse.stats?.get(0)?.totalResolved.isNullOrEmpty())
                                    flmStatisticResponse.stats?.get(0)?.totalResolved else "0"
                                tvPending.text = if (!flmStatisticResponse.stats?.get(0)?.totalPending.isNullOrEmpty())
                                    flmStatisticResponse.stats?.get(0)?.totalPending + " Total Pending" else "0 Total Pending"
                                tvReject.text = if (!flmStatisticResponse.stats?.get(0)?.totalReject.isNullOrEmpty())
                                    flmStatisticResponse.stats?.get(0)?.totalReject + " Total Reject" else "0 Total Reject"
                                tvClose.text = if (!flmStatisticResponse.stats?.get(0)?.totalClosed.isNullOrEmpty())
                                    flmStatisticResponse.stats?.get(0)?.totalClosed + " Total Closed" else "0 Total Closed"
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

    private fun postUpdateTagging(
        token: String?,
        new_tag: String?
    ) {
        homepageViewModel.updateTagging(
            token, new_tag
        )
        homepageViewModel.updateTaggingData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { personalInfoResponse ->
                            if (personalInfoResponse?.success == true) {
                                getPersonalInfo(token)
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

    private fun getListSchedule(token: String?, sort: String?) {
        clearData()
        dailyActivityViewModel.getListSchedule(token, sort)
        dailyActivityViewModel.listSchedule.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loadingBar.hide()
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.getReport()?.success == true) {
                                for (data in scheduleResponse.getReport()?.sites!!) {
                                    if (data.plannedDate == getTimeNow("yyyy-MM-dd")) {
                                        dataList.add(
                                            PojoOfJsonArray(
                                                data.siteID,
                                                parseDateFromBackend(data.plannedDate.toString()),
                                                data.uName,
                                                data.actP0,
                                                data.actP1,
                                                data.actP2,
                                                data.taskPrtg
                                            )
                                        )
                                    }
                                }

                                val groupedMapMap: Map<String?, List<PojoOfJsonArray>> =
                                    dataList.groupBy {
                                        it.date
                                    }

                                for (date: String? in groupedMapMap.keys) {
                                    val dateItem = DateItem()
                                    consolidatedList.add(dateItem)
                                    for (pojoOfJsonArray in groupedMapMap[date]!!) {
                                        val generalItem = GeneralItem(pojoOfJsonArray.name!!)
                                        generalItem.pojoOfJsonArray = pojoOfJsonArray
                                        consolidatedList.add(generalItem)
                                    }
                                }

                                tvTaskToday.text = "Your have ${dataList.size} task for Today"

                                rvTaskFLM.show()
                            }
                            mTaskList?.notifyDataSetChanged()
                        }
                    }

                    is Resource.Error -> {
                        loadingBar.hide()
                        response.message?.let { toast(requireContext(), it) }
                    }

                    is Resource.Loading -> {
                        loadingBar.show()
                    }
                }
            }
        }
    }

    private fun postCheckIn(
        date: String?,
        reg: String?,
        site: String?,
        device_imei: String?,
        device_custodian: String?,
        lat: String?,
        long: String?
    ) {
        homepageViewModel.checkIn(
            date, reg, site, device_imei, device_custodian, lat, long
        )
        homepageViewModel.checkin.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        clUpdateTaggingPopup.hide()
                        response.data?.let { checkinResponse ->
                            if (checkinResponse.success == true) {
                                togglePlayPause()
                                siteID = checkinResponse.cvSvlogPersonal?.svSite
                                MainApp.instance.sharedPreferences?.edit()
                                    ?.putString("checkinid", checkinResponse.cvSvlogPersonal?.svNum)
                                    ?.putString("checkinSiteId", "$siteID")
                                    ?.apply()

                                clCheckInInfo.show()
                                clTitleCaption.hide()

                                btnCheckIn.isEnabled = false
                                btnCheckIn.backgroundTintList = resources.getColorStateList(R.color.color_grey)

                                tvCheckInSiteId.text =
                                    "Site ${checkinResponse.cvSvlogPersonal?.svSite}"
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

    private fun postCheckOut(
        checkinid: String?,
        lat: String?,
        long: String?
    ) {
        homepageViewModel.checkOut(
            checkinid, lat, long
        )
        homepageViewModel.checkout.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { checkinResponse ->
                            if (checkinResponse.success == true) {
                                checkInId = checkinResponse.cvSvlogPersonal?.svNum
                                MainApp.instance.sharedPreferences?.edit()
                                    ?.putString("checkinSiteId", "")
                                    ?.apply()
                                btnCheckIn.isEnabled = true
                                btnCheckIn.backgroundTintList = resources.getColorStateList(R.color.color_green_done)
                                resetStopwatch()

                                clCheckInInfo.hide()
                                clTitleCaption.show()
                            } else {
                                if (checkinResponse.failureMessage == "No records found") {
                                    resetStopwatch()
                                }
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

    private fun clearData() {
        dataList.clear()
        consolidatedList.clear()
    }

    private fun dialogTagging() {
        val dialog = context?.let { Dialog(it) }

        dialog?.setContentView(R.layout.pop_up_tagging)
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val btnAvailable: Button = dialog.findViewById<View>(R.id.btnAvail) as Button
        val btnOnLeave: Button = dialog.findViewById<View>(R.id.btnOnLeave) as Button
        val btnPermission: Button = dialog.findViewById<View>(R.id.btnPermission) as Button

        btnAvailable.setOnClickListener {
            postUpdateTagging(token, "1")
            dialog.dismiss()
        }

        btnOnLeave.setOnClickListener {
            postUpdateTagging(token, "2")
            dialog.dismiss()
        }

        btnPermission.setOnClickListener {
            postUpdateTagging(token, "3")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getSite(token: String, site: String) {
        dailyActivityViewModel.listSite(token, site)
        dailyActivityViewModel.siteData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteResponse ->
                            if (siteResponse.getReport()?.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        for (element in siteResponse.getReport()?.sites!!) {
                                            element.siteId?.let { add(it) }
                                        }
                                    }
                                }

                                val adapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    itemName
                                )
                                etSiteIdPopup.setAdapter(adapter)
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

    private fun dialogEnterSiteID(show: String?) {
        val dialog = context?.let { Dialog(it) }

        dialog?.setContentView(R.layout.pop_up_checkin)
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val btnSubmit: Button = dialog.findViewById<View>(R.id.btnSubmit) as Button
        val btnNo: Button = dialog.findViewById<View>(R.id.btnNo) as Button
        val tvDesc: TextView = dialog.findViewById<View>(R.id.tvDescPopupCheckIn) as TextView
        val etSiteID: AutoCompleteTextView =
            dialog.findViewById<View>(R.id.etSiteId) as AutoCompleteTextView
        val btnClose: ImageView = dialog.findViewById<View>(R.id.btnClose) as ImageView

        if (show == "true") {
            btnSubmit.text = "Yes"
            btnNo.show()
            tvDesc.show()
            etSiteID.hide()
            btnClose.hide()
            btnSubmit.setOnClickListener {
                dialog.dismiss()
            }

            btnNo.setOnClickListener {
                postCheckOut(
                    MainApp.instance.sharedPreferences?.getString("checkinid", ""),
                    replacePointToComma(lat),
                    replacePointToComma(long)
                )
                dialog.dismiss()
            }
        } else {
            btnSubmit.setOnClickListener {
                val cal = Calendar.getInstance()
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)

                when {
                    etSiteID.text.toString().isEmpty() -> {
                        toast(requireContext(), "Please enter Site ID!")
                    }
                    else -> {
                        postCheckIn(
                            sdf.format(cal.time),
                            region,
                            etSiteID.text.toString(),
                            "0",
                            getUserIDJWT(
                                MainApp.instance.sharedPreferences?.getString("tokenJWT", "")
                                    .toString()
                            ),
                            replacePointToComma(lat),
                            replacePointToComma(long)
                        )
                        clCheckInInfo.show()
                        clTitleCaption.hide()
                    }
                }
                dialog.dismiss()
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {
                lat = task.result?.latitude.toString()
                long = task.result?.longitude.toString()
            } else {
                toast(
                    requireContext(),
                    "No location detected. Make sure location is enabled on the device."
                )
            }
        }
    }

    private fun togglePlayPause() {
        Stopwatch.toggle(true)
    }

    private fun resetStopwatch() {
        Stopwatch.reset()

        view.apply {
            tvCheckInSiteId.text = 0L.formatStopwatchTime(false)
        }
    }

    private fun updateDisplayedText(totalTime: Long, useLongerMSFormat: Boolean, schedule: Boolean) {
        tvCheckInSiteId.text =
            "Site ID " + MainApp.instance.sharedPreferences?.getString("checkinSiteId", "") + ": " + totalTime.formatStopwatchTime(useLongerMSFormat)

        if (schedule) {
            dialogEnterSiteID("true")
        }

        if (totalTime != 0L) {
            clCheckInInfo.show()
            clTitleCaption.hide()
            btnCheckIn.isEnabled = false
            btnCheckIn.backgroundTintList = resources.getColorStateList(R.color.color_close)
        } else {
            clTitleCaption.show()
            clCheckInInfo.hide()
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

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                .useDefaultLocationEngine(false)
                .build()
            map?.locationComponent?.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true                       // Enable to make component visible
                cameraMode = CameraMode.TRACKING                        // Set the component's camera mode
                renderMode = RenderMode.COMPASS                         // Set the component's render mode
            }
        }
    }

    private fun location(lats: String?, long: String?) {
        val lat = lats?.toDouble()
        val lng = long?.toDouble()
        val latLng = LatLng(lat!!, lng!!)

        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(13.0) //disable this for not follow zoom
            .tilt(10.0)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(position))
        map?.addMarker(MarkerOptions().position(latLng))
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    companion object {
        fun newInstance(): DashboardFLM {
            return DashboardFLM()
        }
    }
}