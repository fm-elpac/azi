package io.github.fm_elpac.azi

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * 时间戳 相关功能
 */
class AziT {
    companion object {
        // ISO 8601
        private val f: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

        /**
         * 格式化为  ISO 8601 时间字符串
         */
        @JvmStatic
        fun isoT(t: LocalDateTime): String {
            return t.atOffset(ZoneOffset.UTC).format(f)
        }

        /**
         * 返回当前时间对应的时间戳
         */
        @JvmStatic
        fun now(): String {
            return isoT(LocalDateTime.now())
        }
    }
}
