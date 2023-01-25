package com.smartfren.smartops.ui.login

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.LoginRequest
import com.smartfren.smartops.data.models.LoginResponse
import com.smartfren.smartops.data.models.LoginResponseNew
import com.smartfren.smartops.ui.login.repository.LoginRepository
import com.smartfren.smartops.util.Event
import com.smartfren.smartops.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
@SuppressLint("NewApi")
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: LoginRepository,
    private var token: String? = null
) : AndroidViewModel(application) {
    private val _loginData = MutableLiveData<Event<Resource<LoginResponse>>>()

    val loginData: LiveData<Event<Resource<LoginResponse>>> = _loginData

    private val _loginDataNew = MutableLiveData<Event<Resource<LoginResponseNew>>>()

    val loginDataNew: LiveData<Event<Resource<LoginResponseNew>>> = _loginDataNew

    fun loginUser(dataModelLoginBody: LoginRequest, version: String?, fcmToken: String?) = viewModelScope.launch {
        getLogin(dataModelLoginBody, version, fcmToken)
    }

    fun loginUserNew(dataModelLoginBody: LoginRequest) = viewModelScope.launch {
        getLoginNew(dataModelLoginBody)
    }

    private suspend fun getLogin(dataModelLoginBody: LoginRequest, version: String?, fcmToken: String?) {
        _loginData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getLogin(dataModelLoginBody, version, fcmToken)
                if (response.isSuccessful) {
                    if (response.body()?.report?.authorized == false) {
                        toast(getApplication(), "Invalid username or password.")
                    } else {
                        token = response.body()?.report?.userAttributes?.get(0)?.getuToken()
                    }
                    _loginData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _loginData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _loginData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getLoginNew(dataModelLoginBody: LoginRequest) {
        _loginDataNew.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getLoginNew(dataModelLoginBody)
                if (response.isSuccessful) {
                    if (response.body()?.jwt == null) {
                        toast(getApplication(), "Invalid username or password.")
                    }
                    MainApp.instance.sharedPreferences?.edit()
                        ?.putString("token", token)
                        ?.putString("tokenJWT", response.body()?.jwt)
                        ?.putString("levelUserID", getUserLevelIDJWT(response.body()?.jwt))
                        ?.putString("parentUserID", getUserParentIDJWT(response.body()?.jwt))
                        ?.apply()
                    _loginDataNew.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    if (response.code() == 401) {
                        toast(getApplication(), "Invalid username or password. Please contact your PIC.")
                    }
                    _loginDataNew.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _loginDataNew.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _loginDataNew.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginDataNew.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}