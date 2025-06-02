package io.github.fm_elpac.azi

import android.content.Context

/**
 * Azi 接口 API (静态方法调用)
 */
class Azi {
    companion object {
        const val AZI_DIR_APP_DATA = AziApi.AZI_DIR_APP_DATA
        const val AZI_DIR_SDCARD_DATA = AziApi.AZI_DIR_SDCARD_DATA
        const val AZI_DIR_SDCARD_CACHE = AziApi.AZI_DIR_SDCARD_CACHE

        // 用于内部初始化
        private var api: AziApi? = null

        /**
         * 在 app Application 初始化时调用
         */
        @JvmStatic
        fun setContext(c: Context) {
            api = AziApi(c)
        }

        @JvmStatic
        fun initZip(asset_zip: String, target_dir: String, cb: AziCb?) {
            api?.initZip(asset_zip, target_dir, cb)
        }

        @JvmStatic
        fun env(name: String): String? {
            return api?.env(name)
        }

        @JvmStatic
        fun log(text: String) {
            api?.log(text)
        }

        @JvmStatic
        fun sh(cmd: ProcessBuilder): Int {
            val a = api
            if (null != a) {
                return a.sh(cmd)
            }
            return -1
        }

        @JvmStatic
        fun cpAsset(name: String, target: String) {
            api?.cpAsset(name, target)
        }
    }
}
