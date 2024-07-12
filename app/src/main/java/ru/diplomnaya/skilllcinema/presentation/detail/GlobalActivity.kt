package ru.diplomnaya.skilllcinema.presentation.detail

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.GlobalActivityBinding


class GlobalActivity : AppCompatActivity() {
    private var _binding: GlobalActivityBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = GlobalActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bar = supportActionBar
        val gradient = ResourcesCompat.getDrawable(resources, R.drawable.gradient7, null)
        bar!!.setBackgroundDrawable(gradient)
        val urlFilm = intent?.extras?.getString("filmUrlOnGlobal")
        binding.webView.getSettings().setJavaScriptEnabled(true)
        binding.webView.loadUrl("$urlFilm")
        binding.webView.setWebViewClient(MyWebViewClient())
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onBackPressed() {
        if(binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed();
        }

    }

   class MyWebViewClient: WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (view != null) {
                if (request != null) {
                    view.loadUrl(request.url.toString())
                }
            }
            return true
        }

    }

}