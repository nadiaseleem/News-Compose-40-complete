package com.example.news_compose_c40.data.repository.data_sources.remote

import com.example.news_compose_c40.data.model.article.Article
import com.example.news_compose_c40.data.model.source.Source

interface RemoteDataSource {
    suspend fun getSources(categoryId:String):List<Source>

    suspend fun getArticles(sourceId:String,page:Int?,pageSize:Int?) :List<Article>

    suspend fun getArticlesThatHas(searchQuery:String):List<Article>

    suspend fun getArticle(title:String): Article
}
