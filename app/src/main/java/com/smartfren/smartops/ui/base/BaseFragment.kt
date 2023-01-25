package com.smartfren.smartops.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.utils.LoadingDialog

open class BaseFragment : Fragment() {
    lateinit var builder: AlertDialog.Builder
    private var pd: LoadingDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        builder = AlertDialog.Builder(requireContext())
        pd = LoadingDialog(requireContext())
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

    fun showDialog(show: Boolean) {
        if (null != pd)
            if (show)
                pd!!.show()
            else
                pd!!.dismiss()

        pd?.setCancelable(true)
    }

    fun showMessage(message: String) {
        builder.setMessage(message)
            .setTitle(MainApp.instance.getString(R.string.app_name))
            .setPositiveButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }

    override fun onDestroy() {
        if(null!=pd) pd?.let { if(it.isShowing) it.dismiss() }
        super.onDestroy()
    }


}