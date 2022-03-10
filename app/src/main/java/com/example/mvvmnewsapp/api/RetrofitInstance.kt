package com.example.mvvmnewsapp.api

import com.example.mvvmnewsapp.util.Constants.Companion.BASE_API
import com.example.mvvmnewsapp.util.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): NewsAPI {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(logging)
                .addInterceptor(networkConnectionInterceptor).build()
            return Retrofit.Builder().baseUrl(BASE_API).addConverterFactory(GsonConverterFactory.create())
                .client(client).build().create(NewsAPI::class.java)
        }

//        private val retrofit: Retrofit by lazy {
//            val logging = HttpLoggingInterceptor()
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//            val client = OkHttpClient.Builder().addInterceptor(logging).build()
//            Retrofit.Builder().baseUrl(BASE_API).addConverterFactory(GsonConverterFactory.create())
//                .client(client).build()
//        }
//
//        val api: NewsAPI by lazy {
//            retrofit.create(NewsAPI::class.java)
//        }
    }
}