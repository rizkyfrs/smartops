package com.smartfren.smartops.ui.materialdeliveryrequest

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Contact
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.databinding.MdrFormAddBinding
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.materialdeliveryrequest.adapter.ContactsRecyclerAdapter
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.setCurrentTime
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.stolen_report_add.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MDRAddTaskActivity : BaseActivity() {
    private var token: String? = null
    private val contacts = mutableListOf<Contact>()
    private lateinit var binding: MdrFormAddBinding
    private var vendorItem: ArrayList<OptionModel>? = null
    private var vendorId: String? = null
    private var dopRegionItem: ArrayList<OptionModel>? = null
    private var dopRegionId: String? = null
    private var dopItem: ArrayList<OptionModel>? = null
    private var dopId: String? = null
    private var mMaterialDeliverRequestAdapter: ContactsRecyclerAdapter? = null
    private val mMaterialDeliverRequestViewModel: MDRViewModel by viewModels()
    private val mDailyActivityViewModel: DailyActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.mdr_form_add)
        binding = MdrFormAddBinding.inflate(layoutInflater)
        binding.mdrViewModel = mMaterialDeliverRequestViewModel
        val view = binding.root
        setContentView(view)
        setupUI()
        setupRV()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        vendorItem = ArrayList()
        dopRegionItem = ArrayList()
        dopItem = ArrayList()

        binding.etAddRegion.isEnabled = false
        binding.etAddRegion.setText(MainApp.instance.sharedPreferences?.getString("regionName", ""))

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddItemMDR.setOnClickListener {
//            val model = Contact("Marias", 31)
//            mMaterialDeliverRequestAdapter?.addContact(model)
            showDialogAddMDRItem()
        }

        val cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        binding.etAddMdrDateExec.setText(sdf.format(cal.time))

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.etAddMdrDateExec.setText(sdf.format(cal.time))
        }

        binding.btnAddMdrDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvAddMdrWoSiteId.setOnClickListener {
            showDialogSiteID(mDailyActivityViewModel.getSiteInfo())
        }

        binding.btnSaveAddTask.setOnClickListener {
            for (c in contacts) {
                Log.e("cons", "" + c.product_number)
            }
            postStolenReportAdd(
                mMaterialDeliverRequestViewModel.setStatusMDR(), "tes mdr", "1", "1", "3", "TESTSITE_02",
                "tes", "tesdom", "tes flag", binding.etAddMdrDateExec.text.toString(), "90004115",
                setCurrentTime()
            )
        }
        getVendor("", "")
        getListDOP()
    }

    private fun setupRV() {
        val recyclerview = binding.rvForm
        recyclerview.layoutManager = LinearLayoutManager(this)

        mMaterialDeliverRequestAdapter = ContactsRecyclerAdapter(this, contacts)
        recyclerview.adapter = mMaterialDeliverRequestAdapter
    }

    private fun getVendor(start: String?, recperpage: String?) {
        mMaterialDeliverRequestViewModel.setListVendor(start, recperpage)
        mMaterialDeliverRequestViewModel.listVendorData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { vendorResponse ->
                            if (vendorResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (vendorResponse.tbVendor != null)
                                            for (element in vendorResponse.tbVendor!!) {
                                                element?.vendorName?.let { add(it) }
                                                val datas = OptionModel(
                                                    element?.idVendor,
                                                    element?.vendorName
                                                )
                                                vendorItem?.add(datas)
                                            }
                                    }
                                }

                                val adapterGroupLv1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                binding.spMdrVendor.adapter = adapterGroupLv1

//                                intent.getStringExtra("uLevel").let {
//                                    val spinnerPosition = adapterGroupLv1.getPosition(it)
//                                    binding.spMdrVendor.setSelection(spinnerPosition)
//                                }

                                binding.spMdrVendor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        if (vendorItem != null) {
                                            val optionDatas: OptionModel = vendorItem!![i]
                                            vendorId = optionDatas.id
                                            Log.e("tes vendor", "${vendorId}")
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

    private fun getListDOPRegion(region: String?) {
        mMaterialDeliverRequestViewModel.setListDOPRegion(region)
        mMaterialDeliverRequestViewModel.listDOPRegionData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { dopRegionResponse ->
                            if (dopRegionResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (dopRegionResponse.tbDop != null)
                                            for (element in dopRegionResponse.tbDop!!) {
                                                element?.dopCity?.let { add(it) }
                                                val datas = OptionModel(
                                                    element?.dopNum,
                                                    element?.dopCity
                                                )
                                                vendorItem?.add(datas)
                                            }
                                    }
                                }

                                val adapterGroupLv1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                binding.spMdrDOPRegion.adapter = adapterGroupLv1

//                                intent.getStringExtra("uLevel").let {
//                                    val spinnerPosition = adapterGroupLv1.getPosition(it)
//                                    binding.spMdrVendor.setSelection(spinnerPosition)
//                                }

                                binding.spMdrDOPRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        if (vendorItem != null) {
                                            val optionDatas: OptionModel = vendorItem!![i]
                                            dopRegionId = optionDatas.id
                                            Log.e("tes vendor", "${dopRegionId}")
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

    private fun getListDOP() {
        mMaterialDeliverRequestViewModel.setListDOP()
        mMaterialDeliverRequestViewModel.listDOPData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { dopResponse ->
                            if (dopResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (dopResponse.tbDop != null)
                                            for (element in dopResponse.tbDop!!) {
                                                element?.dopCity?.let { add(it) }
                                                val datas = OptionModel(
                                                    element?.dopNum,
                                                    element?.dopCity
                                                )
                                                dopItem?.add(datas)
                                            }
                                    }
                                }

                                val adapterDOP = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                binding.spMdrDOP.adapter = adapterDOP

//                                intent.getStringExtra("uLevel").let {
//                                    val spinnerPosition = adapterGroupLv1.getPosition(it)
//                                    binding.spMdrVendor.setSelection(spinnerPosition)
//                                }

                                binding.spMdrDOP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        if (dopItem != null) {
                                            val optionDatas: OptionModel = dopItem!![i]
                                            dopId = optionDatas.id
                                            Log.e("tes vendor", "$dopId")
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

    private fun postStolenReportAdd(
        fsmdr_status: String?,
        fsmdr_id: String?,
        fsmdr_vendor: String?,
        fsmdr_region: String?,
        fsmdr_dop: String?,
        fsmdr_site_id: String?,
        fsmdr_wo_id: String?,
        fsmdr_wo_dom: String?,
        fsmdr_wo_flag: String?,
        fsmdr_date: String?,
        fsmdr_registrars: String?,
        fsmdr_registerdate: String?
    ) {
        mMaterialDeliverRequestViewModel.setPostAddStockMdr(
            fsmdr_status,
            fsmdr_id,
            fsmdr_vendor,
            fsmdr_region,
            fsmdr_dop,
            fsmdr_site_id,
            fsmdr_wo_id,
            fsmdr_wo_dom,
            fsmdr_wo_flag,
            fsmdr_date,
            fsmdr_registrars,
            fsmdr_registerdate
        )
        mMaterialDeliverRequestViewModel.addStockMdr.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { stolenResponse ->
                            if (stolenResponse.success == true) {

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

    fun showDialogAddMDRItem() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up_mdr_item_add)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val btnAddMDRItem = dialog.findViewById(R.id.btnAddMDRItem) as Button
        val btnClose = dialog.findViewById(R.id.btnCloseAddMDRItem) as ImageView
        val etAddMDRProductNumber = dialog.findViewById(R.id.etAddMDRProductNumber) as EditText
        val etFaultModuleSN = dialog.findViewById(R.id.etAddMDRFaultModuleSN) as AppCompatEditText
        val etAddMDRFaultInfo = dialog.findViewById(R.id.etAddMDRFaultInfo) as EditText
        val etADdMDRVendor = dialog.findViewById(R.id.etADdMDRVendor) as EditText
        val etADdMDRNE = dialog.findViewById(R.id.etADdMDRNE) as EditText
        val etADdMDREquipmentName = dialog.findViewById(R.id.etADdMDREquipmentName) as EditText
        val etADdMDRModuleName = dialog.findViewById(R.id.etADdMDRModuleName) as EditText

        dialog.show()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnAddMDRItem.setOnClickListener {
            val model = Contact(
                etAddMDRProductNumber.text.toString(), etFaultModuleSN.text.toString(), etAddMDRFaultInfo.text.toString(),
                etADdMDRVendor.text.toString(), etADdMDRNE.text.toString(), etADdMDREquipmentName.text.toString(), etADdMDRModuleName.text.toString(),
                ""
            )
            mMaterialDeliverRequestAdapter?.addContact(model)

            dialog.dismiss()
        }
    }

    private fun showDialogSiteID(listSiteID: MutableList<String>) {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up_spinner_search)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val editText = dialog.findViewById(R.id.etSiteID) as EditText
        val listView = dialog.findViewById(R.id.lvSiteID) as ListView
        val btnClose = dialog.findViewById(R.id.btnClose) as ImageView

        dialog.show()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listSiteID)

        listView.adapter = adapter
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            binding.tvAddMdrWoSiteId.text = adapter.getItem(position).toString()
            dialog.dismiss()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

}