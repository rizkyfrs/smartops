package com.smartfren.smartops.ui.testsignal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartfren.smartops.ui.workordermanagement.repository.WorkorderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TesSignalViewModel @Inject constructor(
    application: Application,
    private val repository: WorkorderRepository,
) : AndroidViewModel(application) {
    private val _rssi = MutableLiveData<Int?>()

    fun getRSSI(): LiveData<Int?>? {
        return _rssi
    }

    fun setRSSI(newSignal: Int?) {
        if (_rssi.value != newSignal) {
            _rssi.postValue(newSignal)
        }
    }
}