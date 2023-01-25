/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartfren.smartops.ui.gis

import android.util.Log
import com.smartfren.smartops.ui.gis.BaseDemoActivity
import com.google.maps.android.clustering.ClusterManager
import com.smartfren.smartops.ui.gis.model.MyItem
import com.google.android.gms.maps.GoogleMap
import android.view.LayoutInflater
import android.view.View
import com.smartfren.smartops.R
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import org.json.JSONException
import kotlin.Throws
import com.smartfren.smartops.ui.gis.MyItemReader

/**
 * Simple activity demonstrating ClusterManager.
 */
class ClusteringDemoActivity : BaseDemoActivity() {
    private var mClusterManager: ClusterManager<MyItem>? = null
    override fun startDemo(isRestore: Boolean) {
        if (!isRestore) {
            val indonesiaBounds = LatLngBounds(
                LatLng((-5.968225391239664), 89.4923492893149), // SW bounds
                LatLng((1.5134520668060687), 125.9669586643149)  // NE bounds
            )
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaBounds.center, 3f))
//            map.moveCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 10f))
        }
        mClusterManager = ClusterManager(this, map)
        map.setOnCameraIdleListener(mClusterManager)

        // Add a custom InfoWindowAdapter by setting it to the MarkerManager.Collection object from
        // ClusterManager rather than from GoogleMap.setInfoWindowAdapter
        mClusterManager!!.markerCollection.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                val inflater = LayoutInflater.from(this@ClusteringDemoActivity)
                val view = inflater.inflate(R.layout.custom_info_window, null)
                val textView = view.findViewById<TextView>(R.id.titless)
                val text = if (marker.title != null) marker.title else "Cluster Item"
                textView.text = text
                return view
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })
        mClusterManager!!.markerCollection.setOnInfoWindowClickListener { marker: Marker? ->
            Toast.makeText(
                this@ClusteringDemoActivity,
                "Info window clicked.",
                Toast.LENGTH_SHORT
            ).show()
        }
        mClusterManager!!.setOnClusterClickListener {
            map.moveCamera(CameraUpdateFactory.zoomIn())
            return@setOnClusterClickListener false
        }
        try {
            readItems()
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show()
        }
    }

    @Throws(JSONException::class)
    private fun readItems() {
        val inputStream = resources.openRawResource(R.raw.site)
        val items = MyItemReader().read(inputStream)
        mClusterManager!!.addItems(items)
    }
}