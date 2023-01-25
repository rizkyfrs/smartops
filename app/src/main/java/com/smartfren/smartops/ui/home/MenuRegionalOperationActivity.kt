package com.smartfren.smartops.ui.home

import android.content.Intent
import android.os.Bundle
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.consumable.ConsumableUsageActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivity
import com.smartfren.smartops.ui.history.MyHistoryActivity
import com.smartfren.smartops.ui.reportactivity.ActivityReportActivity
import com.smartfren.smartops.ui.sitevisit.SiteVisitLogActivity
import com.smartfren.smartops.ui.tracker.risk.TrackerRiskActivity
import com.smartfren.smartops.ui.ttwo.TTWOActivity
import com.smartfren.smartops.utils.hide
import kotlinx.android.synthetic.main.menu_regional_operation.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MenuRegionalOperationActivity : BaseActivity() {
    private var type: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_regional_operation)
        setupUI()
    }

    private fun setupUI() {
        type = MainApp.instance.sharedPreferences?.getString("levelID", "")

        if (type == "20") {
            btnDailyActivity.hide()
            btnActivityForm.hide()
            btnActivityLog.hide()
            btnDailyActivity.hide()
            btnMDR.hide()
            btnSiteVisit.hide()
            btnConsumableUsage.hide()
            btnTTWORCA.hide()

            lineOne.hide()
            lineTwo.hide()
            lineThree.hide()
            lineFour.hide()
            lineFive.hide()
            lineSix.hide()
            lineSeven.hide()

            btnTrackerRisk.setOnClickListener {
                val intent = Intent(applicationContext, TrackerRiskActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnDailyActivity.setOnClickListener {
                val intent = Intent(applicationContext, DailyActivity::class.java)
                startActivity(intent)
            }

            btnActivityForm.setOnClickListener {
                val intent = Intent(applicationContext, ActivityReportActivity::class.java)
                startActivity(intent)
            }

            btnActivityLog.setOnClickListener {
                val intent = Intent(applicationContext, MyHistoryActivity::class.java)
                startActivity(intent)
            }

            btnMDR.setOnClickListener {
//            val intent = Intent(applicationContext, MDRActivity::class.java)
//            startActivity(intent)
            }

            btnSiteVisit.setOnClickListener {
                val intent = Intent(applicationContext, SiteVisitLogActivity::class.java)
                startActivity(intent)
            }

            btnConsumableUsage.setOnClickListener {
                val intent = Intent(applicationContext, ConsumableUsageActivity::class.java)
                startActivity(intent)
            }

            btnTTWORCA.setOnClickListener {
                val intent = Intent(applicationContext, TTWOActivity::class.java)
                startActivity(intent)
            }

            btnTrackerRisk.setOnClickListener {
                val intent = Intent(applicationContext, TrackerRiskActivity::class.java)
                startActivity(intent)
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}