package io.github.fm_elpac.azi

import android.content.Context

/**
 * Azi 接口 API 的具体实现
 */
class AziApi(val c: Context) {
    companion object {
        // 固定的环境变量名称
        const val AZI_DIR_APP_DATA = "AZI_DIR_APP_DATA"
        const val AZI_DIR_SDCARD_DATA = "AZI_DIR_SDCARD_DATA"
        const val AZI_DIR_SDCARD_CACHE = "AZI_DIR_SDCARD_CACHE"

        // Android 10 (api level 29)
        const val AAL10 = 29
        // Android system linker (aarch64)
        const val AL = "/system/bin/linker64"
    }

    private val aziSh: AziSh
    private val aziLog: AziLog
    private val aziAsset: AziAsset
    private val aziInit: AziInit

    init {
        aziSh = AziSh(c, this)
        aziLog = AziLog(this)
        aziAsset = AziAsset(c, this)
        aziInit = AziInit(this)

        // DEBUG: 初始化完成 (写日志)
        log("AziApi.init")
    }

    /**
     * 启动初始化 (后台线程执行)
     *
     * asset_zip: 用于初始化的 assets/XXX.azi.zip 文件路径
     * target_dir: 目标解压目录 AZI_DIR_SDCARD_DATA/target_dir
     * cb: 初始化完成后的回调
     */
    fun initZip(asset_zip: String, target_dir: String, cb: AziCb?) {
        aziInit.initZip(asset_zip, target_dir, cb)
    }

    /**
     * 获取环境变量的值
     *
     * name: 环境变量的名称
     */
    fun env(name: String): String? {
        return aziSh.env(name)
    }

    /**
     * 记录 (写入) 日志
     *
     * text: 内容
     */
    fun log(text: String) {
        aziLog.log(text)
    }

    /**
     * 调用 /system/bin/sh 执行命令 (阻塞)
     *
     * cmd: 命令
     *
     * 返回: 退出码 exit_code
     */
    fun sh(cmd: ProcessBuilder): Int {
        return aziSh.sh(cmd)
    }

    /**
     * 从 apk/assets 中复制 (释放) 文件
     *
     * name: assets 中的文件名 (路径)
     * target: 目标路径
     */
    fun cpAsset(name: String, target: String) {
        aziAsset.cpAsset(name, target)
    }

    fun getLog(): AziLog {
        return aziLog
    }
}
