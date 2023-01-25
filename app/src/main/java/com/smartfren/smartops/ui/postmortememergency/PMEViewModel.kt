package com.smartfren.smartops.ui.postmortememergency

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.postmortememergency.repository.PMERepository
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
class ccfViewModel @Inject constructor(
    application: Application,
    private val repository: PMERepository,
) : AndroidViewModel(application) {
    private val _pmeData = MutableLiveData<Event<Resource<PMESummaryResponse>>>()

    val pmeData: LiveData<Event<Resource<PMESummaryResponse>>> = _pmeData

    fun pmeSum() = viewModelScope.launch {
        getPME()
    }

    private suspend fun getPME() {
        _pmeData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getPostMortemSum()
                if (response.isSuccessful) {
                    _pmeData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _pmeData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _pmeData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _pmeData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _pmeData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}