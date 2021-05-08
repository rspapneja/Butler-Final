package com.butlerschocolates.app.viewmodels.sharedViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    val data = MutableLiveData<String>()
    val dataBack = MutableLiveData<String>()

    fun data(item: String) {
        data.value = item
    }
    fun dataBack(item: String) {
        dataBack.value = item
    }
    fun getName(): LiveData<String?>? {
        return data
    }
    fun getName1(): LiveData<String?>? {
        return data
    }
}