package com.smartfren.smartops.ui.regionalusermanager

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.sitemanagementinfo.adapter.SiteManagementInfoDetailAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.daily_activity_detail_new.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegionalUserManagerDetailActivity : BaseActivity() {
    private var actionOpt: ArrayList<OptionModel>? = null
    private val mRegionalUserManagerViewModel: RegionalUserManagerViewModel by viewModels()
    private var mAdapter: SiteManagementInfoDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_activity_detail_new)
        mapView.onCreate(savedInstanceState)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        clearData()

        intent.getStringExtra("regUserMgmID")?.let {
            getRegionalUserManagerDetail(it)
        }
    }

    private fun setupUI() {
        tvTitleAppBar.text = getString(R.string.regional_user_manager_detail)
        actionOpt = ArrayList()
        clInfo.hide()
        mapView.hide()

        val param = recycleView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, 0, 0)
        recycleView.layoutParams = param

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = SiteManagementInfoDetailAdapter(this)
        recyclerview.adapter = mAdapter
        mAdapter?.clearList()
    }

    private fun getRegionalUserManagerDetail(key: String) {
        mRegionalUserManagerViewModel.setViewRegionalUserManager(key)
        mRegionalUserManagerViewModel.viewRegionalUserManager.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { regionalUserManagerResponse ->
                            if (regionalUserManagerResponse.success == true) {
                                tvTitleActivityRegName.hide()
                                tvRegName.hide()
                                tvTitleActivityPriority.hide()
                                tvPriority.hide()
                                tvTitleActualDate.hide()
                                tvActualDate.hide()
                                tvTitlePhotoAfter.invisible()
                                clPhotoAfter.invisible()
                                tvTitleRCALv2.hide()
                                tvRCALv2.hide()
                                tvTitleRCAAction.hide()
                                tvRCAAction.hide()
                                tvTitleActionStatus.hide()
                                tvActionStatus.hide()

                                tvTitleActivityNumber.text = "ID User"
                                tvActivityNumber.text = regionalUserManagerResponse.cvRegusermgm?.idUser
                                tvTitlePlannedDate.text = "Company"
                                tvPlannedDate.text = regionalUserManagerResponse.cvRegusermgm?.uCompany
                                tvTitleDomLv1.text = "Region"
                                tvDomainLev1.text = regionalUserManagerResponse.cvRegusermgm?.uRegion
                                tvTitleDomLv2.text = "ID Parent"
                                tvDomainLev2.text = regionalUserManagerResponse.cvRegusermgm?.idParrent
                                tvTitleNEID.text = "NIK"
                                tvNEID.text = regionalUserManagerResponse.cvRegusermgm?.uNik
                                tvTitleVendor.text = "Username"
                                tvVendor.text = regionalUserManagerResponse.cvRegusermgm?.username
                                tvTitleWOID.text = "Name"
                                tvWOID.text = regionalUserManagerResponse.cvRegusermgm?.uName
                                tvTitleActionNeed.text = "Phone"
                                tvActionNeed.text = regionalUserManagerResponse.cvRegusermgm?.uPhone
                                tvTitlePhotoBefore.text = "Picture"
                                if (regionalUserManagerResponse.cvRegusermgm?.uPicture != null) {
                                    Glide.with(this)
                                        .load("https://oms.smartfren.com/doc/pp/" + regionalUserManagerResponse.cvRegusermgm?.uPicture)
                                        .placeholder(R.drawable.img_account)
                                        .priority(Priority.HIGH)
                                        .error(R.drawable.img_account)
                                        .into(ivPhotoBefore)
                                    ivPhotoBeforeDefault.hide()
                                } else {
                                    ivPhotoBeforeDefault.show()
                                }

                                tvTitleRCALv1.text = "Level"
                                tvRCALv1.text = regionalUserManagerResponse.cvRegusermgm?.uLevel
                                tvTitleRCALv3.text = "Mail"
                                tvRCALv3.text = regionalUserManagerResponse.cvRegusermgm?.uMail
                                tvTitleActionDetail.text = "Activation"
                                tvActionDetail.text = regionalUserManagerResponse.cvRegusermgm?.uActivation
                                tvTitleActionType.text = "Registration Date"
                                tvActionType.text = regionalUserManagerResponse.cvRegusermgm?.uRegistrationdate


                                btnUpdateTask.text = getString(R.string.upda_regional_user)
                                btnUpdateTask.setOnClickListener {
                                    val intent = Intent(applicationContext, RegionalUserManagerAddActivity::class.java)
                                    intent.putExtra("update", "yes")
                                    intent.putExtra("idUser", replacePoint(regionalUserManagerResponse.cvRegusermgm?.idUser))
                                    intent.putExtra("idParent", regionalUserManagerResponse.cvRegusermgm?.idParrent)
                                    intent.putExtra("username", regionalUserManagerResponse.cvRegusermgm?.username)
                                    intent.putExtra("password", regionalUserManagerResponse.cvRegusermgm?.password)
                                    intent.putExtra("uName", regionalUserManagerResponse.cvRegusermgm?.uName)
                                    intent.putExtra("uPhone", regionalUserManagerResponse.cvRegusermgm?.uPhone)
                                    intent.putExtra("uLevel", regionalUserManagerResponse.cvRegusermgm?.uLevel)
                                    intent.putExtra("uMail", regionalUserManagerResponse.cvRegusermgm?.uMail)
                                    intent.putExtra("uActivation", regionalUserManagerResponse.cvRegusermgm?.uActivation)
                                    startActivity(intent)
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

    private fun clearData() {
        actionOpt?.clear()
        mAdapter?.clearList()
    }
}