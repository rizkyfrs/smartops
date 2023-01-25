package com.smartfren.smartops.utils

import android.content.Context
import android.util.Log
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_view.view.*

class CustomMarkerBar(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    override fun refreshContent(e: Entry?, h: Highlight?) {
        val entry = e as BarEntry
        if (entry.yVals != null) {
            val value = entry.yVals[h?.stackIndex!!].toInt()
            tvPrice.text = "$value"
        }
        super.refreshContent(e, h)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}