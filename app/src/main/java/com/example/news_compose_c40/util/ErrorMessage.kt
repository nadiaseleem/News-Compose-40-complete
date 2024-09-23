package com.example.news_compose_c40.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.news_compose_c40.R

@Composable
fun getErrorMessage(errorMessage: String?, errorMessageId: Int?) = errorMessage ?: errorMessageId?.let { stringResource(id = it) } ?: stringResource(id = R.string.something_went_wrong)

