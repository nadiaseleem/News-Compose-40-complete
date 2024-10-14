package com.example.news_compose_c40.data.api

import com.example.news_compose_c40.data.model.article.ArticlesResponse
import com.example.news_compose_c40.data.model.source.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("category") categoryId: String,
    ): SourcesResponse


    @GET("everything")
    suspend fun getArticles(
        @Query("sources") sourceId: String,
        @Query("page") page:Int?=null,
        @Query("pageSize") pageSize: Int?=null
    ): ArticlesResponse

    @GET("everything")
    suspend fun getArticle(
        @Query("q") title: String,
        @Query("searchIn") searchIn:String ="title"
    ): ArticlesResponse

    @GET("everything")
    suspend fun getArticlesThatHas(
        @Query("q") searchQuery: String): ArticlesResponse

}
