package com.smartfren.smartops.ui.consumable

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.sitemanagementinfo.adapter.SiteManagementInfoDetailAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_detail_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ConsumableUsageDetailActivity : BaseActivity(), OnMapReadyCallback {
    private var map: MapboxMap? = null
    private var actionOpt: ArrayList<OptionModel>? = null
    private val consumableUsageViewModel: ConsumableUsageViewModel by viewModels()
    private var mSiteManagementInfoDetailAdapter: SiteManagementInfoDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.daily_activity_detail_new)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        clearData()
        intent.getStringExtra("consumableReportID")?.let {
            getConsumableUsageDetail(it)
        }
    }

    private fun setupUI() {
        tvTitleAppBar.text = "Consumable Usage Detail"
        actionOpt = ArrayList()
        recycleView.hide()
        tvSelectDate.hide()
        mapView.hide()
        tvTitleActivityNumber.hide()
        tvActivityNumber.hide()
        tvTitleActivityRegName.hide()
        tvRegName.hide()
        tvTitleActivityPriority.hide()
        tvPriority.hide()
        tvTitleRCALv1.hide()
        tvRCALv1.hide()
        tvTitleRCALv2.hide()
        tvRCALv2.hide()
        tvTitleRCALv3.hide()
        tvRCALv3.hide()
        tvTitleRCAAction.hide()
        tvRCAAction.hide()
        tvTitleActionDetail.hide()
        tvActionDetail.hide()
        tvTitleActionType.hide()
        tvActionType.hide()
        tvTitleActionStatus.hide()
        tvActionStatus.hide()
        tvTitleActualDate.hide()
        tvActualDate.hide()
        btnUpdateTask.hide()

        val param = tvTitlePlannedDate.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,0,0)
        tvTitlePlannedDate.layoutParams = param

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mSiteManagementInfoDetailAdapter = SiteManagementInfoDetailAdapter(this)
        recyclerview.adapter = mSiteManagementInfoDetailAdapter
        mSiteManagementInfoDetailAdapter?.clearList()
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
                isLocationComponentEnabled =
                    true                       // Enable to make component visible
                cameraMode =
                    CameraMode.TRACKING                        // Set the component's camera mode
                renderMode =
                    RenderMode.COMPASS                         // Set the component's render mode
            }
        }
    }

    private fun location(lats: String?, long: String?) {
        val lat = lats?.toDouble()
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

    private fun getConsumableUsageDetail(key: String) {
        consumableUsageViewModel.setConsumableUsageDetail(key)
        consumableUsageViewModel.consumableUsageDetailData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteManagementResponse ->
                            if (siteManagementResponse.success == true) {
//                                if (siteManagementResponse.cvConsflmreport?.siteLat != null && siteManagementResponse.cvConsflmreport?.siteLong != null) {
//                                    location(
//                                        replaceText(siteManagementResponse.cvConsflmreport?.siteLat),
//                                        replaceText(siteManagementResponse.cvConsflmreport?.siteLong)
//                                    )
//                                }

//                                tvActivityNumber.text = siteManagementResponse.cvHubSiteinfo?.siteId
//                                mapView?.getMapAsync { mapboxMap ->
//                                    mapboxMap.addOnMapClickListener {
//                                        // Create a Uri from an intent string. Use the result to create an Intent.
//                                        val gmmIntentUri =
//                                            Uri.parse("geo:0,0?q=${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLat)},${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLong)}(${siteManagementResponse.cvHubSiteinfo?.siteId})")
//                                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                                        // Make the Intent explicit by setting the Google Maps package
//                                        mapIntent.setPackage("com.google.android.apps.maps")
//                                        // Attempt to start an activity that can handle the Intent
//                                        startActivity(mapIntent)
//                                        true
//                                    }
//
//                                    mapboxMap.setOnMarkerClickListener {
//                                        // Create a Uri from an intent string. Use the result to create an Intent.
//                                        val gmmIntentUri =
//                                            Uri.parse("geo:0,0?q=${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLat)},${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLong)}(${siteManagementResponse.cvHubSiteinfo?.siteId})")
//                                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                                        // Make the Intent explicit by setting the Google Maps package
//                                        mapIntent.setPackage("com.google.android.apps.maps")
//                                        // Attempt to start an activity that can handle the Intent
//                                        startActivity(mapIntent)
//                                        true
//                                    }
//                                }

                                tvInfo.text = siteManagementResponse.cvConsflmreport?.cflmSiteId

                                tvTitlePlannedDate.text = "Consumable Usage ID"
                                tvPlannedDate.text = siteManagementResponse.cvConsflmreport?.cflmUsageNum

                                tvTitleDomLv1.text = "Notes"
                                tvDomainLev1.text = siteManagementResponse.cvConsflmreport?.cflmNotes

                                tvTitleDomLv2.text = "Register By"
                                tvDomainLev2.text = siteManagementResponse.cvConsflmreport?.cflmRegistrars

                                tvTitleNEID.text = "Register Date"
                                tvNEID.text = siteManagementResponse.cvConsflmreport?.cflmRegisterdate

                                tvTitleVendor.text = "Site ID"
                                tvVendor.text = siteManagementResponse.cvConsflmreport?.cflmSiteId

                                tvTitleWOID.text = "Consumable Type"
                                tvWOID.text = siteManagementResponse.cvConsflmreport?.cflmConsType

                                tvTitleActionNeed.text = "Report Type"
                                tvActionNeed.text = siteManagementResponse.cvConsflmreport?.cflmReportType

                                if (siteManagementResponse.cvConsflmreport?.cflmUsageBefore != null&&
                                    siteManagementResponse.cvConsflmreport.cflmUsageBefore!!.isNotEmpty()) {
                                    Glide.with(this)
                                        .load("https://oms.smartfren.com/doc/cons/before/" + siteManagementResponse.cvConsflmreport.cflmUsageBefore)
                                        .into(ivPhotoBefore)
                                    ivPhotoBeforeDefault.hide()
                                    clPhotoBefore.setOnClickListener {
                                        dialogShowImage("https://oms.smartfren.com/doc/cons/before/${siteManagementResponse.cvConsflmreport.cflmUsageBefore}")
                                    }
                                } else {
                                    ivPhotoBeforeDefault.show()
                                }
                                if (siteManagementResponse.cvConsflmreport?.cflmUsageAfter != null &&
                                    siteManagementResponse.cvConsflmreport.cflmUsageAfter!!.isNotEmpty()  ) {
                                    Glide.with(this)
                                        .load("https://oms.smartfren.com/doc/cons/after/" + siteManagementResponse.cvConsflmreport.cflmUsageAfter)
                                        .into(ivPhotoAfter)
                                    ivPhotoAfterDefault.hide()
                                    clPhotoAfter.setOnClickListener {
                                        dialogShowImage("https://oms.smartfren.com/doc/cons/after/${siteManagementResponse.cvConsflmreport.cflmUsageAfter}")
                                    }
                                } else {
                                    ivPhotoAfterDefault.show()
                                }

                                actionOpt?.add(
                                    OptionModel(
                                        "Consumable Usage ID",
                                        siteManagementResponse.cvConsflmreport?.cflmUsageNum
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Notes",
                                        siteManagementResponse.cvConsflmreport?.cflmNotes
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Register By",
                                        siteManagementResponse.cvConsflmreport?.cflmRegistrars
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Register Date",
                                        siteManagementResponse.cvConsflmreport?.cflmRegisterdate
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Site ID",
                                        siteManagementResponse.cvConsflmreport?.cflmSiteId
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Consumable Type",
                                        siteManagementResponse.cvConsflmreport?.cflmConsType
                                    )
                                )

                                actionOpt?.add(
                                    OptionModel(
                                        "Report Type",
                                        siteManagementResponse.cvConsflmreport?.cflmReportType
                                    )
                                )
                                    actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
//                                }
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

    private fun clearData() {
        actionOpt?.clear()
        mSiteManagementInfoDetailAdapter?.clearList()
    }
}