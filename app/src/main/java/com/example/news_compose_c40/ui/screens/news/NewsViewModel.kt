package com.example.news_compose_c40.ui.screens.news

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.news_compose_c40.R
import com.example.news_compose_c40.data.connectivity.NetworkHandler
import com.example.news_compose_c40.data.model.article.Article
import com.example.news_compose_c40.data.model.source.Source
import com.example.news_compose_c40.data.model.source.SourcesResponse
import com.example.news_compose_c40.util.UIMessage
import com.example.news_compose_c40.util.fromJson
import com.example.news_compose_c40.data.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

const val PAGE_SIZE = 3

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepo: NewsRepo, networkHandler: NetworkHandler
) : ConnectivityViewModel(networkHandler) {
    private val _sourcesList = mutableStateOf<List<Source>?>(null)
    val sourcesList: List<Source>? get() = _sourcesList.value

    private var _articlesList = mutableStateOf<List<Article>>(listOf())
    val articlesList: List<Article> get() = _articlesList.value

    private val _uiMessage = mutableStateOf(UIMessage())
    val uiMessage: UIMessage get() = _uiMessage.value

    private val _isErrorDialogVisible = mutableStateOf(false)
    val isErrorDialogVisible: Boolean get() = _isErrorDialogVisible.value


    private var _selectedSourceId by mutableStateOf("")

    fun setSelectedSourceId(sourceId: String) {
        _selectedSourceId = sourceId
    }

    private val _retry = mutableStateOf<(() -> Unit)?>(null)
    val retry: (() -> Unit)? get() = _retry.value



    fun hideErrorDialog() {
        _isErrorDialogVisible.value = false
    }

    fun getSources(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiMessage.value = UIMessage(isLoading = true)
                val sources = newsRepo.getSources(categoryId = categoryId)
                _uiMessage.value = UIMessage(isLoading = false)

                _sourcesList.value = sources

            } catch (e: HttpException) {
                val sourcesResponse = e.response()?.errorBody()?.string()?.fromJson(
                    SourcesResponse::class.java
                )
                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessage = sourcesResponse?.message,
                    retryAction = {
                        getSources(categoryId)
                    })

                _isErrorDialogVisible.value = true

            } catch (e: UnknownHostException) {

                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessageId = R.string.connection_error,
                    retryAction = {
                        getSources(categoryId)
                    })
                _isErrorDialogVisible.value = true

            } catch (e: Exception) {
                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessage = e.localizedMessage,
                    retryAction = {
                        getSources(categoryId)
                    })
                _isErrorDialogVisible.value = true

            }
        }
    }


    fun getNewsBySource(sourceId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiMessage.value = UIMessage(isLoading = true)
                _page = 1
                val articles = newsRepo.getArticles(sourceId = sourceId, _page, PAGE_SIZE)
                _uiMessage.value = UIMessage(isLoading = false)

                if (articles.isNotEmpty())
                    _articlesList.value = articles
                else
                    _uiMessage.value = UIMessage(shouldDisplayNoArticlesFound = true)

                _retry.value = {
                    getNewsBySource(sourceId)
                }
            } catch (e: HttpException) {
                val sourcesResponse = e.response()?.errorBody()?.string()?.fromJson(
                    SourcesResponse::class.java
                )
                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessage = sourcesResponse?.message,
                    retryAction = {
                        getNewsBySource(sourceId)
                    })

                _isErrorDialogVisible.value = true

            } catch (e: UnknownHostException) {

                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessageId = R.string.connection_error,
                    retryAction = {
                        getNewsBySource(sourceId)
                    })
                _isErrorDialogVisible.value = true

            } catch (e: Exception) {
                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessage = e.localizedMessage,
                    retryAction = {
                        getNewsBySource(sourceId)
                    })
                _isErrorDialogVisible.value = true

            }
        }

    }
    //not something we want to react to when it changes ,we just want to keep track of
    private var _page = 1
    val page:Int get() = _page

    private fun appendNews(news: List<Article>) {
        val current = ArrayList(_articlesList.value)
        current.addAll(news)
        _articlesList.value = current
    }


    fun nextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("TAG", "nextPage: $page", )

            try {
                    _uiMessage.value = UIMessage(requestingNextPage = true)
//                    delay(1000)//remove
                    val articles = newsRepo.getArticles(_selectedSourceId, _page, PAGE_SIZE)
                    appendNews(articles)
                    _uiMessage.value = UIMessage(requestingNextPage = false)
                    _page++

            } catch (e: HttpException) {
                val sourcesResponse = e.response()?.errorBody()?.string()?.fromJson(
                    SourcesResponse::class.java
                )
                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessage = sourcesResponse?.message,
                    retryAction = {
                        nextPage()
                    })

                _isErrorDialogVisible.value = true

            } catch (e: UnknownHostException) {

                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessageId = R.string.connection_error,
                    retryAction = {
                        nextPage()
                    })
                _isErrorDialogVisible.value = true

            } catch (e: Exception) {
                _uiMessage.value = UIMessage(
                    isLoading = false,
                    errorMessage = e.localizedMessage,
                    retryAction = {
                        nextPage()
                    })
                _isErrorDialogVisible.value = true

            }
        }

    }

}
