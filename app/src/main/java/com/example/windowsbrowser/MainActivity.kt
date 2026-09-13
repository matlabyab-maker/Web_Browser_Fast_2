package com.example.windowsbrowser

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() {

    private lateinit var webView: WebView
    private lateinit var addressBar: EditText
    private var screenshotCounter = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        addressBar = findViewById(R.id.addressBar)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                addressBar.setText(url)
                return true
            }
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                addressBar.setText(url)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {}

        // Download listener: hand downloads to the system DownloadManager
        webView.setDownloadListener { url, _, contentDisposition, mimeType, _ ->
            try {
                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    val name = URLUtil.guessFileName(url, contentDisposition, mimeType)
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)
                    setMimeType(mimeType)
                }
                (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
                Toast.makeText(this, R.string.download_started, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, R.string.download_failed, Toast.LENGTH_LONG).show()
            }
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { if (webView.canGoBack()) webView.goBack() }
        findViewById<ImageButton>(R.id.btnForward).setOnClickListener { if (webView.canGoForward()) webView.goForward() }
        findViewById<ImageButton>(R.id.btnReload).setOnClickListener { webView.reload() }
        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            webView.loadUrl(getString(R.string.home_url))
        }
        findViewById<ImageButton>(R.id.btnGo).setOnClickListener { loadFromAddressBar() }
        addressBar.setOnEditorActionListener { _, _, _ -> loadFromAddressBar(); true }

        // Floating UI toggle (simulated "Windows-style" panel)
        findViewById<ImageButton>(R.id.btnToggleUI).setOnClickListener { v ->
            val panel = findViewById<View>(R.id.buttonPanel)
            panel.visibility = if (panel.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        // Screenshot of the current page
        findViewById<ImageButton>(R.id.btnScreenshot).setOnClickListener { takeScreenshot() }
        // Save page (MHTML)
        findViewById<ImageButton>(R.id.btnSavePage).setOnClickListener { savePage() }

        webView.loadUrl(getString(R.string.home_url))
    }

    private fun loadFromAddressBar(): Boolean {
        var url = addressBar.text.toString().trim()
        if (url.isEmpty()) return true
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = if (url.contains(".") && !url.contains(" ")) "https://$url"
            else "https://www.google.com/search?q=" + Uri.encode(url)
        }
        webView.loadUrl(url)
        return true
    }

    private fun takeScreenshot() {
        val view = webView
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        screenshotCounter++
        val dir = File(getExternalFilesDir(null), "Screenshots").apply { mkdirs() }
        val file = File(dir, "screenshot_$screenshotCounter.png")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        Toast.makeText(this, getString(R.string.screenshot_saved, file.absolutePath), Toast.LENGTH_LONG).show()
    }

    private fun savePage() {
        val dir = File(getExternalFilesDir(null), "SavedPages").apply { mkdirs() }
        val file = File(dir, "page_${System.currentTimeMillis()}.mht")
        webView.saveWebArchive(file.absolutePath)
        Toast.makeText(this, getString(R.string.page_saved, file.absolutePath), Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
