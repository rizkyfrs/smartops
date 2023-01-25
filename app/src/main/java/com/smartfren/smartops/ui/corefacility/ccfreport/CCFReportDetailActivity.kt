package com.smartfren.smartops.ui.corefacility.ccfreport

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_report_ccf_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CCFReportDetailActivity : BaseActivity() {
    var gpsStatus: Boolean = false
    private var leveID: String? = null
    private var token: String? = null
    private val ccfReportViewModel: CCFReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_ccf_detail)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        locationEnabled()
        intent.getStringExtra("report_id")?.let {
            getCCFReportDetail(token, it)
        }
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        leveID = MainApp.instance.sharedPreferences?.getString("levelID", "")

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getCCFReportDetail(token: String?, report_id: String?) {
        ccfReportViewModel.detailCCFReport(token, report_id)
        ccfReportViewModel.ccfReportDetailData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { ccfReportResponse ->
                            if (ccfReportResponse.getReport()?.success == true) {
                                for (data in ccfReportResponse.getReport()?.info!!) {
                                    tvCCFActivityNumber.text = data.activityNum
                                    tvCCFActivityType.text = data.activityType
                                    tvCCFActivityName.text = data.activityCategory
                                    tvCCFActivityRegion.text = data.region
                                    tvCCFActivityLocation.text = data.activityLoc
                                    tvCCFActivityPIC.text = data.activityPIC
                                    tvCCFActivityReport.text = data.activityReport
                                    tvCCFActivityValidate.text = data.validationCCF
                                    tvSMEActivityValidate.text = data.validationSME

                                    tvCCFActivityReport.setOnClickListener {
                                        val openURL = Intent(Intent.ACTION_VIEW)
                                        openURL.data =
                                            Uri.parse("https://oms.smartfren.com/doc/power/${data.activityReport}")
                                        startActivity(openURL)
                                    }

                                    if (data.validationCCF == "val" && leveID == "96") {
                                        btnValidation.hide()
                                    } else if (data.validationSME == "val" && leveID == "97") {
                                        btnValidation.hide()
                                    }

                                    btnValidation.setOnClickListener {
                                        postValidate(token, data.activityNum)
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

    private fun postValidate(token: String?, report_id: String?) {
        ccfReportViewModel.validate(token, report_id)
        ccfReportViewModel.validateData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { response ->
                            if (response.getReport()?.success == true) {
                                showDialog()
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

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()

    }


    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun alertDialog() {
        val mAlertDialog = AlertDialog.Builder(this@CCFReportDetailActivity)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_oms) //set alertdialog icon
        mAlertDialog.setTitle(getString(R.string.string_title_location_setting)) //set alertdialog title
        mAlertDialog.setMessage(getString(R.string.string_detail_location_setting)) //set alertdialog message
        mAlertDialog.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            //perform some tasks here
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        mAlertDialog.setNegativeButton(getString(R.string.no)) { dialog, id ->
            //perform som tasks here
        }
        mAlertDialog.show()
    }
}