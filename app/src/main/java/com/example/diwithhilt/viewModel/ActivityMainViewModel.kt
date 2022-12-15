package com.example.diwithhilt.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.diwithhilt.api.ApiService
import com.example.diwithhilt.base.BaseViewModel
import com.example.diwithhilt.model.User
import com.example.diwithhilt.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(private val apiService: ApiService) :BaseViewModel(){

    private var recyclerListData : MutableLiveData<Resource<List<User>>> = MutableLiveData()

    init {
        this.makeApiCall()
    }

    fun getRecyclerListDataObserver():MutableLiveData<Resource<List<User>>>{
        return  recyclerListData
    }

    private fun makeApiCall() {
        viewModelScope.launch {
            apiService.getUsers().let {
                if (it.isSuccessful) {
                    recyclerListData.postValue(Resource.success(it.body()))
                } else {
                    recyclerListData.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }
        }
    }
}