package com.smartfren.smartops.ui.testsignal

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.*
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.craxiom.messaging.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.smartfren.smartops.ui.testsignal.test.CalculationUtils
import com.smartfren.smartops.ui.testsignal.test.GetSpeedTestHostsHandler
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.testsignal.test.RadioInfo
import com.smartfren.smartops.custom_ui.TickProgressBar
import com.smartfren.smartops.data.models.CellInfo
import com.smartfren.smartops.data.models.CellularProtocol
import com.smartfren.smartops.data.models.CellularRecordWrapper
import com.smartfren.smartops.ui.testsignal.test.HttpDownloadTest
import com.smartfren.smartops.ui.testsignal.test.HttpUploadTest
import com.smartfren.smartops.ui.testsignal.test.PingTest
import com.smartfren.smartops.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.speed_test_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.achartengine.model.XYMultipleSeriesDataset
import org.achartengine.model.XYSeries
import org.achartengine.renderer.XYMultipleSeriesRenderer
import org.achartengine.renderer.XYSeriesRenderer
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TestSignalActivity : BaseActivity() {
    private var lcMeasure: LineChart? = null
    private var lineDataSet: LineDataSet? = null
    private var lineData: LineData? = null
    private var tickProgressMeasure: TickProgressBar? = null

    private var i: Float = 0f
    private var j: Float = 0f
    private var k: Float = 0f
    var position = 0
    var lastPosition = 0
    var getSpeedTestHostsHandler: GetSpeedTestHostsHandler? = null
    var tempBlackList: HashSet<String>? = null
    var count = 0
    private val cellInfoData = mutableListOf<CellInfo>()
    var mTelephonyManager: TelephonyManager? = null

    var telephonyManager: TelephonyManager? = null
    private val updateHandler = Handler(Looper.getMainLooper())

    var radioInfo: RadioInfo? = null
    private val mTesSignalViewModel: TesSignalViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speed_test_activity)

        init()
//        mTelephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        // for API 31+: you need to first check for READ_PHONE_STATE permission to be able to listen to LISTEN_CALL_STATE
//        if (Build.VERSION.SDK_INT >= 31) {
//            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
//                mTelephonyManager?.listen(
//                    mPhoneStateListener,
//                    PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or PhoneStateListener.LISTEN_CELL_INFO or PhoneStateListener.LISTEN_CELL_LOCATION
//                )
//        } else { // no permission needed
//            mTelephonyManager?.listen(
//                mPhoneStateListener,
//                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or PhoneStateListener.LISTEN_CELL_INFO or PhoneStateListener.LISTEN_CELL_LOCATION
//            )
//        }

//        mTelephonyManager?.listen(
//            mPhoneStateListener,
//            PhoneStateListener.LISTEN_CELL_LOCATION or PhoneStateListener.LISTEN_CELL_INFO or PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
//        )

//        radioInfo = RadioInfo(this

        val dec = DecimalFormat("#.##")

        tempBlackList = HashSet()

        getSpeedTestHostsHandler = GetSpeedTestHostsHandler()
        getSpeedTestHostsHandler!!.start()

        btnBack.setOnClickListener {
            onBackPressed()
        }

