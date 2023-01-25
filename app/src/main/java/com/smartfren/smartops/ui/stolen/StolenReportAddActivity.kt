package com.smartfren.smartops.ui.stolen

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Stolen
import com.smartfren.smartops.entity.SiteInfo
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.stolen.adapter.StolenPhotoAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.stolen_report_add.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StolenReportAddActivity : BaseActivity() {
    private var token: String? = null
    private var userID: String? = null
    private var filepathDoc: MutableList<MultipartBody.Part?> = mutableListOf()
    private var filepathDocChronology: MultipartBody.Part? = null
    private var filepathDocPolice: MultipartBody.Part? = null
    private var filepathDocSIR: MultipartBody.Part? = null
    private var listSiteID: MutableList<String?> = mutableListOf()
    private var listFIle: MutableList<Int?> = mutableListOf()
    private val photo = mutableListOf<Stolen>()
    private var mStolenPhotoAdapter: StolenPhotoAdapter? = null
    private val mStolenReportViewModel: StolenViewModel by viewModels()
    private val mDailyActivityViewModel: DailyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stolen_report_add)
        setupUI()
        setupRV()
    }

    private fun setupUI() {
        listFIle.clear()
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        userID = getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())
        mStolenReportViewModel.setRegionSelected(MainApp.instance.sharedPreferences?.getString("regionName", ""))

        token?.let { getSite(it, mStolenReportViewModel.setRegionCode()) }

        etAddStolenRegion.setText(mStolenReportViewModel.setRegion())
        tvAddStolenSiteID.setOnClickListener {
            showDialogSiteID(mDailyActivityViewModel.getSiteInfo())
        }

        val cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        etAddStolenDoL.setText(sdf.format(cal.time))

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etAddStolenDoL.setText(sdf.format(cal.time))
        }

        btnAddStolenDoL.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnAddStolenPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = "image/*"
            }
            startActivityForResult(intent, 123)
        }

        clAddStolenDocumentation.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = "image/*"
            }
            startActivityForResult(intent, 123)
        }

        clAddStolenDocChronology.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            startActivityForResult(intent, 124)
        }

        clAddStolenDocPoliceReport.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            startActivityForResult(intent, 125)
        }

        clAddStolenDocSIR.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            startActivityForResult(intent, 126)
        }

        btnSaveAddStolen.setOnClickListener {
            if (tvAddStolenSiteID.text.isEmpty()) {
                toast(this, "Please select Site ID!")
            } else if (etAddStolenDoL.text.toString().isEmpty()) {
                toast(this, "Please enter Date of Lost!")
            } else if (etAddStolenExistingSiteSec.text.toString().isEmpty()) {
                toast(this, "Please enter Existing Site Security!")
            } else if (etAddStolenMaterial.text.toString().isEmpty()) {
                toast(this, "Please enter Stolen Material!")
            } else if (etAddStolenChronologyReport.text.toString().isEmpty()) {
                toast(this, "Please enter Chronology Report!")
            } else if (etAddStolenPoliceReport.text.toString().isEmpty()) {
                toast(this, "Please enter Police Report!")
            } else if (etAddStolenRecoverSecurityImprov.text.toString().isEmpty()) {
                toast(this, "Please enter Recovery-Security Improve!")
            } else if (etAddStolenInvestigationReport.text.toString().isEmpty()) {
                toast(this, "Please enter Investigation Report!")
            } else {
                postStolenReportAdd(
                    mStolenReportViewModel.setRegionCode(), tvAddStolenSiteID.text.toString(), parseDateToBackend(etAddStolenDoL.text.toString()),
                    etAddStolenExistingSiteSec.text.toString(), etAddStolenMaterial.text.toString(), "", "",
                    etAddStolenChronologyReport.text.toString(), "", etAddStolenPoliceReport.text.toString(), "",
                    etAddStolenRecoverSecurityImprov.text.toString(), etAddStolenInvestigationReport.text.toString(), "",
                    userID, setCurrentTime()
                )
            }


//            for (i in 0..4) {
//                if (i == 4) {
//                    showDialog("", "$i", false)
//                }
//            }


//            listFIle.sortBy { it }
//            for (i in listFIle.distinct().toList()) {
//                when (i) {
//                    1 -> {
//                        for (file in photo) {
//                            postStolenUploadFile(
//                                token, "tb_stolen_report", "sr_num", "41",
//                                "sr_photo", "nol", file.file
//                            )
//                        }
//                    }
//                    2 -> {
//                        if (filepathDocChronology != null)
//                            postStolenUploadFile(
//                                token, "tb_stolen_report", "sr_num", "41",
//                                "sr_doc_kronologi", "nol", filepathDocChronology
//                            )
//                    }
//                    3 -> {
//                        if (filepathDocPolice != null)
//                            postStolenUploadFile(
//                                token, "tb_stolen_report", "sr_num", "41",
//                                "sr_doc_lkp", "nol", filepathDocPolice
//                            )
//                    }
//                    4 -> {
//                        if (filepathDocSIR != null)
//                            postStolenUploadFile(
//                                token, "tb_stolen_report", "sr_num", "41",
//                                "sr_doc_investigation", "nol", filepathDocSIR
//                            )
//                    }
//                }
//            }
        }

        btnOk.setOnClickListener {
            clAddStolenSuccess.hide()
            finish()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = rvStolenPhoto
        mStolenPhotoAdapter = StolenPhotoAdapter(this, photo)
        recyclerview.adapter = mStolenPhotoAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data!! //The uri with the location of the file
            Log.e("selectedFileee", "$selectedFile")
            val uri: Uri = data.data!!
            val auxFile = uri.path?.let { File(it) }
            val uriString: String = uri.toString()
            var pdfName: String? = null
            if (uriString.startsWith("content://")) {
                var myCursor: Cursor? = null
                try {
                    myCursor = applicationContext!!.contentResolver.query(selectedFile, null, null, null, null)
                    if (myCursor != null && myCursor.moveToFirst()) {
                        pdfName = myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        Log.e("selectedFileeNmae", "$pdfName")
                    }
                } finally {
                    myCursor?.close()
                }
            }
            Log.e("selectfile", "$filepathDoc")
        } else if (requestCode == 123 && resultCode == RESULT_OK) {
            listFIle.add(1)
            if (data?.clipData != null) {
                // Getting the length of data and logging up the logs using index
                for (index in 0 until data.clipData?.itemCount!!) {
                    // Getting the URIs of the selected files and logging them into logcat at debug level
                    val uri: Uri = data.clipData?.getItemAt(index)!!.uri
                    val fileType = getFileType(uri)
                    val file = File.createTempFile("img", ".$fileType")

                    val model = Stolen("", "$uri", buildImageBodyPart2("file_load", "${file.name}", file))
                    mStolenPhotoAdapter?.addPhoto(model)
                    filepathDoc.add(buildImageBodyPart2("file_load", "${file.name}", file))

                    Log.e("filesUri [$uri] : ", uri.toString())
                }
                llStolenDocumentation.hide()
                rvStolenPhoto.show()
            } else if (data?.data != null) {
                val uri: Uri? = data.data
                val fileType = uri?.let { getFileType(it) }
                val file = File.createTempFile("img", ".$fileType")
                val model = Stolen("", "$uri", buildImageBodyPart2("file_load", "${file.name}", file))
                mStolenPhotoAdapter?.addPhoto(model)
                filepathDoc.add(buildImageBodyPart2("file_load", "${file.name}", file))
                llStolenDocumentation.hide()
                rvStolenPhoto.show()
            }
        } else if (requestCode == 124 && resultCode == RESULT_OK) {
            listFIle.add(2)
            data?.data?.let { uri ->
                val fileType = getFileType(uri)
                val file = File.createTempFile("doc", ".$fileType")
                filepathDocChronology = buildImageBodyPart2("file_load", "${file.name}", file)
                llStolenDocChronology.hide()
                tvDocChronology.show()
                tvDocChronology.text = "${file.name}"
            }
        } else if (requestCode == 125 && resultCode == RESULT_OK) {
            listFIle.add(3)
            data?.data?.let { uri ->
                val fileType = getFileType(uri)
                val file = File.createTempFile("doc", ".$fileType")
                filepathDocPolice = buildImageBodyPart2("file_load", "${file.name}", file)
                llStolenDocPoliceReport.hide()
                tvDocPolice.show()
                tvDocPolice.text = "${file.name}"
            }
        } else if (requestCode == 126 && resultCode == RESULT_OK) {
            listFIle.add(4)
            data?.data?.let { uri ->
                val fileType = getFileType(uri)
                val file = File.createTempFile("doc", ".$fileType")
                filepathDocSIR = buildImageBodyPart2("file_load", "${file.name}", file)
                llStolenDocSIR.hide()
                tvDocSIR.show()
                tvDocSIR.text = "${file.name}"
            }
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postStolenReportAdd(
        sr_region: String?, sr_site_id: String?, sr_date_of_loss: String?, sr_existing_security: String?, sr_stolen_material: String?,
        sr_photo: String?, sr_alarm: String?, sr_kronologi: String?, sr_doc_kronologi: String?, sr_lkp: String?, sr_doc_lkp: String?,
        sr_recovery: String?, sr_investigation: String?, sr_doc_investigation: String?, sr_registrars: String?, sr_registerdate: String?
    ) {
        mStolenReportViewModel.setPostStolenReport(
            sr_region,
            sr_site_id,
            sr_date_of_loss,
            sr_existing_security,
            sr_stolen_material,
            "",
            sr_alarm,
            sr_kronologi,
            "",
            sr_lkp,
            "",
            sr_recovery,
            sr_investigation,
            "",
            sr_registrars,
            sr_registerdate
        )
        mStolenReportViewModel.postStolenReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenResponse ->
                            if (stolenResponse.success == true) {
                                listFIle.sortBy { it }
                                for (i in listFIle.distinct().toList()) {
                                    when (i) {
                                        1 -> {
                                            for (file in photo) {
                                                postStolenUploadFile(
                                                    token, "tb_stolen_report", "sr_num", stolenResponse.cvStolenReport?.srNum,
                                                    "sr_photo", "nol", file.file
                                                )
                                            }
                                        }
                                        2 -> {
                                            if (filepathDocChronology != null)
                                                postStolenUploadFile(
                                                    token, "tb_stolen_report", "sr_num", stolenResponse.cvStolenReport?.srNum,
                                                    "sr_doc_kronologi", "nol", filepathDocChronology
                                                )
                                        }
                                        3 -> {
                                            if (filepathDocPolice != null)
                                                postStolenUploadFile(
                                                    token, "tb_stolen_report", "sr_num", stolenResponse.cvStolenReport?.srNum,
                                                    "sr_doc_lkp", "nol", filepathDocPolice
                                                )
                                        }
                                        4 -> {
                                            if (filepathDocSIR != null)
                                                postStolenUploadFile(
                                                    token, "tb_stolen_report", "sr_num", stolenResponse.cvStolenReport?.srNum,
                                                    "sr_doc_investigation", "nol", filepathDocSIR
                                                )
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

    private fun postStolenUploadFile(
        token: String?,
        table_name: String?,
        table_key: String?,
        table_key_id: String?,
        table_column: String?,
        file_dir: String?,
        file_load: MultipartBody.Part?
    ) {
        mStolenReportViewModel.setPostUploadFileStolen(
            token, table_name, table_key, table_key_id, table_column, file_dir, file_load
        )
        mStolenReportViewModel.postUploadFileStolen.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenResponse ->
                            if (stolenResponse.report?.success == true) {
                                clAddStolenSuccess.show()
                                tvTitleSuccess.text = getString(R.string.add_success)
                                tvSuccess.text = getString(R.string.stolen_report_has_been_add)
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

    private fun getSite(token: String, site: String) {
        listSiteID.clear()
//        listSiteID.addAll()
//        mDailyActivityViewModel.listSite(token, site)
//        mDailyActivityViewModel.siteData.observe(this) { event ->
//            event.getContentIfNotHandled()?.let { response ->
//                when (response) {
//                    is Resource.Success -> {
//                        loading(false)
//                        response.data?.let { siteResponse ->
//                            if (siteResponse.getReport()?.success == true) {
//                                val itemName = object : ArrayList<String>() {
//                                    init {
//                                        for (element in siteResponse.getReport()?.sites!!) {
//                                            element.siteId?.let { add(it) }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        loading(false)
//                        response.message?.let {
//                            toast(this, it)
//                        }
//                    }
//
//                    is Resource.Loading -> {
//                        loading(true)
//                    }
//                }
//            }
//        }
    }

    private fun showDialogSiteID(listSiteID: MutableList<String>) {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up_spinner_search)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val editText = dialog.findViewById(R.id.etSiteID) as EditText
        val listView = dialog.findViewById(R.id.lvSiteID) as ListView
        val btnClose = dialog.findViewById(R.id.btnClose) as ImageView

        dialog.show()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listSiteID)

        listView.adapter = adapter
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            tvAddStolenSiteID.text = adapter.getItem(position).toString()
            dialog.dismiss()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }
}