package com.example.news_compose_c40.ui.activity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news_compose_c40.data.connectivity.NetworkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
open class BaseViewModel @Inject constructor(val networkHandler: NetworkHandler):ViewModel() {

    private var _isConnected= mutableStateOf<Boolean>(false)
    val isConnected: Boolean get() = _isConnected.value

    private var wasDisconnected = false

    fun updateConnectivity(){

        _isConnected.value = networkHandler.isNetworkAvailable()

        if(!_isConnected.value){
            wasDisconnected = true
        }
        _showBackOnlineMessage.value = _isConnected.value && wasDisconnected
    }


    private val _showBackOnlineMessage = mutableStateOf(false)
    val showBackOnlineMessage: Boolean get() = _showBackOnlineMessage.value


    fun hideBackOnlineMessage() {
       _showBackOnlineMessage.value = false
    }

}