//        btnTest.hide()

        startButton.setOnClickListener {
//            fun onClick(v: View?) {
            startButton.isEnabled = false

            //Restart test icin eger baglanti koparsa
            if (getSpeedTestHostsHandler == null) {
                getSpeedTestHostsHandler = GetSpeedTestHostsHandler()
                getSpeedTestHostsHandler!!.start()
            }
            Thread(object : Runnable {
                var rotate: RotateAnimation? = null

                var pingTextView = findViewById(R.id.pingTextView) as TextView
                var downloadTextView = findViewById(R.id.downloadTextView) as TextView
                var uploadTextView = findViewById(R.id.uploadTextView) as TextView
                override fun run() {
                    runOnUiThread { startButton.text = "Selecting best server based on ping..." }

                    i = 0f
                    //Get egcodes.speedtest hosts
                    var timeCount = 600 //1min
                    while (!getSpeedTestHostsHandler!!.isFinished) {
                        timeCount--
                        try {
                            Thread.sleep(100)
                        } catch (e: InterruptedException) {
                        }
                        if (timeCount <= 0) {
                            runOnUiThread {
                                Toast.makeText(applicationContext, "No Connection...", Toast.LENGTH_LONG).show()
                                startButton.isEnabled = true
                                startButton.textSize = 16f
                                startButton.text = "Restart Test"
                            }
                            getSpeedTestHostsHandler = null
                            return
                        }
                    }

                    //Find closest server
                    val mapKey = getSpeedTestHostsHandler!!.getMapKey()
                    val mapValue = getSpeedTestHostsHandler!!.getMapValue()
                    val selfLat = getSpeedTestHostsHandler!!.getSelfLat()
                    val selfLon = getSpeedTestHostsHandler!!.getSelfLon()
                    var tmp = 19349458.0
                    var dist = 0.0
                    var findServerIndex = 0
                    for (index in mapKey.keys) {
                        if (tempBlackList!!.contains(mapValue[index]!![5])) {
                            continue
                        }
                        val source = Location("Source")
                        source.latitude = selfLat
                        source.longitude = selfLon
                        val ls = mapValue[index]!!
                        val dest = Location("Dest")
                        dest.latitude = ls[0].toDouble()
                        dest.longitude = ls[1].toDouble()
                        val distance = source.distanceTo(dest).toDouble()
                        if (tmp > distance) {
                            tmp = distance
                            dist = distance
                            findServerIndex = index
                        }
                    }
                    val testAddr = mapKey[findServerIndex]!!.replace("http://", "https://")
                    val info = mapValue[findServerIndex]
                    val distance = dist
                    if (info == null) {
                        runOnUiThread {
                            startButton.textSize = 12f
                            startButton.text = "There was a problem in getting Host Location. Try again later."
                        }
                        return
                    }
                    runOnUiThread {
                        startButton.textSize = 13f
                        startButton.text =
                            String.format("Host Location: %s [Distance: %s km]", info[2], DecimalFormat("#.##").format(distance / 1000))
                    }

                    //Init Ping graphic
                    val chartPing = findViewById(R.id.chartPing) as LinearLayout
                    val pingRenderer = XYSeriesRenderer()
                    val pingFill: XYSeriesRenderer.FillOutsideLine =
                        XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL)
                    pingFill.setColor(Color.parseColor("#4d5a6a"))
                    pingRenderer.addFillOutsideLine(pingFill)
                    pingRenderer.setDisplayChartValues(false)
                    pingRenderer.setShowLegendItem(false)
                    pingRenderer.setColor(Color.parseColor("#4d5a6a"))
                    pingRenderer.setLineWidth(5F)
                    val multiPingRenderer = XYMultipleSeriesRenderer()
                    multiPingRenderer.setXLabels(0)
                    multiPingRenderer.setYLabels(0)
                    multiPingRenderer.setZoomEnabled(false)
                    multiPingRenderer.setXAxisColor(Color.parseColor("#647488"))
                    multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"))
                    multiPingRenderer.setPanEnabled(true, true)
                    multiPingRenderer.setZoomButtonsVisible(false)
                    multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00))
                    multiPingRenderer.addSeriesRenderer(pingRenderer)

                    //Init Download graphic
                    val chartDownload = findViewById(R.id.chartDownload) as LinearLayout
                    val downloadRenderer = XYSeriesRenderer()
                    val downloadFill: XYSeriesRenderer.FillOutsideLine =
                        XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL)
                    downloadFill.setColor(Color.parseColor("#4d5a6a"))
                    downloadRenderer.addFillOutsideLine(downloadFill)
                    downloadRenderer.setDisplayChartValues(false)
                    downloadRenderer.setColor(Color.parseColor("#4d5a6a"))
                    downloadRenderer.setShowLegendItem(false)
                    downloadRenderer.setLineWidth(5F)
                    val multiDownloadRenderer = XYMultipleSeriesRenderer()
                    multiDownloadRenderer.setXLabels(0)
                    multiDownloadRenderer.setYLabels(0)
                    multiDownloadRenderer.setZoomEnabled(false)
                    multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"))
                    multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"))
                    multiDownloadRenderer.setPanEnabled(false, false)
                    multiDownloadRenderer.setZoomButtonsVisible(false)
                    multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00))
                    multiDownloadRenderer.addSeriesRenderer(downloadRenderer)

                    //Init Upload graphic
                    val chartUpload = findViewById(R.id.chartUpload) as LinearLayout
                    val uploadRenderer = XYSeriesRenderer()
                    val uploadFill: XYSeriesRenderer.FillOutsideLine =
                        XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL)
                    uploadFill.setColor(Color.parseColor("#4d5a6a"))
                    uploadRenderer.addFillOutsideLine(uploadFill)
                    uploadRenderer.setDisplayChartValues(false)
                    uploadRenderer.setColor(Color.parseColor("#4d5a6a"))
                    uploadRenderer.setShowLegendItem(false)
                    uploadRenderer.setLineWidth(5F)
                    val multiUploadRenderer = XYMultipleSeriesRenderer()
                    multiUploadRenderer.setXLabels(0)
                    multiUploadRenderer.setYLabels(0)
                    multiUploadRenderer.setZoomEnabled(false)
                    multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"))
                    multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"))
                    multiUploadRenderer.setPanEnabled(false, false)
                    multiUploadRenderer.setZoomButtonsVisible(false)
                    multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00))
                    multiUploadRenderer.addSeriesRenderer(uploadRenderer)

                    //Reset value, graphics
                    runOnUiThread {
                        pingTextView.text = "0 ms"
                        chartPing.removeAllViews()
                        downloadTextView.text = "0 Mbps"
                        chartDownload.removeAllViews()
                        uploadTextView.text = "0 Mbps"
                        chartUpload.removeAllViews()
                    }
                    val pingRateList: MutableList<Double> = ArrayList()
                    val downloadRateList: MutableList<Double> = ArrayList()
                    val uploadRateList: MutableList<Double> = ArrayList()
                    var pingTestStarted = false
                    var pingTestFinished = false
                    var downloadTestStarted = false
                    var downloadTestFinished = false
                    var uploadTestStarted = false
                    var uploadTestFinished = false

                    //Init Test
                    val pingTest = PingTest(info[6].replace(":8080", ""), 3)
                    val downloadTest =
                        HttpDownloadTest(testAddr.replace(testAddr.split("/").toTypedArray()[testAddr.split("/").toTypedArray().size - 1], ""))
                    val uploadTest = HttpUploadTest(testAddr)

                    //Tests
                    while (true) {
                        if (!pingTestStarted) {
                            pingTest.start()
                            pingTestStarted = true
                        }
                        if (pingTestFinished && !downloadTestStarted) {
                            downloadTest.start()
                            downloadTestStarted = true
                        }
                        if (downloadTestFinished && !uploadTestStarted) {
                            uploadTest.start()
                            uploadTestStarted = true
                        }


                        //Ping Test
                        if (pingTestFinished) {
                            //Failure
                            if (pingTest.avgRtt.equals(0)) {
                                println("Ping error...")
                            } else {
                                //Success
                                runOnUiThread {
                                    tickProgressBar.setmPUnit("ms")
                                    pingTextView.setText(dec.format(pingTest.getAvgRtt()).toString() + " ms")
                                }
                            }
                        } else {
                            pingRateList.add(pingTest.getInstantRtt())
                            runOnUiThread {
                                pingTextView.setText(dec.format(pingTest.getInstantRtt()).toString() + " ms")
                            }

                            //Update chart
                            runOnUiThread { // Creating an  XYSeries for Income
                                val pingSeries = XYSeries("")
                                pingSeries.setTitle("")
                                var count = 0
                                val tmpLs: List<Double> = ArrayList(pingRateList)
                                for (`vals` in tmpLs) {
                                    pingSeries.add(count++.toDouble(), `vals`)
                                }
                                val dataset = XYMultipleSeriesDataset()
                                dataset.addSeries(pingSeries)
                                if (i == 0f) {
                                    linechart!!.clear()
                                    lineDataSet!!.clear()
                                    lineDataSet!!.color = Color.rgb(255, 207, 223)
                                    lineData = LineData(lineDataSet)
                                    linechart!!.data = lineData
                                    linechart!!.invalidate()
                                }
                                if (i > 10) {
                                    val data = linechart!!.data
                                    val set = data.getDataSetByIndex(0) as LineDataSet
                                    if (set != null) {
                                        data.addEntry(Entry(i, (10 * pingTest.instantRtt).toFloat()), 0)
                                        linechart!!.notifyDataSetChanged()
                                        linechart!!.setVisibleXRange(0f, i)
                                        linechart!!.invalidate()
                                    }
                                } else {
                                    linechart!!.setVisibleXRange(0f, 10f)
                                    val data = linechart!!.data
                                    val set = data.getDataSetByIndex(0) as LineDataSet
                                    if (set != null) {
                                        data.addEntry(Entry(i, (10 * pingTest.instantRtt).toFloat()), 0)
                                        linechart!!.notifyDataSetChanged()
                                        linechart!!.invalidate()
                                    }
                                }
                                i++
                                tickProgressBar.setmPUnit("ms")
//                                    val chartView: GraphicalView = ChartFactory.getLineChartView(baseContext, dataset, multiPingRenderer)
//                                    chartPing.addView(chartView, 0)
                            }
                        }


                        //Download Test
                        if (pingTestFinished) {
                            if (downloadTestFinished) {
                                //Failure
                                if (downloadTest.finalDownloadRate.equals(0)) {
                                    println("Download error...")
                                } else {
                                    //Success
                                    runOnUiThread {
                                        downloadTextView.setText(
                                            dec.format(downloadTest.getFinalDownloadRate()).toString() + " Mbps"
                                        )
                                    }
                                }
                            } else {
                                //Calc position
                                val downloadRate: Double = downloadTest.instantDownloadRate
                                downloadRateList.add(downloadRate)
                                position = getPositionByRate2(downloadRate)
                                runOnUiThread {
                                    rotate = RotateAnimation(
                                        lastPosition.toFloat(),
                                        position.toFloat(),
                                        Animation.RELATIVE_TO_SELF,
                                        0.5f,
                                        Animation.RELATIVE_TO_SELF,
                                        0.5f
                                    )
                                    rotate!!.interpolator = LinearInterpolator()
                                    rotate!!.duration = 100
//                                    barImageView.startAnimation(rotate)
                                    downloadTextView.setText(dec.format(downloadTest.instantDownloadRate).toString() + " Mbps")
                                    tvSpeed.text = dec.format(downloadTest.instantDownloadRate).toString()
                                    tickProgressBar!!.progress = (downloadTest.instantDownloadRate).toInt()
                                }
                                lastPosition = position

                                //Update chart
                                runOnUiThread { // Creating an  XYSeries for Income
                                    if (j == 0f) {
                                        iv_download.setAlpha(1.0f)
                                        iv_upload.setAlpha(0.5f)
                                        linechart!!.clear()
                                        lineDataSet!!.clear()
                                        lineDataSet!!.color = Color.rgb(224, 249, 181)
                                        lineData = LineData(lineDataSet)
                                        linechart!!.data = lineData
                                        linechart!!.invalidate()
                                    }
                                    if (j > 100) {
                                        val data = linechart!!.data
                                        val set = data.getDataSetByIndex(0) as LineDataSet
                                        if (set != null) {
                                            data.addEntry(Entry(j, (1000 * downloadTest.instantDownloadRate).toFloat()), 0)
                                            linechart!!.notifyDataSetChanged()
                                            linechart!!.setVisibleXRange(0f, j)
                                            linechart!!.invalidate()
                                        }
                                    } else {
                                        linechart!!.setVisibleXRange(0f, 100f)
                                        val data = linechart!!.data
                                        val set = data.getDataSetByIndex(0) as LineDataSet
                                        if (set != null) {
                                            data.addEntry(Entry(j, (1000 * downloadTest.instantDownloadRate).toFloat()), 0)
                                            linechart!!.notifyDataSetChanged()
                                            linechart!!.invalidate()
                                        }
                                    }
                                    j++
                                    val downloadSeries = XYSeries("")
                                    downloadSeries.setTitle("")
                                    val tmpLs: List<Double> = ArrayList(downloadRateList)
                                    var count = 0
                                    for (`val` in tmpLs) {
                                        downloadSeries.add(count++.toDouble(), `val`)
                                    }
                                    val dataset = XYMultipleSeriesDataset()
                                    dataset.addSeries(downloadSeries)
//                                        val chartView: GraphicalView = ChartFactory.getLineChartView(baseContext, dataset, multiDownloadRenderer)
//                                        chartDownload.addView(chartView, 0)
                                }
                            }
                        }


                        //Upload Test
                        if (downloadTestFinished) {
                            if (uploadTestFinished) {
                                //Failure
                                if (uploadTest.getFinalUploadRate().equals(0)) {
                                    println("Upload error...")
                                } else {
                                    //Success
                                    runOnUiThread {
                                        uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()).toString() + " Mbps")
                                    }
                                }
                            } else {
                                //Calc position
                                val uploadRate: Double = uploadTest.getInstantUploadRate()
                                uploadRateList.add(uploadRate)
                                position = getPositionByRate2(uploadRate)
                                runOnUiThread {
                                    rotate = RotateAnimation(
                                        lastPosition.toFloat(),
                                        position.toFloat(),
                                        Animation.RELATIVE_TO_SELF,
                                        0.5f,
                                        Animation.RELATIVE_TO_SELF,
                                        0.5f
                                    )
                                    rotate!!.interpolator = LinearInterpolator()
                                    rotate!!.duration = 100
//                                    barImageView.startAnimation(rotate)
                                    uploadTextView.text = dec.format(uploadTest.instantUploadRate).toString() + " Mbps"
                                    tvSpeed.text = dec.format(uploadTest.instantUploadRate).toString()
                                    tickProgressBar!!.progress = (uploadTest.instantUploadRate).toInt()
                                }
                                lastPosition = position

                                //Update chart
                                runOnUiThread { // Creating an  XYSeries for Income
                                    if (k == 0f) {
                                        iv_download.alpha = 1.0f
                                        iv_upload.alpha = 1.0f
                                        linechart!!.clear()
                                        lineDataSet!!.clear()
                                        lineDataSet!!.color = Color.rgb(145, 174, 210)
                                        lineData = LineData(lineDataSet)
                                        linechart!!.data = lineData
                                        linechart!!.invalidate()
                                    }
                                    if (k > 100) {
                                        val data = linechart!!.data
                                        val set = data.getDataSetByIndex(0) as LineDataSet
                                        if (set != null) {
                                            data.addEntry(Entry(k, (1000 * uploadTest.instantUploadRate).toFloat()), 0)
                                            linechart!!.notifyDataSetChanged()
                                            linechart!!.setVisibleXRange(0f, k)
                                            linechart!!.invalidate()
                                        }
                                    } else {
                                        linechart!!.setVisibleXRange(0f, 100f)
                                        val data = linechart!!.data
                                        val set = data.getDataSetByIndex(0) as LineDataSet
                                        if (set != null) {
                                            data.addEntry(Entry(k, (1000 * uploadTest.instantUploadRate).toFloat()), 0)
                                            linechart!!.notifyDataSetChanged()
                                            linechart!!.invalidate()
                                        }
                                    }
                                    k++
                                    val uploadSeries = XYSeries("")
                                    uploadSeries.title = ""
                                    var count = 0
                                    val tmpLs: List<Double> = ArrayList(uploadRateList)
                                    for (ves in tmpLs) {
//                                            if (count == 0) {
//                                                ves = 0.0
//                                            }
                                        uploadSeries.add(count++.toDouble(), ves)
                                    }
                                    val dataset = XYMultipleSeriesDataset()
                                    dataset.addSeries(uploadSeries)
//                                        val chartView: GraphicalView = ChartFactory.getLineChartView(baseContext, dataset, multiUploadRenderer)
//                                        chartUpload.addView(chartView, 0)
                                }
                            }
                        }

                        //Test bitti
                        if (pingTestFinished && downloadTestFinished && uploadTest.isFinished) {
                            break
                        }
                        if (pingTest.isFinished) {
                            pingTestFinished = true
                        }
                        if (downloadTest.isFinished) {
                            downloadTestFinished = true
                        }
                        if (uploadTest.isFinished) {
                            uploadTestFinished = true
                        }
                        if (pingTestStarted && !pingTestFinished) {
                            try {
                                Thread.sleep(300)
                            } catch (e: InterruptedException) {
                            }
                        } else {
                            try {
                                Thread.sleep(100)
                            } catch (e: InterruptedException) {
                            }
                        }
                    }

                    //Thread bitiminde button yeniden aktif ediliyor
                    runOnUiThread {
                        startButton.isEnabled = true
                        startButton.textSize = 16f
                        startButton.text = "Restart Test"
                        defaultValues()
                    }
                }
            }).start()
