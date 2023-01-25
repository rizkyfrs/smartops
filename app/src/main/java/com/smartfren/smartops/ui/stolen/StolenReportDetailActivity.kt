package com.smartfren.smartops.ui.stolen

import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.stolen_report_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StolenReportDetailActivity : BaseActivity() {

    private val mStolenReportViewModel: StolenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stolen_report_detail)
        setupUI()
    }

    private fun setupUI() {
        getViewStolenReport(intent.getStringExtra("stolenID"))

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getViewStolenReport(key: String?) {
        mStolenReportViewModel.setViewStolenReport(key)
        mStolenReportViewModel.viewStolenReport.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenResponse ->
                            if (stolenResponse.success == true) {
                                tvStolenNumber.text = stolenResponse.cvStolenReport?.srNum
                                tvStolenRegion.text = stolenResponse.cvStolenReport?.srRegion
                                tvStolenSiteID.text = stolenResponse.cvStolenReport?.srSiteId
                                tvStolenDoL.text = stolenResponse.cvStolenReport?.srDateOfLoss
                                if (stolenResponse.cvStolenReport?.srExistingSecurity != null) tvStolenExistSecure.text =
                                    stolenResponse.cvStolenReport.srExistingSecurity
                                if (stolenResponse.cvStolenReport?.srStolenMaterial != null) tvStolenMaterial.text =
                                    stolenResponse.cvStolenReport.srStolenMaterial
                                if (stolenResponse.cvStolenReport?.srAlarmName != null) tvStolenAlarmName.text =
                                    stolenResponse.cvStolenReport.srAlarmName
                                if (stolenResponse.cvStolenReport?.srPhoto != null) {
                                    Glide.with(this)
                                        .load("https://oms.smartfren.com/doc/cons/before/" + stolenResponse.cvStolenReport.srPhoto)
                                        .into(ivStolenPhoto)
                                    ivStolenPhotoDefault.hide()
                                    ivStolenPhoto.setOnClickListener {
                                        dialogShowImage("https://oms.smartfren.com/doc/cons/before/${stolenResponse.cvStolenReport.srPhoto}")
                                    }
                                } else {
                                    ivStolenPhotoDefault.show()
                                }
                                if (stolenResponse.cvStolenReport?.srKronologi != null) tvStolenKronologi.text =
                                    stolenResponse.cvStolenReport.srKronologi
                                if (stolenResponse.cvStolenReport?.srDocKronologi != null) {
                                    tvStolenDocKronologi.show()
                                    tvStolenDocKronologi.text = stolenResponse.cvStolenReport.srDocKronologi
                                }
                                if (stolenResponse.cvStolenReport?.srLkp != null) tvStolenLkp.text = stolenResponse.cvStolenReport.srLkp
                                if (stolenResponse.cvStolenReport?.srDocLkp != null) {
                                    tvStolenDocLkp.show()
                                    tvStolenDocLkp.text = stolenResponse.cvStolenReport.srDocLkp
                                }
                                if (stolenResponse.cvStolenReport?.srRecovery != null) tvStolenRecovery.text =
                                    stolenResponse.cvStolenReport.srRecovery
                                if (stolenResponse.cvStolenReport?.srInvestigation != null) tvStolenInvestigation.text =
                                    stolenResponse.cvStolenReport.srInvestigation
                                if (stolenResponse.cvStolenReport?.srDocInvestigation != null) {
                                    tvStolenDocInvestigation.show()
                                    tvStolenDocInvestigation.text = stolenResponse.cvStolenReport.srDocInvestigation
                                }
                                if (stolenResponse.cvStolenReport?.srRegistrars != null) tvStolenRegis.text =
                                    stolenResponse.cvStolenReport.srRegistrars
                                if (stolenResponse.cvStolenReport?.srRegisterdate != null) tvStolenRegisDate.text =
                                    stolenResponse.cvStolenReport.srRegisterdate
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
}