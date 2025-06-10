package io.github.fm_elpac.azi

import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.OutputStreamWriter

class AziLog(val azi: AziApi) {
    // 时间戳
    private val t: String
    // 日志文件
    private var logO: File = File("/") // placeholder
    private var logE: File = File("/")
    // 日志文件写入器
    private val w: BufferedWriter
    private var wo: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))
    private var we: BufferedWriter = BufferedWriter(OutputStreamWriter(System.err))

    init {
        // 时间戳
        t = AziT.now()

        // 创建日志文件
        val log = createLogFile(t)
        w = makeWriter(log)

        resetLogOE()
    }

    fun log(text: String) {
        // logcat
        println("AziLog: " + text)

        // 写入日志文件
        writeLine(w, text)
    }

    // stdout
    fun logO(text: String) {
        writeLine(wo, text)
    }

    // stderr
    fun logE(text: String) {
        writeLine(we, text)
    }

    fun makeWriter(f: File): BufferedWriter {
        // append: true
        return BufferedWriter(FileWriter(f, true))
    }

    /**
     * 重新创建 logO, logE 写入器
     */
    fun resetLogOE() {
        // stdout
        logO = createLogFileO(t)
        // stderr
        logE = createLogFileE(t)

        wo = makeWriter(logO)
        we = makeWriter(logE)
    }

    fun getLogO(): File {
        return logO
    }

    fun getLogE(): File {
        return logE
    }

    /**
     * 写入一行文本, 前面添加时间戳
     */
    fun writeLine(w: BufferedWriter, text: String) {
        val t = AziT.now()
        val line = t + "  " + text + "\n"
        w.write(line)
        w.flush()
    }

    /**
     * 创建日志文件 AZI_DIR_SDCARD_CACHE/azi_log/name.txt
     */
    fun createLogFile(name: String): File {
        val p = azi.env(AziApi.AZI_DIR_SDCARD_CACHE)!!
        val dir = File(File(p), "azi_log")
        // 创建目录, 如果不存在
        dir.mkdirs()

        val f = File(dir, name + ".txt")
        return f
    }

    /**
     * 创建日志文件 -o.txt (stdout)
     */
    fun createLogFileO(t: String): File {
        return createLogFile(t + "-o")
    }

    /**
     * 创建日志文件 -e.txt (stderr)
     */
    fun createLogFileE(t: String): File {
        return createLogFile(t + "-e")
    }
}