//            }
        }
    }

    fun content() {
        count++
        Log.e("tess cou", "" + count)

        mTelephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        // for API 31+: you need to first check for READ_PHONE_STATE permission to be able to listen to LISTEN_CALL_STATE
        if (Build.VERSION.SDK_INT >= 31) {
//            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                mTelephonyManager?.listen(
//                    mPhoneStateListener,
//                    PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or PhoneStateListener.LISTEN_CELL_INFO or PhoneStateListener.LISTEN_CELL_LOCATION
//                )
//            }
            showInfo()
        } else { // no permission needed
            mTelephonyManager?.listen(
                mPhoneStateListener,
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or PhoneStateListener.LISTEN_CELL_INFO or PhoneStateListener.LISTEN_CELL_LOCATION
            )
        }

//        tvProvider.text = "${radioInfo?.provider}"
//        tvMCC.text = "${radioInfo?.lte_MCC}"
//        tvMNC.text = "${radioInfo?.lte_MNC}"
//        tvCI.text = "${radioInfo?.lteCI}"
//        tvPCI.text = "${radioInfo?.ltePCI}"
//        tvTAC.text = "${radioInfo?.lteTAC}"
//        tvRSRP.text = "${radioInfo?.lteRSRP}\ndBm"
//        tvRSRQ.text = "${radioInfo?.lteRSRQ}\ndB"
//        tvSINR.text = "${radioInfo?.lteSINR}\ndB"
//        tvRSSI.text = "${radioInfo?.lteRSSI}\ndBm"
//        showInfo()
        updateHandler.postDelayed(updateRunnable, 1000)
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            content()
        }
    }

    private fun refreshItem(millisecond: Int) {
        val handler = Handler()
        val runnable = Runnable { content() }
        handler.postDelayed(runnable, millisecond.toLong())
    }

    override fun onResume() {
        super.onResume()
        getSpeedTestHostsHandler = GetSpeedTestHostsHandler()
        getSpeedTestHostsHandler!!.start()

        content()
    }

    fun getPositionByRate(rate: Double): Int {
        if (rate <= 1) {
            return (rate * 30).toInt()
        } else if (rate <= 10) {
            return (rate * 6).toInt() + 30
        } else if (rate <= 30) {
            return ((rate - 10) * 3).toInt() + 90
        } else if (rate <= 50) {
            return ((rate - 30) * 1.5).toInt() + 150
        } else if (rate <= 100) {
            return ((rate - 50) * 1.2).toInt() + 180
        }
        return 0
    }

    override fun onPause() {
        super.onPause()
        updateHandler.removeCallbacksAndMessages(null)
    }

    private val mPhoneStateListener: PhoneStateListener = object : PhoneStateListener() {
        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
//            onCellInfoChanged(null)
            Log.e("tes signal strenght", "tes " + signalStrength)
        }

        override fun onCellLocationChanged(location: CellLocation) {
//            onCellInfoChanged(null)
            Log.e("tes location changed", "tes " + location)
        }

        override fun onCellInfoChanged(cellInfoList: MutableList<android.telephony.CellInfo>?) {
            super.onCellInfoChanged(cellInfoList)
            var cellInfoList = cellInfoList
            var signal = 0
            var EARFCN = 0
            var cellid = 0
            var cid = 0
            var mnc = 0
            var mcc = 0
            var lac = 0
            var pci = 0
            var rsrp = 0
            var rsrq = 0
            var rssnr = 0
            var rssi = 0
            var type = ""
            //StringBuilder error = new StringBuilder();
            val tsLong = System.currentTimeMillis() / 1000
            val ts = tsLong.toString()
            cellInfoList = mTelephonyManager!!.allCellInfo
            if (mTelephonyManager!!.allCellInfo != null) {
//                val cellids = IntArray(cellInfoList.size)
                for (i in cellInfoList.indices) {
                    val info = cellInfoList[i]
                    if (info is CellInfoGsm) {
                        signal = info.cellSignalStrength.dbm
                        type = "GSM"
//                        cellids[i] = cid
                        cid = info.cellIdentity.cid
                        mnc = info.cellIdentity.mnc
                        lac = info.cellIdentity.lac
                        mcc = info.cellIdentity.mcc
                    } else if (info is CellInfoWcdma) {
//                        cellids[i] = cid
                        signal = info.cellSignalStrength.dbm
                        type = "WCDMA"
                        cid = info.cellIdentity.cid
                        mnc = info.cellIdentity.mnc
                        lac = info.cellIdentity.lac
                        mcc = info.cellIdentity.mcc
                    } else if (info is CellInfoCdma) {
//                        cellids[i] = cid
                        signal = info.cellSignalStrength.dbm
                        type = "CDMA"
                        cid = info.cellIdentity.basestationId
                        mnc = info.cellIdentity.systemId
                        lac = info.cellIdentity.networkId
//                        if (mnc != 0) mcc = 280
                    } else if (info is CellInfoLte) {
//                        cellids[i] = cid
                        signal = info.cellSignalStrength.dbm
                        type = "LTE"
                        EARFCN = info.cellIdentity.earfcn
                        cid = info.cellIdentity.ci
                        mnc = info.cellIdentity.mnc
                        lac = info.cellIdentity.tac
                        mcc = info.cellIdentity.mcc
                        pci = info.cellIdentity.pci
                        rsrp = info.cellSignalStrength.rsrp
                        rsrq = info.cellSignalStrength.rsrq
                        rssnr = info.cellSignalStrength.rssnr
                        rssi = info.cellSignalStrength.dbm

                        if (mTelephonyManager?.networkOperatorName != null) tvCellInfoType.text =
                            "${mTelephonyManager?.networkOperatorName + " " + "(${type})"}"
                    }
                    Log.e(
                        "tes info ",
                        "" + ts + "\n Lat: " + "\n Type: " + type + "\n Signal: " + signal + "\n CellID: " + cid + "\n MNC: " + mnc + "\n LAC: " + lac + "\n"
                    )
                    if (EARFCN != 0) tvEARFCN.text = "$EARFCN"
                    val eNodebId = CalculationUtils.getEnodebIdFromCellId(cid)
                    if (eNodebId != 0) tveNB.text = "$eNodebId"
                    val sectorId = CalculationUtils.getSectorIdFromCellId(cid)
                    tvTitleBand.text = "Cell Id"
                    if (sectorId != 0) tvBand.text = "$sectorId"

                    if (mcc != 0 && mcc != Int.MAX_VALUE) tvMCC.text = "$mcc"
                    if (mnc != 0 && mnc != Int.MAX_VALUE) tvMNC.text = "$mnc"
                    if (cid != 0) tvCI.text = "$cid"
                    if (pci != 0) tvPCI.text = "$pci"
                    if (lac != 0) tvTAC.text = "$lac"
                    tvRSRP.text = "${rsrp}\ndBm"
                    tvRSRQ.text = "${rsrq}\ndB"
                    tvSINR.text = "${rssnr}\ndB"
                    tvRSSI.text = "${rssi}\ndBm"

                    if (rsrp > -84) {
                        tvRSRP.background = resources.getDrawable(R.drawable.circle_excelent)
                    } else if (rsrp <= -85 && rsrp >= -102) {
                        tvRSRP.background = resources.getDrawable(R.drawable.circle_good)
                    } else if (rsrp <= -103 && rsrp >= -111) {
                        tvRSRP.background = resources.getDrawable(R.drawable.circle_warning)
                    } else if (rsrp < -111) {
                        tvRSRP.background = resources.getDrawable(R.drawable.circle_bad)
                    }

                    if (rsrq > -9) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_good)
                    } else if (rsrq <= -9 && rsrq >= -12) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_warning)
                    } else if (rsrq < -13) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_bad)
                    }

                    if (rssnr > 12.5) {
                        tvSINR.background = resources.getDrawable(R.drawable.circle_excelent)
                    } else if (rssnr >= 10 && rssnr <= 12.5) {
                        tvSINR.background = resources.getDrawable(R.drawable.circle_good)
                    } else if (rssnr >= 7 && rssnr <= 10) {
                        tvSINR.background = resources.getDrawable(R.drawable.circle_warning)
                    } else if (rssnr < 7) {
                        tvSINR.background = resources.getDrawable(R.drawable.circle_bad)
                    }

                    if (rssi > -65) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_excelent)
                    } else if (rssi <= -65 && rssi >= -75) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_good)
                    } else if (rssi <= -75 && rssi >= -85) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_warning)
                    } else if (rssi < -85) {
                        tvRSRQ.background = resources.getDrawable(R.drawable.circle_bad)
                    }
                }
            } else Log.i("info", "null cellinfo")
