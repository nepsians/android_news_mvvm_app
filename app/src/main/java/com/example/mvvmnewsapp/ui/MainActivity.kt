package com.example.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.util.NetworkConnectionInterceptor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this);
        val newsRepository = NewsRepository(networkConnectionInterceptor, ArticleDatabase(this))
        val viewModelProviderFactory = NewViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment;
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}



















//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val response: Response<NewsResponse> = api.getBreakingNews();
//                for(data: Article in response.body()!!.articles) {
//                    Log.d("Response: ", data.title)
//                }
//            }catch (e: Exception){
//                Log.d("Error: ", e.toString())
//            }
//        }