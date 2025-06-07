package io.github.fm_elpac.azi

import java.io.File

import android.content.Context

class AziSh(val c: Context, val azi: AziApi) {
    // 命令编号 (执行计数)
    private var pI: Int = 0

    // 获取 AZI_DIR_APP_DATA 的值
    private fun envDirAppData(): String {
        return c.getFilesDir().absolutePath
    }

    // 获取 AZI_DIR_SDCARD_DATA 的值
    private fun envDirSdcardData(): String {
        return c.getExternalFilesDir(null)!!.absolutePath
    }

    // 获取 AZI_DIR_SDCARD_CACHE 的值
    private fun envDirSdcardCache(): String {
        return c.getExternalCacheDir()!!.absolutePath
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

    // 阻塞: 等待命令执行结束
    fun sh(cmd: ProcessBuilder): Int {
        pI += 1
        val i = pI
        // 日志 (文本)
        val t1 = "AziSh.sh(" + i + "):  " + cmd.command()
        // 日志 高级用法
        val log = azi.getLog()
        log.log(t1)
        log.logO(t1)
        log.logE(t1)

        // stdout, stderr 输出 (追加) 到日志文件
        cmd.redirectOutput(ProcessBuilder.Redirect.appendTo(log.getLogO()))
        cmd.redirectError(ProcessBuilder.Redirect.appendTo(log.getLogE()))
        // 设置环境变量
        val e = cmd.environment()
        e.put(AziApi.AZI_DIR_APP_DATA, env(AziApi.AZI_DIR_APP_DATA))
        e.put(AziApi.AZI_DIR_SDCARD_DATA, env(AziApi.AZI_DIR_SDCARD_DATA))
        e.put(AziApi.AZI_DIR_SDCARD_CACHE, env(AziApi.AZI_DIR_SDCARD_CACHE))

        // 开始执行
        val p = cmd.start()
        // 等待结束, 获取退出码
        val c = p.waitFor()
        // DEBUG
        val t2 = "AziSh.sh(" + i + ")  exit code " + c
        if (0 != c) {
            // 记录错误: 所有日志文件
            log.logE(t2)
            log.logO(t2)
            log.log(t2)
        } else {
            // 成功: 仅写入 logO 日志
            log.logO(t2)
        }
        return c
    }
}
