package com.example.mvvmnewsapp.repository

import com.example.mvvmnewsapp.api.RetrofitInstance
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.util.NetworkConnectionInterceptor

class NewsRepository(
    val networkConnectionInterceptor: NetworkConnectionInterceptor,
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = RetrofitInstance.invoke(networkConnectionInterceptor)
        .getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) = RetrofitInstance.invoke(networkConnectionInterceptor)
        .searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}