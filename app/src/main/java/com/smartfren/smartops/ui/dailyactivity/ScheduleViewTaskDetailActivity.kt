package com.smartfren.smartops.ui.dailyactivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.bumptech.glide.Glide
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
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.reportpm.ReportSiteActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_detail_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleViewTaskDetailActivity : BaseActivity(), OnMapReadyCallback {
    private var token: String? = null
    private var levelID: String? = null
    private var site: String? = null
    private var gpsStatus: Boolean = false
    private var map: MapboxMap? = null
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.daily_activity_detail_new)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        intent.getStringExtra("activityNumber")?.let {
            token?.let { it1 ->
                site?.let { it2 ->
                    getDetailSchedule(
                        it1,
                        it2,
                        it
                    )
                }
            }
        }
    }

    private fun setupUI() {
        tvSelectDate.text = getString(R.string.reference)
        tvSelectDate.setOnClickListener {
            val intent = Intent(applicationContext, ReportSiteActivity::class.java)
            intent.putExtra("site", site)
            startActivity(intent)
        }
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        levelID = MainApp.instance.sharedPreferences?.getString("levelID", "")
        site = intent.getStringExtra("site")
        if (levelID == "34" || levelID == "40") {
            btnUpdateTask.text = getString(R.string.validation)
            if (intent.getStringExtra("actStatus") == "PROPOSE TO CLOSE") {
                btnUpdateTask.show()
            } else {
                btnUpdateTask.hide()
            }
        }

        if (intent.getStringExtra("liveFLM") == "yes") {
            btnUpdateTask.hide()
        }

        btnBack.setOnClickListener {
            onBackPressed()
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
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
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
        val lat = if (lats?.toDouble()!! > 90.0) 90.0 else lats.toDouble()
        val lng = long?.toDouble()
        val latLng = lat?.let { lng?.let { it1 -> LatLng(it, it1) } }

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

    private fun getDetailSchedule(token: String, site: String, activityNumber: String) {
        dailyActivityViewModel.getActivityTaskDetailSchedule(token, site, activityNumber)
        dailyActivityViewModel.activityTaskDetailSchedule.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.getReport()?.success == true) {
                                for (data in scheduleResponse.getReport()?.task!!) {
                                    if (data.siteID != "") tvInfo.text = data.siteID
                                    if (data.actNum != "") tvActivityNumber.text = data.actNum
                                    if (data.actRegionName != "") tvRegName.text =
                                        data.actRegionName
                                    if (data.domainLev2 != "") tvDomainLev1.text = data.domainLev1
                                    if (data.domainLev2 != "") tvDomainLev2.text = data.domainLev2
                                    if (data.neid != "") tvNEID.text = data.neid
                                    if (data.vendor != "" && data.vendor != null) tvVendor.text =
                                        data.vendor
                                    if (data.woid != "") tvWOID.text = data.woid
                                    if (data.priority != "") {
                                        when (data.priority) {
                                            "P0" -> {
                                                tvPriority.background =
                                                    resources.getDrawable(R.drawable.bg_p0)
                                            }
                                            "P1" -> {
                                                tvPriority.background =
                                                    resources.getDrawable(R.drawable.bg_p1)
                                            }
                                            "P2" -> {
                                                tvPriority.background =
                                                    resources.getDrawable(R.drawable.bg_p2)
                                            }
                                        }
                                        tvPriority.text = data.priority
                                    }
                                    tvActionNeed.text = data.actionNeeded
                                    data.plannedDate?.let {
                                        tvPlannedDate.text = parseDateFromBackend(it)
                                    }
                                    data.actualDate?.let {
                                        tvActualDate.text = parseDateFromBackend(it)
                                    }
                                    if (data.picBefore != null) {
                                        Glide.with(this)
                                            .load(removeBackSlash(data.picBefore.toString()))
                                            .into(ivPhotoBefore)
                                        ivPhotoBeforeDefault.hide()
                                        clPhotoBefore.setOnClickListener {
                                            dialogShowImage(data.picBefore.toString())
                                        }
                                    } else {
                                        ivPhotoBeforeDefault.show()
                                    }
                                    if (data.picAfter != null) {
                                        Glide.with(this)
                                            .load(removeBackSlash(data.picAfter.toString()))
                                            .into(ivPhotoAfter)
                                        ivPhotoAfterDefault.hide()
                                        clPhotoAfter.setOnClickListener {
                                            dialogShowImage(data.picAfter.toString())
                                        }
                                    } else {
                                        ivPhotoAfterDefault.show()
                                    }
                                    if (data.rca1 != null) tvRCALv1.text = data.rca1
                                    if (data.rca2 != null) tvRCALv2.text = data.rca2
                                    if (data.rca3 != null) tvRCALv3.text = data.rca3
                                    if (data.rCAAction != null) tvRCAAction.text = data.rCAAction
                                    if (data.actionDetail != null) tvActionDetail.text =
                                        data.actionDetail
                                    if (data.actionType != null) tvActionType.text = data.actionType
                                    if (data.taskStatus != null) tvActionStatus.text =
                                        data.taskStatus
                                    if (data.pendingRemarks1 != null && data.pendingRemarks1?.isNotEmpty()!!) {
//                                        clEleven.show()
//                                        tvPendingLv1.show()
//                                        tvPendingLv1.text = data.pendingRemarks1
                                    }
                                    if (data.pendingRemarks2 != null && data.pendingRemarks2?.isNotEmpty()!!) {
//                                        clTwelve.show()
//                                        tvPendingLv2.show()
//                                        tvPendingLv2.text = data.pendingRemarks2
                                    }

                                    if (data.taskStatus == "CLOSED") {
                                        btnUpdateTask.hide()
                                    }

                                    if (data.siteLat != null && data.siteLong != null) {
                                        location(data.siteLat, data.siteLong)
                                    }
                                    mapView?.getMapAsync { mapboxMap ->
                                        mapboxMap.addOnMapClickListener {
                                            // Create a Uri from an intent string. Use the result to create an Intent.
                                            val gmmIntentUri =
                                                Uri.parse("geo:0,0?q=${data.siteLat},${data.siteLong}(${data.siteID})")
                                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                            // Make the Intent explicit by setting the Google Maps package
                                            mapIntent.setPackage("com.google.android.apps.maps")
                                            // Attempt to start an activity that can handle the Intent
                                            startActivity(mapIntent)
                                            true
                                        }

                                        mapboxMap.setOnMarkerClickListener {
                                            // Create a Uri from an intent string. Use the result to create an Intent.
                                            val gmmIntentUri =
                                                Uri.parse("geo:0,0?q=${data.siteLat},${data.siteLong}(${data.siteID})")
                                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                            // Make the Intent explicit by setting the Google Maps package
                                            mapIntent.setPackage("com.google.android.apps.maps")
                                            // Attempt to start an activity that can handle the Intent
                                            startActivity(mapIntent)
                                            true
                                        }
                                    }

                                    btnUpdateTask.setOnClickListener {
                                        locationEnabled()
                                        if (gpsStatus) {
                                            if (levelID == "34" || levelID == "40") {
                                                intent.getStringExtra("activityNumber")?.let {
                                                    token.let { it1 ->
                                                        postValidateTask(
                                                            it1,
                                                            it
                                                        )
                                                    }
                                                }
                                            } else {
                                                val intent = Intent(
                                                    applicationContext,
                                                    ScheduleUpdateTaskActivity::class.java
                                                )
                                                intent.putExtra("type", "update")
                                                intent.putExtra("domainLev1", data.domainLev1)
                                                intent.putExtra("activityNum", data.actNum)
                                                intent.putExtra("siteID", data.siteID)
                                                intent.putExtra("picBefore", data.picBefore)
                                                intent.putExtra("picAfter", data.picAfter)
                                                intent.putExtra("actDetail", data.actionDetail)
                                                intent.putExtra("rca1", data.rca1)
                                                intent.putExtra("rca2", data.rca2)
                                                intent.putExtra("rca3", data.rca3)
                                                intent.putExtra("rca4", data.rCAAction)
                                                intent.putExtra("actType", data.actionType)
                                                intent.putExtra("actStat", data.taskStatus)
                                                intent.putExtra("pending1", data.pendingRemarks1)
                                                intent.putExtra("pending2", data.pendingRemarks1)
                                                startActivity(intent)
                                            }
                                        } else {
                                            alertDialogLocation()
                                        }
                                    }
                                }
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

    private fun postValidateTask(token: String, taskNum: String) {
        dailyActivityViewModel.postValidateTask(token, taskNum)
        dailyActivityViewModel.postValidateTaskData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { responses ->
                            if (responses.getReport()?.success == true) {
                                showDialog("", "", true)
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

    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun dialogShowImage(images: String) {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.pop_up_image)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val image: ImageView = dialog.findViewById<View>(R.id.ivPopup) as ImageView
        val btnClose: ImageView = dialog.findViewById<View>(R.id.btnClose) as ImageView

        Glide.with(this)
            .load(removeBackSlash(images))
            .into(image)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}