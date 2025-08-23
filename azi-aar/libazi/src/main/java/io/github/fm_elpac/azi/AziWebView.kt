package io.github.fm_elpac.azi

import java.io.File

import android.content.Context
import android.os.Looper
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface

/**
 * Azi 对 Android WebView 的封装
 */
class AziWebView(val c: Context) {
    val w: WebView
    val h = Handler(Looper.getMainLooper())

    init {
        // 启用调试 (不安全)
        //WebView.setWebContentsDebuggingEnabled(true)

        w = WebView(c)

        // 默认初始化行为

        // 启用 javascript
        w.settings.javaScriptEnabled = true
        // 启用 localStorage, sessionStorage
        w.settings.domStorageEnabled = true

        // 允许访问本地文件 (比如 sdcard)
        w.settings.allowFileAccess = true
        w.settings.allowFileAccessFromFileURLs = true
        w.settings.allowUniversalAccessFromFileURLs = true

        //w.settings.setDatabaseEnabled(true)
        w.settings.mediaPlaybackRequiresUserGesture = false
        w.settings.setGeolocationEnabled(false)
        // TODO
        w.setWebViewClient(WebViewClient())

        // 修复: 高版本 Android 滚动行为 (拉伸) 导致页面滚动到底部/顶部时, 页面内容变形
        w.overScrollMode = WebView.OVER_SCROLL_NEVER
    }

    /**
     * 返回原始 WebView
     */
    fun getWebView(): WebView {
        return w
    }

    /**
     * 添加 JavascriptInterface
     */
    fun addJsApi(name: String, api: Any) {
        w.addJavascriptInterface(api, name)
    }

    /**
     * 加载任意 URL
     */
    fun loadUrl(url: String) {
        // DEBUG
        Azi.log("AziWebView.loadUrl()  " + url)

        w.loadUrl(url)
    }

    /**
     * 加载本地文件 file:///
     */
    fun loadLocal(path: String) {
        loadUrl("file://" + path)
    }

    /**
     * 加载 apk assets 文件 file:///android_asset/
     */
    fun loadAsset(path: String) {
        loadUrl("file:///android_asset/" + path)
    }

    /**
     * 加载 (内置的) Azi ui-loader
     */
    fun loadLoader() {
        loadAsset("azi/loader/index.html")
    }

    /**
     * 加载 azi 环境变量中的文件: AZI_ENV_SDCARD_DATA, AZI_ENV_SDCARD_CACHE
     */
    fun loadSdcard(azi_env: String, path: String) {
        // path join
        val f = File(File(Azi.env(azi_env)!!), path)

        loadLocal(f.absolutePath)
    }

    /**
     * 检查是否能回退, 如果可以, 回退, 返回 true
     */
    fun checkGoback(): Boolean {
        if (w.canGoBack()) {
            w.goBack()
            return true
        }
        return false
    }

    /**
     * 在 UI 线程执行
     */
    fun runOnUiThread(r: Runnable) {
        h.post(r)
    }
}
