package com.smartfren.smartops.ui.corefacility.nfmtiket

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_report_nfm_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NFMTicketDetailActivity : BaseActivity() {
    private var gpsStatus: Boolean = false
    private var leveID: String? = null
    private var token: String? = null
    private val nfmTicketViewModel: NFMTicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_nfm_detail)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        locationEnabled()
        intent.getStringExtra("ticketNum")?.let {
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

    private fun getCCFReportDetail(token: String?, ticket_id: String?) {
        nfmTicketViewModel.detailNFMTicket(token, ticket_id)
        nfmTicketViewModel.nfmTicketDetailData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { nfmTicketResponse ->
                            if (nfmTicketResponse.getReport()?.success == true) {
                                for (data in nfmTicketResponse.getReport()?.info!!) {
                                    tvNFMTicketNum.text = data.nFMticketNum
                                    tvNFMAlarmCode.text = data.nFMAlarmCode
                                    tvNFMAlarmValue.text = data.nFMAlarmValue
                                    tvNFMAlarmLocation.text = data.nFMAlarmValue?.take(10)
                                    tvNFMActivityLocation.text = data.nfmip
                                    tvNFMActivityRegisDate.text = data.nFMAlarmRegisterDate
                                    tvNFMActivityReport.text = data.nFMTicketACKStatus

                                    if (data.nFMTicketACKStatus == "ACK") {
                                        btnNFMValidation.hide()
                                    }

                                    btnNFMValidation.setOnClickListener {
                                        if (gpsStatus) {
                                            postValidate(token, data.nFMticketID)
                                        } else {
                                            alertDialogLocation()
                                        }
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

    private fun postValidate(token: String?, ticket_id: String?) {
        nfmTicketViewModel.validate(token, ticket_id)
        nfmTicketViewModel.validateData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { responses ->
                            if (responses.getReport()?.success == true) {
                                showDialog("", "", true)
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

    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}