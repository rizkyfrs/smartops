package com.smartfren.smartops.ui.home

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.scottyab.rootbeer.RootBeer
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.dailyactivity.DailyActivityViewModel
import com.smartfren.smartops.ui.login.LoginActivity
import com.smartfren.smartops.ui.nfm.NFMHomeActivity
import com.smartfren.smartops.ui.workordermanagement.WorkorderActivity
import com.smartfren.smartops.utils.BackgroundSoundService
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.baseframe.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomepageActivity : BaseActivity() {
    private var token: String? = null
    private var regionNames: String? = null
    private var levelUserID: String? = null
    private var woID: String? = null
    private var incwoID: String? = null
    private var status: String? = null
    private var idNe: String? = null
    private var raised: String? = null
    private val homepageViewModel: HomepageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.baseframe)
        rootDetection()

        token = MainApp.instance.sharedPreferences?.getString("token", "")
        levelUserID = MainApp.instance.sharedPreferences?.getString("levelUserID", "")
        Log.e("leveluserid", "$levelUserID")

        token?.let { it1 -> getPersonalInfo(it1) }
//        getPersonalInfo()

        val androidId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        Log.e("------>", "" + androidId)

//        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.e(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.e(TAG, msg)
//        })
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

    private fun getPersonalInfo(token: String?) {
//        if (levelUserID == "35" || levelUserID == "34" || levelUserID == "32" || levelUserID == "40" || levelUserID == "21") {
//            val dashboardFlm = DashboardFLM.newInstance()
//            loadFragment(dashboardFlm, baseframe)
//        } else {
//            val dashboard = DashboardNew.newInstance()
//            loadFragment(dashboard, baseframe)
//        }
        homepageViewModel.personalInfo(token)
        homepageViewModel.personalInfoData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        loading(false)
                        response.data?.getReport().let { personalInfoResponse ->
                            if (personalInfoResponse?.success == true) {
                                if (personalInfoResponse.info?.get(0)?.levelID == "35" ||
                                    personalInfoResponse.info?.get(0)?.levelID == "34" ||
                                    personalInfoResponse.info?.get(0)?.levelID == "32" ||
                                    personalInfoResponse.info?.get(0)?.levelID == "40" ||
                                    personalInfoResponse.info?.get(0)?.levelID == "21"
                                ) {
                                    val dashboardFlm = DashboardFLM.newInstance()
                                    loadFragment(dashboardFlm, baseframe)
                                } else {
                                    val dashboard = DashboardNew.newInstance()
                                    loadFragment(dashboard, baseframe)
                                }

                                if (!intent.getStringExtra("wo").isNullOrEmpty()) {
                                    woID = intent.getStringExtra("woID")
                                    incwoID = intent.getStringExtra("incwoID")
                                    if (intent.getStringExtra("wo") == "ALARM CLEARED") {
                                        val intent = Intent(this, WorkorderActivity::class.java)
                                        intent.putExtra("woID", woID)
                                        intent.putExtra("incwoID", incwoID)
                                        startActivity(intent)
                                    } else {
                                        showDialogNotification(intent.getStringExtra("wo"), woID, "wo")
                                    }
                                } else if (!intent.getStringExtra("location").isNullOrEmpty()) {
                                    status = intent.getStringExtra("status")
                                    idNe = intent.getStringExtra("content")
                                    raised = intent.getStringExtra("raised")
                                    showDialogNotification(status, idNe + "\n" + raised, "nfm")
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
//                        loading(true)
                    }
                }
            }
        }
    }

    private fun showDialogNotification(titles: String?, descs: String?, type: String?) {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val btnOk = dialog.findViewById(R.id.btnOk) as Button
        val imgView = dialog.findViewById(R.id.imgSuccess) as ImageView
        val title = dialog.findViewById(R.id.tvTitleSuccess) as TextView
        val desc = dialog.findViewById(R.id.tvSuccess) as TextView

        imgView.setImageDrawable(resources.getDrawable(R.drawable.ic_error))
        imgView.imageTintList = resources.getColorStateList(R.color.color_pink)

        if (titles != null && titles != "") title.text = titles
        if (descs != null && descs != "") desc.text = descs

        btnOk.setOnClickListener {
            this.stopService(Intent(this, BackgroundSoundService::class.java))
            dialog.dismiss()
            if (type == "wo") {
                val intent = Intent(this, WorkorderActivity::class.java)
                intent.putExtra("woID", woID)
                intent.putExtra("incwoID", incwoID)
                startActivity(intent)
            } else if (type == "nfm") {
                val intent = Intent(this, NFMHomeActivity::class.java)
                intent.putExtra("statusNFM", status)
//                intent.putExtra("content", idNe)
                startActivity(intent)
            }

            MainApp.instance.sharedPreferences?.edit()
                ?.putString("titleFCM", "")
                ?.putString("bodyFCM", "")
                ?.putString("woIDFCM", "")
                ?.putString("siteIDFCM", "")
                ?.putString("incwoIDFCM", "")
                ?.putString("statusFCM", "")
                ?.putString("raisedFCM", "")
                ?.putString("clearedFCM", "")
                ?.putString("locationFCM", "")
                ?.putString("contentFCM", "")
                ?.putString("ne_idFCM", "")
                ?.apply()
        }

        dialog.show()
    }
}