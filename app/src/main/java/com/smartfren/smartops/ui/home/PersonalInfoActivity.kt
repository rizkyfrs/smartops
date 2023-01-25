package com.smartfren.smartops.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.buildImageBodyPart2
import com.smartfren.smartops.utils.showDialog
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import java.io.File
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PersonalInfoActivity : BaseActivity() {
    private var token: String? = null
    private var filepath: MultipartBody.Part? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val homepageViewModel: HomepageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        setupUI()
    }

    private fun setupUI() {
        token = MainApp.instance.sharedPreferences?.getString("token", "")
        getPersonalInfo(token)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        btnBack.setOnClickListener {
            onBackPressed()
        }

        ivProfile.setOnClickListener {
            ImagePicker.with(this) // User can only capture image from Camera
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        btnSaveEditProfile.setOnClickListener {
            postUpdatePersonalInfo(
                token, etEditProfileIDNum.text.toString(),
                etEditProfileName.text.toString(), etEditProfileEmail.text.toString(),
                etEditProfileTelp.text.toString(), filepath
            )
        }
    }

    private fun getPersonalInfo(token: String?) {
        homepageViewModel.personalInfo(token)
        homepageViewModel.personalInfoData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { personalInfoResponse ->
                            if (personalInfoResponse?.success == true) {
                                Glide.with(this)
                                    .load(personalInfoResponse.info?.get(0)?.pICPhoto)
                                    .placeholder(R.drawable.img_account)
                                    .priority(Priority.HIGH)
                                    .error(R.drawable.img_account)
                                    .into(ivProfile)

                                tvUsernames.text = personalInfoResponse.info?.get(0)?.pICName
                                etEditProfileIDNum.setText(personalInfoResponse.info?.get(0)?.iDNum)
                                etEditProfileName.setText(personalInfoResponse.info?.get(0)?.pICName)
                                etEditProfileEmail.setText(personalInfoResponse.info?.get(0)?.pICMail)
                                etEditProfileTelp.setText(personalInfoResponse.info?.get(0)?.pICNumber)
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

    private fun postUpdatePersonalInfo(
        token: String?,
        idNum: String?,
        name: String?,
        email: String?,
        telp: String?,
        pp: MultipartBody.Part?
    ) {
        homepageViewModel.postUpdatePersonalInfo(
            token, idNum,
            name, email,
            telp, pp
        )
        homepageViewModel.updatePersonalInfo.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { personalInfoResponse ->
                            if (personalInfoResponse?.success == true) {
                                showDialog(
                                    getString(R.string.update_success),
                                    "Profile has been update.",
                                    true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!
            val file: File? = ImagePicker.getFile(data)

            // Use Uri object instead of File to avoid storage permissions
            Glide.with(this)
                .asBitmap()
                .load(fileUri)
                .into(ivProfile)
            filepath = buildImageBodyPart2("pic_pp", "${UUID.randomUUID()}.png", file)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                etEditLatitude.setText(task.result!!.latitude.toString())
                etEditLongitude.setText(task.result!!.longitude.toString())
            } else {
                toast(this, "No location detected. Make sure location is enabled on the device.")
            }
        }
    }
}