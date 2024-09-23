package com.example.news_compose_c40.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.text.intl.Locale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SettingsModule {

    @Provides
    fun provideInitialLanguage():String{
        val languageMap = mapOf("en" to "English", "ar" to "العربية")
        val languageCode = Locale.current.language
        val systemLanguage = languageMap[languageCode] ?: "English"

        return AppCompatDelegate.getApplicationLocales()[0]?.displayLanguage ?: systemLanguage
    }
}