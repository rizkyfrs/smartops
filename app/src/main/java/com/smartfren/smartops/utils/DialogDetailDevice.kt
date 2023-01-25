package com.smartfren.smartops.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.smartfren.smartops.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_detail_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DialogDetailDevice : BaseActivity(), OnMapReadyCallback {
    private var map: MapboxMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.device_status)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setupUI()
    }

    private fun setupUI() {
        if (intent.getStringExtra("lat") != null && intent.getStringExtra("long") != null) {
            location(intent.getStringExtra("lat"), intent.getStringExtra("long"))
        }
        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.addOnMapClickListener {
                // Create a Uri from an intent string. Use the result to create an Intent.
//                val gmmIntentUri =
//                    Uri.parse("geo:0,0?q=${data.siteLat},${data.siteLong}(${data.siteID})")
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                // Make the Intent explicit by setting the Google Maps package
//                mapIntent.setPackage("com.google.android.apps.maps")
                // Attempt to start an activity that can handle the Intent
//                startActivity(mapIntent)
                true
            }

            mapboxMap.setOnMarkerClickListener {
                // Create a Uri from an intent string. Use the result to create an Intent.
//                val gmmIntentUri =
//                    Uri.parse("geo:0,0?q=${data.siteLat},${data.siteLong}(${data.siteID})")
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                // Make the Intent explicit by setting the Google Maps package
//                mapIntent.setPackage("com.google.android.apps.maps")
                // Attempt to start an activity that can handle the Intent
//                startActivity(mapIntent)
                true
            }
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
}