package com.smartfren.smartops.ui.reportpm

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
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
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.removeBackSlash
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.reference_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ReportSiteActivity : BaseActivity(), OnMapReadyCallback {
    private var token = ""
    private var map: MapboxMap? = null
    private val mReportPMViewModel: ReportPMViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.reference_new)
        mapViewReference.onCreate(savedInstanceState)
        mapViewReference.getMapAsync(this)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "").toString()

        token.let {
            intent.getStringExtra("site")?.let { it1 ->
                getReportSite(it, it1)
                getReportPM(it, it1, "/latest")
            }
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
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .build()
            map?.locationComponent?.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true        // Enable to make component visible
                cameraMode = CameraMode.TRACKING         // Set the component's camera mode
                renderMode = RenderMode.COMPASS          // Set the component's render mode
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
        mapViewReference?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapViewReference?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewReference?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewReference?.onDestroy()
    }

    private fun getReportSite(token: String, site: String) {
        mReportPMViewModel.getReportSiteTask(token, site)
        mReportPMViewModel.getReportSiteData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { reportSiteResponse ->
                            if (reportSiteResponse.getReport()?.success == true) {
                                for (data in reportSiteResponse.getReport()?.info!!) {
                                    tvReferenceInfo.text = data.siteID + " - " + data.siteName
                                    if (data.siteID != null)  tvReferenceSiteID.text = data.siteID
                                    if (data.siteOldID != null) tvReferenceOldID.text = data.siteOldID
                                    if (data.siteIDFAP != null) tvReferenceIDFAP.text = data.siteIDFAP
                                    if (data.siteIDCDMA != null)  tvReferenceIDCDMA.text = data.siteIDCDMA
//                                    tvReferenceSiteName.text = data.oMRegionName
                                    if (data.oMManager != null) tvReferenceOMManager.text = data.oMManager
                                    if (data.oMAreaCoord != null) tvReferenceOMAC.text = data.oMAreaCoord
                                    if (data.oMClusterPIC != null) tvReferenceOMPIC.text = data.oMClusterPIC
                                    if (data.siteName != null) tvReferenceSiteName.text = data.siteName
                                    if (data.siteCity != null) tvReferenceCity.text = data.siteCity
                                    if (data.siteStatus != null) tvReferenceStatus.text = data.siteStatus
                                    if (data.siteLongitude != null) tvReferenceLongitude.text = data.siteLongitude
                                    if (data.siteLatitude != null)  tvReferenceLatitude.text = data.siteLatitude
                                    if (data.towerProviderGroup != null) tvReferenceTowerPG.text = data.towerProviderGroup
                                    if (data.towerProvider != null) tvReferenceTowerProvider.text = data.towerProvider
                                    if (data.siteLongitude != null && data.siteLongitude != null) {
                                        location(data.siteLatitude, data.siteLongitude)
                                    }
                                    mapViewReference?.getMapAsync { mapboxMap ->
                                        mapboxMap.addOnMapClickListener {
                                            // Create a Uri from an intent string. Use the result to create an Intent.
                                            val gmmIntentUri =
                                                Uri.parse("geo:0,0?q=${data.siteLatitude},${data.siteLongitude}(${data.siteName})")
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
                                                Uri.parse("geo:0,0?q=${data.siteLatitude},${data.siteLongitude}(${data.siteName})")
                                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                            // Make the Intent explicit by setting the Google Maps package
                                            mapIntent.setPackage("com.google.android.apps.maps")
                                            // Attempt to start an activity that can handle the Intent
                                            startActivity(mapIntent)
                                            true
                                        }
                                    }
                                }
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

    private fun getReportPM(token: String, site: String, date: String) {
        mReportPMViewModel.getReportPMTask(token, site, date)
        mReportPMViewModel.getReportPMData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { reportPMResponse ->
                            if (reportPMResponse.getReport()?.success == true) {
                                for (data in reportPMResponse.getReport()?.report!!) {
                                    tvReferenceSite.text = data.siteID
                                    (data.periodMonth + " / " + data.periodYear).also {
                                        tvReferencePeriod.text = it
                                    }
                                    cvReferenceTwo.setOnClickListener {
                                        val url =
                                            removeBackSlash(data.tPMaintenanceReport.toString())
                                        val intent = Intent(
                                            this@ReportSiteActivity,
                                            ReportViewPdfActivity::class.java
                                        )
                                        intent.putExtra("urlPdf", "https://$url")
                                        startActivity(intent)
                                    }
                                }
                            } else {
                                cvReferenceTwo.hide()
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
}