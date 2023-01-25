package com.smartfren.smartops.ui.womttr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.womttr.repository.WOMTTRRepository
import com.smartfren.smartops.util.Event
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hasInternetConnection
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WOMTTRViewModel @Inject constructor(
    application: Application,
    private val repository: WOMTTRRepository,
) : AndroidViewModel(application) {
    private val _womttrData = MutableLiveData<Event<Resource<WOMTTRResponse>>>()

    val womttrData: LiveData<Event<Resource<WOMTTRResponse>>> = _womttrData

    fun setWOMTTR() = viewModelScope.launch {
        getWOMTTR()
    }

    private suspend fun getWOMTTR() {
        _womttrData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getWOMTTR()
                if (response.isSuccessful) {
                    _womttrData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _womttrData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _womttrData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _womttrData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _womttrData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}