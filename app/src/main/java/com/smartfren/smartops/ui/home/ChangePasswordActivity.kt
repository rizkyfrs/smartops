package com.smartfren.smartops.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.change_password_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {
    private var token: String? = null
    private val homepageViewModel: HomepageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_activity)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSaveEditProfile.setOnClickListener {
            if (etPasswordNewConf.text.toString() != etPasswordNew.text.toString()) {
                toast(this@ChangePasswordActivity, "Password not same!")
            } else {
                postChangePassword(token, etPasswordNew.text.toString())
            }
        }

//        etPasswordNewConf.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun afterTextChanged(editable: Editable) {
//                val passwrd: String = etPasswordNew.text.toString()
//                if (editable.isNotEmpty() && passwrd.isNotEmpty()) {
//                    if (!etPasswordNewConf.equals(passwrd)) {
//                        toast(this@ChangePasswordActivity, "Password not same!")
//                    }
//                }
//            }
//        })
    }

    private fun postChangePassword(
        token: String?,
        new_password: String?
    ) {
        homepageViewModel.changePassword(
            token, new_password
        )
        homepageViewModel.changePasswordData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { personalInfoResponse ->
                            if (personalInfoResponse?.success == true) {
                                showDialog("", "", true)
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