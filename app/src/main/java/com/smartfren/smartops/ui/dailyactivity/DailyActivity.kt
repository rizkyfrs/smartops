package com.smartfren.smartops.ui.dailyactivity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.adapter.DailyActivityAdapter
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DailyActivity : BaseActivity() {
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels()
    private var dataList = ArrayList<PojoOfJsonArray>()
    private var mScheduleDailyActivityAdapter: DailyActivityAdapter? = null
    private val consolidatedList = mutableListOf<ListItem>()
    private var token: String? = null
    private var leveID: String? = null
    private var region: String? = null
    private var listPICId: MutableList<String?> = mutableListOf()
    private var listPICName: MutableList<String?> = mutableListOf()
    private var listDate: MutableList<String?> = mutableListOf()
    private var selected: Boolean? = null
    private var scheduleDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
        clearData()

        if (leveID == "34" || leveID == "40") {
            clInfo.hide()
            clSort.show()
            clSortPic.show()
            getListTeamSchedule(token)
            getListDateSchedule(token)
        } else {
            getListSchedule(token, "")
        }

        if (selected == true) {
            clSortPic.hide()
        }
    }

    private fun setupUI() {
        leveID = MainApp.instance.sharedPreferences?.getString("levelID", "")
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        region = MainApp.instance.sharedPreferences?.getString("regionName", "")
//        tvRegional.text = "Regional $region"
        scheduleDate = "Weekly"
        "${resources.getText(R.string.schedule_date)} : $scheduleDate".also { tvInfo.text = it }

        val cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

//        etDateExec.setText(sdf.format(cal.time))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                "${resources.getText(R.string.schedule_date)} : ${
                    parseDateFull(
                        sdf.format(cal.time),
                        "dd-MM-yyyy"
                    )
                }".also { tvInfo.text = it }
            }

        tvSelectDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        selected = false
        tvSortName.setTextColor(resources.getColor(R.color.white))
        tvSortName.setOnClickListener {
            if (selected == true) {
                tvSortName.backgroundTintList = resources.getColorStateList(R.color.color_pink)
                tvSortName.setTextColor(resources.getColor(R.color.white))
                tvSortDate.backgroundTintList = resources.getColorStateList(R.color.color_closed)
                tvSortDate.setTextColor(resources.getColor(R.color.color_grey))
                clDateSort.hide()
                clSortPic.show()
                spSortDate.setSelection(0)
                clearData()
                mScheduleDailyActivityAdapter?.notifyDataSetChanged()
            }
            selected = false
        }

        tvSortDate.setOnClickListener {
            if (selected == false) {
                tvSortDate.backgroundTintList = resources.getColorStateList(R.color.color_pink)
                tvSortDate.setTextColor(resources.getColor(R.color.white))
                tvSortName.setTextColor(resources.getColor(R.color.color_grey))
                tvSortName.backgroundTintList = resources.getColorStateList(R.color.color_closed)
                clDateSort.show()
                clSortPic.hide()
                spPICName.setSelection(0)
                clearData()
                mScheduleDailyActivityAdapter?.notifyDataSetChanged()
            }
            selected = true
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRV() {
        val recyclerview = recycleView
        recyclerview.layoutManager = LinearLayoutManager(this)

        mScheduleDailyActivityAdapter = DailyActivityAdapter(this, consolidatedList)
        recyclerview.adapter = mScheduleDailyActivityAdapter
    }

    private fun getListSchedule(token: String?, sort: String?) {
        clearData()
        dailyActivityViewModel.getListSchedule(token, sort)
        dailyActivityViewModel.listSchedule.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.getReport()?.success == true) {
                                for (data in scheduleResponse.getReport()?.sites!!) {
                                    dataList.add(
                                        PojoOfJsonArray(
                                            data.siteID,
                                            parseDateFromBackend(data.plannedDate.toString()),
                                            data.uName,
                                            data.actP0,
                                            data.actP1,
                                            data.actP2,
                                            data.taskPrtg
                                        )
                                    )
                                }

                                val groupedMapMap: Map<String?, List<PojoOfJsonArray>> =
                                    dataList.groupBy {
                                        it.date
                                    }

                                for (date: String? in groupedMapMap.keys) {
                                    val dateItem = DateItem()
                                    dateItem.date = date
                                    consolidatedList.add(dateItem)
                                    for (pojoOfJsonArray in groupedMapMap[date]!!) {
                                        val generalItem = GeneralItem(pojoOfJsonArray.name!!)
                                        generalItem.pojoOfJsonArray = pojoOfJsonArray
                                        consolidatedList.add(generalItem)
                                    }
                                }
                            }
                            mScheduleDailyActivityAdapter?.notifyDataSetChanged()
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

    private fun getListTeamSchedule(token: String?) {
        dailyActivityViewModel.getListTeamSchedule(token)
        dailyActivityViewModel.listTeamSchedule.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { scheduleResponse ->
                            if (scheduleResponse.getReport()?.success == true) {
                                listPICId.clear()
                                listPICName.clear()

                                listPICId.add("0")
                                listPICName.add(getString(R.string.select_FLM_PIC))

                                for (data in scheduleResponse.getReport()?.team!!) {
                                    listPICId.add(data.userID)
                                    listPICName.add(data.userFullName)
                                }

                                val adapter = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_list_item_1,
                                    listPICName
                                )
                                spPICName.adapter = adapter
                                spPICName.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            adapterView: AdapterView<*>,
                                            view: View?,
                                            i: Int,
                                            l: Long
                                        ) {
                                            if (i != 0) {
                                                getListSchedule(token, "/pic/${listPICId[i]}")
                                            }
                                        }

                                        override fun onNothingSelected(adapterView: AdapterView<*>) {

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

    private fun getListDateSchedule(token: String?) {
        dailyActivityViewModel.getListDateSchedule(token)
        dailyActivityViewModel.listDateSchedule.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { dateScheduleResponse ->
                            if (dateScheduleResponse.getReport()?.success == true) {
                                listDate.clear()

                                listDate.add(getString(R.string.select_date))

                                for (element in dateScheduleResponse.getReport()?.date!!) {
                                    listDate.add(element.calendarDate)
                                }

                                val adapter = ArrayAdapter(
                                    this, android.R.layout.simple_list_item_1, listDate
                                )
                                spSortDate.adapter = adapter

                                spSortDate.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            adapterView: AdapterView<*>,
                                            view: View?,
                                            i: Int,
                                            l: Long
                                        ) {
                                            if (i != 0) getListSchedule(
                                                token,
                                                "/date/${listDate[i]}"
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
        dataList.clear()
        consolidatedList.clear()
    }
}