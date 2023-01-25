package com.smartfren.smartops.ui.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.scottyab.rootbeer.RootBeer
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.ui.login.LoginActivity
import com.smartfren.smartops.utils.toast
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SplashScreen : AppCompatActivity() {
    val delay = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootDetection()
        Handler().postDelayed(Runnable {
            MainApp.instance.sharedPreferences?.let {
                if (!it.getString("token", "").equals("")) {
                    val intent = Intent(this, HomepageActivity::class.java)
                    intent.putExtra("firstLogin", false)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }, delay)
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
}