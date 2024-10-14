package com.example.news_compose_c40.data.repository

import com.example.news_compose_c40.data.connectivity.NetworkHandler
import com.example.news_compose_c40.data.model.article.Article
import com.example.news_compose_c40.data.model.source.Source
import com.example.news_compose_c40.data.repository.data_sources.local.LocalDataSource
import com.example.news_compose_c40.data.repository.data_sources.remote.RemoteDataSource
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(val networkHandler: NetworkHandler,val remoteDataSource: RemoteDataSource,val localDataSource: LocalDataSource):NewsRepo {
    override suspend fun getSources(categoryId: String): List<Source> {
        if (networkHandler.isNetworkAvailable()){
           val sources = remoteDataSource.getSources(categoryId)
            localDataSource.saveSources(sources)
            return sources
        }else{
           return localDataSource.getSources(categoryId)
        }
    }

    override suspend fun getArticles(sourceId: String,page:Int?,pageSize:Int?): List<Article> {
        if (networkHandler.isNetworkAvailable()){
            val articles = remoteDataSource.getArticles(sourceId,page, pageSize)
            articles.forEach { it.sourceId = sourceId }
            localDataSource.saveArticles(articles)
            return articles
        }else{
            return localDataSource.getArticles(sourceId)
        }
    }

    override suspend fun getArticlesThatHas(searchQuery: String): List<Article> {
       if (networkHandler.isNetworkAvailable()){
           val articles = remoteDataSource.getArticlesThatHas(searchQuery)
           localDataSource.saveArticles(articles)
           return articles
       }else{
           return localDataSource.getArticlesThatHas(searchQuery)
       }
    }

    override suspend fun getArticle(title: String): Article {
       if (networkHandler.isNetworkAvailable()){
           return remoteDataSource.getArticle(title)
       }else{
           return localDataSource.getArticle(title)
       }
    }
}
