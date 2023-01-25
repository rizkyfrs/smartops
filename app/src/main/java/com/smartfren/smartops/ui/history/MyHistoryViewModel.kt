package com.smartfren.smartops.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.ActivityHistoryLogResponse
import com.smartfren.smartops.ui.history.repository.MyHistoryRepository
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
class MyHistoryViewModel @Inject constructor(
    application: Application,
    private val repository: MyHistoryRepository,
) : AndroidViewModel(application) {
    private val _listHistory = MutableLiveData<Event<Resource<ActivityHistoryLogResponse>>>()

    val activityHistoryData: LiveData<Event<Resource<ActivityHistoryLogResponse>>> = _listHistory

    fun listHistory(token: String?, page: String?) = viewModelScope.launch {
        getListHistory(token, page)
    }

    private suspend fun getListHistory(token: String?, page: String?) {
        _listHistory.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListHistory(token, page)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listHistory.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listHistory.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listHistory.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listHistory.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listHistory.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}