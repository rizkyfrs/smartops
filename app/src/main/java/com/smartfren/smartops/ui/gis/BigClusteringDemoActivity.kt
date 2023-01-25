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

import com.smartfren.smartops.ui.gis.BaseDemoActivity
import com.google.maps.android.clustering.ClusterManager
import com.smartfren.smartops.ui.gis.model.MyItem
import org.json.JSONException
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlin.Throws
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.gis.MyItemReader

class BigClusteringDemoActivity : BaseDemoActivity() {
    private var mClusterManager: ClusterManager<MyItem>? = null
    override fun startDemo(isRestore: Boolean) {
        if (!isRestore) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))
        }
        mClusterManager = ClusterManager(this, map)
        map.setOnCameraIdleListener(mClusterManager)
        try {
            readItems()
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show()
        }
    }

    @Throws(JSONException::class)
    private fun readItems() {
        val inputStream = resources.openRawResource(R.raw.radar_search)
        val items = MyItemReader().read(inputStream)
        for (i in 0..9) {
            val offset = i / 60.0
            for (item in items) {
                val position = item.position
                val lat = position.latitude + offset
                val lng = position.longitude + offset
                val offsetItem = MyItem(lat, lng)
                mClusterManager!!.addItem(offsetItem)
            }
        }
    }
}