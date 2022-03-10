package com.example.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article){
    lateinit var viewModel: NewsViewModel
    private val arg: ArticleFragmentArgs by navArgs();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val article = arg.article;
        webView.apply {
            webViewClient = WebViewClient();
            article.url?.let { loadUrl(it) };
        }

        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_LONG).show()

//            findNavController().navigate(R.id.savedNewsFragment)
        }
    }
}