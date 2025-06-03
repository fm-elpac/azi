package io.github.fm_elpac.azi_demo

import android.app.Activity
import android.os.Bundle

import io.github.fm_elpac.azi.Azi

class MainActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Azi.log("azi-demo MainActivity.onCreate()")

        Azi.initZip("test-init.azi.zip", "demo", null)
    }

    // TODO
}
