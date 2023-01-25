package com.smartfren.smartops.ui.login

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.scottyab.rootbeer.RootBeer
import com.smartfren.smartops.BuildConfig
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.LoginRequest
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hide
import com.smartfren.smartops.utils.md5
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private var fcmToken: String? = null
    private var telephonyManager: TelephonyManager? = null
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        getLocation()
        rootDetection()
        getFirebaseToken()
    }

    private fun rootDetection() {
        val rootBeer = RootBeer(this)
        if (rootBeer.isRooted) {
            //we found indication of root
            finish()
        } else {
            //we didn't find indication of root
        }
    }

    private fun getFirebaseToken() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            fcmToken = token

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.e(ContentValues.TAG, msg)
        })
    }

    private fun setupUI() {
        tvVersionLogin.text = "v${BuildConfig.VERSION_NAME}"
        tvForgotPassword.hide()

        MainApp.instance.sharedPreferences?.let {
            if (!it.getString("token", "").equals("")) {
                val intent = Intent(this, HomepageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else if (it.getString("username", "") != null && it.getString("password", "") != null &&
                it.getString("username", "") != "" && it.getString("password", "") != ""
            ) {
                cbRemember.isChecked = true
                etUsername.setText(it.getString("username", ""))
                etPassword.setText(it.getString("password", ""))
            }
        }

        btnLogin.setOnClickListener {
            when {
                etUsername.text.toString().isEmpty() -> {
                    toast(this@LoginActivity, getString(R.string.please_enter_username))
                }
                etPassword.text.toString().isEmpty() -> {
                    toast(this@LoginActivity, getString(R.string.please_enter_password))
                }
                else -> {
                    if (cbRemember.isChecked) {
                        MainApp.instance.sharedPreferences?.edit()
                            ?.putString("username", etUsername.text.toString().trim())
                            ?.putString("password", etPassword.text.toString().trim())
                            ?.apply()
                    } else {
                        MainApp.instance.sharedPreferences?.edit()
                            ?.putString("username", "")
                            ?.putString("password", "")
                            ?.apply()
                    }
                    getLogin(etUsername.text.toString().trim(), etPassword.text.toString().trim())
//                    getLoginNew(etUsername.text.toString().trim(), etPassword.text.toString().trim())
                }
            }
        }
    }

    private fun deviceId() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                102
            )
            return
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@LoginActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            } else {
                ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if ((ContextCompat.checkSelfPermission(
                        this@LoginActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    deviceId()
                }
//                }
//                val imeiNumber = telephonyManager?.imei
//                MainApp.instance.sharedPreferences?.edit()?.putString("imei", imeiNumber)?.apply()
            } else {
                finish()
            }
            102 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            } else {
                finish()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getLogin(email: String?, password: String?) {
        val dataModelLoginBody = LoginRequest(email, md5(password.toString()))
        loginViewModel.loginUser(dataModelLoginBody, "${BuildConfig.VERSION_NAME}", fcmToken)
        loginViewModel.loginData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { loginResponse ->
                            if (loginResponse.report?.authorized == true) {
                                getLoginNew(etUsername.text.toString().trim(), etPassword.text.toString().trim())
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

    private fun getLoginNew(email: String?, password: String?) {
        val dataModelLoginBody = LoginRequest(email, password)
        loginViewModel.loginUserNew(dataModelLoginBody)
        loginViewModel.loginDataNew.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.let { loginResponse ->
                            if (loginResponse.jwt != null) {
                                val intent = Intent(applicationContext, HomepageActivity::class.java)
                                startActivity(intent)
                                finish()
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
}