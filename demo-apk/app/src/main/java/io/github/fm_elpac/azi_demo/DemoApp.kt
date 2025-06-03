package io.github.fm_elpac.azi_demo

import android.app.Application

import io.github.fm_elpac.azi.Azi

class DemoApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // init azi
        Azi.setContext(this)
    }
}
