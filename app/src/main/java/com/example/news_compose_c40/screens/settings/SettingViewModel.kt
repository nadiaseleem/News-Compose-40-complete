package com.example.news_compose_c40.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intellij.lang.annotations.Language
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(initialLanguage:String):ViewModel() {
    private val _isExpanded = mutableStateOf(false)
    val isExpanded :Boolean get() = _isExpanded.value

    private val _selectedLanguage = mutableStateOf(initialLanguage)
    val selectedLanguage :String get() = _selectedLanguage.value

    fun setIsExpanded(isExpanded:Boolean){
        _isExpanded.value = isExpanded
    }

    fun setSelectedLanguage(language: String){
        _selectedLanguage.value = language
    }
}