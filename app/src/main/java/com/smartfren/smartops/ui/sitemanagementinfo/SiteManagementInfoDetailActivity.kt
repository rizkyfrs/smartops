package com.smartfren.smartops.ui.sitemanagementinfo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
class SiteManagementInfoDetailActivity : BaseActivity(), OnMapReadyCallback {
    private var map: MapboxMap? = null
    private var actionOpt: ArrayList<OptionModel>? = null
    private val siteManagementInfoViewModel: SiteManagementInfoViewModel by viewModels()
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
        intent.getStringExtra("siteID")?.let {
            getSiteManagementInfoDetail(it)
            tvInfo.text = it
        }
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.site_management_info)
        actionOpt = ArrayList()
        hsv.show()
        recycleView.show()
        clActivity.hide()
        tvSelectDate.hide()

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

    private fun getSiteManagementInfoDetail(key: String) {
        siteManagementInfoViewModel.siteManagementInfoDetail(key)
        siteManagementInfoViewModel.siteManagementInfoDetailData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteManagementResponse ->
                            if (siteManagementResponse.success == true) {
                                if (siteManagementResponse.cvHubSiteinfo?.siteLat != null && siteManagementResponse.cvHubSiteinfo?.siteLong != null) {
                                    location(
                                        replaceText(siteManagementResponse.cvHubSiteinfo?.siteLat),
                                        replaceText(siteManagementResponse.cvHubSiteinfo?.siteLong)
                                    )
                                }

                                tvActivityNumber.text = siteManagementResponse.cvHubSiteinfo?.siteId
                                mapView?.getMapAsync { mapboxMap ->
                                    mapboxMap.addOnMapClickListener {
                                        // Create a Uri from an intent string. Use the result to create an Intent.
                                        val gmmIntentUri =
                                            Uri.parse("geo:0,0?q=${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLat)},${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLong)}(${siteManagementResponse.cvHubSiteinfo?.siteId})")
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
                                            Uri.parse("geo:0,0?q=${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLat)},${replaceText(siteManagementResponse.cvHubSiteinfo?.siteLong)}(${siteManagementResponse.cvHubSiteinfo?.siteId})")
                                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        // Make the Intent explicit by setting the Google Maps package
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        // Attempt to start an activity that can handle the Intent
                                        startActivity(mapIntent)
                                        true
                                    }
                                }

                                actionOpt?.add(
                                    OptionModel(
                                        "Site ID",
                                        siteManagementResponse.cvHubSiteinfo?.siteId
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site ID FAP",
                                        siteManagementResponse.cvHubSiteinfo?.siteIdFap
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Vendor",
                                        siteManagementResponse.cvHubSiteinfo?.siteVendor
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Tagging",
                                        siteManagementResponse.cvHubSiteinfo?.siteTag
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Status On Air",
                                        siteManagementResponse.cvHubSiteinfo?.siteStatus
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Sales - Region",
                                        siteManagementResponse.cvHubSiteinfo?.siteSalesRegion
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Sales - Cluster",
                                        siteManagementResponse.cvHubSiteinfo?.siteSalesCluster
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Latitude",
                                        siteManagementResponse.cvHubSiteinfo?.siteLat
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Longitude",
                                        siteManagementResponse.cvHubSiteinfo?.siteLong
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Street Address",
                                        siteManagementResponse.cvHubSiteinfo?.siteAddress
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Propinsi",
                                        siteManagementResponse.cvHubSiteinfo?.siteAddressPropinsi
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Kabupaten",
                                        siteManagementResponse.cvHubSiteinfo?.siteAddressKabupaten
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Kecamatan",
                                        siteManagementResponse.cvHubSiteinfo?.siteAddressKecamatan
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Kelurahan",
                                        siteManagementResponse.cvHubSiteinfo?.siteAddressKelurahan
                                    )
                                )
                                actionOpt?.add(
                                    OptionModel(
                                        "Site Technology",
                                        siteManagementResponse.cvHubSiteinfo?.siteTechnology
                                    )
                                )
                                siteManagementResponse.cvHubSiteinfo?.siteVvipCat.let {
                                    actionOpt?.add(OptionModel("Site VVIP category", it))
                                }
                                siteManagementResponse.cvHubSiteinfo?.sitePhase.let {
                                    actionOpt?.add(OptionModel("Site Phase", it))
                                }
                                actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }

                                tabGeneralInfo.setOnClickListener {
                                    clearData()
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site ID",
                                            siteManagementResponse.cvHubSiteinfo?.siteId
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site ID FAP",
                                            siteManagementResponse.cvHubSiteinfo?.siteIdFap
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Vendor",
                                            siteManagementResponse.cvHubSiteinfo?.siteVendor
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Tagging",
                                            siteManagementResponse.cvHubSiteinfo?.siteTag
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Status On Air",
                                            siteManagementResponse.cvHubSiteinfo?.siteStatus
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Sales - Region",
                                            siteManagementResponse.cvHubSiteinfo?.siteSalesRegion
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Sales - Cluster",
                                            siteManagementResponse.cvHubSiteinfo?.siteSalesCluster
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Latitude",
                                            siteManagementResponse.cvHubSiteinfo?.siteLat
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Longitude",
                                            siteManagementResponse.cvHubSiteinfo?.siteLong
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Street Address",
                                            siteManagementResponse.cvHubSiteinfo?.siteAddress
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Propinsi",
                                            siteManagementResponse.cvHubSiteinfo?.siteAddressPropinsi
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Kabupaten",
                                            siteManagementResponse.cvHubSiteinfo?.siteAddressKabupaten
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Kecamatan",
                                            siteManagementResponse.cvHubSiteinfo?.siteAddressKecamatan
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Kelurahan",
                                            siteManagementResponse.cvHubSiteinfo?.siteAddressKelurahan
                                        )
                                    )
                                    actionOpt?.add(
                                        OptionModel(
                                            "Site Technology",
                                            siteManagementResponse.cvHubSiteinfo?.siteTechnology
                                        )
                                    )
                                    siteManagementResponse.cvHubSiteinfo?.siteVvipCat.let {
                                        actionOpt?.add(OptionModel("Site VVIP category", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.sitePhase.let {
                                        actionOpt?.add(OptionModel("Site Phase", it))
                                    }
                                    actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
                                }

                                tabOperationInfo.setOnClickListener {
                                    clearData()
                                    siteManagementResponse.cvHubSiteinfo?.idRegion.let {
                                        actionOpt?.add(OptionModel("OM - Region", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteClusterName.let {
                                        actionOpt?.add(OptionModel("Cluster Name", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteClusterPic.let {
                                        actionOpt?.add(OptionModel("FLM Cluster PIC", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteSalesCluster.let {
                                        actionOpt?.add(OptionModel("FLM AC", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.sitePhase.let {
                                        actionOpt?.add(OptionModel("FLM Manager", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteGsCap.let {
                                        actionOpt?.add(OptionModel("Fixed Genset", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteGsType.let {
                                        actionOpt?.add(OptionModel("Fixed Genset Type", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteFixgs.let {
                                        actionOpt?.add(OptionModel("Fixed Genset Capacity", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteMorphology.let {
                                        actionOpt?.add(OptionModel("Morphology", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteTotalCell.let {
                                        actionOpt?.add(OptionModel("Total Cell", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteUplinkId.let {
                                        actionOpt?.add(OptionModel("Site Uplink ID", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteLocType.let {
                                        actionOpt?.add(OptionModel("Location Type", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteDependency.let {
                                        actionOpt?.add(OptionModel("Dependency", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteFoNode.let {
                                        actionOpt?.add(OptionModel("FO Node", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteIpranswStatus.let {
                                        actionOpt?.add(OptionModel("IPRAN Status", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteTowerType.let {
                                        actionOpt?.add(OptionModel("Tower Type", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteTowerHeight.let {
                                        actionOpt?.add(OptionModel("Tower Height", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteBuildingHeight.let {
                                        actionOpt?.add(OptionModel("Building Height", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteTotalHeight.let {
                                        actionOpt?.add(OptionModel("Total Height", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteAntennaNum.let {
                                        actionOpt?.add(OptionModel("Antenna Num", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteAntennaType.let {
                                        actionOpt?.add(OptionModel("Antenna Type", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.siteTxType.let {
                                        actionOpt?.add(OptionModel("TX Type", it))
                                    }

                                    actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
                                }

                                tabTower.setOnClickListener {
                                    clearData()
                                    siteManagementResponse.cvHubSiteinfo?.tpmTpGroup.let {
                                        actionOpt?.add(OptionModel("TP Group", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmTpName.let {
                                        actionOpt?.add(OptionModel("TP Name", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmTpCategory.let {
                                        actionOpt?.add(OptionModel("TP Category", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmTpRemarks.let {
                                        actionOpt?.add(OptionModel("TP Remark", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteSoArea.let {
                                        actionOpt?.add(OptionModel("Site SO Area", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteBudgetCat.let {
                                        actionOpt?.add(OptionModel("Site Budget Cat", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteTechPlan.let {
                                        actionOpt?.add(OptionModel("Site Tech Plan", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteCoverageType.let {
                                        actionOpt?.add(OptionModel("Site Coverage Type", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteContractType.let {
                                        actionOpt?.add(OptionModel("Site Contract Type", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteMsClass.let {
                                        actionOpt?.add(OptionModel("Site MS Class", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteMsVendor.let {
                                        actionOpt?.add(OptionModel("Site MS Vendor", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteLeaseStart.let {
                                        actionOpt?.add(OptionModel("Lease Date - Start", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.tpmSiteLeaseEnd.let {
                                        actionOpt?.add(OptionModel("Lease Date - End", it))
                                    }

                                    actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
                                }

                                tabElectricity.setOnClickListener {
                                    clearData()
                                    siteManagementResponse.cvHubSiteinfo?.edCustomerId.let {
                                        actionOpt?.add(OptionModel("Electricity Cust. ID", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.edCostCenter.let {
                                        actionOpt?.add(OptionModel("Cost Center", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.edCompanyCode.let {
                                        actionOpt?.add(OptionModel("Company Code", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.edContract.let {
                                        actionOpt?.add(OptionModel("Contract Under", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.edCapacity.let {
                                        actionOpt?.add(OptionModel("Capacity", it))
                                    }

                                    actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
                                }

                                tabPMInfo.setOnClickListener {
                                    clearData()
                                    siteManagementResponse.cvHubSiteinfo?.pmStatus.let {
                                        actionOpt?.add(OptionModel("PM Overall Status", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.dDate.let {
                                        actionOpt?.add(OptionModel("Completion Date", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.a1.let {
                                        actionOpt?.add(OptionModel("A.1. CME - Direct", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.a2.let {
                                        actionOpt?.add(OptionModel("A.2. CME- TP", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.b.let {
                                        actionOpt?.add(OptionModel("B. Genset", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.c.let {
                                        actionOpt?.add(OptionModel("C. Power", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.d.let {
                                        actionOpt?.add(OptionModel("D. eNodeB", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.e.let {
                                        actionOpt?.add(OptionModel("E. Microwave", it))
                                    }
                                    siteManagementResponse.cvHubSiteinfo?.f.let {
                                        actionOpt?.add(OptionModel("F. Kebersihan", it))
                                    }

                                    actionOpt?.let { mSiteManagementInfoDetailAdapter?.updateList(it) }
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

    private fun clearData() {
        actionOpt?.clear()
        mSiteManagementInfoDetailAdapter?.clearList()
    }
}