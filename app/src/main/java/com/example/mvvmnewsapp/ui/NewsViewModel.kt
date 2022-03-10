package com.example.mvvmnewsapp.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.models.Article
import com.example.mvvmnewsapp.models.NewsResponse
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.util.NoInternetException
import com.example.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsRepsonse: NewsResponse? = null;

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null;

    init {
        getBreakingNews(countryCode = "us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        try {
            breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is NoInternetException -> breakingNews.postValue(Resource.Error("No Internet Connection"))
                else -> breakingNews.postValue(Resource.Error("Something went wrong"))
            }
        }
    }

    fun searchNews(searchQuery: String, fromTyping: Boolean) = viewModelScope.launch {
        try {
            searchNews.postValue(Resource.Loading())
            val response = newsRepository.searchNews(searchQuery, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response, fromTyping))
        } catch (t: Throwable) {
            when (t) {
                is NoInternetException -> searchNews.postValue(Resource.Error("No Internet Connection"))
                else -> searchNews.postValue(Resource.Error("Something went wrong"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsRepsonse == null) {
                    breakingNewsRepsonse = resultResponse
                } else {
                    val oldArticles = breakingNewsRepsonse?.articles;
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsRepsonse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(
        response: Response<NewsResponse>,
        fromTyping: Boolean
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles;
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }

                if (fromTyping) return Resource.Success(resultResponse)

                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}