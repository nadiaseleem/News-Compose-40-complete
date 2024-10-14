package com.example.news_compose_c40.data.repository.data_sources.remote

import com.example.news_compose_c40.data.api.NewsService
import com.example.news_compose_c40.data.model.article.Article
import com.example.news_compose_c40.data.model.source.Source
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val newsService:NewsService):RemoteDataSource{
    override suspend fun getSources(categoryId: String): List<Source> {
       return  newsService.getSources(categoryId).sources ?: listOf()
    }

    override suspend fun getArticles(sourceId: String,page:Int?,pageSize:Int?): List<Article> {
        return newsService.getArticles(sourceId=sourceId,page,pageSize).articles ?: listOf()
    }

    override suspend fun getArticlesThatHas(searchQuery: String): List<Article> {
       return newsService.getArticlesThatHas(searchQuery).articles ?: listOf()
    }

    override suspend fun getArticle(title: String): Article {
       return newsService.getArticle(title).articles?.get(0) ?: Article()
    }
}
