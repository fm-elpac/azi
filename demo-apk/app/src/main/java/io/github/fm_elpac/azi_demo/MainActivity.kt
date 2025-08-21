package io.github.fm_elpac.azi_demo

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface

import io.github.fm_elpac.azi.Azi
import io.github.fm_elpac.azi.AziCb
import io.github.fm_elpac.azi.AziWebView

class MainActivity: Activity() {
    var aw: AziWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Azi.log("azi-demo MainActivity.onCreate()")

        val w = AziWebView(this)
        // 显示 WebView
        setContentView(w.getWebView())

        // 显示并绘制顶部系统栏
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // TODO
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // 添加自定义 js api
        w.addJsApi("demo", DemoApi(this))
        w.addJsApi("azi_api", LoaderApi())
        // 显示 ui-loader
        w.loadLoader()

        aw = w
        // 开始 (后台) 初始化
        val cb = object: AziCb {
            override fun ok() {
                aw?.loadSdcard(Azi.AZI_DIR_SDCARD_DATA, "demo/index.html")
            }
        }
        Azi.initZip("test-init.azi.zip", "demo", cb)
    }

    // 在受限的 WebView 环境中加载页面
    fun openCleanWebView(url: String) {
        Azi.log("Demo.openCleanWebView()  " + url)

        val w = WebView(this)
        w.settings.javaScriptEnabled = true
        w.setWebViewClient(WebViewClient())

        setContentView(w)
        w.loadUrl(url)
    }
}

class DemoApi(val a: MainActivity) {
    // 打开外部页面 URL
    @JavascriptInterface
    fun openPage(url: String) {
        a.aw?.runOnUiThread {
            a.openCleanWebView(url)
        }
    }
}

class LoaderApi() {
    // azi_api.getJsLoadList()
    @JavascriptInterface
    fun getJsLoadList(): List<String> {
        return listOf<String>()
    }

    // azi_api.checkInit()
    @JavascriptInterface
    fun checkInit(): String {
        return "加载中 .. ."
    }
}
