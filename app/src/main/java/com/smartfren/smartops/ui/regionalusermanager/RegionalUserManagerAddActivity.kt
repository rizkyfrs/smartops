package com.smartfren.smartops.ui.regionalusermanager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.OptionModel
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.regional_user_manager_add.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegionalUserManagerAddActivity : BaseActivity() {
    private var token: String? = null
    private var region: String? = null
    private var regionName: String? = null
    private var type: String? = null
    private var userID: String? = null
    private var userIDRegis: String? = null
    private var userStatus: String? = null
    private var userStatuse: String? = null
    private var company: String? = null
    private var level: String? = null
    private var levele: String? = null
    private var userIDParent: String? = null
    private var levelItem: ArrayList<OptionModel>? = null
    private var userIDParentItem: ArrayList<OptionModel>? = null
    private val mRegionalUserManagerViewModel: RegionalUserManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.regional_user_manager_add)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        type = MainApp.instance.sharedPreferences?.getString("levelID", "")
        region = MainApp.instance.sharedPreferences?.getString("regionCode", "")
        regionName = MainApp.instance.sharedPreferences?.getString("regionName", "")
        userIDRegis = getUserIDJWT(MainApp.instance.sharedPreferences?.getString("tokenJWT", "").toString())
        levelItem = ArrayList()
        userIDParentItem = ArrayList()

        val cal = Calendar.getInstance()
        val myFormat = "ddMMyyyy" // mention the format you need
        val myFormatToBackend = "yyyy-MM-dd HH:mm:ss" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val sdfToBackend = SimpleDateFormat(myFormatToBackend, Locale.US)

        getListUser()
        getRegionalUserLevel()

        when {
            intent.getStringExtra("update") == "yes" -> {
                tvTitleAppBar.text = "Update Regional User Manager"
                btnSaveAddTask.text = "Update User"

                val listItemCompany = arrayOf("SMARTFREN")

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItemCompany)

                spAddRUMCompany.adapter = adapter

                spAddRUMCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
//                        company = adapterView.selectedItem.toString()
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {

                    }
                }

                userID = intent.getStringExtra("idUser")
                levele = intent.getStringExtra("uLevel")
                userStatuse = intent.getStringExtra("uActivation")
                etAddRUMUsername.setText(intent.getStringExtra("username"))
//                etAddRUMPassword.setText(intent.getStringExtra("password"))
                etAddRUMName.setText(intent.getStringExtra("uName"))
                etAddRUMPhone.setText(intent.getStringExtra("uPhone"))
                etAddRUMEmail.setText(intent.getStringExtra("uMail"))
