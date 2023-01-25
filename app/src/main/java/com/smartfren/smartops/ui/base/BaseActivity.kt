package com.smartfren.smartops.ui.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mapbox.mapboxsdk.Mapbox
import com.smartfren.smartops.BuildConfig
import com.smartfren.smartops.R
import com.smartfren.smartops.utils.LoadingDialog

abstract class BaseActivity : AppCompatActivity() {
    private var pd: LoadingDialog? = null
    lateinit var fm: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        pd = LoadingDialog(this)
        fm = supportFragmentManager
        changeStatusBarWhite()
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    }

    fun loading(loaded: Boolean) {
        if (loaded) showProgress() else hideProgress()
    }

    fun showProgress() {
        pd?.show()
    }

    fun hideProgress() {
        pd?.dismiss()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    protected fun loadFragment(fragment: Fragment, baseframe: View) {
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.commit()
    }

    fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    open fun changeStatusBarWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_pink)
        }
    }
}