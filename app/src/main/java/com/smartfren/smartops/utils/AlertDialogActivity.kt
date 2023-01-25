package com.smartfren.smartops.utils

import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.base.BaseActivity
import com.smartfren.smartops.ui.nfm.NFMHomeActivity
import com.smartfren.smartops.ui.workordermanagement.WorkorderActivity

class AlertDialogActivity : BaseActivity() {
    private var woID: String? = null
    private var incwoID: String? = null
    private var status: String? = null
    private var idNe: String? = null
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_dialog_activity)
        lockScreen()
    }

    private fun lockScreen() {

        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.inKeyguardRestrictedInputMode()) {
            if (!intent.getStringExtra("wo").isNullOrEmpty()) {
                woID = intent.getStringExtra("woID")
                incwoID = intent.getStringExtra("incwoID")
                if (intent.getStringExtra("wo") == "ALARM CLEARED") {
//                val intent = Intent(this, WorkorderActivity::class.java)
//                intent.putExtra("woID", woID)
//                intent.putExtra("incwoID", incwoID)
//                startActivity(intent)
                } else {
                    handler.postDelayed({ // Do something after 1s = 1000ms
                        showDialogNotification(intent.getStringExtra("wo"), intent.getStringExtra("woID"), "wo")
                    }, 1000)
                }
            } else if (!intent.getStringExtra("location").isNullOrEmpty()) {
                status = intent.getStringExtra("location")
                idNe = intent.getStringExtra("content")
                handler.postDelayed({ // Do something after 1s = 1000ms
                    showDialogNotification(
                        intent.getStringExtra("status"), intent.getStringExtra("content") + "\n" +
                                intent.getStringExtra("raised"), "nfm"
                    )
                }, 1000)
            }
        }

        /// FLAG_DISMISS_KEYGUARD
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
//            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            keyguardManager.requestDismissKeyguard(this, null)
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
//        }
//
///// FLAG_SHOW_WHEN_LOCKED
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            setShowWhenLocked(true)
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
//        }
//
///// FLAG_SHOW_WHEN_LOCKED | FLAG_DISMISS_KEYGUARD
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            keyguardManager.requestDismissKeyguard(this, null)
//            setShowWhenLocked(true)
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
//                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
//        }
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