//                intent.getStringExtra("idParent").let {
//                    etAddDRUMDirectSuper.setText(it)
//                }

                btnSaveAddTask.setOnClickListener {
                    when {
                        etAddDRUMDirectSuper.text?.isEmpty()!! -> {
                            toast(this, "Please enter Direct Superior.")
                        }
                        etAddRUMUsername.text?.isEmpty()!! -> {
                            toast(this, "Please enter Username.")
                        }
                        etAddRUMPassword.text?.isEmpty()!! -> {
                            toast(this, "Please enter Password.")
                        }
                        etAddRUMName.text?.isEmpty()!! -> {
                            toast(this, "Please enter Name.")
                        }
                        etAddRUMPhone.text?.isEmpty()!! -> {
                            toast(this, "Please enter Phone.")
                        }
                        etAddRUMEmail.text?.isEmpty()!! -> {
                            toast(this, "Please enter Email.")
                        }
                        else -> {
                            postEditRegionalUserManager(
                                userID, userID, "1", region, userIDParent, etAddRUMNIK.text.toString(),
                                etAddRUMUsername.text.toString(), etAddRUMPassword.text.toString(),
                                etAddRUMName.text.toString(), etAddRUMPhone.text.toString(), level, etAddRUMEmail.text.toString(), userStatus
                            )
                            Log.e(
                                "tess Response", "" +
                                        userID + "\n" + region + "\n" + etAddRUMUsername.text.toString() + "\n" + etAddRUMPassword.text.toString() + "\n" +
                                        etAddRUMName.text.toString() + "\n" + etAddRUMPhone.text.toString() + "\n" + level + "\n" + etAddRUMEmail.text.toString() + "\n" + userStatus + "\n" +
                                        company
                            )
                        }
                    }
                }
            }
            else -> {
                tvTitleAppBar.text = "Add Regional User Manager"
                clAddRUMCompany.hide()
                userID = region + sdf.format(cal.time)

                btnSaveAddTask.setOnClickListener {
                    when {
                        etAddDRUMDirectSuper.text?.isEmpty()!! -> {
                            toast(this, "Please enter Direct Superior.")
                        }
                        etAddRUMUsername.text?.isEmpty()!! -> {
                            toast(this, "Please enter Username.")
                        }
                        etAddRUMPassword.text?.isEmpty()!! -> {
                            toast(this, "Please enter Password.")
                        }
                        etAddRUMName.text?.isEmpty()!! -> {
                            toast(this, "Please enter Name.")
                        }
                        etAddRUMPhone.text?.isEmpty()!! -> {
                            toast(this, "Please enter Phone.")
                        }
                        etAddRUMEmail.text?.isEmpty()!! -> {
                            toast(this, "Please enter Email.")
                        }
                        else -> {
                            postAddRegionalUserManager(
                                userID,
                                region,
                                userIDParent,
                                etAddRUMNIK.text.toString(),
                                etAddRUMUsername.text.toString(),
                                etAddRUMPassword.text.toString(),
                                etAddRUMName.text.toString(),
                                etAddRUMPhone.text.toString(),
                                level,
                                etAddRUMEmail.text.toString(),
                                userStatus,
                                userIDRegis,
                                sdfToBackend.format(cal.time)
                            )
                            Log.e(
                                "tess Response", "" +
                                        userID + "\n" + region + "\n" + userIDParent + "\n" + etAddRUMPassword.text.toString() + "\n" +
                                        etAddRUMName.text.toString() + "\n" + etAddRUMPhone.text.toString() + "\n" + level + "\n" + etAddRUMEmail.text.toString() + "\n" + userStatus + "\n" +
                                        userIDRegis + "\n" + sdfToBackend.format(cal.time)
                            )
                        }
                    }
                }
            }
        }

        etAddRUMRegion.isEnabled = false
        etAddRUMRegion.setText(regionName)

        val listItemUserStatus = arrayOf("ACTIVE", "INACTIVE")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItemUserStatus)

        spAddRUMUserStatus.adapter = adapter

        val spinnerPosition = adapter.getPosition(userStatuse)
        spAddRUMUserStatus.setSelection(spinnerPosition)

        spAddRUMUserStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                userStatus = if (i == 0) {
                    "y"
                } else {
                    "n"
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getRegionalUserLevel() {
        mRegionalUserManagerViewModel.setListRegionalUserManagerLevel()
        mRegionalUserManagerViewModel.listRegionalUserManagerLevel.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { regionalUserManagerResponse ->
                            if (regionalUserManagerResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (regionalUserManagerResponse.cvApirefReguserlevel != null)
                                            for (element in regionalUserManagerResponse.cvApirefReguserlevel) {
                                                element.userlevelname?.let { add(it) }
                                                val datas = OptionModel(
                                                    element.userlevelid,
                                                    element.userlevelname
                                                )
                                                levelItem?.add(datas)
                                            }
                                    }
                                }

                                val adapterGroupLv1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)

                                spAddRUMLevel.adapter = adapterGroupLv1

                                intent.getStringExtra("uLevel").let {
                                    val spinnerPosition = adapterGroupLv1.getPosition(it)
                                    spAddRUMLevel.setSelection(spinnerPosition)
                                }

                                spAddRUMLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                                        if (levelItem != null) {
                                            val optionDatas: OptionModel = levelItem!![i]
                                            level = optionDatas.id
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

    private fun postAddRegionalUserManager(
        id_user: String?,
        u_region: String?,
        id_parrent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?,
        u_registrars: String?,
        u_registrationdate: String?
    ) {
        mRegionalUserManagerViewModel.setAddRegionalUserManager(
            id_user, u_region, id_parrent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation, u_registrars, u_registrationdate
        )
        mRegionalUserManagerViewModel.addRegionalUserManager.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { updateTTWORCAResponse ->
                            if (updateTTWORCAResponse.success == true) {
                                showDialog("Add Success.", "User has been add.", true)
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

    private fun postEditRegionalUserManager(
        key: String?,
        id_user: String?,
        u_company: String?,
        u_region: String?,
        id_parrent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?
    ) {
        mRegionalUserManagerViewModel.setEditRegionalUserManager(
            key, id_user, u_company, u_region, id_parrent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation
        )
        mRegionalUserManagerViewModel.editRegionalUserManager.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { updateTTWORCAResponse ->
                            if (updateTTWORCAResponse.success == true) {
                                showDialog("Update Success.", "User has been update.", true)
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

    private fun getListUser() {
        mRegionalUserManagerViewModel.setListRegionalUserManager("1", "", "", "")
        mRegionalUserManagerViewModel.listRegionalUserManager.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { userResponse ->
                            if (userResponse.success == true) {
                                val itemName = object : ArrayList<String>() {
                                    init {
                                        if (userResponse.cvRegusermgm != null)
                                            for (element in userResponse.cvRegusermgm) {
                                                element.uName?.let { add(it) }
                                                val datas = OptionModel(
                                                    element.idUser,
                                                    element.uName
                                                )
                                                userIDParentItem?.add(datas)
                                            }
                                    }
                                }

                                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemName)
                                etAddDRUMDirectSuper.setAdapter(adapter)

//                                intent.getStringExtra("idParent").let {
//                                    val spinnerPosition = adapter.getPosition(it)
//                                    etAddDRUMDirectSuper.setSelection(spinnerPosition)
//                                }

                                etAddDRUMDirectSuper.onItemClickListener = object : AdapterView.OnItemClickListener {
                                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                        if (userIDParentItem != null) {
                                            val optionDatas: OptionModel = userIDParentItem!![p2]
                                            userIDParent = optionDatas.id
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
}