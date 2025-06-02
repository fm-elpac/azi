package io.github.fm_elpac.azi

import android.content.Context

class AziSh(val c: Context, val azi: AziApi) {

    // 获取 AZI_DIR_APP_DATA 的值
    private fun envDirAppData(): String {
        return c.getFilesDir().getAbsolutePath()
    }

    // 获取 AZI_DIR_SDCARD_DATA 的值
    private fun envDirSdcardData(): String {
        return c.getExternalFilesDir(null)!!.getAbsolutePath()
    }

    // 获取 AZI_DIR_SDCARD_CACHE 的值
    private fun envDirSdcardCache(): String {
        return c.getExternalCacheDir()!!.getAbsolutePath()
    }

    init {
        // DEBUG
        println("AziSh.env AZI_DIR_APP_DATA " + envDirAppData())
        println("AziSh.env AZI_DIR_SDCARD_DATA " + envDirSdcardData())
        println("AziSh.env AZI_DIR_SDCARD_CACHE " + envDirSdcardCache())
    }

    fun env(name: String): String? {
        if (name == AziApi.AZI_DIR_APP_DATA) {
            return envDirAppData()
        }
        if (name == AziApi.AZI_DIR_SDCARD_DATA) {
            return envDirSdcardData()
        }
        if (name == AziApi.AZI_DIR_SDCARD_CACHE) {
            return envDirSdcardCache()
        }
        return null
    }

    fun sh(cmd: String): Int {
        // TODO
        return -1
    }

    // TODO
}
