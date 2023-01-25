package com.smartfren.smartops.ui.nfm

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.ContentMenu
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.ui.nfm.adapter.NFMHomeAdapter
import kotlinx.android.synthetic.main.nfm_homebase_activity.*

class NFMHomeActivity : BaseActivity() {
    private var mNFMHomeAdapter: NFMHomeAdapter? = null
    private var cityNFM = ""
    private var idNFM = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfm_homebase_activity)
        setupRV()
        setupUI()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, HomepageActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//    }

    private fun setupUI() {
        if (!intent.getStringExtra("statusNFM").isNullOrEmpty()) {
//            cityNFM = intent.getStringExtra("location").toString()
//            idNFM = intent.getStringExtra("content").toString()
            val intent = Intent(this, NFMAlarmActivity::class.java)
//            intent.putExtra("cityNFMItem", "IOT-DPS")
//            intent.putExtra("idNFMItem", "$idNFM")
            startActivity(intent)
        }

        mNFMHomeAdapter?.clearList()
        val menuList = ArrayList<ContentMenu>()

        menuList.add(ContentMenu(1, "IOT-ACH", 0))
        menuList.add(ContentMenu(2, "IOT-BDG", 0))
        menuList.add(ContentMenu(3, "IOT-BGR", 0))
        menuList.add(ContentMenu(4, "IOT-BJM", 0))
        menuList.add(ContentMenu(5, "IOT-BRU", 0))
        menuList.add(ContentMenu(6, "IOT-BSD-1", 0))
        menuList.add(ContentMenu(7, "IOT-BSD-2", 0))
        menuList.add(ContentMenu(8, "IOT-BSD-3", 0))
        menuList.add(ContentMenu(9, "IOT-BSD-4", 0))
        menuList.add(ContentMenu(10, "IOT-BSD-5", 0))
        menuList.add(ContentMenu(11, "IOT-BTM", 0))
        menuList.add(ContentMenu(12, "IOT-CRB", 0))
        menuList.add(ContentMenu(13, "IOT-DPR", 0))
        menuList.add(ContentMenu(14, "IOT-JBR", 0))
        menuList.add(ContentMenu(15, "IOT-LMP", 0))
        menuList.add(ContentMenu(16, "IOT-MDN", 0))
        menuList.add(ContentMenu(17, "IOT-MDO", 0))
        menuList.add(ContentMenu(18, "IOT-MDU", 0))
        menuList.add(ContentMenu(19, "IOT-MKS", 0))
        menuList.add(ContentMenu(20, "IOT-MLG", 0))
        menuList.add(ContentMenu(21, "IOT-PDG", 0))
        menuList.add(ContentMenu(22, "IOT-PKU", 0))
        menuList.add(ContentMenu(23, "IOT-PLB", 0))
        menuList.add(ContentMenu(24, "IOT-SBY", 0))
        menuList.add(ContentMenu(25, "IOT-SDC", 0))
        menuList.add(ContentMenu(26, "IOT-SLO", 0))
        menuList.add(ContentMenu(27, "IOT-SMR", 0))
        menuList.add(ContentMenu(28, "IOT-YGY", 0))

        mNFMHomeAdapter?.updateList(menuList)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        clBtnAlarm.setOnClickListener {
            val intent = Intent(this, NFMAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
//        val param = recyclerview.layoutParams as ViewGroup.MarginLayoutParams
//        param.topMargin = 40
//        recyclerview.layoutParams = param
        recyclerview.layoutManager = LinearLayoutManager(this)

        mNFMHomeAdapter = NFMHomeAdapter(this)
        recyclerview.adapter = mNFMHomeAdapter
    }
}