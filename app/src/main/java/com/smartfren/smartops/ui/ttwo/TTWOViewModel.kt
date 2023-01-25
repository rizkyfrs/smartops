package com.smartfren.smartops.ui.ttwo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.ttwo.repository.TTWORepository
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
class TTWOViewModel @Inject constructor(
    application: Application,
    private val repository: TTWORepository,
) : AndroidViewModel(application) {
    private val _listTTWO = MutableLiveData<Event<Resource<TTWOListResponse>>>()
    val listTTWO: LiveData<Event<Resource<TTWOListResponse>>> = _listTTWO

    private val _viewTTWO = MutableLiveData<Event<Resource<TTWOViewResponse>>>()
    val viewTTWO: LiveData<Event<Resource<TTWOViewResponse>>> = _viewTTWO

    private val _listTTWORCALv1 = MutableLiveData<Event<Resource<TTWORCALvListResponse>>>()
    val listTTWORCALv1: LiveData<Event<Resource<TTWORCALvListResponse>>> = _listTTWORCALv1

    private val _listTTWORCALv2 = MutableLiveData<Event<Resource<TTWORCALvListResponse>>>()
    val listTTWORCALv2: LiveData<Event<Resource<TTWORCALvListResponse>>> = _listTTWORCALv2

    private val _listTTWORCALv3 = MutableLiveData<Event<Resource<TTWORCALvListResponse>>>()
    val listTTWORCALv3: LiveData<Event<Resource<TTWORCALvListResponse>>> = _listTTWORCALv3

    private val _updateTTWORCA = MutableLiveData<Event<Resource<TTWORCAUpdateResponse>>>()
    val updateTTWORCA: LiveData<Event<Resource<TTWORCAUpdateResponse>>> = _updateTTWORCA

    fun setListTTWO(start: String?, recperpage: String?) = viewModelScope.launch {
        getListTTWO(start, recperpage)
    }

    fun setViewTTWO(key: String?) = viewModelScope.launch {
        getViewTTWO(key)
    }

    fun setListTTWORCALv1(key: String?, recperpage: String?, rca1: String?, rca2: String?) = viewModelScope.launch {
        getListTTWORCALv1(key, recperpage, rca1, rca2)
    }

    fun setListTTWORCALv2(key: String?, recperpage: String?, rca1: String?, rca2: String?) = viewModelScope.launch {
        getListTTWORCALv2(key, recperpage, rca1, rca2)
    }

    fun setListTTWORCALv3(key: String?, recperpage: String?, rca1: String?, rca2: String?) = viewModelScope.launch {
        getListTTWORCALv3(key, recperpage, rca1, rca2)
    }

    fun setPostTTWORCA(key: String?, u_rca_1: String?, u_rca_2: String?, u_rca_3: String?,
                       time_duration: String?, site_pic_id: String?, site_id: String?) = viewModelScope.launch {
        postUpdateTTWORCA(key, u_rca_1, u_rca_2, u_rca_3, time_duration, site_pic_id, site_id)
    }

    private suspend fun getListTTWO(start: String?, recperpage: String?) {
        _listTTWO.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListTTWO(start, recperpage)
                if (response.isSuccessful) {
                    if (response.body()?.cvFsttwohistRCA == null || response.body()?.cvFsttwohistRCA!!.isEmpty()) {
                        toast(getApplication(), "List is empty.")
                    }
                    _listTTWO.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listTTWO.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listTTWO.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listTTWO.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listTTWO.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getViewTTWO(key: String?) {
        _viewTTWO.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getViewTTWO(key)
                if (response.isSuccessful) {
                    _viewTTWO.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _viewTTWO.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _viewTTWO.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _viewTTWO.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _viewTTWO.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListTTWORCALv1(key: String?, recperpage: String?, rca1: String?, rca2: String?) {
        _listTTWORCALv1.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListTTWORCA(key, recperpage, rca1, rca2)
                if (response.isSuccessful) {
                    _listTTWORCALv1.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listTTWORCALv1.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listTTWORCALv1.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listTTWORCALv1.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listTTWORCALv1.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListTTWORCALv2(key: String?, recperpage: String?, rca1: String?, rca2: String?) {
        _listTTWORCALv2.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListTTWORCA(key, recperpage, rca1, rca2)
                if (response.isSuccessful) {
                    _listTTWORCALv2.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listTTWORCALv2.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listTTWORCALv2.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listTTWORCALv2.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listTTWORCALv2.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListTTWORCALv3(key: String?, recperpage: String?, rca1: String?, rca2: String?) {
        _listTTWORCALv3.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListTTWORCA(key, recperpage, rca1, rca2)
                if (response.isSuccessful) {
                    _listTTWORCALv3.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listTTWORCALv3.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listTTWORCALv3.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listTTWORCALv3.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listTTWORCALv3.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postUpdateTTWORCA(
        key: String?, u_rca_1: String?, u_rca_2: String?, u_rca_3: String?,
        time_duration: String?, site_pic_id: String?, site_id: String?
    ) {
        _updateTTWORCA.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postUpdateTTWORCA(
                    key, u_rca_1, u_rca_2, u_rca_3, time_duration, site_pic_id, site_id
                )
                if (response.isSuccessful) {
                    _updateTTWORCA.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _updateTTWORCA.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _updateTTWORCA.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _updateTTWORCA.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _updateTTWORCA.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}