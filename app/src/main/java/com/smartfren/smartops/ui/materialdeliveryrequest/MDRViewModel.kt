package com.smartfren.smartops.ui.materialdeliveryrequest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.materialdeliveryrequest.repository.MDRRepository
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
class MDRViewModel @Inject constructor(
    application: Application,
    private val repository: MDRRepository,
) : AndroidViewModel(application) {
    private val _listMdr = MutableLiveData<Event<Resource<MDRListResponse>>>()
    val activityMDRData: LiveData<Event<Resource<MDRListResponse>>> = _listMdr

    private val _addStockMdr = MutableLiveData<Event<Resource<AddStockMDRResponse>>>()
    val addStockMdr: LiveData<Event<Resource<AddStockMDRResponse>>> = _addStockMdr

    private val _statusMDRSelection = MutableLiveData<Int>()
    val statusMDRSelection: LiveData<Int> = _statusMDRSelection

    private val _listVendor = MutableLiveData<Event<Resource<VendorListResponse>>>()
    val listVendorData: LiveData<Event<Resource<VendorListResponse>>> = _listVendor

    private val _listDOPRegion = MutableLiveData<Event<Resource<DOPListResponse>>>()
    val listDOPRegionData: LiveData<Event<Resource<DOPListResponse>>> = _listDOPRegion

    private val _listDOP = MutableLiveData<Event<Resource<DOPListResponse>>>()
    val listDOPData: LiveData<Event<Resource<DOPListResponse>>> = _listDOP

    private val _detailDOP = MutableLiveData<Event<Resource<DOPDetailResponse>>>()
    val detailDOPData: LiveData<Event<Resource<DOPDetailResponse>>> = _detailDOP

    fun listMDR(start: String?, recperpage: String?) = viewModelScope.launch {
        getListMdr(start, recperpage)
    }

    fun setListVendor(start: String?, recperpage: String?) = viewModelScope.launch {
        getListVendor(start, recperpage)
    }

    fun setListDOPRegion(region: String?) = viewModelScope.launch {
        getListDOPRegion(region)
    }

    fun setListDOP() = viewModelScope.launch {
        getListDOP()
    }

    fun setDetailDOP(key: String?) = viewModelScope.launch {
        getDetailDOP(key)
    }

    fun setStatusMDRSelected(statusMDR: Int) {
        _statusMDRSelection.value = statusMDR
    }

    fun setStatusMDR(): String {
        val statusmdr = statusMDRSelection.value
        return statusmdr.toString()
    }

    fun setPostAddStockMdr(
        fsmdr_status: String?,
        fsmdr_id: String?,
        fsmdr_vendor: String?,
        fsmdr_region: String?,
        fsmdr_dop: String?,
        fsmdr_site_id: String?,
        fsmdr_wo_id: String?,
        fsmdr_wo_dom: String?,
        fsmdr_wo_flag: String?,
        fsmdr_date: String?,
        fsmdr_registrars: String?,
        fsmdr_registerdate: String?
    ) = viewModelScope.launch {
        getPostAddStockMdr(
            fsmdr_status,
            fsmdr_id,
            fsmdr_vendor,
            fsmdr_region,
            fsmdr_dop,
            fsmdr_site_id,
            fsmdr_wo_id,
            fsmdr_wo_dom,
            fsmdr_wo_flag,
            fsmdr_date,
            fsmdr_registrars,
            fsmdr_registerdate
        )
    }

    private suspend fun getListMdr(start: String?, recperpage: String?) {
        _listMdr.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListMdr(start, recperpage)
                if (response.isSuccessful) {
                    _listMdr.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listMdr.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listMdr.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listMdr.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listMdr.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListVendor(start: String?, recperpage: String?) {
        _listVendor.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListVendor(start, recperpage)
                if (response.isSuccessful) {
                    _listVendor.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listVendor.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listVendor.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listVendor.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listVendor.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListDOPRegion(region: String?) {
        _listDOPRegion.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListDOPRegion(region)
                if (response.isSuccessful) {
                    _listDOPRegion.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listDOPRegion.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listDOPRegion.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listDOPRegion.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listDOPRegion.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListDOP() {
        _listDOP.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListDOPRegion("")
                if (response.isSuccessful) {
                    _listDOP.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listDOP.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listDOP.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listDOP.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listDOP.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getDetailDOP(key: String?) {
        _detailDOP.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getDetailDOP(key)
                if (response.isSuccessful) {
                    _detailDOP.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _detailDOP.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _detailDOP.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _detailDOP.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _detailDOP.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getPostAddStockMdr(
        fsmdr_status: String?,
        fsmdr_id: String?,
        fsmdr_vendor: String?,
        fsmdr_region: String?,
        fsmdr_dop: String?,
        fsmdr_site_id: String?,
        fsmdr_wo_id: String?,
        fsmdr_wo_dom: String?,
        fsmdr_wo_flag: String?,
        fsmdr_date: String?,
        fsmdr_registrars: String?,
        fsmdr_registerdate: String?
    ) {
        _addStockMdr.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postAddMDR(
                    fsmdr_status,
                    fsmdr_id,
                    fsmdr_vendor,
                    fsmdr_region,
                    fsmdr_dop,
                    fsmdr_site_id,
                    fsmdr_wo_id,
                    fsmdr_wo_dom,
                    fsmdr_wo_flag,
                    fsmdr_date,
                    fsmdr_registrars,
                    fsmdr_registerdate
                )
                if (response.isSuccessful) {
                    _addStockMdr.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _addStockMdr.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _addStockMdr.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _addStockMdr.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _addStockMdr.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}