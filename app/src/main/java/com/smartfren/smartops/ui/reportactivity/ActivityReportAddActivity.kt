package com.smartfren.smartops.ui.reportactivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.parseDateToBackend
import com.smartfren.smartops.utils.showDialogSiteID
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_form_add_task.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ActivityReportAddActivity : BaseActivity() {
    private var token: String? = null
    private var region: String? = null
    private var regionNames: String? = null
    private var elementAvailable: String? = null
    private var actType: String? = null
    private var actTypeName: String? = null
    private var listActTypeId: MutableList<String?> = mutableListOf()
    private var listActTypeName: MutableList<String?> = mutableListOf()
    private var listSiteID: MutableList<String?> = mutableListOf()
    private val mActivityReportViewModel: ActivityReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_add_task)
        setupUI()
    }

    private fun setupUI() {
        region = MainApp.instance.sharedPreferences?.getString("regionName", "")
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        etAddActivityRegion.isEnabled = false
        etAddActivityRegion.setText(region)

        when (region) {
            "JB" -> {
                regionNames = "1"
            }
            "NS" -> {
                regionNames = "2"
            }
            "SS" -> {
                regionNames = "3"
            }
            "WJ" -> {
                regionNames = "4"
            }
            "CJ" -> {
                regionNames = "5"
            }
            "EJ" -> {
                regionNames = "6"
            }
            "SK" -> {
                regionNames = "7"
            }
        }

        regionNames?.let { token?.let { it1 -> getSite(it1, it) } }

        getFormGroup(token)

        val cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        etAddActivityDate.setText(sdf.format(cal.time))

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etAddActivityDate.setText(sdf.format(cal.time))
        }

        tvAddSiteID.setOnClickListener {
            showDialogSiteID(listSiteID)
        }

        btnAddActivityDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.item_element))
        spElementAvail.adapter = adapter
        spElementAvail.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                elementAvailable = i.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        btnBack.setOnClickListener {
            onBackPressed()

        }

        btnSaveAddTask.setOnClickListener {
            if (tvAddSiteID.text!!.isEmpty()) {
                toast(this, "Please enter Site ID.")
            } else {
                postActivityReportForm(
                    token,
                    actType,
                    regionNames,
                    tvAddSiteID.text.toString(),
                    parseDateToBackend(etAddActivityDate.text.toString()),
                    elementAvailable,
                    etAddActivityNotes.text.toString()
                )
            }
        }
    }

    private fun getFormGroup(token: String?) {
        mActivityReportViewModel.listFormGroup(token)
        mActivityReportViewModel.formGroupData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { formGroupResponse ->
                            if (formGroupResponse.getReport()?.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        for (element in formGroupResponse.getReport()?.options!!) {
                                            element.formActivityGroup?.let { add(it) }
                                        }
                                    }
                                }
                                val adapter = ArrayAdapter(this@ActivityReportAddActivity, android.R.layout.simple_list_item_1, itemName)

                                spActivityGroup.adapter = adapter

                                spActivityGroup.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            adapterView: AdapterView<*>,
                                            view: View?,
                                            i: Int,
                                            l: Long,
                                        ) {
                                            postFormGroupType(
                                                MainApp.instance.sharedPreferences?.getString(
                                                    "token",
                                                    ""
                                                )!!, adapterView.selectedItem.toString()
                                            )
                                        }

                                        override fun onNothingSelected(adapterView: AdapterView<*>) {

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

    private fun postFormGroupType(token: String, form_group: String) {
        mActivityReportViewModel.listFormGroupType(token, form_group)
        mActivityReportViewModel.formGroupTypeData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { formGroupTypeResponse ->
                            if (formGroupTypeResponse.getReport()?.success == true) {
                                listActTypeId.clear()
                                listActTypeName.clear()
                                for (data in formGroupTypeResponse.getReport()?.options!!) {
                                    listActTypeId.add(data.activityID)
                                    listActTypeName.add(data.activityType)
                                }
                                val adapter = ArrayAdapter(
                                    this@ActivityReportAddActivity,
                                    android.R.layout.simple_list_item_1,
                                    listActTypeName
                                )

                                spActivityType.adapter = adapter

                                spActivityType.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            adapterView: AdapterView<*>,
                                            view: View?,
                                            i: Int,
                                            l: Long
                                        ) {
                                            actType = listActTypeId[i]
                                            actTypeName = adapterView.selectedItem.toString()
                                        }

                                        override fun onNothingSelected(adapterView: AdapterView<*>) {

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


    private fun getSite(token: String, site: String) {
        listSiteID.clear()
        mActivityReportViewModel.listSite(token, site)
        mActivityReportViewModel.siteData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { siteResponse ->
                            if (siteResponse.getReport()?.success == true) {
                                val itemName = object : java.util.ArrayList<String>() {
                                    init {
                                        for (element in siteResponse.getReport()?.sites!!) {
                                            element.siteId?.let { add(it) }
                                        }
                                    }
                                }
                                listSiteID.addAll(itemName)
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

    private fun postActivityReportForm(
        token: String?,
        act_theme_id: String?,
        act_region: String?,
        act_site_id: String?,
        act_date: String?,
        act_avail: String?,
        act_notes: String?,
    ) {
        mActivityReportViewModel.postForm(token, act_theme_id, act_region, act_site_id, act_date, act_avail, act_notes)
        mActivityReportViewModel.activityReportForm.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { activityReportFormResponse ->
                            if (activityReportFormResponse.getReport()?.success == true) {
                                showDialog(
                                    activityReportFormResponse.getReport()?.id,
                                    actTypeName,
                                    act_site_id,
                                    etAddActivityDate.text.toString()
                                )
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

    private fun showDialog(formNum: String?, nameActivity: String?, site: String?, date: String?) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ActivityChecklistActivity::class.java)
            intent.putExtra("formNum", formNum)
            intent.putExtra("nameActivity", nameActivity)
            intent.putExtra("siteID", site)
            intent.putExtra("dateActivity", date)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

}