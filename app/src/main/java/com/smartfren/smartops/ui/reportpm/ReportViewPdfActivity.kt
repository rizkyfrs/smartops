package com.smartfren.smartops.ui.reportpm

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.*
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import kotlinx.android.synthetic.main.pdf_view_activity.*
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ReportViewPdfActivity : BaseActivity() {
    lateinit var pdfView: PDFView
    internal var apkStorage: File? = null
    internal var outputFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_view_activity)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        apkStorage = File(this.filesDir, "omsmedia")
        if (!apkStorage!!.exists()) {
            apkStorage!!.mkdir()
        }

        DownloadingTask().execute()

        pdfView = findViewById(R.id.pdfView)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DownloadingTask : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loading(true)
        }

        override fun doInBackground(vararg arg0: Void): Void? {
            try {
                outputFile = File(apkStorage, "media" + ".pdf")//Create Output file in Main File
//
                //Create New File if not present
                val url = URL(intent.getStringExtra("urlPdf").toString())//Create Download URl
                val c = url.openConnection() as HttpURLConnection//Open Url Connection
                c.requestMethod = "GET"//Set Request Method to "GET" since we are grtting data
                c.connect()//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(ContentValues.TAG, "Server returned HTTP " + c.responseCode + " " + c.responseMessage)
                }

                outputFile!!.createNewFile()
                Log.e(ContentValues.TAG, "File Created")

                val fos = FileOutputStream(outputFile!!)//Get OutputStream for NewFile Location

                val iss = c.inputStream//Get InputStream for connection

                val buffer = ByteArray(1024)//Set buffer type
                var len1 = 0//init length
                while ({ len1 = iss.read(buffer); len1 }() != -1) {
                    fos.write(buffer, 0, len1)//Write new file
                }

                //Close all connection after doing task
                fos.close()
                iss.close()
            } catch (e: Exception) {
                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e(ContentValues.TAG, "Download Error Exception " + e.message)
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            loading(false)
            val myFile = File(filesDir, "omsmedia/" + "media.pdf")

            pdfView.fromFile(myFile)
                .onPageError(object : OnPageErrorListener {
                    override fun onPageError(page: Int, t: Throwable) {
                        loading(false)
                        Toast.makeText(this@ReportViewPdfActivity, "Terjadi kesalahan", Toast.LENGTH_LONG).show()
                    }
                })
                .onTap(object : OnTapListener {
                    override fun onTap(e: MotionEvent): Boolean {
                        return true
                    }
                })
                .onRender(object : OnRenderListener {
                    override fun onInitiallyRendered(nbPages: Int, pageWidth: Float, pageHeight: Float) {
                        pdfView.fitToWidth()
                    }
                })
                .enableAnnotationRendering(true)
                .invalidPageColor(Color.WHITE)
                .load()
        }
    }

}