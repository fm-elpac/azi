package io.github.fm_elpac.azi_demo

import android.app.Activity
import android.os.Bundle

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

        // TODO
        // status bar color (black)
        window.statusBarColor = 0xff000000.toInt()

        aw = w
        // 开始 (后台) 初始化
        val cb = object: AziCb {
            override fun ok() {
                aw?.loadSdcard(Azi.AZI_DIR_SDCARD_DATA, "demo/index.html")
            }
        }
        Azi.initZip("test-init.azi.zip", "demo", cb)
    }

    // TODO
}
