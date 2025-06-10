package io.github.fm_elpac.azi

import java.io.File
import java.io.FileOutputStream

import android.content.Context

class AziAsset(val c: Context, val azi: AziApi) {

    fun cpAsset(name: String, target: String) {
        azi.log("AziAsset.cp assets/" + name + " -> " + target)

        // assets: InputStream
        val inputStream = c.assets.open(name)
        // 目标输出文件
        val outputStream = FileOutputStream(File(target))
        // 复制 stream
        inputStream.use { i ->
            outputStream.use { o ->
                // 缓冲区 1MB
                i.copyTo(o, 1024 * 1024)
            }
        }
    }
}