//            if (data.length > 0) writeToFile(data, 0)
            //if (error.length() > 0) writeToFile(error, 1);
//            tvNetworkInfo.setText("Changes in Cell info: " + (changes++).toString() + " Current Cells: " + cells)
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun showInfo() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val infos = listGetTowerInfo()
        for (info in infos) {
            Log.d(TAG, "info: $info")
//            mBinding.tvInfo.text = "${sdf.format(Date().time)}: $info\n$text"

            tvCellInfoType.text = "${info.provider + " " + "(${info.cellinfotype})"}"
//            tvProvider.text = "${info.provider}"

            tvEARFCN.text = "${info.EARFCN}"
            val eNodebId = CalculationUtils.getEnodebIdFromCellId(info.cellId.toInt())
            tveNB.text = "$eNodebId"
            val sectorId = CalculationUtils.getSectorIdFromCellId(info.cellId.toInt())
            tvTitleBand.text = "Cell Id"
            tvBand.text = "$sectorId"
            tvMCC.text = "${info.mcc}"
            if (info.mnc != "0") {
                tvMNC.text = "${info.mnc}"
            }
            if (info.tac != "0") {
                tvTAC.text = "${info.tac}"
            }
            if (info.cellId != "0") {
                tvTitleCI.text = "CGI"
                tvCI.text = "${info.cellId}"
            }
            tvPCI.text = "${info.pci}"
            if (info.rsrp.toInt() > -84) {
                tvRSRP.background = resources.getDrawable(R.drawable.circle_excelent)
            } else if (info.rsrp.toInt() <= -85 && info.rsrp.toInt() >= -102) {
                tvRSRP.background = resources.getDrawable(R.drawable.circle_good)
            } else if (info.rsrp.toInt() <= -103 && info.rsrp.toInt() >= -111) {
                tvRSRP.background = resources.getDrawable(R.drawable.circle_warning)
            } else if (info.rsrp.toInt() < -111) {
                tvRSRP.background = resources.getDrawable(R.drawable.circle_bad)
            }

            if (info.rsrq.toInt() > -9) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_good)
            } else if (info.rsrq.toInt() <= -9 && info.rsrq.toInt() >= -12) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_warning)
            } else if (info.rsrq.toInt() < -13) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_bad)
            }

            if (info.sinr.toInt() > 12.5) {
                tvSINR.background = resources.getDrawable(R.drawable.circle_excelent)
            } else if (info.sinr.toInt() >= 10 && info.sinr.toInt() <= 12.5) {
                tvSINR.background = resources.getDrawable(R.drawable.circle_good)
            } else if (info.sinr.toInt() >= 7 && info.sinr.toInt() <= 10) {
                tvSINR.background = resources.getDrawable(R.drawable.circle_warning)
            } else if (info.sinr.toInt() < 7) {
                tvSINR.background = resources.getDrawable(R.drawable.circle_bad)
            }

            if (info.rssi.toInt() > -65) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_excelent)
            } else if (info.rssi.toInt() <= -65 && info.rssi.toInt() >= -75) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_good)
            } else if (info.rssi.toInt() <= -75 && info.rssi.toInt() >= -85) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_warning)
            } else if (info.rssi.toInt() < -85) {
                tvRSRQ.background = resources.getDrawable(R.drawable.circle_bad)
            }

            tvRSRP.text = "${info.rsrp}\ndBm"
            tvRSRQ.text = "${info.rsrq}\ndB"
            tvSINR.text = "${info.sinr}\ndB"
            tvRSSI.text = "${info.rssi}\ndBm"
        }
    }

    private fun listGetTowerInfo(): List<CellInfo> {
        var band = -1
        var EARFCN = -1
        var eNB = -1
        var mcc = -1
        var mnc = -1
        var lac = -1
        var cellId = -1
        var pci = -1
        var rsrp = -1
        var rsrq = -1
        var sinr = -1
        var rssi = -1
        var cellInfo = ""
        val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        var operator = ""
//        mcc = operator.substring(0, 3).toInt()
        val list: MutableList<CellInfo> = ArrayList()
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val infos = tm.allCellInfo
        if (infos == null) {
            val nullList = ArrayList<CellInfo>()
//            nullList.add("获取为空")
            return nullList
        }
        for (info in infos) {
            when (info) {
                //判断主流通信技术
                //电信2g
//                is CellInfoCdma -> {
//                    val cellIdentityCdma = info.cellIdentity
//                    mnc = cellIdentityCdma.systemId
//                    lac = cellIdentityCdma.networkId
//                    cellId = cellIdentityCdma.basestationId
//                    rssi = info.cellSignalStrength.cdmaDbm
//                    cellInfo = "CDMA"
//                }
                //2g
//                is CellInfoGsm -> {
//                    val cellIdentityGsm = info.cellIdentity
//                    mnc = cellIdentityGsm.mnc
//                    lac = cellIdentityGsm.lac
//                    cellId = cellIdentityGsm.cid
//                    rssi = info.cellSignalStrength.dbm
//                    cellInfo = "GSM"
//                }
                //3g-4g
                is CellInfoLte -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val cellIdentityLte = info.cellIdentity
                        cellInfo = "LTE"    //流量＋wifi, 流量
                        operator = cellIdentityLte.operatorAlphaShort.toString()
                        band = cellIdentityLte.bandwidth
                        EARFCN = cellIdentityLte.earfcn
                        eNB = info.cellSignalStrength.cqi
                        mcc = cellIdentityLte.mcc
                        mnc = cellIdentityLte.mnc
                        lac = cellIdentityLte.tac
                        cellId = cellIdentityLte.ci
                        pci = cellIdentityLte.pci
                        rsrp = info.cellSignalStrength.rsrp
                        rsrq = info.cellSignalStrength.rsrq
                        if (info.cellSignalStrength.rssnr != Int.MAX_VALUE) {
                            sinr = info.cellSignalStrength.rssnr
                        } else {
                            sinr = 0
                        }
                        if (info.cellSignalStrength.rssi != Int.MAX_VALUE) {
                            rssi = info.cellSignalStrength.rssi
                        } else {
                            rssi = info.cellSignalStrength.dbm
                        }
                        Log.e("celinf", "$cellIdentityLte")
                    }

//                    cellInfoData.add(CellInfo(operator, mcc.toString(), mnc.toString(), "$lac", "$cellId", "$pci",
//                        "$rsrp", "$rsrq", "$sinr", "$rssi"))
                }
                //3g
//                is CellInfoWcdma -> {
//                    val cellIdentityWcdma = info.cellIdentity
//                    mnc = cellIdentityWcdma.mnc
//                    lac = cellIdentityWcdma.lac
//                    cellId = cellIdentityWcdma.cid
//                    rssi = info.cellSignalStrength.dbm
//                    cellInfo = "WCDMA"    //wifi
//                }
                else -> {
                    Log.e(TAG, "get CellInfo error ")
                }
            }
            if (mnc == Int.MAX_VALUE) {
                break
            }
            val jsonObject = JSONObject()
            try {
                jsonObject.put("cellId", cellId)
                jsonObject.put("lac", lac)
                jsonObject.put("level", rssi)
                jsonObject.put("mcc", mcc)
                jsonObject.put("mnc", mnc)
                jsonObject.put("pci", pci)
                jsonObject.put("cellInfo", cellInfo)
                jsonObject.put("provider", operator)
                jsonObject.put("rsrp", rsrp)
                jsonObject.put("rsrq", rsrq)
                jsonObject.put("sinr", sinr)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            list.clear()

//            if (cellInfo == "LTE")
            list.add(
                CellInfo(
                    cellInfo, operator, "$band", "$EARFCN", "$eNB", "$mcc", "$mnc", "$lac",
                    "$cellId", "$pci", "$rsrp", "$rsrq", "$sinr", "$rssi"
                )
            )
        }
        return list
    }

    private fun processCellularGroup(cellularGroup: List<CellularRecordWrapper>) {
        if (cellularGroup.isEmpty()) clearCellularUi()
        val gsmNeighbors: MutableList<GsmRecordData> = ArrayList()
        val umtsNeighbors: MutableList<UmtsRecordData> = ArrayList()
        val lteNeighbors: MutableList<LteRecordData> = ArrayList()
        val nrNeighbors: MutableList<NrRecordData> = ArrayList()
        for (cellularRecord in cellularGroup) {
            when (cellularRecord.cellularProtocol) {
//                NONE -> return
                CellularProtocol.GSM -> {
                    val gsmData = (cellularRecord.cellularRecord as GsmRecord).data
                    if (gsmData.hasServingCell() && gsmData.servingCell.value) {
//                        viewModel.setServingCellProtocol(cellularRecord.cellularProtocol)
//                        processGsmServingCell(gsmData)
                    } else {
                        gsmNeighbors.add(gsmData)
                    }
                }
                CellularProtocol.CDMA -> {}
                CellularProtocol.UMTS -> {
                    val umtsData = (cellularRecord.cellularRecord as UmtsRecord).data
                    if (umtsData.hasServingCell() && umtsData.servingCell.value) {
//                        viewModel.setServingCellProtocol(cellularRecord.cellularProtocol)
//                        processUmtsServingCell(umtsData)
                    } else {
                        umtsNeighbors.add(umtsData)
                    }
                }
                CellularProtocol.LTE -> {
                    val lteData = (cellularRecord.cellularRecord as LteRecord).data
                    if (lteData.hasServingCell() && lteData.servingCell.value) {
//                        viewModel.setServingCellProtocol(cellularRecord.cellularProtocol)
//                        processLteServingCell(lteData)
                    } else {
                        lteNeighbors.add(lteData)
                    }
                }
                CellularProtocol.NR -> {
                    val nrData = (cellularRecord.cellularRecord as NrRecord).data
                    if (nrData.hasServingCell() && nrData.servingCell.value) {
//                        viewModel.setServingCellProtocol(cellularRecord.cellularProtocol)
//                        processNrServingCell(nrData)
                    } else {
                        nrNeighbors.add(nrData)
                    }
                }
            }
        }
//        processGsmNeighbors(gsmNeighbors)
//        processUmtsNeighbors(umtsNeighbors)
//        processLteNeighbors(lteNeighbors)
//        processNrNeighbors(nrNeighbors)
    }

    private fun clearCellularUi() {
        // TODO Will this happen via the other listener?
        /*viewModel.setDataNetworkType("");
        viewModel.setCarrier("");
        viewModel.setVoiceNetworkType("");*/
//        viewModel.setServingCellProtocol(CellularProtocol.NONE)
//        viewModel.setMcc("")
//        viewModel.setMnc("")
//        viewModel.setAreaCode("")
//        viewModel.setCellId(null)
//        viewModel.setChannelNumber("")
//        viewModel.setPci("")
//        viewModel.setBandwidth("")
//        viewModel.setTa("")
//        viewModel.setSignalOne(null)
//        viewModel.setSignalTwo(null)
//        viewModel.setNrNeighbors(Collections.emptySortedSet())
//        viewModel.setLteNeighbors(Collections.emptySortedSet())
//        viewModel.setUmtsNeighbors(Collections.emptySortedSet())
//        viewModel.setGsmNeighbors(Collections.emptySortedSet())
    }

    private fun getPositionByRate2(rate: Double): Int {
        if (rate <= 1) {
            return (rate * 30).toInt()
        } else if (rate <= 2) {
            return (rate * 3).toInt() + 30
        } else if (rate <= 3) {
            return (rate * 3).toInt() + 60
        } else if (rate <= 4) {
            return (rate * 3).toInt() + 90
        } else if (rate <= 5) {
            return (rate * 3).toInt() + 120
        } else if (rate <= 10) {
            return ((rate - 5) * 6).toInt() + 150
        } else if (rate <= 50) {
            return ((rate - 10) * 1.33).toInt() + 180
        } else if (rate <= 100) {
            return ((rate - 50) * 0.6).toInt() + 180
        }
        return 0
    }

    fun init() {
        iv_upload.alpha = 0.5f
        iv_download.alpha = 0.5f
        val entryList: MutableList<Entry> = ArrayList()
        entryList.add(Entry(0F, 0F))
        lineDataSet = LineDataSet(entryList, "")
        lineDataSet?.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet?.setDrawValues(false)
        lineDataSet?.setDrawCircleHole(false)
        lineDataSet?.color = Color.rgb(145, 174, 210)
        lineDataSet?.setCircleColor(Color.rgb(145, 174, 210))
        lineDataSet?.lineWidth = 2f
        lineDataSet?.setDrawFilled(false)
        lineDataSet?.isHighlightEnabled = false
        lineDataSet?.setDrawCircles(false)
        lineData = LineData(lineDataSet)
        linechart?.data = lineData
        linechart?.axisRight?.setDrawGridLines(false)
        linechart?.axisRight?.setDrawLabels(false)
        linechart?.axisLeft?.setDrawGridLines(false)
        linechart?.axisLeft?.setDrawLabels(false)
        linechart?.fitScreen()
        linechart?.setVisibleXRange(0f, 10f)
        linechart?.setNoDataText("TAP SCAN")
        linechart?.setNoDataTextColor(R.color.cp_0)
        linechart?.xAxis?.setDrawGridLines(false)
        linechart?.xAxis?.setDrawLabels(false)
        linechart?.legend?.isEnabled = false
        linechart?.description?.isEnabled = false
        linechart?.setScaleEnabled(true)
        linechart?.setDrawBorders(false)
        linechart?.axisLeft?.isEnabled = false
        linechart?.axisRight?.isEnabled = false
        linechart?.xAxis?.isEnabled = false
        linechart?.setViewPortOffsets(10f, 10f, 10f, 10f)
        linechart?.animateX(1000)
        tempBlackList = HashSet()
        getSpeedTestHostsHandler = GetSpeedTestHostsHandler()
        getSpeedTestHostsHandler!!.start()
        defaultValues()
    }

    private fun defaultValues() {
        tickProgressBar!!.progress = 0
        tvSpeed.text = "0.0"
    }

    companion object {
        var radioInfo: RadioInfo? = null
    }
}