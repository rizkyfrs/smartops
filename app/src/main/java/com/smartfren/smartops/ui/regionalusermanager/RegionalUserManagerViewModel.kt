package com.smartfren.smartops.ui.regionalusermanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.RegionalUserLevelResponse
import com.smartfren.smartops.data.models.RegionalUserManagerAddResponse
import com.smartfren.smartops.data.models.RegionalUserManagerListResponse
import com.smartfren.smartops.data.models.RegionalUserManagerViewResponse
import com.smartfren.smartops.ui.regionalusermanager.repository.RegionalUserManagerRepository
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
class RegionalUserManagerViewModel @Inject constructor(
    application: Application,
    private val repository: RegionalUserManagerRepository,
) : AndroidViewModel(application) {
    private val _listRegionalUserManager = MutableLiveData<Event<Resource<RegionalUserManagerListResponse>>>()
    val listRegionalUserManager: LiveData<Event<Resource<RegionalUserManagerListResponse>>> = _listRegionalUserManager

    private val _listRegionalUserManagerItem = MutableLiveData<Event<Resource<RegionalUserManagerListResponse>>>()
    val listRegionalUserManagerItem: LiveData<Event<Resource<RegionalUserManagerListResponse>>> = _listRegionalUserManagerItem

    private val _viewRegionalUserManager = MutableLiveData<Event<Resource<RegionalUserManagerViewResponse>>>()
    val viewRegionalUserManager: LiveData<Event<Resource<RegionalUserManagerViewResponse>>> = _viewRegionalUserManager

    private val _listRegionalUserManagerLevel = MutableLiveData<Event<Resource<RegionalUserLevelResponse>>>()
    val listRegionalUserManagerLevel: LiveData<Event<Resource<RegionalUserLevelResponse>>> = _listRegionalUserManagerLevel

    private val _addRegionalUserManager = MutableLiveData<Event<Resource<RegionalUserManagerAddResponse>>>()
    val addRegionalUserManager: LiveData<Event<Resource<RegionalUserManagerAddResponse>>> = _addRegionalUserManager

    private val _editRegionalUserManager = MutableLiveData<Event<Resource<RegionalUserManagerAddResponse>>>()
    val editRegionalUserManager: LiveData<Event<Resource<RegionalUserManagerAddResponse>>> = _editRegionalUserManager

    fun setListRegionalUserManager(start: String?, recperpage: String?, u_level: String?, id_parrent: String?) = viewModelScope.launch {
        getListRegionalUserManager(start, recperpage, u_level, id_parrent)
    }

    fun setListRegionalUserManagerItem(start: String?, recperpage: String?, u_level: String?, id_parrent: String?) = viewModelScope.launch {
        getListRegionalUserManagerItem(start, recperpage, u_level, id_parrent)
    }

    fun setViewRegionalUserManager(key: String?) = viewModelScope.launch {
        getViewRegionalUserManager(key)
    }

    fun setListRegionalUserManagerLevel() = viewModelScope.launch {
        getListRegionalUserManagerLevel()
    }

    fun setAddRegionalUserManager(
        id_user: String?,
        u_region: String?,
        id_parent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?,
        u_registrars: String?,
        u_registrationdate: String?
    ) = viewModelScope.launch {
        postAddRegionalUserManager(
            id_user, u_region, id_parent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation, u_registrars, u_registrationdate
        )
    }

    fun setEditRegionalUserManager(
        key: String?,
        id_user: String?,
        u_company: String?,
        u_region: String?,
        id_parent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?
    ) = viewModelScope.launch {
        postEditRegionalUserManager(
            key, id_user, u_company, u_region, id_parent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation
        )
    }

    private suspend fun getListRegionalUserManager(start: String?, recperpage: String?, u_level: String?, id_parrent: String?) {
        _listRegionalUserManager.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListRegionalUserManager(start, recperpage, u_level, id_parrent)
                if (response.isSuccessful) {
                    _listRegionalUserManager.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listRegionalUserManager.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listRegionalUserManager.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listRegionalUserManager.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listRegionalUserManager.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListRegionalUserManagerItem(start: String?, recperpage: String?, u_level: String?, id_parrent: String?) {
        _listRegionalUserManagerItem.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListRegionalUserManager(start, recperpage, u_level, id_parrent)
                if (response.isSuccessful) {
                    _listRegionalUserManagerItem.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listRegionalUserManagerItem.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listRegionalUserManagerItem.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listRegionalUserManagerItem.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listRegionalUserManagerItem.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getViewRegionalUserManager(key: String?) {
        _viewRegionalUserManager.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getViewRegionalUserManager(key)
                if (response.isSuccessful) {
                    _viewRegionalUserManager.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _viewRegionalUserManager.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _viewRegionalUserManager.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _viewRegionalUserManager.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _viewRegionalUserManager.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListRegionalUserManagerLevel() {
        _listRegionalUserManagerLevel.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListRegionalUserLevel()
                if (response.isSuccessful) {
                    _listRegionalUserManagerLevel.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listRegionalUserManagerLevel.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listRegionalUserManagerLevel.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listRegionalUserManagerLevel.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listRegionalUserManagerLevel.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postAddRegionalUserManager(
        id_user: String?,
        u_region: String?,
        id_parrent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?,
        u_registrars: String?,
        u_registrationdate: String?
    ) {
        _addRegionalUserManager.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postAddRegionalUserManager(
                    id_user,
                    u_region,
                    id_parrent,
                    u_nik,
                    username,
                    password,
                    u_name,
                    u_phone,
                    u_level,
                    u_mail,
                    u_activation,
                    u_registrars,
                    u_registrationdate
                )
                if (response.isSuccessful) {
                    _addRegionalUserManager.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _addRegionalUserManager.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _addRegionalUserManager.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _addRegionalUserManager.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _addRegionalUserManager.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postEditRegionalUserManager(
        key: String?,
        id_user: String?,
        u_company: String?,
        u_region: String?,
        id_parrent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?
    ) {
        _editRegionalUserManager.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postEditRegionalUserManager(
                    key, id_user, u_company, u_region, id_parrent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation
                )
                if (response.isSuccessful) {
                    _editRegionalUserManager.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _editRegionalUserManager.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _editRegionalUserManager.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _editRegionalUserManager.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _editRegionalUserManager.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}