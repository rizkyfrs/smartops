package com.smartfren.smartops.ui.materialdeliveryrequest

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Contact
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.materialdeliveryrequest.adapter.MDRAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.show
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MDRActivity : BaseActivity() {
    private var counter: Int = 0
    private var token: String? = null
    private var levelID: String? = null
    private var region: String? = null
    private var mMaterialDeliverRequestAdapter: MDRAdapter? = null
    private val mMaterialDeliverRequestViewModel: MDRViewModel by viewModels()

    private val contacts = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        counter = 1
        mMaterialDeliverRequestAdapter?.clearList()
        getListMdr("$counter", "10")
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        levelID = MainApp.instance.sharedPreferences?.getString("levelID", "")
        region = MainApp.instance.sharedPreferences?.getString("regionName", "")

        clInfo.hide()
        indicatorP2.hide()
        indicatorP0.backgroundTintList = resources.getColorStateList(R.color.color_propose_to_closed)
        indicatorP1.backgroundTintList = resources.getColorStateList(R.color.color_closed)

        indicatorP0.text = getString(R.string.sent)
        indicatorP1.text = getString(R.string.closed)
        tvTitleAppBar.text = "Material Delivery Request"

//        if (levelID == "34") {
            btnAddTask.show()
//        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddTask.setOnClickListener {
            val intent = Intent(this, MDRAddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMaterialDeliverRequestAdapter = MDRAdapter(this)
//        val adapter = ContactsRecyclerAdapter(contacts)
        recyclerview.adapter = mMaterialDeliverRequestAdapter


    }

    private fun getListMdr(start: String?, recperpage: String?) {
        mMaterialDeliverRequestViewModel.listMDR(start, recperpage)
        mMaterialDeliverRequestViewModel.activityMDRData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { mdrResponse ->
                            if (mdrResponse.success == true) {
                                nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                                    if (v.getChildAt(v.childCount - 1) != null) {
                                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                                            //code to fetch more data for endless scrolling
                                            if (mdrResponse.totalRecordCount!! > mMaterialDeliverRequestAdapter?.itemCount!!) loadMore()
                                        }
                                    }
                                }

                                mdrResponse.tbStockMdr.let {
                                    mMaterialDeliverRequestAdapter?.updateList(
                                        it
                                    )
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

    private fun loadMore() {
        counter += 10
        getListMdr("$counter", "10")
    }
}