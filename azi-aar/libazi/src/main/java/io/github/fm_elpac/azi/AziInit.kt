package io.github.fm_elpac.azi

import java.io.File

import android.os.Build
import android.os.Looper
import android.os.Handler

class AziInit(val azi: AziApi) {

    fun initZip(asset_zip: String, target_dir: String, cb: AziCb?) {
        val i = this
        // 创建新线程, 并执行
        Thread {
            i.doInitZip(asset_zip, target_dir, cb)
        }.start()
    }

    // 在后台线程中运行
    fun doInitZip(asset_zip: String, target_dir: String, cb: AziCb?) {
        // DEBUG
        azi.log("AziInit.initZip()  " + asset_zip + " -> " + target_dir)

        checkUnzip(asset_zip, target_dir)
        checkInitSh(target_dir)

        okCb(cb)
    }

    // 执行成功后的回调
    private fun okCb(cb: AziCb?) {
        val h = Handler(Looper.getMainLooper())

        // run on ui thread
        h.post {
            cb?.ok()
        }
    }

    // 从 assets/azi/unzip 释放到 AZI_DIR_APP_DATA/azi/unzip
    // 并 chmod +x
    private fun setupUnzip(): String {
        val d_data = File(azi.env(AziApi.AZI_DIR_APP_DATA)!!)
        val d_azi = File(d_data, "azi")
        // 尝试创建目录
        d_azi.mkdirs()
        // 复制文件
        val p_unzip = File(d_azi, "unzip").absolutePath
        azi.cpAsset("azi/unzip", p_unzip)
        // chmod +x
        azi.sh(ProcessBuilder("chmod", "+x", p_unzip))

        return p_unzip
    }

    private fun getDirTarget(target_dir: String): File {
        val d_sdcard = File(azi.env(AziApi.AZI_DIR_SDCARD_DATA)!!)
        val d_target = File(d_sdcard, target_dir)
        return d_target
    }

    // 检查目录 AZI_DIR_SDCARD_DATA/target_dir 是否存在, 不存在则从 assets/asset_zip 解压
    private fun checkUnzip(asset_zip: String, target_dir: String) {
        val d_target = getDirTarget(target_dir)
        if (d_target.exists()) {
            // 跳过
            azi.log("  skip exist  " + d_target.absolutePath)
            return
        }

        val p_unzip = setupUnzip()

        val d_cache = File(azi.env(AziApi.AZI_DIR_SDCARD_CACHE)!!)
        val d_zip = File(d_cache, asset_zip)
        // 从 assets 复制 zip 到 AZI_DIR_SDCARD_CACHE (如果不存在)
        if (!d_zip.exists()) {
            azi.cpAsset(asset_zip, d_zip.absolutePath)
        }
        // 解压 zip
        val p_zip = d_zip.absolutePath
        val p_target = d_target.absolutePath
        val pb: ProcessBuilder
        if (Build.VERSION.SDK_INT >= AziApi.AAL10) {
            pb = ProcessBuilder(AziApi.AL, p_unzip, p_zip, p_target)
        } else {
            pb = ProcessBuilder(p_unzip, p_zip, p_target)
        }
        azi.sh(pb)
    }

    // 检查执行 azi_init.sh
    private fun checkInitSh(target_dir: String) {
        val d_sdcard = File(azi.env(AziApi.AZI_DIR_SDCARD_DATA)!!)
        val azi_ok = File(d_sdcard, "azi/azi_ok")
        if (azi_ok.exists()) {
            // 跳过
            azi.log("  skip sh  " + azi_ok.absolutePath)
            return
        }

        val d_target = getDirTarget(target_dir)
        val init_sh = File(d_target, "azi_init.sh")
        if (init_sh.exists()) {
            // 执行初始化脚本
            val c = azi.sh(ProcessBuilder("/system/bin/sh", init_sh.absolutePath))
            if (0 != c) {
                throw Exception("azi_init.sh exit " + c)
            }
        }
        // 创建 azi_ok 文件
        val d_azi = File(d_sdcard, "azi")
        d_azi.mkdirs()
        // DEBUG
        azi.log("  touch " + azi_ok.absolutePath)
        azi_ok.createNewFile()
    }
}
