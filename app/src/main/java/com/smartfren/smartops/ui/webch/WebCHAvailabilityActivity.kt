package com.smartfren.smartops.ui.webch

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.databinding.WebchAvailabilityBinding
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity.btnBack
import kotlinx.android.synthetic.main.webch_availability.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WebCHAvailabilityActivity : BaseActivity() {
    private var token: String? = null
    private lateinit var binding: WebchAvailabilityBinding
    private val webCHAvailabilityViewModel: WebCHAvailabilityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WebchAvailabilityBinding.inflate(layoutInflater)
        binding.webchAvailabilityViewModel = webCHAvailabilityViewModel
        val view = binding.root
        setContentView(view)

        token = MainApp.instance.sharedPreferences?.getString("token", "")

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnRetrieveReport.setOnClickListener {
            if (webCHAvailabilityViewModel.setPeriod() != "0") {
                val intent = Intent(applicationContext, LineChartActivity::class.java)
                intent.putExtra("token", token)
                intent.putExtra("region", webCHAvailabilityViewModel.setRegion())
                intent.putExtra("nameregion", webCHAvailabilityViewModel.setNameRegion())
                intent.putExtra("period", webCHAvailabilityViewModel.setPeriod())
                intent.putExtra("nameperiod", webCHAvailabilityViewModel.setNamePeriod())
                startActivity(intent)
            } else {
                toast(this, "Please Select Period.")
            }
        }
    }
}